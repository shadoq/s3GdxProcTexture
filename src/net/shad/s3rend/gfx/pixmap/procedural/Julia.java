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
package net.shad.s3rend.gfx.pixmap.procedural;

import com.badlogic.gdx.graphics.Pixmap;

/**
 * Generate Julia Set fractal
 * 
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Julia implements ProceduralInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 0.0d, 0.0d, 2.0d, 2.0d, 0.43d, 0.21d, 64);
	}

	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, -0.5f + Math.random() * 1, -0.5f + Math.random() * 1, 1.0f + Math.random() * 1, 1.0f + Math.random() * 1, -0.5f + Math.random() * 1, -0.5f + Math.random() * 1, 64);
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
	public static void generate(final Pixmap pixmap, double xCenter, double yCenter, double xSize, double ySize, double xIterations, double yIterations, int maxIterations){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		double xStart=xCenter - xSize;
		double yStart=yCenter - ySize;

		double xStep=(xSize * 2f) / width;
		double yStep=(ySize * 2f) / height;

		double px=0, py=0;
		double zx=0.0, zy=0.0, zx2=0.0, zy2=0.0;
		int value=0;
		float grey=0;

		py=yStart;
		for (int y=0; y < width; y++){
			px=xStart;
			for (int x=0; x < height; x++){

				zx=px;
				zy=py;
				value=0;

				for (int m=0; m < maxIterations; m++){
					zx2=zx;
					zx=zx * zx - zy * zy + xIterations;
					zy=2 * zx2 * zy + yIterations;
					if ((zx * zx + zy * zy) > 4){
						break;
					}
					value++;
				}
				grey=((maxIterations - value) * (255 / maxIterations));
				pixmap.drawPixel(x, y, ((int) grey << 24) | ((int) grey << 16) | ((int) grey << 8) | 255);
				px+=xStep;
			}
			py+=yStep;
		}
	}
}
