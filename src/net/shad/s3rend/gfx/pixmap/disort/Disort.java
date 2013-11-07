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
package net.shad.s3rend.gfx.pixmap.disort;

import net.shad.s3rend.gfx.pixmap.filter.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 *
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Disort implements ProceduralInterface, FilterPixmapInterface, Filter2PixmapInterface
{

	private int widthDest;
	private int heightDest;
	private int[] sourceCache;
	private int[] source2Cache;
	private int[] maskCache;

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, pixmap, 1);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, pixmap, 1);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, pixmap, (float) Math.random() * 8);
	}

	/**
	 *
	 * @param pixmapDst
	 * @param pixmapSource
	 * @param pixmapSource2
	 */
	@Override
	public void filter(Pixmap pixmapDst, Pixmap pixmapMask){
		generate(pixmapDst, pixmapMask, 1);
	}

	/**
	 * 
	 * @param pixmapDest
	 * @param pixmapMask
	 * @param power 
	 */
	public static void generate(final Pixmap pixmapDest, final Pixmap pixmapMask, float power){

		int widthDest=pixmapDest.getWidth();
		int heightDest=pixmapDest.getHeight();

		int widthMask=pixmapMask.getWidth();
		int heightMask=pixmapMask.getHeight();

		int rgb=0;
		int r=0;
		int g=0;
		int b=0;
		int a=0;

		Pixmap dstPixmap=new Pixmap(widthDest, widthDest, pixmapDest.getFormat());

		Vector3 norm=new Vector3();

		for (int y=0; y < heightDest; y++){
			for (int x=0; x < widthDest; x++){

				rgb=pixmapMask.getPixel(x % widthMask, y % heightMask);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;
				a=(rgb & 0x000000ff);

				norm.x=r - 127;
				norm.y=g - 127;
				norm.z=b - 127;
				norm.nor();

				float v=(x + (norm.x * power)) % widthDest;
				float u=(y + (norm.y * power)) % heightDest;

				int vt=v >= 0 ? (int) v : (int) v - 1;
				int ut=u >= 0 ? (int) u : (int) u - 1;

				//
				// Texel1
				//
				rgb=pixmapDest.getPixel(vt, ut);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				int outR=r;
				int outG=g;
				int outB=b;
				int outA=255;

				//
				// Texel2
				//
				rgb=pixmapDest.getPixel(vt, ut + heightDest);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				outR+=r;
				outG+=g;
				outB+=b;

				//
				// Texel3
				//
				rgb=pixmapDest.getPixel((vt + widthDest), ut);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				outR+=r;
				outG+=g;
				outB+=b;

				//
				// Texel 4
				//
				rgb=pixmapDest.getPixel(vt + widthDest, ut + heightDest);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				outR+=r;
				outG+=g;
				outB+=b;

				//
				// Clamp
				//
				outR=(outR < 255) ? outR : 255;
				outR=(outR > 0) ? outR : 0;
				outG=(outG < 255) ? outG : 255;
				outG=(outG > 0) ? outG : 0;
				outB=(outB < 255) ? outB : 255;
				outB=(outB > 0) ? outB : 0;

				dstPixmap.drawPixel(x, y, ((int) outR << 24) | ((int) outG << 16) | ((int) outB << 8) | outA);
			}
		}
		pixmapDest.drawPixmap(dstPixmap, 0, 0);
	}
}
