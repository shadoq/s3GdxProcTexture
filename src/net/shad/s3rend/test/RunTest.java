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
package net.shad.s3rend.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Run Test
 * @author Jarek
 */
public class RunTest 
{
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args){
		LwjglApplicationConfiguration config=new LwjglApplicationConfiguration();
		config.width=1280;
		config.height=720;
		config.title="JCodecGdx Test";
		config.useGL20=false;
		config.forceExit=false;
		LwjglApplication lwjglApplication=new LwjglApplication(new ProceduralPixmapTest(), config);
	}
}
