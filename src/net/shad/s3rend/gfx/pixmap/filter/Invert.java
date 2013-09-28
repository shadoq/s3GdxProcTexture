/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shad.s3rend.gfx.pixmap.filter;

import com.badlogic.gdx.graphics.Pixmap;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 *
 * @author Jarek
 */
public class Invert implements ProceduralInterface, FilterInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		process(pixmap);
	}

	@Override
	public void filter(Pixmap pixmap){
		process(pixmap);
	}

	@Override
	public void random(final Pixmap pixmap){
		process(pixmap);
	}

	/**
	 *
	 * @param pixmap
	 * @param xCenter
	 * @param yCenter
	 * @param xSize
	 * @param ySize
	 * @param maxIterations
	 */
	public static void process(final Pixmap pixmap){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		for (int y=0; y < width; y++){
			for (int x=0; x < height; x++){
				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				r=255 - r;
				g=255 - g;
				b=255 - b;

				pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);

			}
		}
	}
}
