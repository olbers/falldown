package com.gdx.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

//TREY IS PUTTING THIS HERE TO TEST THE COMMIT CHANGES FEATURE

public class GdxTest implements ApplicationListener {
	
	Application app;
	GL10 gl; // OpenGL object
	ImmediateModeRenderer renderer; // renderer for drawing geometry on the fly (since I can't load textures..)
	
	float x, vx;
	float y, vy;
	float w = 100;
	
	Wall[] walls;
	Wall floor = null;
	
	float camY;
	float camSpeed = 2f;
	
	int highscore=0;
	int score = 0;
	int maxWalls = 30;
	
	float width, height;
	
	float r;
	float g;
	float b;
	float a;
	
	enum Mode
	{
		TITLE,
		GAME,
		DEAD
	};
	Mode mode = Mode.TITLE;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		app = Gdx.app;
		
		gl = Gdx.graphics.getGL10();
		renderer = new ImmediateModeRenderer();
		
		resetGame();
	}
	
	private void initWalls(float startY)
	{
		float y = startY+10;
		float yspace = 2f*w;
		walls = new Wall[maxWalls];
		for (int i=0; i<walls.length; i++)
		{
			walls[i] = new Wall(y,(float)Math.random()*(app.getGraphics().getWidth()-w));
			y += yspace;
			yspace-=5;
		}
		floor = walls[0];
		
		//Set the colors
		r = (float)Math.random();
		g = (float)Math.random();
		b = (float)Math.random();
		a = (float)((Math.random() * .5) + .5);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	private void resetGame()
	{
		initWalls(w);
		
		x = app.getGraphics().getWidth()/2;
		vx = 0;
		y = 0;
		vy = 0;
		
		camY = -app.getGraphics().getHeight()/2;
		score = 0;
	}
	
	private void renderGame()
	{
		// friction
		vx *= 0.95f;
		
		float accScale = 0.2f;
		vx += app.getInput().getAccelerometerY()*accScale;
		
		// gravity
		vy += 1f;
		
		// constrain position on floor
		if (floor != null)
		{
			boolean onFloor = y >= floor.y;
			if(onFloor && x > (floor.holeX - 10) && x < (floor.holeX + 10))
			{
				vx = 0;
			}
			else if (onFloor)
			{
				y = floor.y;
				vy = 0;
			}
		}
		
		// update position
		x += vx;
		y += vy;
		
		// check if block hits the top of the screen
		if (y < camY)
		{
			camSpeed = 2f; //Reset the cam speed, the game is over
			mode = Mode.DEAD;
			return;
		}
		
		// constrain box to screen
		if (x < 0)
		{
			x = 0;
			vx = 0;
		}
		if (x > width-w)
		{
			x = width-w;
			vx = 0;
		}
		
		// update floor
		if(y > floor.y + 50) //Make sure current floor has been passed before finding a new floor
		{
			floor = null;
			for(int i=0; i<walls.length; i++)
			{
				Wall wall = walls[i];
				if (y <= wall.y)
				{
					floor = wall;
					break;
				}
			}
			if(floor == null) //If we didn't find a new floor generate more floors
			{
				//TODO:  Fix this with LEGIT code :D
				initWalls(y+300);
				
				//Increase camera speed
				camSpeed += .1f;
			}
		}
		
		// translate to camera
		gl.glTranslatef(0,-camY, 0);
		camY += camSpeed;
		
		// draw player
		drawRect(x, y-w, w, w, 1,1,1,1);
		
		// draw walls
		for(Wall wall : walls)
		{
			drawRect(0,wall.y,wall.holeX,50,r,g,b,a);
			drawRect(wall.holeX+w,wall.y,width,50,r,g,b,a);
		}
	}
	
	private void renderTitle()
	{
		// green background
		drawRect(0,0,width,height,0,1,0,1);
		
		if (app.getInput().isTouched())
		{
			resetGame();
			mode = Mode.GAME;
		}
	}
	
	private void renderDeath()
	{
		// red background
		drawRect(0,0,width,height,1,0,0,1);
		
		if (app.getInput().isTouched())
		{
			resetGame();
			mode = Mode.GAME;
		}
	}

	@Override
	public void render() {
		gl = Gdx.graphics.getGL10();
		
		// clear screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// setup coordinate system (leftx=0, topy=0)
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, -1, 1);
		
		// render screen depending on the current mode
		if (mode == Mode.TITLE)
			renderTitle();
		else if (mode == Mode.GAME)
			renderGame();
		else if (mode == Mode.DEAD)
			renderDeath();
	}
	
	private void drawRect(float x, float y, float width, float height, float r, float g, float b, float a)
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

	@Override
	public void resize(int width, int height) 
	{
		// update our screen size variables
		this.width = width;
		this.height = height;
		
		// set OpenGL viewport
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
