package com.gdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SceneGame extends Scene
{
	final float tiltMultiplier = 500f;
	final float clickMultiplier = 10f;
	final float deceleration = 700f;
	final float gravity = 1500f;
	final float dropHeight = 300f;
	final float camSpeedIncrement = 10f;
	final float initialCamSpeed = 120f;
	final int numWalls = 30;
	final float wallWidth = 50;
	final float blockSize = 100;
		
	float x, vx, ax;
	float y, vy, ay;
	Mesh block;
	
	Wall[] walls;
	Wall floor = null;
	
	float camY;
	float camSpeed;
	
	int score = 0;
	
	boolean initialized = false;
	
	static SpriteBatch batch;
	static BitmapFont font;

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
			ax = input.getAccelerometerY()*tiltMultiplier;
		}
		else
		{
			// control acceleration using the touched x-coord instead
			if (input.isTouched())
				ax = (input.getX() - screenWidth/2)*clickMultiplier;
			else
				ax = 0;
		}
		
		// apply gravity
		ay = gravity;
		
		// apply friction (deceleration opposite to velocity)
		float decel = -deceleration * Math.signum(vx);
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
		float screenBottom = camY + screenHeight - 1;
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
		if (x > screenWidth-blockSize)
		{
			x = screenWidth-blockSize;
			vx = 0;
		}
		
		// update floor
		if(y > floor.y + wallWidth) //Make sure current floor has been passed before finding a new floor
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
				setWalls(y+dropHeight);
				
				//Increase camera speed
				camSpeed += camSpeedIncrement;
			}
		}
	}

	@Override
	public void render() {
		super.render();
		
		// translate to camera
		gl.glTranslatef(0,-camY, 0);
		
		// draw block
		gl.glTranslatef(x,y-blockSize,0);
		block.render(GL10.GL_TRIANGLE_STRIP);
		gl.glTranslatef(-x,-y+blockSize,0);
		
		// draw walls
		for(Wall wall : walls)
			wall.render();
		
		// draw score
		batch.begin();
		font.draw(batch, "score: "+(int)y, 6, screenHeight);
		batch.end();
	}
	
	public void init() {
		// NOTE: We can only initialize everything once we know the screen dimensions.
		//       That is why this isn't called in the constructor.
		
		initWalls(blockSize);
		initBlock();
		initCam();
		
		initialized = true;
		
		if (batch == null)
			batch = new SpriteBatch();
		
		if (font == null)
			font = new BitmapFont();
	}
	
	private void initWalls(float startY)
	{
		walls = new Wall[numWalls];
		
		for (int i=0; i<walls.length; i++)
		{
			float holeX = (float)Math.random()*(screenWidth-blockSize);
			walls[i] = new Wall(holeX,wallWidth,blockSize,screenWidth);
		}
		
		setWalls(startY);
	}
	
	private void setWalls(float y)
	{
		//Set the colors
		float r = (float)Math.random();
		float g = (float)Math.random();
		float b = (float)Math.random();
		float a = (float)((Math.random() * .5) + .5);
		float color = Color.toFloatBits(r, g, b, a);
		
		float yspace = 2f*blockSize;
		
		for (int i=0; i<walls.length; i++)
		{
			walls[i].set(y, color);
			y += yspace;
			yspace-=5;
		}
		
		floor = walls[0];
	}
	
	private void initBlock()
	{
		x = screenWidth/2 - blockSize/2;
		vx = 0;
		ax = 0;
		
		y = 0;
		vy = 0;
		ay = 0;
		
		block = makeMesh();
		
		float c = Color.toFloatBits(1f, 1f, 1f, 1f);
		block.setVertices(makeVerts(0,0,blockSize,blockSize,c));
	}
	
	private float[] makeVerts(float left, float top, float right, float bottom, float color)
	{
		return new float[] {
				left,top,0,color,
				right,top,0,color,
				left,bottom,0,color,
				right,bottom,0,color
		};
	}
	private Mesh makeMesh()
	{
		return new Mesh(true, 4, 0, 
				new VertexAttribute(Usage.Position, 3, "a_pos"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_col"));
	}
	
	private void initCam()
	{
		camY = -screenHeight/2;
		camSpeed = initialCamSpeed;
	}
}