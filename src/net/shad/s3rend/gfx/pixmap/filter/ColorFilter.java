/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shad.s3rend.gfx.pixmap.filter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 *
 * @author Jarek
 */
public class ColorFilter implements ProceduralInterface, FilterInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, Color.WHITE, 255, 255, 255, 127, 127, 127, 127);
	}

	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, Color.WHITE, 255, 255, 255, 127, 127, 127, 127);
	}

	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, Color.WHITE, (int) 255, 255, 255, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 127);
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
	public static void generate(final Pixmap pixmap, Color colorBase, int colorPercentRed, int colorPercentGreen, int colorPercentBlue, int brithness, int contrast, int saturation, int alpha){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		brithness=((brithness) * 2) - 256;

		float fconstrast=(float) contrast / 128.0f;
		if (contrast > 64){
			fconstrast=fconstrast * fconstrast * fconstrast;
		}
		contrast=(int) (fconstrast * 256.0f);

		int minalpha=(int) ((alpha >= 127) ? ((alpha - 127) * 2.f - (alpha - 127) / 128.f) : 0);
		int maxalpha=(int) ((alpha <= 127) ? (alpha * 2.f + alpha / 127.f) : 255);
		float alphamult=(maxalpha - minalpha) / 255.f;

		for (int y=0; y < width; y++){
			for (int x=0; x < height; x++){
				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				//
				// Color Base + Color Percent + Brithness
				//
				r=(int) (colorBase.r * 255 + ((r * colorPercentRed) >> 8) + brithness);
				g=(int) (colorBase.g * 255 + ((g * colorPercentGreen) >> 8) + brithness);
				b=(int) (colorBase.b * 255 + ((b * colorPercentBlue) >> 8) + brithness);

				//
				// Contrast
				//
				int c=(((r - 127) * contrast) >> 8) + 127;
				r=(c < 0x00) ? 0x00 : (c > 0xff) ? 0xff : c;

				c=(((g - 127) * contrast) >> 8) + 127;
				g=(c < 0x00) ? 0x00 : (c > 0xff) ? 0xff : c;

				c=(((b - 127) * contrast) >> 8) + 127;
				b=(c < 0x00) ? 0x00 : (c > 0xff) ? 0xff : c;

				//
				// Saturation
				//
				if (saturation != 127){
					int l=r + g + b;
					int u=(3 * r - l) * saturation / 127;
					int v=(3 * b - l) * saturation / 127;
					r=(u + l) / 3;
					g=(l - (u + v)) / 3;
					b=(v + l) / 3;
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

				pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | (int) (a * alphamult + minalpha));
			}
		}
	}
}
