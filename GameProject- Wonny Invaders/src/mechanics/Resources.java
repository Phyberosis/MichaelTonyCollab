package mechanics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class Resources
{
	/**
	 * resource management class for loading resources
	 */

	private final int imageCount = 6; 	//update as needed to current number of images
	private final String[] imageAdresses = {
			"Error.png",				// 0
			"playerStandard.png",		// 1
			"playerStandardAlt.png",	// 2
			"playerSuccess.png",		// 3
			"playerDamage.png",			// 4
			"playerRecovery.png"		// 5
			};
	private BufferedImage[] images;
	
	public Resources()
	{
		//initiate image resource array
		images = new BufferedImage[imageCount];
		
		//get address of image folder
		String address  = "";
		try {
			address = Resources.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			address = address.substring(0, address.lastIndexOf("/") - 3) + "res/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		//load images into image array
		try {
			
			for(int index = 0; index < imageCount; index++)
			{
			    images[index] = ImageIO.read(new File(address + imageAdresses[index]));
			    index ++;
			}
		} catch (IOException e) {
		}
	}
	
	public synchronized BufferedImage getImg(int index)
	{
		if (index >= imageCount || index < 0)
		{
			return images[0];// error image
		}
		
		return images[index];
	}
}
