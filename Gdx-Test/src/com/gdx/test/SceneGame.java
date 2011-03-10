package com.gdx.test;

public class SceneGame extends Scene
{
	float x, vx, ax;
	float y, vy, ay;
	float w = 100;
	float wallw = 50;
	
	Wall[] walls;
	Wall floor = null;
	
	float camY;
	float camSpeed = 2f;
	int maxWalls = 30;
	
	float r;
	float g;
	float b;
	float a;
	
	int score = 0;
	
	boolean initialized = false;

	@Override
	public void update(float dt) {
		super.update(dt);
		
		if (!initialized)
			init();
		
		// NOTE: our block physics assume constant acceleration during the last dt seconds
				
		// update block position
		x += vx*dt + 0.5f*ax*dt*dt;
		y += vy*dt + 0.5f*ay*dt*dt;
		
		// update block velocity
		vx += ax*dt;
		vy += ay*dt;
		
		// update block acceleration
		if (input.isAccelerometerAvailable())
		{
			// NOTE: accelerometer units in m/s^2 ranging [-10,10]
			ax = input.getAccelerometerY()*500f;
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
			changeScene(new SceneDeath());
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

	@Override
	public void render() {
		super.render();
		
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
	
	public void init() {
		// NOTE: We can only initialize everything once we know the screen dimensions.
		//       That is why this isn't called in the constructor.
		
		initWalls(w);
		initBlock();
		initCam();
		
		initialized = true;
	}
	
	private void initWalls(float startY)
	{
		float y = startY+10;
		float yspace = 2f*w;
		walls = new Wall[maxWalls];
		for (int i=0; i<walls.length; i++)
		{
			walls[i] = new Wall(y,(float)Math.random()*(width-w));
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
	
	private void initBlock()
	{
		x = width/2 - w/2;
		vx = 0;
		ax = 0;
		
		y = 0;
		vy = 0;
		ay = 0;
	}
	
	private void initCam()
	{
		camY = -height/2;
		camSpeed = 90f;
	}
}