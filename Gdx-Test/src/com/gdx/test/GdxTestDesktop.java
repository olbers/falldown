package com.gdx.test;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class GdxTestDesktop {

	public static void main(String[] args)
	{
		new JoglApplication(new GdxTest(), "Gdx Test", 800, 400, false);
	}
}
