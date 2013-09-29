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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * Fill pixmap
 * 
 * @author Jaroslaw Czub (http://shad.net.pl)
 */
public class Flat implements ProceduralInterface
{

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void generate(Pixmap pixmap){
		generate(pixmap, Color.BLACK);
	}

	/**
	 * 
	 * @param pixmap 
	 */
	@Override
	public void random(Pixmap pixmap){
		generate(pixmap, Color.BLACK);
	}

	/**
	 * 
	 * @param pixmap
	 * @param color 
	 */
	public void generate(Pixmap pixmap, Color color){
		pixmap.setColor(color);
		pixmap.fill();
	}
}