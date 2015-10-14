package com.pixulted.chromacannon;

import android.graphics.Bitmap;




public class Item extends Entity
{
	//BufferedImage texture;
	//Gun gun;
	Bitmap texture;
	int type;
	
	public Item()
	{}
	public Item(int x, int y){
		posX=x;
		posY=y;
	}
	
	public Bitmap getTexture(){return texture;}
	public int getType(){return type;}
	
	public void setTexture(Bitmap a){texture=a;}
	public void setType(int a){type=a;}
	
/*
	public void loadTexture(String ID)
	{
		switch (ID)
		{
        case "Spreader":  
        	try{
        		texture = ImageIO.read(new File("./resources/S_item.png"));}
    		catch (IOException e)
    		{
    			//Map.createMessage("Failed to load "+ID+"'s texture!");
    		}
                 break;
        case "Rifle":  
        	try{
        		texture = ImageIO.read(new File("./resources/Rifle.png"));}
    		catch (IOException e)
    		{
    			//Map.createMessage("Failed to load "+ID+"'s texture!");
    		}
                 break;
                 
		}
	}
	*/
	
}
