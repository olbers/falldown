package com.gdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;

/*
 * A scene is a unique mode of the game.
 * This is a generic scene that contains logic and data common to all possible scenes.
 */
public abstract class Scene
{
	float screenWidth;
	float screenHeight;
	GL10 gl;
	Input input;
	
	Color clearColor;
	
	public Scene()
	{
		clearColor = new Color(0f,0f,0f,1f);
	}
	
	public void update(float dt)
	{
		screenWidth = Gdx.app.getGraphics().getWidth();
		screenHeight = Gdx.app.getGraphics().getHeight();
		input = Gdx.app.getInput();
	}
	
	public void render()
	{
		// update our local reference to the graphics
		gl = Gdx.app.getGraphics().getGL10();
		
		// clear screen
		gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// set projection
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, screenWidth, screenHeight, 0, -1, 1);
		
		// setup coordinate system (leftx=0, topy=0)
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	void changeScene(Scene scene)
	{
		GdxTest.scene = scene;
	}
}