package com.gdx.test;

public class SceneDeath extends Scene
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
		
		// red background
		drawRect(0,0,width,height,1,0,0,1);
	}
	
}