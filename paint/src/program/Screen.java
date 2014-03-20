package program;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen 
{
	private int width, height;
	private BufferedImage img;
	private int[] imgpixels;
	public Screen(int width, int height)
	{
		this.width = width;
		this.height = height;
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imgpixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		clear();
	}
	public void clear()
	{
		for(int i = 0;i < imgpixels.length;i++)
		{
			imgpixels[i] = 0xffffff;
		}
	}
	public void changePixel(int x, int y, int col)
	{
		imgpixels[x + y * width] = col;
	}
	public void render(Graphics g)
	{
		g.drawImage(img, 0, 0, width, height, null);
	}
	public int[] getImgpixels(){return imgpixels;}
	public void setImgpixels(int[] imgpixels) {this.imgpixels = imgpixels;}
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height;}
	
}
