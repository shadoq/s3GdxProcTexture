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
 * Simple pixmap invert filter
 * 
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Invert implements ProceduralInterface, FilterPixmapInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		process(pixmap);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void filter(Pixmap pixmap){
		process(pixmap);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void random(final Pixmap pixmap){
		process(pixmap);
	}


	/**
	 * Main process invert
	 * @param pixmap 
	 */
	public static void process(final Pixmap pixmap){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		for (int y=0; y < height; y++){
			for (int x=0; x < width; x++){
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
