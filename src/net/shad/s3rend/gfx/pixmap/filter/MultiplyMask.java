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
 *
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class MultiplyMask implements ProceduralInterface, FilterPixmapInterface, Filter3PixmapInterface
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
		generate(pixmap, pixmap, pixmap, pixmap, 0.5f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, pixmap, pixmap, pixmap, 0.5f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, pixmap, pixmap, pixmap, (float) Math.random());
	}

	/**
	 *
	 * @param pixmapDst
	 * @param pixmapSource
	 * @param pixmapSource2
	 */
	@Override
	public void filter(Pixmap pixmapDst, Pixmap pixmapSource, Pixmap pixmapSource2){
		generate(pixmapDst, pixmapDst, pixmapSource, pixmapSource2, 0.5f);
	}

	/**
	 *
	 * @param pixmap
	 * @param amplify - The bulge value
	 */
	public static void generate(final Pixmap pixmapDest, final Pixmap pixmapSource, final Pixmap pixmapSource2, final Pixmap pixmapMask, float percent){

		int widthDest=pixmapDest.getWidth();
		int heightDest=pixmapDest.getHeight();

		int widthSource=pixmapSource.getWidth();
		int heightSource=pixmapSource.getHeight();

		int widthSource2=pixmapSource2.getWidth();
		int heightSource2=pixmapSource2.getHeight();

		int widthMask=pixmapMask.getWidth();
		int heightMask=pixmapMask.getHeight();

		int rgb=0;
		int r=0;
		int g=0;
		int b=0;
		int a=0;
		percent=percent * 260;

		if (percent < 2){
			pixmapDest.drawPixmap(pixmapSource, 0, 0);
			return;
		} else if (percent > 254){
			pixmapDest.drawPixmap(pixmapSource2, 0, 0);
			return;
		}

		for (int y=0; y < heightDest; y++){
			for (int x=0; x < widthDest; x++){

				rgb=pixmapMask.getPixel(x % widthMask, y % heightMask);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;
				a=(rgb & 0x000000ff);
				a=(a + (r + g + b)) >> 2;

				if (percent > a){
					rgb=pixmapSource2.getPixel(x % widthSource2, y % heightSource2);
				} else {
					rgb=pixmapSource.getPixel(x % widthSource, y % heightSource);
				}
				pixmapDest.drawPixel(x, y, rgb);
			}
		}
	}

	/**
	 * Set cache data
	 *
	 * @param pixmapDest
	 * @param pixmapSource
	 * @param pixmapSource2
	 * @param pixmapMask
	 */
	public void setPixmap(final Pixmap pixmapDest, final Pixmap pixmapSource, final Pixmap pixmapSource2, final Pixmap pixmapMask){

		int widthSource=pixmapSource.getWidth();
		int heightSource=pixmapSource.getHeight();

		int widthSource2=pixmapSource2.getWidth();
		int heightSource2=pixmapSource2.getHeight();

		int widthMask=pixmapMask.getWidth();
		int heightMask=pixmapMask.getHeight();

		if (widthDest != pixmapDest.getWidth() || heightDest != pixmapDest.getHeight() || sourceCache == null || source2Cache == null || maskCache == null){
			widthDest=pixmapDest.getWidth();
			heightDest=pixmapDest.getHeight();

			sourceCache=new int[widthDest * heightDest + 8];
			source2Cache=new int[widthDest * heightDest + 8];
			maskCache=new int[widthDest * heightDest + 8];
		}

		int rgb=0;
		int r=0;
		int g=0;
		int b=0;
		int a=0;
		int idx=0;

		for (int y=0; y < heightDest; y++){
			for (int x=0; x < widthDest; x++){

				idx=y * widthDest + x;
				rgb=pixmapMask.getPixel(x % widthMask, y % heightMask);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;
				a=(rgb & 0x000000ff);
				a=(a + (r + g + b)) >> 2;
				maskCache[idx]=a;

				rgb=pixmapSource.getPixel(x % widthSource, y % heightSource);
				sourceCache[idx]=rgb;

				rgb=pixmapSource2.getPixel(x % widthSource2, y % heightSource2);
				source2Cache[idx]=rgb;
			}
		}

	}

	/**
	 * Generate from cache data
	 *
	 * @param pixmapDest
	 * @param percent
	 */
	public void generate(final Pixmap pixmapDest, float percent){

		percent=percent * 260;

		int rgb=0;
		int idx=0;

		for (int y=0; y < heightDest; y++){
			for (int x=0; x < widthDest; x++){

				idx=y * widthDest + x;
				if (percent > maskCache[idx]){
					rgb=source2Cache[idx];
				} else {
					rgb=sourceCache[idx];
				}
				pixmapDest.drawPixel(x, y, rgb);
			}
		}

	}
}
