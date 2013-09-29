/**
 * *****************************************************************************
 * Copyright 2013 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package net.shad.s3rend.gfx.pixmap.filter;

import com.badlogic.gdx.graphics.Pixmap;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 * Class to generate threshold operation of the image
 * 
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Threshold implements ProceduralInterface, FilterInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 128, 128, 0);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, 128, 128, 0);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, (int) (96.0f + Math.random() * 64), (int) (96.0f + Math.random() * 64), (int) (Math.random() * 4));
	}

	/**
	 * Main process threshold filter
	 * 
	 * @param pixmap
	 * @param threshold - Threshold value
	 * @param ratio - Threshold strength
	 * @param mode - 0 - Expand downwards, 1 - Expand upwards, 2 - Compress below, 3 - Compress above
	 */
	public static void generate(final Pixmap pixmap, int threshold, int ratio, int mode){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();
		float fRatio=0;

		switch (mode){

			//
			// Expand downwards
			//
			case 0:
			default:

				fRatio=1 + ratio * 0.1f;
				for (int y=0; y < width; y++){
					for (int x=0; x < height; x++){

						int rgb=pixmap.getPixel(x, y);
						int r=(rgb & 0xff000000) >>> 24;
						int g=(rgb & 0x00ff0000) >>> 16;
						int b=(rgb & 0x0000ff00) >>> 8;
						int a=(rgb & 0x000000ff);

						r=expandIntensity(r, fRatio, threshold);
						g=expandIntensity(g, fRatio, threshold);
						b=expandIntensity(b, fRatio, threshold);

						pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
					}
				}
				break;

			//
			// Expand upwards
			//
			case 1:
				fRatio=1 + ratio * 0.1f;
				for (int y=0; y < width; y++){
					for (int x=0; x < height; x++){

						int rgb=pixmap.getPixel(x, y);
						int r=(rgb & 0xff000000) >>> 24;
						int g=(rgb & 0x00ff0000) >>> 16;
						int b=(rgb & 0x0000ff00) >>> 8;
						int a=(rgb & 0x000000ff);

						r=expandIntensity(255 - r, fRatio, 255 - threshold);
						g=expandIntensity(255 - g, fRatio, 255 - threshold);
						b=expandIntensity(255 - b, fRatio, 255 - threshold);

						pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
					}
				}
				break;

			//
			// Compress below
			//
			case 2:

				fRatio=1 + ratio * 0.1f;
				for (int y=0; y < width; y++){
					for (int x=0; x < height; x++){

						int rgb=pixmap.getPixel(x, y);
						int r=(rgb & 0xff000000) >>> 24;
						int g=(rgb & 0x00ff0000) >>> 16;
						int b=(rgb & 0x0000ff00) >>> 8;
						int a=(rgb & 0x000000ff);

						r=compressIntensity(r, fRatio, threshold);
						g=compressIntensity(g, fRatio, threshold);
						b=compressIntensity(b, fRatio, threshold);

						pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
					}
				}
				break;

			//
			// Compress above
			//
			case 3:
				fRatio=1 + ratio * 0.1f;
				for (int y=0; y < width; y++){
					for (int x=0; x < height; x++){

						int rgb=pixmap.getPixel(x, y);
						int r=(rgb & 0xff000000) >>> 24;
						int g=(rgb & 0x00ff0000) >>> 16;
						int b=(rgb & 0x0000ff00) >>> 8;
						int a=(rgb & 0x000000ff);

						r=compressIntensity(255 - r, fRatio, 255 - threshold);
						g=compressIntensity(255 - g, fRatio, 255 - threshold);
						b=compressIntensity(255 - b, fRatio, 255 - threshold);

						pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
					}
				}
				break;

		}

	}

	/**
	 *
	 * @param intensity
	 * @param ratio
	 * @param threshold
	 * @return
	 */
	private static int expandIntensity(int intensity, float ratio, int threshold){
		if (intensity < threshold){
			int newIntensity=(threshold - (int) ((threshold - intensity) * ratio));
			return newIntensity < 0 ? 0 : newIntensity;
		} else {
			return intensity;
		}
	}

	/**
	 *
	 * @param intensity
	 * @param ratio
	 * @param threshold
	 * @return
	 */
	private static int compressIntensity(int intensity, float ratio, int threshold){
		if (intensity < threshold){
			int newIntensity=threshold - (int) ((threshold - intensity) / ratio);
			return newIntensity < 0 ? 0 : newIntensity;
		} else {
			return intensity;
		}
	}
}
