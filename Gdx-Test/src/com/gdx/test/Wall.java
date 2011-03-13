package com.gdx.test;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Wall {
	public float y;
	public float holeX;
	float wallw;
	float holew;
	float right;
	
	Mesh mesh1, mesh2;
	
	public Wall(float holeX, float wallw, float holew, float right)
	{
		this.holeX = holeX;
		this.right = right;
		this.wallw = wallw;
		this.holew = holew;
		
		mesh1 = makeMesh();
		mesh2 = makeMesh();
	}
	
	public void set(float y, float color)
	{
		this.y = y;
		mesh1.setVertices(makeVerts(0,y,holeX,y+wallw,color));
		mesh2.setVertices(makeVerts(holeX+holew,y,right, y+wallw,color));
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
	
	public void render()
	{
		mesh1.render(GL10.GL_TRIANGLE_STRIP);
		mesh2.render(GL10.GL_TRIANGLE_STRIP);
	}
}
