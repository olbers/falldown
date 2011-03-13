package com.gdx.test;

import com.badlogic.gdx.graphics.Color;

public class SceneTitle extends Scene
{
	public SceneTitle()
	{
		super();
		clearColor = new Color(0f,1f,0f,1f);
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