package com.gdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneTitle extends Scene
{
	Texture logo;
	SpriteBatch batch;
	
	final float wallWidth = 50;
	final float wallSpace = 80;
	final float holeWidth = 100;
	final float camSpeed = 60f;
	final float alpha = 0.2f;
	
	int numWalls;
	Wall walls[];
	int topWall=0;
	
	float y = 0;
	float camY = 0;
	
	boolean initialized = false;
	
	Mesh screen;
	
	public SceneTitle()
	{
		super();
		clearColor = new Color(0f,0f,0f,1f);
		logo = new Texture(Gdx.files.internal("data/fallingdown.png"));
		batch = new SpriteBatch();
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if (!initialized)
			init();
		
		if (input.isTouched())
		{
			changeScene(new SceneGame());
		}
		
		camY += camSpeed*dt;
		while (walls[topWall].y+wallWidth < camY)
		{
			float holeX = (float)Math.random()*(screenWidth-holeWidth);
			walls[topWall] = new Wall(holeX,wallWidth,holeWidth,screenWidth);
			walls[topWall].set(y, Util.getRandomColor(alpha));
			y+=wallWidth+wallSpace;
			topWall = (topWall+1)%numWalls;
		}
	}
	
	private void init()
	{
		screen = Util.makeMesh();
		
		screen.setVertices(Util.makeVerts(0, 0, screenWidth, screenHeight, -0.1f,
				Color.toFloatBits(0f, 0f, 0f, 0.5f)));
		
		numWalls = (int)(screenHeight / (wallWidth+wallSpace))+1;
		
		walls = new Wall[numWalls];
		for (int i=0; i<numWalls; ++i)
		{
			float holeX = (float)Math.random()*(screenWidth-holeWidth);
			walls[i] = new Wall(holeX,wallWidth,holeWidth,screenWidth);
			walls[i].set(y, Util.getRandomColor(alpha));
			y+=wallWidth+wallSpace;
		}
		
		initialized = true;
	}

	@Override
	public void render() {
		super.render();
		
		gl.glTranslatef(0,-camY,0);
		
		// draw walls
		for (Wall w : walls)
			w.render();
		
		// draw logo
		batch.begin();
		batch.draw(logo, 
				screenWidth/2 - logo.getWidth()/2,
				screenHeight/2 - logo.getHeight()/2,
				0,0, logo.getWidth(), logo.getHeight());
		batch.end();
	}
}