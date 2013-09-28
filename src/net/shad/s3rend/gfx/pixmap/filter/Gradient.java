/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shad.s3rend.gfx.pixmap.filter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 *
 * @author Jarek
 */
public class Gradient implements ProceduralInterface, FilterInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, 1.0f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void filter(final Pixmap pixmap){
		generate(pixmap, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, 1.0f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK, 1.0f);
	}

	/**
	 *
	 * @param pixmap
	 * @param topLeft
	 * @param topRight
	 * @param bottomLeft
	 * @param bottomRight
	 * @param alpha
	 */
	public static void generate(final Pixmap pixmap, final Color topLeft, final Color topRight, final Color bottomLeft, final Color bottomRight, final float alpha){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		if (DEBUG){
			Gdx.app.log("Gradient::generate()", "pixmap: " + pixmap + " topLeft: " + topLeft + " topRight: " + topRight + " bottomLeft: " + bottomLeft + " bottomRight: " + bottomRight + " alpha: " + alpha);
		}
		float finv_WH=1.0f / (float) (width * height);


		for (int y=0; y < width; y++){
			for (int x=0; x < height; x++){

				//
				// Calculate precent of gradient bar
				//
				float tLeft=(float) ((width - x) * (height - y)) * finv_WH;
				float tRight=(float) ((x) * (height - y)) * finv_WH;
				float lBottom=(float) ((width - x) * (y)) * finv_WH;
				float rBottom=(float) ((x) * (y)) * finv_WH;

				//
				// Draw gradient pixel
				//
				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				r=(int) (r + ((topLeft.r * tLeft * 255) + (topRight.r * tRight * 255) + (bottomLeft.r * lBottom * 255) + (bottomRight.r * rBottom * 255)) * alpha);
				g=(int) (g + ((topLeft.g * tLeft * 255) + (topRight.g * tRight * 255) + (bottomLeft.g * lBottom * 255) + (bottomRight.g * rBottom * 255)) * alpha);
				b=(int) (b + ((topLeft.b * tLeft * 255) + (topRight.b * tRight * 255) + (bottomLeft.b * lBottom * 255) + (bottomRight.b * rBottom * 255)) * alpha);

				//
				// Clamp
				//
				r=r > 255 ? 255 : r;
				g=g > 255 ? 255 : g;
				b=b > 255 ? 255 : b;

				pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
			}
		}

	}
}
