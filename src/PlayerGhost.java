package com.pixulted.chromacannon;

//import android.graphics.Color;
import android.graphics.Paint;

//import java.awt.Color;


public class PlayerGhost extends Particle
{
	public PlayerGhost(int x, int y, int life, Paint c)
	{
		setPosX(x);
		setPosY(y);
		setVelX(0);
		setVelY(0);
		health = life;
		color = c;
		dead=false;
	}
	
}
