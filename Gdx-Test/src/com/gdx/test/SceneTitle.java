package com.gdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneTitle extends Scene
{
	Texture logo;
	SpriteBatch batch;
	
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
		
		if (input.isTouched())
		{
			changeScene(new SceneGame());
		}
	}

	@Override
	public void render() {
		super.render();
		batch.begin();
		batch.draw(logo, 
				screenWidth/2 - logo.getWidth()/2,
				screenHeight/2 - logo.getHeight()/2,
				0,0, logo.getWidth(), logo.getHeight());
		batch.end();
	}
}