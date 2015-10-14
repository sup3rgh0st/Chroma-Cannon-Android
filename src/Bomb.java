package com.pixulted.chromacannon;

public class Bomb extends Entity
{
	int radius;
	int life;
	
	public Bomb(int x,int y)
	{
		radius=0;
		life=0;
		posX=x;
		posY=y;
	}
	public void updateRadius()
	{
		radius+=10;
	}
	public void updateLife()
	{
		life++;
	}
	public int getRadius(){return radius;}
	public int getLife(){return life;}
}
