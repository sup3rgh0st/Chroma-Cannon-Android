package com.pixulted.chromacannon;

//import android.graphics.Color;
import android.graphics.Paint;

//import java.awt.Color;

public class Bullet extends Particle
{
	
	public Bullet()
	{
		
	}
	public Bullet(int x,int y,int o)
	{
		orientation = o;
		setPosX(x);
		setPosY(y);
	}
	public Bullet(int x,int y,int a,int b,int h)
	{
		setPosX(x);
		setPosY(y);
		setVelX((int) (a*DEVICESCALE*ChromaCannon.SCREENWIDTH));
		setVelY((int) (b*DEVICESCALE*ChromaCannon.SCREENWIDTH));
		health = h;
	}
	public Bullet(int x,int y,int a,int b,int h,Paint c)//Splash bullets
	{
		setPosX(x);
		setPosY(y);
		setVelX(a);
		setVelY(b);
		health = h;
		color = c;
		hasColor=true;
	}
}
