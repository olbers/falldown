package com.gdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Util {
	public static float getRandomColor(float alpha)
	{
		float r = (float)Math.random();
		float g = (float)Math.random();
		float b = (float)Math.random();
		return Color.toFloatBits(r, g, b, alpha);
	}
	
	public static float[] makeVerts(float left, float top, float right, float bottom, float z, float color)
	{
		return new float[] {
				left,top,z,color,
				right,top,z,color,
				left,bottom,z,color,
				right,bottom,z,color
		};
	}
	
	public static Mesh makeMesh()
	{
		return new Mesh(true, 4, 0, 
				new VertexAttribute(Usage.Position, 3, "a_pos"),
				new VertexAttribute(Usage.ColorPacked, 4, "a_col"));
	}
}
