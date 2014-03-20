package program.util;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageRotator 
{
	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;
	private static final int ALPHA = 3;
	/**
	 * Creates a new BufferedImage from an existing BufferedImage
	 * at a given rotation. Uses TYPE_INT_ARGB.
	 * 
	 * @param img = BufferedImage to be rotated.
	 * @param radians = double, number of radians to rotate 
	 * clockwise.
	 * @param hasAlphaChannel = boolean, indicating if source 
	 * graphic has alpha values encoded.
	 * @return BufferedImage with rotated data.
	 */
	public BufferedImage rotate(BufferedImage img, double radians,
			boolean hasAlphaChannel)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		// calc dims needed for new Array
		int newWidth = (int)(1 +
				Math.abs(Math.cos(radians)) * width 
				+ Math.abs(Math.sin(radians)) * height);
		int newHeight = (int)(1 + 
				Math.abs(Math.sin(radians)) * width 
				+ Math.abs(Math.cos(radians)) * height);
		
//		System.out.println("new: " + newWidth + "," + newHeight);
			
		BufferedImage newImage = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_ARGB);
				
		double[] transformMatrix = new double[4];
		transformMatrix[0] = Math.cos(radians);
		transformMatrix[1] = -Math.sin(radians);
		transformMatrix[2] = transformMatrix[1] * -1;
		transformMatrix[3] = transformMatrix[0];
		
		double lowestX = 0;
		double lowestY = 0;

		// determine offset for newImage array
		double[] xy = matrixMultiply(transformMatrix, 0, 0); 
		lowestX = xy[0] < lowestX ? xy[0] : lowestX;
		lowestY = xy[1] < lowestY ? xy[1] : lowestY;

		xy = matrixMultiply(transformMatrix, 0, height - 1);
		lowestX = xy[0] < lowestX ? xy[0] : lowestX;
		lowestY = xy[1] < lowestY ? xy[1] : lowestY;

		xy = matrixMultiply(transformMatrix, width - 1, 0);
		lowestX = xy[0] < lowestX ? xy[0] : lowestX;
		lowestY = xy[1] < lowestY ? xy[1] : lowestY;

		xy = matrixMultiply(transformMatrix, width - 1, height - 1);
		lowestX = xy[0] < lowestX ? xy[0] : lowestX;
		lowestY = xy[1] < lowestY ? xy[1] : lowestY;
		
		int xAdj = (int)Math.floor(lowestX);
		int yAdj = (int)Math.floor(lowestY);		
				
		Raster imgRaster = img.getRaster();
		/*
		 * Each point in the new array is given a "BoundingTriangle" object,
		 * which will hold (up to) the three closest rotated points 
		 * and serve as a data source for interpolating the pixel
		 * color value at that point.
		 */
		BoundingTriangle[] triangles = new BoundingTriangle[newWidth * newHeight];
		for (int i = 0; i < triangles.length; i++)
		{
			triangles[i] = new BoundingTriangle();
		}
		
		int[] pixel = new int[4];
		int xLow, yLow;
		double distance;
		// iteration for each pixel & it's rotated address
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				xy = matrixMultiply(transformMatrix, i, j); 
				xy[0] -= xAdj;
				xy[1] -= yAdj;
				
				xLow = (int)xy[0];
				yLow = (int)xy[1];
				
				pixel = imgRaster.getPixel(i, j, pixel);
				if (!hasAlphaChannel) pixel[3] = 255;
				
				// add data to the four surrounding new array locations
				// "distance" used for ranking only. 
				// For true distance, take square root of 'distance'.
				distance = (xy[0] - xLow) * (xy[0] - xLow) 
						+ (xy[1] - yLow) * (xy[1] - yLow);
				triangles[(yLow * newWidth) + xLow].add(distance, pixel);
				
				if (xLow + 1 < newWidth)
				{
					distance = (xy[0] - (xLow + 1)) * (xy[0] - (xLow + 1)) 
						+ (xy[1] - yLow) * (xy[1] - yLow);
					triangles[(yLow * newWidth) + (xLow + 1)].add(distance, pixel);
				}
				if (yLow + 1 < newHeight)
				{
					distance = (xy[0] - xLow) * (xy[0] - xLow) 
							+ (xy[1] - (yLow + 1)) * (xy[1] - (yLow + 1));
					triangles[((yLow + 1) * newWidth) + xLow].add(distance, pixel);
				}
				
				if (yLow + 1 < newHeight && xLow + 1 < newWidth)
				{
					distance = (xy[0] - (xLow + 1)) * (xy[0] - (xLow + 1)) 
							+ (xy[1] - (yLow + 1)) * (xy[1] - (yLow + 1));
					triangles[((yLow + 1) * newWidth) + (xLow + 1)].add(distance, pixel);
				}
			}
		}
		
		
		// last pass: raster for new image, uses interpolated data
		WritableRaster newImgRaster = newImage.getRaster();
		for (int j = 0; j < newHeight; j++)
		{
			for (int i = 0; i < newWidth; i++)
			{
				newImgRaster.setPixel(i, j, triangles[i + j * newWidth].interpolate());
			}
		}
		
		return newImage;
	}
	
	double[] matrixMultiply(double[] transformMatrix, double xVal, double yVal)
	{
		double[] result = new double[2];
		
		result[0] = transformMatrix[0] * xVal + transformMatrix[1] * yVal;
		result[1] = transformMatrix[2] * xVal + transformMatrix[3] * yVal;
		
		return result;
	}
	
    class BoundingTriangle
	{
		double[] distances = {2, 2, 2};
		int[] colors = {0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0
		};
		int largest = 0;
		boolean nailed = false;
		int bullseye = 0;
		
		void add(double distance, int[] pixel)
		{
			if (nailed)
			{
				return;
			}
			else if (distance < 0.00000001)
			{
				nailed = true;
			}
			
			if (distance < distances[largest])
			{

				distances[largest] = distance;
				colors[largest * 4 + RED] = pixel[RED];
				colors[largest * 4 + GREEN] = pixel[GREEN];
				colors[largest * 4 + BLUE] = pixel[BLUE];
				colors[largest * 4 + ALPHA] = pixel[ALPHA];
			}
			if (nailed) 
			{		
				bullseye = largest;
				return;
			}
			
			// find new "largest"
			double maxDistance = 0;
			for (int i = 0; i < 3; i++)
			{
				if (distances[i] > maxDistance)
				{
					maxDistance = distances[i];
					largest = i;
				}
			}
		}
		
		int[] interpolate()
		{
			int[] interpPixel = new int[4];
			
			if (nailed)
			{
				interpPixel[RED] = (int)colors[bullseye * 4 + RED];
				interpPixel[GREEN] = (int)colors[bullseye * 4 + GREEN];
				interpPixel[BLUE] = (int)colors[bullseye * 4 + BLUE];
				interpPixel[ALPHA] = (int)colors[bullseye * 4 + ALPHA];
				return interpPixel;
			}
				
			/*
			 *  Uses weighted average, where weight is inversely 
			 * proportional to distance.
			 */ 
			double count = 0;
			double weight = 0;
			double largest = 0;
			
			double redSum = 0;
			double greenSum = 0;
			double blueSum = 0;
			double alphaSum = 0;


			for (int i = 0; i < 3; i++)
			{
				// interpolation for colors: only visible pixels
				if (colors[i * 4 + ALPHA] > 0)
				{
					largest = (distances[i] > largest) ? distances[i]:largest;
				}
			}
			
			for (int i = 0; i < 3; i++)
			{	
				if (colors[i * 4 + ALPHA] > 0)
				{					
					weight = largest / Math.sqrt(distances[i]);
					
					redSum += colors[i * 4 + RED] * weight;
					greenSum += colors[i * 4 + GREEN] * weight;
					blueSum += colors[i * 4 + BLUE] * weight;
					
					count += weight;
				}
			}
			interpPixel[RED] = (int)(redSum / count);
			interpPixel[GREEN] = (int)(greenSum / count);
			interpPixel[BLUE] = (int)(blueSum / count);
			
			// interpolation for alpha, use all
			count = 0;
			for (int i = 0; i < 3; i++)
			{
				largest = (distances[i] > largest) ? distances[i]:largest;
			}
			
			for (int i = 0; i < 3; i++)
			{
				weight = largest / distances[i];
				alphaSum += colors[i * 4 + ALPHA] * weight;
				count += weight;
			}
			interpPixel[ALPHA] = (int)(alphaSum / count);

			return interpPixel;
				
		}
	}
}