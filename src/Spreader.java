package com.pixulted.chromacannon;

public class Spreader extends Gun
{
	public Spreader()
	{
		super();
		gunLife = 200;
		firingSpeed = 3;
		bulletLife = (int) (50*DEVICESCALE*ChromaCannon.SCREENWIDTH);
		name = "Spreader";
		MAX_GUN_LIFE=(int) gunLife;
		//setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.blueball));
		//loadTexture(name);
	}
}
