package com.gdx.test;

import com.badlogic.gdx.graphics.Color;

public class SceneDeath extends Scene
{
	public SceneDeath()
	{
		super();
		clearColor = new Color(1f,0f,0f,1f);
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
	}
	
}