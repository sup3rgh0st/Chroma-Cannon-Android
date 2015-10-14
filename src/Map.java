package com.pixulted.chromacannon;
/*ChromaCannon
 * 
 * Ryan Magliola
 */
/*

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Map extends JPanel implements Runnable
{
	static boolean[] keys = new boolean[155];
	static List<PlayerGhost> activeGhosts = new CopyOnWriteArrayList<PlayerGhost>();
	static List<Bullet> activeBullets = new CopyOnWriteArrayList<Bullet>();
	static List<Man> activeEnemies = new CopyOnWriteArrayList<Man>();
	static List<Message> activeMessages = new CopyOnWriteArrayList<Message>();
	static List<Item> activeItems = new CopyOnWriteArrayList<Item>();
	static List<Bomb> activeBombs = new CopyOnWriteArrayList<Bomb>();
	
	private Font font_10 = new Font("Kozuka_Gothic_Pr6N_L", Font.PLAIN, 10);
	private Font font_15 = new Font("Kozuka_Gothic_Pr6N_L", Font.PLAIN, 15);
	private Font font_35 = new Font("Kozuka_Gothic_Pr6N_L", Font.PLAIN, 35);
	
	final byte P1_UP = KeyEvent.VK_W;
	final byte P1_DOWN = KeyEvent.VK_S;
	final byte P1_LEFT = KeyEvent.VK_A;
	final byte P1_RIGHT = KeyEvent.VK_D;
	final byte P1_SHOTUP = KeyEvent.VK_UP;
	final byte P1_SHOTDOWN = KeyEvent.VK_DOWN;
	final byte P1_SHOTLEFT = KeyEvent.VK_LEFT;
	final byte P1_SHOTRIGHT = KeyEvent.VK_RIGHT;
	
	final byte DEBUG = KeyEvent.VK_P;
	final byte ESCAPE = KeyEvent.VK_ESCAPE;
	final byte RESET = KeyEvent.VK_SPACE;
	final byte SHIFT = KeyEvent.VK_SHIFT;
	final byte SPAWN = KeyEvent.VK_Q;
	final byte MOVE = KeyEvent.VK_E;
	final byte SHIFTFILL = KeyEvent.VK_R;
	final byte MISC = KeyEvent.VK_X;
	
	Color BG = Color.BLUE;
	int BG_Red;
	int BG_Green;
	int BG_Blue;
	int BG_Step;
	final int INCREMENT = (3 * (10/3));
	
	final int MAX_HEALTH = 200;
	boolean shooting=false;
	boolean slow=false;
	int highScore;
	int tries;
	double timeAlive=0;
	
	byte waitSkip = 10;
	
	boolean debug=false;
	boolean stop=true;
	boolean firstTick=true;
	boolean makeRush=false;
	int rushSize;
	
	BufferedImage shiftBar = null;
	
	Sound cantBeat= new Sound("F_Shy.au");
	Sound wonders= new Sound("Wonders.wav");
	Sound wondersS= new Sound("WondersSS.wav");
	Sound ohComeOn= new Sound("OhComeOn.wav");
	
	public Map()
	{
		BG_Red= 255;
		BG_Green= 0;
		BG_Blue= 0;
		BG_Step= 1;
		
		highScore=0;
		tries=0;
		
		System.out.println("[Map] A new Map was initiated");
		createMessage("(c) Ryan Magliola - wip - version:"+Start.version);
		createMessage("Music by AbranteDr | SFX (c) Hasbro");
		for(int i=0;i<keys.length;i++)
		{keys[i]=false;}
		
		setFocusable(true);
		requestFocusInWindow();
	      
	      //KeyListener
	      addKeyListener(new KeyAdapter() {
	         @Override
	         public void keyTyped(KeyEvent e) {}

	         @Override
	         public void keyReleased(KeyEvent e) {
	            removeKeyPressHandler(e.getKeyCode());
	         }

	         @Override
	         public void keyPressed(KeyEvent e) {
	            addKeyPressHandler(e.getKeyCode());
	         }
	      });
	      //End of KeyListener
	      initPlayers();
	      
	}
	Player p1;
	public void initPlayers()
	{
		p1 = new Player(new Rifle());
		p1.setPosX(1280/2);
		p1.setPosY(720/2);
		p1.setVelX(4);
		p1.setVelY(4);
		p1.setHealth(MAX_HEALTH);
		p1.setMessage(null);
		p1.setScore(0);
		p1.setLevel(1);
		createMessage("Player One Joined the game!");
	}
	public void reset()
	{
		createMessage("Resetting...");
		
		initPlayers();
		timeAlive=0;
		activeBullets.clear();
		activeEnemies.clear();
		activeItems.clear();
		activeGhosts.clear();
		stop=false;
		firstTick=true;
		debug=false;
		createMessage("Game Restarted!");
		addTry();
	}
	
	public void addKeyPressHandler(int a)
	{keys[a]=true;}
	
	public void removeKeyPressHandler(int a)
	{keys[a]=false;}
	
	public boolean isPressed(int a)
	{return keys[a];}
	
	public void paint(Graphics g)
	{
		g.setFont(font_10);
		
		//Background Shake
		if(shooting==true) //&& !slow)
		{
			int m = 4+(Math.abs(p1.getHealth()-200)/20);
			int sX = (int)(Math.random()*(2*m)-m);
			int sY = (int)(Math.random()*(2*m)-m);
			g.translate(sX, sY);
		}
		
		
		//Background Color
		if(startup==false)
			g.setColor(Color.RED);
		else
			g.setColor(Color.WHITE);
		
		if(stop==false)
		{
			switch(BG_Step) //INCREMENT --> Speed of Color Change
			{
			case(1):
				BG_Green+=INCREMENT;
				if(BG_Green>=255){
					BG_Step++;
					BG_Green=255;}
				break;
			case(2):
				BG_Red-=INCREMENT;
				if(BG_Red<=0){
					BG_Step++;
					BG_Red=0;}
				break;
			case(3):
				BG_Blue+=INCREMENT;
				if(BG_Blue>=255){
					BG_Step++;
					BG_Blue=255;}
				break;
			case(4):
				BG_Green-=INCREMENT;
				if(BG_Green<=0){
					BG_Step++;
					BG_Green=0;}
				break;
			case(5):
				BG_Red+=INCREMENT;
				if(BG_Red>=255){
					BG_Step++;
					BG_Red=255;}
				break;
			case(6):
				BG_Blue-=INCREMENT;
				if(BG_Blue<=0){
					BG_Step=1;
					BG_Blue=0;}
				break;
			}
			BG = new Color(BG_Red,BG_Green,BG_Blue);
			g.setColor(BG);
		}
		
		//if(!slow)
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);	
		try{
			for(Item a:activeItems)//Draw Items
			{
				g.drawImage(a.getTexture(), a.getPosX()-(a.getTexture().getWidth()/2), a.getPosY()-(a.getTexture().getHeight()/2),40,40,null);
			}
		}
		catch(ConcurrentModificationException e)
		{
			createMessage("An unimportant error with items has occured!",200);
		}
		
		try{
			for(PlayerGhost a:activeGhosts)//Draws Player Ghosts 
			{
				g.setColor(a.getColor());
				g.fillRect(a.getPosX()-p1.getSize(), a.getPosY()-p1.getSize(), p1.getSize()*2, p1.getSize()*2);
				a.decay();
				if(a.getHealth()<=0)
					a.dead=true;
			}
		}
		catch(ConcurrentModificationException e)
		{
			createMessage("An unimportant error with player ghosts has occured!",200);
		}
		g.setColor(Color.BLACK);
		try{
			for(Bullet a:activeBullets)//Draw Bullets
			{
				if(a.hasColor){
					g.setColor(a.getColor());
					g.fillRect(a.getPosX()-8, a.getPosY()-8, 16, 16);
				}
				else{
					g.setColor(Color.BLACK);
					g.drawRect(a.getPosX()-1, a.getPosY()-1, 2, 2);
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			createMessage("An unimportant error with bullets has occured!",200);
		}
		//Draw Player
		g.setColor(Color.BLACK);
		g.drawRect(p1.getPosX()-p1.getSize(), p1.getPosY()-p1.getSize(), p1.getSize()*2, p1.getSize()*2);
		
		g.setColor(getInverseColor(BG));
		try{
			for(Man a:activeEnemies)//Draw Enemies
			{
				if(a.getType()=="Wizard")// Are you a Wizard?!
				{
					g.drawRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getSize()*2, a.getSize()*2);
				}
				else
				{
					g.fillRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getSize()*2, a.getSize()*2);
					g.setColor(Color.BLACK);
					g.drawRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getSize()*2, a.getSize()*2);
					g.setColor(getInverseColor(BG));
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			createMessage("An unimportant error with activeEnemies has occured!",200);
		}
		g.setColor(Color.BLACK);
		//Draw startup stuff
		if(startup==true)
		{
			BufferedImage controls = null;
			BufferedImage you = null;
			BufferedImage powerHealth = null;
			BufferedImage controlsShift = null;
			
			URL url1 = this.getClass().getClassLoader().getResource("controls.png");
			URL url2 = this.getClass().getClassLoader().getResource("you.png");
			URL url3 = this.getClass().getClassLoader().getResource("shiftBar.png");
			URL url4 = this.getClass().getClassLoader().getResource("PowerHealth.png");
			URL url5 = this.getClass().getClassLoader().getResource("controls_Shift.png");
			
			try{
			    controls = ImageIO.read(url1);
			    you      = ImageIO.read(url2);
			    shiftBar = ImageIO.read(url3);
			    powerHealth = ImageIO.read(url4);
			    controlsShift = ImageIO.read(url5);
			}
    		catch (IOException e){System.out.println("[Map] Failed to load crutial textures!");Map.createMessage("Failed to load crutial textures!");}
			
			g.drawImage(controls, 640-(controls.getWidth()/2), 50, null);
			g.drawImage(you, 640-(you.getWidth()/2)-104, 333, null);
			g.drawImage(controlsShift, 640-(controlsShift.getWidth()/2),450,null);
			g.drawImage(powerHealth,905,11,null);
			
			g.setFont(font_15);
			//g.drawString("(c) Ryan Magliola - wip - version:"+Start.version, 20, 655);
			//g.drawString("The following is a work in progress, and does not represent the final product.", 20, 671);
			//g.setFont(font_20);
			g.drawString("Build Code: "+Start.BUILDNAME, 12, 680);
			
			g.setFont(font_10);
		}
		//Shift bar
		int shiftHold = p1.getCharge();
		g.setColor(Color.CYAN);
		g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10,(int)Math.min((int) (shiftHold*0.162), 162), 30);
		if(shiftHold > 1000)
		{
			shiftHold-=1000;
			g.setColor(Color.GREEN);
			g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10,(int)Math.min((int) (shiftHold*0.162), 162), 30);
		}
		if(shiftHold > 1000)
		{
			shiftHold-=1000;
			g.setColor(Color.YELLOW);
			g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10,(int)Math.min((int) (shiftHold*0.162), 162), 30);
		}
		if(shiftHold > 1000)
		{
			shiftHold-=1000;
			g.setColor(Color.ORANGE);
			g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10,(int)Math.min((int) (shiftHold*0.162), 162), 30);
		}
		if(shiftHold > 1000)
		{
			shiftHold-=1000;
			g.setColor(Color.RED);
			g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10,(int)Math.min((int) (shiftHold*0.162), 162), 30);
		}
		if(shiftHold > 1000)
		{
			g.setColor(this.getInverseColor(BG));
			g.fillRect(this.getWidth()-shiftBar.getWidth()+5,10,162,30);
		}
			
		g.drawImage(shiftBar, this.getWidth()-shiftBar.getWidth(), 5, null);
		
		//Health Bar
		g.setColor(Color.RED);
		g.fillRect(this.getWidth()-shiftBar.getWidth()+5, 10+45,(int)Math.min((int) (p1.getHealth()/1.23), 162), 30);
		g.drawImage(shiftBar, this.getWidth()-shiftBar.getWidth(), 5+45, null);

		//Static Debug Info
		g.setColor(Color.BLACK);
		g.drawString("FPS: "+fps,20, 20);//+" Slow="+slow, 20, 20);
		g.drawString("Player 1 Score: "+p1.getScore()+"  Level: "+p1.getLevel(), 20, 30);
		g.drawString("Player 1 Health: "+p1.getHealth()+" Charge: "+p1.getCharge(), 20, 40);
		//g.drawString("ActiveEnemies: "+activeEnemies.size()+" ActiveParticles: "+activeBullets.size(), 20, 50);
		
		//Draw Player Messages
		if(p1.getMessage()!=null)
		{
			g.setFont(font_15);
			g.drawString(p1.getMessage().getMessage(), p1.getPosX()-7, p1.getPosY()-15);
			g.drawString("Score:"+p1.getScore(), p1.getPosX()-7, p1.getPosY()+25);
			p1.getMessage().decay();
			if(p1.getMessage().getLife() <=0)
				p1.setMessage(null);
			g.setFont(font_10);
		}
		
		//Draw Console Messages
		int y=0;
		try
		{
			for(Message a:activeMessages)
			{
				g.drawString(a.getMessage(), 20, 50+(y*10));
				a.decay();
				if(a.getLife() <=0)
					a.makeDead();
				y++;
			}
		}
		catch(ConcurrentModificationException e)
		{
			createMessage("An unimportant error with activeMessages has occured!",200);
		}
		//Debug
		if(debug){
			g.drawString("Debug Mode enabled!", 500, 50);
			g.drawString("(c) Ryan Magliola", 505, 60);
			g.drawString("Q to spawn enemy", 505, 70);
			g.drawString("E to force Move()", 505, 80);
			g.drawString("R to fill Shift Power", 505, 90);
		}
		
		//Final Score Generator
		int finalScore=-1;
		if(p1.getHealth()<=0)
		{
			if(finalScore==-1)
				finalScore=p1.getScore();
			
			if(finalScore>getHighScore())
				setHighScore(finalScore);
			
			g.setColor(new Color(0,0,0,0.5f));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			
			g.setFont(font_35);
			
			final int SHAKE = 3;
			final int ALPHA = 255;
			
			g.setColor(new Color(0,0,0,0.5f));
			int sX = (int)(Math.random()*(SHAKE*2)-SHAKE);
			int sY = (int)(Math.random()*(SHAKE*2)-SHAKE);
			g.drawString("GAME OVER", 350+sX, 350+sY);
			g.drawString("SCORE: "+finalScore, 350+sX, 400+sY);
			g.drawString("HIGH SCORE: "+getHighScore(), 350+sX, 450+sY);
			g.drawString("ATTEMPT #: "+getTries(), 350+sX, 500+sY);
			g.drawString("SECONDS ALIVE : "+timeAlive/100, 350+sX, 550+sY);
			
			g.setColor(new Color(255,0,0,ALPHA));
			sX = (int)(Math.random()*(SHAKE*2)-SHAKE);
			sY = (int)(Math.random()*(SHAKE*2)-SHAKE);
			g.drawString("GAME OVER", 350+sX, 350+sY);
			g.drawString("SCORE: "+finalScore, 350+sX, 400+sY);
			g.drawString("HIGH SCORE: "+getHighScore(), 350+sX, 450+sY);
			g.drawString("ATTEMPT #: "+getTries(), 350+sX, 500+sY);
			g.drawString("SECONDS ALIVE : "+timeAlive/100, 350+sX, 550+sY);
			
			g.setColor(new Color(0,255,0,ALPHA));
			sX = (int)(Math.random()*(SHAKE*2)-SHAKE);
			sY = (int)(Math.random()*(SHAKE*2)-SHAKE);
			g.drawString("GAME OVER", 350+sX, 350+sY);
			g.drawString("SCORE: "+finalScore, 350+sX, 400+sY);
			g.drawString("HIGH SCORE: "+getHighScore(), 350+sX, 450+sY);
			g.drawString("ATTEMPT #: "+getTries(), 350+sX, 500+sY);
			g.drawString("SECONDS ALIVE : "+timeAlive/100, 350+sX, 550+sY);
			
			g.setColor(new Color(0,0,255,ALPHA));
			sX = (int)(Math.random()*(SHAKE*2)-SHAKE);
			sY = (int)(Math.random()*(SHAKE*2)-SHAKE);
			g.drawString("GAME OVER", 350+sX, 350+sY);
			g.drawString("SCORE: "+finalScore, 350+sX, 400+sY);
			g.drawString("HIGH SCORE: "+getHighScore(), 350+sX, 450+sY);
			g.drawString("ATTEMPT #: "+getTries(), 350+sX, 500+sY);
			g.drawString("SECONDS ALIVE : "+timeAlive/100, 350+sX, 550+sY);
			
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER", 350, 350);
			g.drawString("SCORE: "+finalScore, 350, 400);
			g.drawString("HIGH SCORE: "+getHighScore(), 350, 450);
			g.drawString("ATTEMPT #: "+getTries(), 350, 500);
			g.drawString("SECONDS ALIVE : "+timeAlive/100, 350, 550);
			
			g.drawString("Press SPACE to Restart",450+sX,250+sY);
			
			
			
			stop = true;
			if(firstTick)
			{
				firstTick=false;
				ohComeOn.getClip().setMicrosecondPosition(0);
				ohComeOn.getClip().start();
				createSplash(p1.getPosX(),p1.getPosY(),300);
				createSplash(p1.getPosX(),p1.getPosY(),150);
				createSplash(p1.getPosX(),p1.getPosY(),100);
				createSplash(p1.getPosX(),p1.getPosY(),50);
				createSplash(p1.getPosX(),p1.getPosY(),10);
			}
		}
		
		//FrameSkip Graph
		if(debug)
			frameGraph(g);
		
	}
	BufferedImage frameGraph = new BufferedImage(100,101,BufferedImage.TYPE_INT_ARGB);
	int[][] previousFPS = new int[100][2];
	public void frameGraph(Graphics g)
	{
		for(int i=0;i<previousFPS.length-1;i++)
		{
				previousFPS[i][0]=previousFPS[i+1][0];
				previousFPS[i][1]=previousFPS[i+1][1];
		}
		if(fps>=0 && fps <=100)
			previousFPS[99][0]=fps;
		
		previousFPS[99][1]=waitSkip;
		frameGraph= new BufferedImage(100,101,BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0;i<100;i++)
		{
			frameGraph.setRGB(i, Math.abs(previousFPS[i][0]-100), Color.BLUE.getRGB());
			frameGraph.setRGB(i, Math.abs(previousFPS[i][1]-100), Color.GREEN.getRGB());
		}
		g.drawRect(this.getWidth()-100*2 -1, this.getHeight()- 101*2-1, 100*2, 101*2);
		g.drawString("debug", this.getWidth()-100*2 +3, this.getHeight()- 101*2+10);
		g.drawImage(frameGraph, this.getWidth()-100*2, this.getHeight()- 101*2, 100*2, 101*2, null);
	}
	@SuppressWarnings("static-access")
	public void actionKeyPressHandler()
	{
		boolean bulletCreated = false;
		shooting = false;
		//slow = false;
		int bulletVelX = 0;
		int bulletVelY = 0;
		
		if(stop==false)
		{
		if(isPressed(P1_UP))// W
		{
			p1.setPosY(p1.getPosY()-p1.getVelY());
		}
		if(isPressed(P1_DOWN))// S
		{
			p1.setPosY(p1.getPosY()+p1.getVelY());
		}
		if(isPressed(P1_LEFT))// A
		{
			p1.setPosX(p1.getPosX()-p1.getVelX());
		}
		if(isPressed(P1_RIGHT))// D
		{
			p1.setPosX(p1.getPosX()+p1.getVelX());
		}
		if(isPressed(P1_SHOTUP))// Arrow_UP
		{
			bulletVelY += -5-p1.getVelY();
			bulletCreated = true;
			shooting = true;
		}
		if(isPressed(P1_SHOTDOWN))// Arrow_DOWN
		{
			bulletVelY += 5+p1.getVelY();
			bulletCreated = true;
			shooting = true;
		}
		if(isPressed(P1_SHOTRIGHT))// Arrow_RIGHT
		{
			bulletVelX += 5+p1.getVelX();
			bulletCreated = true;
			shooting = true;
		}
		if(isPressed(P1_SHOTLEFT))// Arrow_LEFT
		{
			bulletVelX += -5-p1.getVelX();
			bulletCreated = true;
			shooting = true;
		}
		}
		if(bulletCreated)
		{
			if(!(bulletVelX==0 && bulletVelY==0))
			activeBullets.add(new Bullet(p1.getPosX(),p1.getPosY(),bulletVelX,bulletVelY,p1.getEquippedGun().getBulletLife()));
		}
		 
		if(isPressed(RESET))
		{
			if(stop==true)
			{
				System.out.println("[Map] Restarting...");
				reset();
			}
			else//Bomb! TODO
			{
				if(p1.getCharge()>(Math.max(1000,1000*p1.getLevel()/10)) && activeBombs.size()==0)
				{
					p1.addCharge(Math.min(-1000,-1000*p1.getLevel()/10));
					activeBombs.add(new Bomb(p1.getPosX(),p1.getPosY()));
				}
			}
		}
		if(isPressed(ESCAPE))
		{
			System.out.println("[Map] Escape Detected! Closing Program!");
			System.exit(0);
		}
		if(isPressed(DEBUG))
		{
			
("Debug Mode activated!");
			debug=true;
		}
		if(isPressed(SPAWN))
		{
			addEnemey();
		}
		if(isPressed(MOVE))
		{
			if(debug)
			{
				//updateParticles();
				aiHandler();
			}
		}
		if(isPressed(MISC))
		{
			activeEnemies.add(new Wizard());
		}
		if(isPressed(SHIFTFILL))
		{
			if(debug)
				p1.addCharge(5);
		}
		if((isPressed(SHIFT) && p1.getCharge()>10) || (isPressed(SHIFT) && slow==true && p1.getCharge()>0))
		{
			slow = true;
			if(wonders.getClip().isRunning())
			{
				long pos = wonders.getClip().getMicrosecondPosition()%wonders.getLength();
				wonders.getClip().stop();
				wondersS.getClip().setMicrosecondPosition(pos*4);
				wondersS.getClip().loop(wondersS.getClip().LOOP_CONTINUOUSLY);
			}
		}
		else
		{
			slow = false;
			if(wondersS.getClip().isRunning())
			{
				long pos = wondersS.getClip().getMicrosecondPosition()%wondersS.getLength();
				wondersS.getClip().stop();
				wonders.getClip().setMicrosecondPosition(pos/4);
				wonders.getClip().loop(wonders.getClip().LOOP_CONTINUOUSLY);
			}
			
		}
	}
	
	public static void createMessage(String a,int b) //Custom
	{activeMessages.add(new Message(a,b));}
	public static void createMessage(String a) //System General
	{activeMessages.add(new Message(a,50));}
	
	public void updateParticles()
	{
		for(Bullet a:activeBullets)
		{
			a.move();
			if(a.getPosX()<-10||a.getPosX()>this.getWidth()+10)
				a.makeDead();
			if(a.getPosY()<-10||a.getPosY()>this.getHeight()+10)
				a.makeDead();
			a.decay();
		}
	}
	public void removeDeadParticles()
	{
		for(int i=activeBullets.size()-1;i>=0;i--)
		{
			if(activeBullets.get(i).checkDead())
			{
				activeBullets.remove(i);
			}
		}
		for(int i=activeEnemies.size()-1;i>=0;i--)
		{
			if(activeEnemies.get(i).checkDead())
			{
				createMessage("A "+activeEnemies.get(i).getType()+" was killed!");
				if(activeEnemies.get(i).getType()=="Wizard")
				{
					p1.addCharge(500);
					createSplash(activeEnemies.get(i).getPosX(),activeEnemies.get(i).getPosY(),50);
				}
				activeEnemies.remove(i);
				p1.addCharge(3);
				
			}
		}
		for(int i=activeMessages.size()-1;i>=0;i--)
		{
			if(activeMessages.get(i).checkDead())
			{
				activeMessages.remove(i);
			}
		}
		for(int i=activeGhosts.size()-1;i>=0;i--)
		{
			if(activeGhosts.get(i).checkDead())
			{
				activeGhosts.remove(i);
			}
		}
		for(int i=activeBombs.size()-1;i>=0;i--)
		{
			if(activeBombs.get(i).checkDead())
			{
				activeBombs.remove(i);
			}
		}
	}
	public void createSplash(int x,int y, int r)
	{
		System.out.println("[Map] Creating Splash @X:"+x+" Y:"+y);
		double dist;
		int rnd;
		for(int scanX=x-r;scanX<x+r;scanX++)
		{
			for(int scanY=y-r;scanY<y+r;scanY++)
			{
				
				dist = Math.sqrt( Math.pow(scanX-x, 2) + Math.pow(scanY-y, 2) );
				if(dist<r)
				{
					
					rnd=(int)(Math.random()*(r*25)+Math.abs(r-dist));
					if(rnd==r)
					{
						activeGhosts.add(new PlayerGhost(scanX,scanY,200,getRandomColor()));
					}
				}
			}
		}
	}
	public void createBombSplash(Bomb b)
	{
		int x=b.getPosX();
		int y=b.getPosY();
		int temp=0;
		
		b.updateLife();
		Color color = getInverseColor(BG);
		
		for(int ang=0;ang<360;ang+=2)
		{
			x= (int) Math.round(b.getPosX()+(Math.cos(ang)*b.getRadius()));
			y= (int) Math.round(b.getPosY()+(Math.sin(ang)*b.getRadius()));
			temp= (int) (Math.random()*b.getLife());
			
			activeBullets.add(new Bullet(x,y,0,0,1,color));
			activeGhosts.add(new PlayerGhost(x,y,10,color));
			
			if(temp==1)
				activeGhosts.add(new PlayerGhost(x,y,150,color));
		}
		b.updateRadius();
		if(b.getRadius()>300)
			b.makeDead();
	}
	public void addEnemey()
	{
		int a =(int) (Math.random()*3+1);
		if(a==1){
			activeEnemies.add(new Spy());
		}
		else{
			activeEnemies.add(new Grunt());
		}
		
		a =(int) (Math.random()*750+1);
		if(a==50){
			activeEnemies.add(new Wizard());
		}
	}
	public Color getRandomColor()
	{
		return new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254));
	}
	public void addRush(int a)
	{
		createMessage(a+" enemy RUSH!");
		for(int i=0;i<a;i++){
			addEnemey();
			activeEnemies.size();
		}
		makeRush=false;
		rushSize=0;
	}
	public void aiHandler()
	{
		if(activeEnemies.size() < p1.getLevel()*2) //Spawn Enemies
		{
			addEnemey();
		}
		for(Man a:activeEnemies) //Set Velocity of Enemies
		{
			int distX = a.getPosX()- p1.getPosX();
			int distY = a.getPosY()- p1.getPosY();
			
			double distZ =Math.sqrt( (Math.pow(distX,2.0)+Math.pow(distY,2.0)) );
			
			if(a.getPosY()>p1.getPosY()){ //Player NORTH of Enemy
				a.setVelY((int)(distY/(distZ/a.getWalkingSpeed()))*-1  );}
			else{                         //Player SOUTH of Enemy
				a.setVelY(Math.abs((int)(distY/(distZ/a.getWalkingSpeed()))));}
			
			if(a.getPosX()>p1.getPosX()){ //Player WEST of Enemy
				a.setVelX((int) (distX/(distZ/a.getWalkingSpeed()))*-1 );}
			else{                         //Player EAST of Enemy
				a.setVelX(Math.abs((int)(distX/(distZ/a.getWalkingSpeed())) ));}
			
			a.setPosX(a.getPosX()+a.getVelX());
			a.setPosY(a.getPosY()+a.getVelY());
			
		}
	}
	public void checkBulletCollision()
	{
		boolean createSplash = false;
		final int SPLASHRANGE = 30;
		int splashPosX=0;
		int splashPosY=0;
		int splashVelX=0;
		int splashVelY=0;
		
		for(Man a:activeEnemies)
		{
			for(Particle b:activeBullets)
			{
					if(b.getPosX()+1>a.getPosX()-a.getSize() && b.getPosX()-1<a.getPosX()+a.getSize())
					{
						if(b.getPosY()+1>a.getPosY()-a.getSize() && b.getPosY()-1<a.getPosY()+a.getSize())
						{
							b.makeDead();
							a.makeDead();
							p1.setScore(p1.getScore()+1);
							
							//Splash Bullets
							createSplash=true;
							splashPosX=b.getPosX();
							splashPosY=b.getPosY();
							splashVelX=b.getVelX();
							splashVelY=b.getVelY();
							
							checkForPlayerAchievement(p1.getScore());
						}
					}
			}
			if(a.getPosX()+a.getSize()>p1.getPosX()-p1.getSize() && a.getPosX()-a.getSize()<p1.getPosX()+p1.getSize())
			{
				if(a.getPosY()+a.getSize()>p1.getPosY()-p1.getSize() && a.getPosY()-a.getSize()<p1.getPosY()+p1.getSize())
				{
					p1.setHealth(p1.getHealth()-1);
				}
			}
			if(a.getType()=="Spy")//Growing Spies!
			{
				int x=(int)(Math.random()*6+1);
				if(x==5)
					((Spy) a).grow();
			}
		}
		if(createSplash==true)
		{
			int newVelX1=0;
			int newVelY1=0;
			int newVelX2=0;
			int newVelY2=0;
			
			activeBullets.add(new Bullet(splashPosX,splashPosY,splashVelX,splashVelY,SPLASHRANGE,this.getInverseColor(BG)));
			
			if(splashVelX==0||splashVelY==0)//U D L R
			{
				if(splashVelX==0)//U D
				{
					newVelX1=5;
					newVelY1=splashVelY;
					newVelX2=-5;
					newVelY2=splashVelY;
				}
				else//L R
				{
					newVelX1=splashVelX;
					newVelY1=5;
					newVelX2=splashVelX;
					newVelY2=-5;
				}
			}
			else //UR UL DR DL
			{
				if(splashVelX>0)//UR DR
				{
					newVelX1=0;
					newVelY1=splashVelY;
					newVelX2=splashVelX;
					newVelY2=0;
				}
				else//UL DL
				{
					newVelX1=0;
					newVelY1=splashVelY;
					newVelX2=splashVelX;
					newVelY2=0;
				}
			}
			newVelX1 =(int)( Math.random()*4-4)+newVelX1;
			newVelY1 =(int)( Math.random()*4-4)+newVelY1;
			newVelX2 =(int)( Math.random()*4-4)+newVelX2;
			newVelY2 =(int)( Math.random()*4-4)+newVelY2;
			activeBullets.add(new Bullet(splashPosX,splashPosY,newVelX1,newVelY1,SPLASHRANGE,this.getInverseColor(BG)));
			activeBullets.add(new Bullet(splashPosX,splashPosY,newVelX2,newVelY2,SPLASHRANGE,this.getInverseColor(BG)));
		}
	}
	
	public void checkForPlayerAchievement(int score)
	{
		if(p1.getHealth()<=15)
		{
			p1.setMessage(new Message("Low Health!"+p1.getHealth(),10));
		}
		
		if(score%200==50)
		{
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=p1.getLevel()*4;
		}
		
		if(score>1300&& score%200==0)
		{
			p1.levelUp();

			p1.setMessage(new Message("Level "+p1.getLevel()+"!",100));
		}
		
		switch (score)
		{
		case 10:
			p1.levelUp();
			p1.setMessage(new Message("Level 2!",100));
			p1.setScore(p1.getScore()+1);
			break;
		case 20:
			p1.levelUp();
			p1.setMessage(new Message("Level 3!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=10;
			break;
		case 40:
			p1.levelUp();
			p1.setMessage(new Message("Level 4!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=10;
			break;
		case 60:
			p1.levelUp();
			p1.setMessage(new Message("Level 5!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=20;
			break;
		case 100:
			p1.levelUp();
			p1.setMessage(new Message("Level 6!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=25;
			break;
		case 200:
			p1.levelUp();
			p1.setMessage(new Message("Level 7!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=30;
			break;
		case 400:
			p1.levelUp();
			p1.setMessage(new Message("Level 8!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=35;
			break;
		case 410:// Non-Level
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=35;
			break;
		case 550:// Non-Level
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=35;
			break;
		case 700:
			p1.levelUp();
			p1.setMessage(new Message("Level 9!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=50;
			break;
		case 850:
			p1.levelUp();
			p1.setMessage(new Message("Level 10!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=75;
			break;
		case 1025:
			p1.levelUp();
			p1.setMessage(new Message("Level 11!",100));
			p1.setScore(p1.getScore()+1);
			makeRush=true;
			rushSize=100;
			break;
		}
	}
	public Color getInverseColor(Color a)
	{
		return new Color(255-a.getRed(),255-a.getGreen(),255-a.getBlue());
	}
	public void createPlayerGhost()
	{
		activeGhosts.add(new PlayerGhost(p1.getPosX(),p1.getPosY(),255/ (10/3),this.getInverseColor(BG)));
		if(p1.getHealth()<50)
		{
			int a=(int)(Math.random()*(p1.getHealth())+1);
			if(a==1)
				activeGhosts.add(new PlayerGhost(p1.getPosX(),p1.getPosY(),250,this.getInverseColor(BG)));
		}
	}
	public void bombHandler()
	{
		for(int i=activeBombs.size()-1;i>=0;i--)
		{
			createBombSplash(activeBombs.get(i));
		}
	}
	
	//static int sleep = 10;
	boolean startup = true;
	@SuppressWarnings("static-access")
	@Override
	public void run()
	{
		Thread thisThread = Thread.currentThread();
		
		while(runner == thisThread && stop==true)
		{
			actionKeyPressHandler();
		}
		cantBeat.getClip().start();
		try {
			Thread.currentThread().sleep(cantBeat.getClip().getMicrosecondLength()/1000  -100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		wonders.getClip().loop(wonders.getClip().LOOP_CONTINUOUSLY);
		
		startup=false;
		while(runner == thisThread)
		{
			setBeforeSleep((int)System.currentTimeMillis());
			
			
			actionKeyPressHandler();
			updateParticles();
			removeDeadParticles();
			bombHandler();
			
			if(stop==false){
				timeAlive++;
				checkBulletCollision();
				createPlayerGhost();
			}
			if(!debug){
			aiHandler();
			}
			
			if(slow==true){
				p1.addCharge(-4);
			}
			
			if(makeRush)
				addRush(rushSize);
			
			
			repaint();
			//Hush Now Quiet Now, Lay Down Your Sleepy Head
			if(slow==true)
				try {Thread.currentThread().sleep(waitSkip*4);} 
				catch(InterruptedException e) {e.printStackTrace();}
			else
				try {Thread.currentThread().sleep(waitSkip);}
			catch(InterruptedException e) {e.printStackTrace();}
			//Wake Up You Lazy Bum!
			setAfterSleep((int)System.currentTimeMillis());
			fps = (int)getFPS();
			//waitSkipHandler();
		}
	}
	Thread runner;
	@Override
	public void addNotify()
	{
		super.addNotify();
		runner = new Thread(this);
		runner.start();
    }
	
	public int getHighScore(){return highScore;}
	public int getTries(){return tries;}
	
	public void setHighScore(int a){highScore=a;}
	public void addTry(){tries++;}
	//------------------- FPS CALC -------------------
		int fps = 100;
		public double getFPS()
		{
			return (1000.0/(getAfterSleep()-getBeforeSleep()));
		}
		
		double beforeSleep; // Calculated before sleep
		double afterSleep;  // Calculated after sleep
		public void setBeforeSleep(double a){beforeSleep=a;}
		public void setAfterSleep(double a){afterSleep=a;}
		public double getBeforeSleep(){return beforeSleep;}
		public double getAfterSleep(){return afterSleep;}
		//------------------------------------------------

}
*/