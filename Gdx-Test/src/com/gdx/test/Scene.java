package com.gdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

/*
 * A scene is a unique mode of the game.
 * This is a generic scene that contains logic and data common to all possible scenes.
 */
public abstract class Scene
{
	float width;
	float height;
	GL10 gl;
	Input input;
	
	ImmediateModeRenderer renderer;
	
	public Scene()
	{
		renderer = new ImmediateModeRenderer();
	}
	
	public void update(float dt)
	{
		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		input = Gdx.app.getInput();
	}
	
	public void render()
	{
		// update our local reference to the graphics
		gl = Gdx.app.getGraphics().getGL10();
		
		// clear screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// setup coordinate system (leftx=0, topy=0)
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, -1, 1);
	}
	
	void changeScene(Scene scene)
	{
		GdxTest.scene = scene;
	}
	
	void drawRect(float x, float y, float width, float height, float r, float g, float b, float a)
	{
		renderer.begin(GL10.GL_TRIANGLE_STRIP);
		
		renderer.color(r,g,b,a);
		renderer.vertex(x,y,0);
		
		renderer.color(r,g,b,a);
		renderer.vertex(x+width,y,0);
		
		renderer.color(r,g,b,a);
		renderer.vertex(x,y+height,0);
		
		renderer.color(r,g,b,a);
		renderer.vertex(x+width,y+height,0);
		
		renderer.end();
	}
}