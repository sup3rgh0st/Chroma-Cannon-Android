package com.pixulted.chromacannon;

public class Player extends Man
{
	
	int score;
	//int health;
	int level = 1;
	int charge;
	int slowCharge;
	
	
	Message msg;
	
	public Player()
	{
		System.out.println("[Player] A new Player was initiated");
		setUp();
	}
	public Player(Gun a)
	{
		equippedGun=a;
		System.out.println("[Player] A new Player was initiated holding a "+a.getName());
		setUp();
	}
	public void setUp()
	{
		size=7;
		score=0;

		health=100;
		charge=500;
	}
	
	public int getSlowCharge() {
		return slowCharge;
	}
	public void setSlowCharge(int slowCharge) {
		this.slowCharge = slowCharge;
	}
	public void decaySlowCharge(){
		slowCharge--;
	}
	
	public Gun getEquippedGun(){return equippedGun;}
	public int getScore(){return score;}
	public Message getMessage(){return msg;}
	public int getLevel(){return level;}
	public int getCharge(){return charge;}
	
	public void setEquippedGun(Gun a){equippedGun=a;}
	public void setScore(int a){score=a;}
	public void setMessage(Message a){msg=a;}
	public void levelUp(){
		level++;
		ChromaCannon.backMessage = "Level "+level+" Up!!!";
		ChromaCannon.backMessageX=ChromaCannon.SCREENWIDTH;
	}
	public void setCharge(int a){charge=a;}
	public void addCharge(int a){charge+=a;}
	public void setLevel(int a){level=a;}

}
