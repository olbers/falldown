package com.gdx.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class GdxTest implements ApplicationListener {
	
	Application app;
	GL10 gl; // OpenGL object
	ImmediateModeRenderer renderer; // renderer for drawing geometry on the fly (since I can't load textures..)
	
	float x, vx, ax;
	float y, vy, ay;
	float w = 100;
	float wallw = 50;
	
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
	
	private void resetGame()
	{
		initWalls(w);
		resetBlock();
		resetCam();
		score = 0;
	}
	
	private void resetBlock()
	{
		x = app.getGraphics().getWidth()/2 - w/2;
		vx = 0;
		ax = 0;
		
		y = 0;
		vy = 0;
		ay = 0;
	}
	
	private void resetCam()
	{
		camY = -app.getGraphics().getHeight()/2;
		camSpeed = 90f;
	}
	
	private void updateGame(float dt)
	{
		// NOTE: our block physics assume constant acceleration during the last dt seconds
				
		// update block position
		x += vx*dt + 0.5f*ax*dt*dt;
		y += vy*dt + 0.5f*ay*dt*dt;
		
		// update block velocity
		vx += ax*dt;
		vy += ay*dt;
		
		// update block acceleration
		Input input = app.getInput();
		if (input.isAccelerometerAvailable())
		{
			// NOTE: accelerometer units in m/s^2 ranging [-10,10]
			ax = input.getAccelerometerY()*10f;
		}
		else
		{
			// control acceleration using the touched x-coord instead
			if (input.isTouched())
				ax = (input.getX() - width/2)*10f;
			else
				ax = 0;
		}
		
		// apply gravity
		ay = 1500f;
		
		// apply friction (deceleration opposite to velocity)
		float decel = -1000 * Math.signum(vx);
		if (Math.abs(decel*dt) < Math.abs(vx))
			vx += decel*dt;
		else
			vx = 0;

		// update camera position
		camY += camSpeed*dt;
		
		// constrain block position on floor
		if (floor != null)
		{
			boolean onFloor = y >= floor.y;
			boolean overHole = Math.abs(x-floor.holeX) < 10f;
			
			if (onFloor)
			{
				if (overHole)
				{
					x = floor.holeX;
					vx = 0;
				}
				else
				{
					y = floor.y;
					vy = 0;
				}
			}
		}
			
		// keep block above the bottom of the screen
		float screenBottom = camY + height - 1;
		if (y > screenBottom)
		{
			y = screenBottom;
			vy = camSpeed; // travel with cam to prevent bouncing
		}
		
		// game over if camera overtakes the block
		if (y < camY)
		{
			mode = Mode.DEAD;
			return;
		}
		
		// apply left and right boundaries to block
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
		if(y > floor.y + wallw) //Make sure current floor has been passed before finding a new floor
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
				camSpeed += 10f;
			}
		}
	}
	
	private void renderGame()
	{
		// translate to camera
		gl.glTranslatef(0,-camY, 0);
		
		// draw player
		drawRect(x, y-w, w, w, 1,1,1,1);
		
		// draw walls
		for(Wall wall : walls)
		{
			drawRect(0,wall.y,wall.holeX,wallw,r,g,b,a);
			drawRect(wall.holeX+w,wall.y,width,wallw,r,g,b,a);
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
		
		float dt = app.getGraphics().getDeltaTime();
		
		// render screen depending on the current mode
		if (mode == Mode.TITLE)
		{
			renderTitle();
		}
		else if (mode == Mode.GAME)
		{
			updateGame(dt);
			renderGame();
		}
		else if (mode == Mode.DEAD)
		{
			renderDeath();
		}
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
