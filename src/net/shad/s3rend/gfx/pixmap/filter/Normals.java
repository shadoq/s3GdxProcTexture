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
import com.badlogic.gdx.math.Vector3;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 * Class to generate normal map with image
 * Using Sobel filter operation to detect edge of the image
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Normals implements ProceduralInterface, FilterPixmapInterface
{

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 64);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, 64);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, (int) (32.0f + Math.random() * 64));
	}

	/**
	 * Main normal map filter process
	 * @param pixmap
	 * @param amplify - The bulge value
	 */
	public static void generate(final Pixmap pixmap, int amplify){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		Pixmap dstPixmap=new Pixmap(width, height, pixmap.getFormat());

		for (int y=0; y < height; y++){
			for (int x=0; x < width; x++){

				int rgb=pixmap.getPixel(x, y);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				//
				// 1 Column
				//
				int rgbXnYn=pixmap.getPixel(x - 1, y - 1);
				int rXnYn=(rgbXnYn & 0xff000000) >>> 24;
//				int gXnYn=(rgbXnYn & 0x00ff0000) >>> 16;
//				int bXnYn=(rgbXnYn & 0x0000ff00) >>> 8;

				int rgbXnY=pixmap.getPixel(x - 1, y);
				int rXnY=(rgbXnY & 0xff000000) >>> 24;
//				int gXnY=(rgbXnY & 0x00ff0000) >>> 16;
//				int bXnY=(rgbXnY & 0x0000ff00) >>> 8;

				int rgbXnYp=pixmap.getPixel(x - 1, y + 1);
				int rXnYp=(rgbXnYp & 0xff000000) >>> 24;
//				int gXnYp=(rgbXnYp & 0x00ff0000) >>> 16;
//				int bXnYp=(rgbXnYp & 0x0000ff00) >>> 8;

				//
				// 2 Column
				//
				int rgbXYn=pixmap.getPixel(x, y - 1);
				int rXYn=(rgbXYn & 0xff000000) >>> 24;
//				int gXYn=(rgbXYn & 0x00ff0000) >>> 16;
//				int bXYn=(rgbXYn & 0x0000ff00) >>> 8;

				int rgbXYp=pixmap.getPixel(x, y + 1);
				int rXYp=(rgbXYp & 0xff000000) >>> 24;
//				int gXYp=(rgbXYp & 0x00ff0000) >>> 16;
//				int bXYp=(rgbXYp & 0x0000ff00) >>> 8;

				//
				// 3 Column
				//
				int rgbXpYn=pixmap.getPixel(x + 1, y - 1);
				int rXpYn=(rgbXpYn & 0xff000000) >>> 24;
//				int gXpYn=(rgbXpYn & 0x00ff0000) >>> 16;
//				int bXpYn=(rgbXpYn & 0x0000ff00) >>> 8;

				int rgbXpY=pixmap.getPixel(x + 1, y);
				int rXpY=(rgbXpY & 0xff000000) >>> 24;
//				int gXpY=(rgbXpY & 0x00ff0000) >>> 16;
//				int bXpY=(rgbXpY & 0x0000ff00) >>> 8;

				int rgbXpYp=pixmap.getPixel(x + 1, y + 1);
				int rXpYp=(rgbXpYp & 0xff000000) >>> 24;
//				int gXpYp=(rgbXpYp & 0x00ff0000) >>> 16;
//				int bXpYp=(rgbXpYp & 0x0000ff00) >>> 8;

				//
				// Y Sobel filter operation
				//
				// [ 1  2  1]				
				// [			
				// [-1 -2 -1]			
				//				
				float dY=0;
				dY+=rXnYn * 1.0f;
				dY+=rXYn * 2.0f;
				dY+=rXpYn * 1.0f;
				
				dY+=rXnYp * -1.0f;
				dY+=rXYp * -2.0f;
				dY+=rXpYp * -1.0f;

				//
				// X Sobel filter
				//
				// [-1     1]
				// [-2     2]
				// [-1     1]
				//
				float dX=0;
				dX=rXnYn * -1.0f;
				dX+=rXnY * -2.0f;
				dX+=rXnYp * -1.0f;
				
				dX+=rXpYn * 1.0f;
				dX+=rXpY * 2.0f;
				dX+=rXpYp * 1.0f;

				//
				// Compute the cross product of the two vectors
				//
				Vector3 norm=new Vector3();
				norm.x=-dX * amplify / 255.0f;
				norm.y=-dY * amplify / 255.0f;
				norm.z=1.0f;

				//
				// Normalize
				//
				norm.nor();

				//
				// Store
				// [-1.0f->1.0f] -> [0 -> 255]
				//
				r=(int) ((norm.x + 1.0f) / 2.0f * 255.0f);	
				g=(int) ((norm.y + 1.0f) / 2.0f * 255.0f);
				b=(int) ((norm.z + 1.0f) / 2.0f * 255.0f);

				dstPixmap.drawPixel(x, y, ((int) r << 24) | ((int) g << 16) | ((int) b << 8) | a);
			}
		}
		pixmap.drawPixmap(dstPixmap, 0, 0);
	}
}
