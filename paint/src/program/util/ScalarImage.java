package program.util;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;
import static org.imgscalr.Scalr.pad;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr.Method;

public class ScalarImage 
{
	public static BufferedImage createThumbnail(BufferedImage img) 
	{
		  // Create quickly, then smooth and brighten it.
		  img = org.imgscalr.Scalr.resize(img, Method.SPEED, 125, OP_ANTIALIAS, OP_BRIGHTER);
		 
		  // Let's add a little border before we return result.
		  return pad(img, 4);
	}
}
