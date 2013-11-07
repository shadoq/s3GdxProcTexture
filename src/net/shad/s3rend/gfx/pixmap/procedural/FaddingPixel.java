/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shad.s3rend.gfx.pixmap.procedural;

import com.badlogic.gdx.graphics.Pixmap;
import java.util.Random;

/**
 *
 * @author Jarek
 */
public class FaddingPixel implements ProceduralInterface
{

	static Random rnd=new Random();

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 10, 10, 0);
	}

	@Override
	public void random(Pixmap pixmap){
		generate(pixmap, (int) (Math.random() * 20), (int) (20 + Math.random() * 20), 0);
	}

	/**
	 *
	 * @param pixmap
	 * @param speed
	 * @param number
	 * @param mode
	 */
	public static void generate(final Pixmap pixmap, int speed, int number, int mode){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		for (int y=0; y < height; y++){
			for (int x=0; x < width; x++){

				//
				// Read color pixel
				//
				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				//
				// Process
				//
				if (r == 0 && g == 0 && b == 0){
					if ((rnd.nextInt(100) < number) && (rnd.nextInt(100) < number)){
						switch (mode){
							default:
								if (rnd.nextInt(100) < 50){
									r=0;
								} else {
									r=255;
								}
								;
								if (rnd.nextInt(100) < 50){
									g=0;
								} else {
									g=255;
								}
								if (rnd.nextInt(100) < 50){
									b=0;
								} else {
									b=255;
								}
								break;
							case 1:
								r=255;
								g=255;
								b=255;
								break;
							case 2:
								r=255;
								g=0;
								b=0;
								break;
							case 3:
								r=0;
								g=255;
								b=0;
								break;
							case 4:
								r=0;
								g=0;
								b=255;
								break;
							case 5:
								r=255;
								g=255;
								b=0;
								break;
							case 6:
								r=0;
								g=255;
								b=255;
								break;
						}
					}
				} else {
					r-=21 - speed;
					g-=21 - speed;
					b-=21 - speed;
				}

				//
				// Clamp
				//
				r=(r < 255) ? r : 255;
				r=(r > 0) ? r : 0;
				g=(g < 255) ? g : 255;
				g=(g > 0) ? g : 0;
				b=(b < 255) ? b : 255;
				b=(b > 0) ? b : 0;

				//
				// Draw pixel
				//
				pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
			}
		}
	}
}
