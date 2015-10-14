package com.pixulted.chromacannon;

public class Wizard extends Man {

	public Wizard(int x, int y)
	{
		
		//equippedGun = new Rifle();
				type = "Wizard";
				health=1;
				MAXHEALTH = health;
				
				int side = (int)( Math.random()*4)+1;
				
				size = 13;
				setVelX(4);
				setVelY(4);
				setWalkingSpeed((int) (4*DEVICESCALE*ChromaCannon.SCREENWIDTH));
				
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

}
