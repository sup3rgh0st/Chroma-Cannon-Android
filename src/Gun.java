package com.pixulted.chromacannon;

public class Gun extends Item
{
	int firingSpeed;
	int bulletLife;
	double gunLife;
	double coolDown;
	int MAX_GUN_LIFE;
	
	String name;
	
	public Gun()
	{
		posX = 33;//null
		posY = 33;//null
		type = 1;
	}
	
	public void setName(String a){name=a;}
	public void setBulletLife(int a){bulletLife=a;}
	public void setFiringSpeed(int a){firingSpeed=a;}
	//public void updateCoolDown(){coolDown--;}
	public void setCoolDown(double a){coolDown=a;}
	public void subtractCoolDown(double a){coolDown-=a;}
	public void setGunLife(double a){gunLife=a;}
	public void subtractGunLife(double a){gunLife-=a;}
	
	public double getGunLife(){return gunLife;}
	public String getName(){return name;}
	public int getBulletLife(){return bulletLife;}
	public int getFiringSpeed(){return firingSpeed;}
	public double getCoolDown(){return coolDown;}
	public int getMAXGUNLIFE(){return MAX_GUN_LIFE;}
}
