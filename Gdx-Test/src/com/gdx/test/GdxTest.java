package com.gdx.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class GdxTest implements ApplicationListener {
	
	public static Scene scene;
	
	@Override
	public void create() {
		scene = new SceneTitle();
	}

	@Override
	public void render() {
		float dt = Gdx.app.getGraphics().getDeltaTime();
		
		// we use a copy of the scene reference because the actual scene may change during update
		Scene s = scene;
		s.update(dt);
		s.render();
	}

	@Override
	public void resize(int w, int h) 
	{
		// set OpenGL viewport
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glViewport(0, 0, w, h);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
