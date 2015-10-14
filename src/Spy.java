package com.pixulted.chromacannon;

public class Spy extends Man
{
	public Spy(int x, int y)
	{
		//equippedGun = new Rifle();
				type = "Spy";
				health=5;
				MAXHEALTH = health;
				
				int side = (int)( Math.random()*4)+1;
				
				size=3;
				setVelX(2);
				setVelY(2);
				setWalkingSpeed((int) (3*DEVICESCALE*ChromaCannon.SCREENWIDTH));

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
				}
	}
	public void grow()
	{
		size++;
	}
}
