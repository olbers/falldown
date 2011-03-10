package com.gdx.test;

public class SceneTitle extends Scene
{
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
		
		// green background
		drawRect(0,0,width,height,0,1,0,1);
	}
}