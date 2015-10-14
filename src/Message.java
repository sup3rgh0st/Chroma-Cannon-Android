package com.pixulted.chromacannon;

public class Message
{
	String message;
	int life;
	boolean dead = false;
	
	public Message(){}
	public Message(String a,int b)
	{
		message=a;
		life=b;
	}
	
	public void decay()
	{life--;}
	public void makeDead()
	{dead=true;}
	public boolean checkDead()
	{return dead;}
	
	public int getLife()
	{return life;}
	
	public String getMessage()
	{
		return message;
	}
}
