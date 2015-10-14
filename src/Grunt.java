package com.pixulted.chromacannon;

public class Grunt extends Man
{
	public Grunt(int x,int y)
	{
		
		//equippedGun = new Rifle();
		type = "Grunt";
		health = 1;
		MAXHEALTH = health;
		
		int side = (int)( Math.random()*4)+1;

		size = 20;
		setVelX(3);
		setVelY(3);
		setWalkingSpeed((int) (7*DEVICESCALE*ChromaCannon.SCREENWIDTH));
		
		switch (side) {
        case 1:  posX=(int)( Math.random()*x) +1;
        		 posY= -5;
                 break;
        case 2:  posX=(int)( Math.random()*x) +1;
		 		 posY= y+5;
		 		 break;
        case 3:  posX=-5;
		 		 posY=(int)( Math.random()*y)+1;
		 		 break;
        case 4:  posX=x+5;
		 		 posY=(int)( Math.random()*y)+1;
		 		 break;
		
		/*
		Point p = new Point();
		display.getSize(p);
		switch (side) {
        case 1:  posX=(int)( Math.random()*p.x) +1;
        		 posY= -5;
                 break;
        case 2:  posX=(int)( Math.random()*p.x) +1;
		 		 posY= p.y+5;
		 		 break;
        case 3:  posX=-5;
		 		 posY=(int)( Math.random()*p.y)+1;
		 		 break;
        case 4:  posX=p.x+5;
		 		 posY=(int)( Math.random()*p.y)+1;
		 		 break;
		 */
		}
	}
}
