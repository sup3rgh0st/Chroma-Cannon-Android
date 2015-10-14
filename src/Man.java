package com.pixulted.chromacannon;

public class Man extends Entity
{
	Gun equippedGun;
	boolean dead;
	int walkingSpeed;
	int size;
	String type;
	
	public Man()
	{
		
	}
	
	public void setWalkingSpeed(int a){walkingSpeed=a;}
	
	public int getWalkingSpeed(){return walkingSpeed;}
	public int getSize(){return (int) (size*DEVICESCALE*ChromaCannon.SCREENWIDTH);}
	public String getType(){return type;}
}
