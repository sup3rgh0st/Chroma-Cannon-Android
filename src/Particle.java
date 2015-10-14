package com.pixulted.chromacannon;

import android.graphics.Paint;

public class Particle extends Entity
{
	
	int orientation;
	
	Paint color;
	boolean hasColor=false;
	
	public Particle()
	{}
	public Particle(int a)
	{health=a;}
	public void move(double slow)
	{
		setPosX((int) (getPosX()+getVelX()*slow));
		setPosY((int) (getPosY()+getVelY()*slow));
	}
	
	public void decay(double slow)
	{
		health-=(slow);
		if(health<=0)
		{
			dead=true;
		}
	}
	
	public double getHealth(){return health;}
	public Paint getColor(){return color;}
	
	public void setColor(Paint a){color=a;}
}
