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

import net.shad.s3rend.gfx.pixmap.disort.*;
import net.shad.s3rend.gfx.pixmap.filter.*;
import net.shad.s3rend.gfx.pixmap.procedural.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Testing procedural texture
 *
 * @author Jarek
 */
public class ProceduralPixmapTest implements ApplicationListener
{

	private SpriteBatch spriteBatch;
	private Stage stage;
	private Skin skin;
	private Pixmap pixmap;
	private Texture texture;
	private ProceduralInterface procText=null;
	private String[] effectList={
		// Procedural
		Mandelbrot.class.getName(),
		Julia.class.getName(),
		Cell.class.getName(),
		Flat.class.getName(),
		// Filter
		Noise.class.getName(),
		NoiseGrey.class.getName(),
		Glow.class.getName(),
		Gradient.class.getName(),
		ColorFilter.class.getName(),
		Threshold.class.getName(),
		Invert.class.getName(),
		Normals.class.getName(),
		Alpha.class.getName(),
		// Disort
		RotoZoom.class.getName(),};
	private String[] effectListName={
		// Procedural
		Mandelbrot.class.getSimpleName(),
		Julia.class.getSimpleName(),
		Cell.class.getSimpleName(),
		Flat.class.getSimpleName(),
		// Filter
		Noise.class.getSimpleName(),
		NoiseGrey.class.getSimpleName(),
		Glow.class.getSimpleName(),
		Gradient.class.getSimpleName(),
		ColorFilter.class.getSimpleName(),
		Threshold.class.getSimpleName(),
		Invert.class.getSimpleName(),
		Normals.class.getSimpleName(),
		Alpha.class.getSimpleName(),
		// Disort
		RotoZoom.class.getSimpleName(),};

	@Override
	public void create(){

		gui();

		pixmap=new Pixmap(512, 512, Pixmap.Format.RGBA8888);
		Mandelbrot.generate(pixmap, 0.0, 0.0, 1.0, 1.0, 32);
		texture=new Texture(pixmap);
	}

	@Override
	public void render(){
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		texture.draw(pixmap, 0, 0);
		drawScreenQuad(texture);

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void pause(){
	}

	@Override
	public void resume(){
	}

	@Override
	public void dispose(){
	}

	/**
	 * Build GUI
	 */
	public void gui(){

		spriteBatch=new SpriteBatch(10);
		stage=new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		skin=new Skin(Gdx.files.internal("data/uiskin.json"));

		Window window=new Window("BTN", skin);
		window.defaults().align(Align.top | Align.left);

		int i=0;
		final ButtonGroup buttonGroup=new ButtonGroup();

		for (String effectName : effectList){
			Button buttonLoop=new TextButton(effectListName[i], skin);
			buttonLoop.setName(effectName);
			buttonGroup.add(buttonLoop);

			buttonLoop.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y){
					Button checked=buttonGroup.getChecked();

					try {
						procText=(ProceduralInterface) Class.forName(checked.getName()).newInstance();
						if (procText instanceof ProceduralInterface){
							procText.random(pixmap);
//							procText.generate(pixmap);
						}
					} catch (InstantiationException ex) {
						Gdx.app.log(this.getClass().getName(), "Error creare effect ...", ex);
					} catch (IllegalAccessException ex) {
						Gdx.app.log(this.getClass().getName(), "Error creare effect ...", ex);
					} catch (ClassNotFoundException ex) {
						Gdx.app.log(this.getClass().getName(), "Error creare effect ...", ex);
					}
				}
			});

			window.add(buttonLoop).fillX();
			if (i % 3 == 2){
				window.row();
			}
			i++;
		}
		window.pack();
		window.setX(0);
		window.setY(window.getHeight());
		stage.addActor(window);
	}

	private void drawScreenQuad(Texture text){
		spriteBatch=new SpriteBatch(10);
		spriteBatch.begin();
		spriteBatch.setColor(1f, 1f, 1f, 1f);
		spriteBatch.enableBlending();
		spriteBatch.draw(text, 0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
		spriteBatch.end();
	}
}
