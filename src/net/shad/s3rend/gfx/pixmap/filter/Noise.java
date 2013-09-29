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
 * Add color RGB noise to the pixmap
 * 
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Noise implements ProceduralInterface, FilterInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 64, 64, 64);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, 64, 64, 64);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, (int) (32.0f + Math.random() * 32), (int) (32.0f + Math.random() * 32), (int) (32.0f + Math.random() * 32));
	}

	/**
	 * Main RGB noise process
	 * @param pixmap
	 * @param rangeR - Red noise range
	 * @param rangeG - Green noise range
	 * @param rangeB - Blue noise range
	 */
	public static void generate(final Pixmap pixmap, int rangeR, int rangeG, int rangeB){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();
		int startR=-(rangeR / 2);
		int startG=-(rangeG / 2);
		int startB=-(rangeB / 2);

		for (int y=0; y < width; y++){
			for (int x=0; x < height; x++){
				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				//
				// Add noise
				//
				r=r + (int) ((startR + Math.random() * rangeR));
				g=g + (int) ((startG + Math.random() * rangeG));
				b=b + (int) ((startB + Math.random() * rangeB));

				//
				// Clamp
				//
				r=r > 255 ? 255 : r;
				g=g > 255 ? 255 : g;
				b=b > 255 ? 255 : b;

				r=r < 0 ? 0 : r;
				g=g < 0 ? 0 : g;
				b=b < 0 ? 0 : b;

				pixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);

			}
		}
	}
}
