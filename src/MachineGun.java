package com.pixulted.chromacannon;

//import android.graphics.BitmapFactory;

public class MachineGun extends Gun
{
	public MachineGun()
	{
		super();
		gunLife = 200;
		firingSpeed = 1;
		bulletLife = (int) (50*DEVICESCALE*ChromaCannon.SCREENWIDTH);
		name = "MachineGun";
		MAX_GUN_LIFE=(int) gunLife;
		//setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.blueball));
		//loadTexture(name);
	}
	
}