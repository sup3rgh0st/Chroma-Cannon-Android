package com.pixulted.chromacannon;

public class Rifle extends Gun
{
	public Rifle()
	{
		super();
		gunLife=Integer.MAX_VALUE;
		firingSpeed = 5;
		bulletLife = (int) (50*DEVICESCALE*ChromaCannon.SCREENWIDTH);
		name = "Rifle";
		MAX_GUN_LIFE=(int) gunLife;
		//loadTexture(name);
	}
	
}
