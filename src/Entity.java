package com.pixulted.chromacannon;

public class Entity
{
	//final static double DEVICESCALE = 0.00078125;
	final static double DEVICESCALE = 0.00125;
	
	int posX;
	int posY;
	
	double health;
	double MAXHEALTH;
	
	int velX;
	int velY;
	
	boolean dead = false;
	
	public Entity()
	{
		
	}
	
	
	public int getPosX(){return posX;}
	public int getPosY(){return posY;}
	public int getVelX(){return velX;}
	public int getVelY(){return velY;}
	
	public double getHealth(){return health;}
	public double getMAXHEALTH(){return MAXHEALTH;}
	public void setHealth(int a){health=a;}
	public void subtractHealth(double a){health-=a;}
	
	public void setPosX(int a){posX=a;}
	public void setPosY(int a){posY=a;}
	public void setVelX(int a){velX=a;}
	public void setVelY(int a){velY=a;}
	
	public void makeDead()
	{dead=true;}
	public boolean checkDead()
	{return dead;}
	
}
