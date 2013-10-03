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

import com.badlogic.gdx.graphics.Color;
import net.shad.s3rend.gfx.pixmap.filter.*;
import com.badlogic.gdx.graphics.Pixmap;
import net.shad.s3rend.gfx.pixmap.procedural.ProceduralInterface;

/**
 *
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class RotoZoom implements ProceduralInterface, FilterInterface
{

	private static final float PI=(float) Math.PI;
	private static final float PI2=(float) Math.PI * 2;
	private static final float PI1_2=(float) Math.PI * 0.5f;
	private static final float PI1_4=(float) Math.PI * 0.25f;
	private static final float DIV_PI2_360=(float) (1.0f / (360.0f / PI2));

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void generate(final Pixmap pixmap){
		generate(pixmap, 0.5f, 0.5f, 0, 1f, 1f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void filter(Pixmap pixmap){
		generate(pixmap, 0.5f, 0.5f, 0, 1f, 1f);
	}

	/**
	 *
	 * @param pixmap
	 */
	@Override
	public void random(final Pixmap pixmap){
		generate(pixmap, (float) Math.random(), (float) Math.random(), (float) (32.0f + Math.random() * 64), (float) Math.random(), (float) Math.random());
	}

	/**
	 *
	 * @param pixmap
	 * @param amplify - The bulge value
	 */
	public static void generate(final Pixmap pixmap, float centerX, float centerY, float rotate, float zoomX, float zoomY){

		int width=pixmap.getWidth();
		int height=pixmap.getHeight();

		//
		// Rotate
		//
		rotate=rotate * PI2;

		//
		// Zoom
		//
		zoomX=(float) Math.pow(.5f, zoomX - 1);
		zoomY=(float) Math.pow(.5f, zoomY - 1);

		float c=(float) (Math.cos(rotate));
		float s=(float) (Math.sin(rotate));

		float tw2=(float) width / 2.0f;
		float th2=(float) height / 2.0f;

		float ys=s * -th2;
		float yc=c * -th2;

		Pixmap dstPixmap=new Pixmap(width, height, pixmap.getFormat());
		dstPixmap.setColor(Color.RED);
		dstPixmap.fill();

		for (int y=0; y < width; y++){

			//
			// x' = cos(x)-sin(y) + Center X;
			//
			float u=(((c * -tw2) - ys) * zoomX) + centerX;

			//
			// y' = sin(x)+cos(y) + Center Y;
			//
			float v=(((s * -tw2) + yc) * zoomY) + centerY;

			for (int x=0; x < height; x++){

				int ut=u >= 0 ? (int) u : (int) u - 1;
				int vt=v >= 0 ? (int) v : (int) v - 1;

				//
				// Texels
				// 1 | 2
				// -------
				// 3 | 4
				//

				//
				// Texel1
				//
				int rgb=pixmap.getPixel(vt, ut);
				int r=(rgb & 0xff000000) >>> 24;
				int g=(rgb & 0x00ff0000) >>> 16;
				int b=(rgb & 0x0000ff00) >>> 8;
				int a=(rgb & 0x000000ff);

				int outR=r;
				int outG=g;
				int outB=b;
				int outA=255;

				//
				// Texel2
				//
				rgb=pixmap.getPixel(vt, ut + height);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				outR+=r;
				outG+=g;
				outB+=b;

				//
				// Texel3
				//
				rgb=pixmap.getPixel((vt + width), ut);
				r=(rgb & 0xff000000) >>> 24;
				g=(rgb & 0x00ff0000) >>> 16;
				b=(rgb & 0x0000ff00) >>> 8;

				outR+=r;
				outG+=g;
				outB+=b;

				//
				// Texel 4
				//
				rgb=pixmap.getPixel(vt + width, ut + height);
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

				//
				// Vectors X
				//
				u+=c * zoomX;
				v+=s * zoomY;
			}
			//
			// Vectors Y
			//
			ys+=s;
			yc+=c;
		}
		pixmap.drawPixmap(dstPixmap, 0, 0);
	}
};
