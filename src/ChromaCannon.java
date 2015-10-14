package com.pixulted.chromacannon;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class ChromaCannon extends Activity implements OnTouchListener {
	
	static List<PlayerGhost> activeGhosts = new CopyOnWriteArrayList<PlayerGhost>();
	static List<Bullet> activeBullets = new CopyOnWriteArrayList<Bullet>();
	static List<Man> activeEnemies = new CopyOnWriteArrayList<Man>();
	static List<Message> activeMessages = new CopyOnWriteArrayList<Message>();
	static List<Item> activeItems = new CopyOnWriteArrayList<Item>();
	static List<Bomb> activeBombs = new CopyOnWriteArrayList<Bomb>();
	
	OurView v;
	PlayerMovementHandler pmh;
	
	static boolean paidVersion = true;
	
	//final static double DEVICESCALE = 0.00078125;
	final static double DEVICESCALE = 0.00125;
	static int SCREENWIDTH = 9001;//Screen Width not set when first gun is equipped 
	final String VERSION = "w5 0.5.3";
	double previousSystemTime;
	double systemTime;
	double msPerTick;
	
	static String backMessage;
	static int backMessageX;
	
	Player p1;
	
	MediaPlayer wonders;
	MediaPlayer wondersSlow;
	MediaPlayer fstart;
	MediaPlayer ohcomeon;
	Bitmap youarrowll;
	Bitmap youarrowlr;
	Bitmap youarrowul;
	Bitmap youarrowur;
	Bitmap youarrowl;
	Bitmap youarrowr;
	Bitmap youarrowu;
	Bitmap youarrowd;
	//Bitmap powerhealth;//  Text "Power Health"
	Bitmap shiftbar;
	Bitmap itemM;
	Bitmap itemS;
	Bitmap itemH;
	Bitmap itemClock;
	Bitmap whiteglow;
	Bitmap staticRef;
	//Bitmap static_1;
	//Bitmap static_2;
	//Bitmap static_3;
	//Bitmap static_4;
	//Bitmap static_5;
	//Bitmap static_6;
	//Bitmap static_7;
	//Bitmap static_8;
	//Bitmap static_9;
	
	Paint BG = new Paint();
	Paint invBG = new Paint();
	int BG_Red;
	int BG_Green;
	int BG_Blue;
	int BG_Step=1;
	final int INCREMENT = (3 * (10/3));
	int bgSpin=1;
	
	final int MAX_HEALTH = 200;
	double SLOW = 0.25;
	double gameTick;
	boolean slow=false;
	boolean dead=false;
	boolean shooting;
	boolean godMode;
	boolean firstTick = true;
	boolean lastTick = true;
	boolean shotOnTick = false;
	boolean pause = false;
	double timeAlive=0;
	
	boolean makeRush=false;
	int rushSize;
	
	//TODO Typeface TFpixelade=Typeface.createFromAsset(getAssets(),"pixelade.ttf");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//Create Full Screen Set-Up
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		v = new OurView(this);
		v.setOnTouchListener(this);
		pmh = new PlayerMovementHandler(this);
		//.setOnTouchListener(this);
		
		x = y = 0;
		x2 = y2 = 0;
		wonders = MediaPlayer.create(ChromaCannon.this, R.raw.gameloop);
		wondersSlow = MediaPlayer.create(ChromaCannon.this, R.raw.gameloopslow);
		fstart = MediaPlayer.create(ChromaCannon.this, R.raw.fstart);
		ohcomeon = MediaPlayer.create(ChromaCannon.this, R.raw.ohcomeon);
		
		ohcomeon.setVolume(50f, 50f);
		//wonders.setVolume(80f, 80f);
		//wondersSlow.setVolume(80f, 80f);
		//fstart.setVolume(80f, 80f);
		
		
		//static_5 = BitmapFactory.decodeResource(getResources(), R.drawable.static_5);
		//static_6 = BitmapFactory.decodeResource(getResources(), R.drawable.static_6);
		//static_7 = BitmapFactory.decodeResource(getResources(), R.drawable.static_7);
		//static_8 = BitmapFactory.decodeResource(getResources(), R.drawable.static_8);
		//static_9 = BitmapFactory.decodeResource(getResources(), R.drawable.static_9);
		//BG_Step=1;
		//trimBitmaps();
		setContentView(v);
		reset();
	}
	
	public void initPlayers()
	{
		p1 = new Player(new Rifle());
		p1.setPosX(100);
		p1.setPosY(100);
		p1.setVelX(4);
		p1.setVelY(4);
		p1.setHealth(MAX_HEALTH);
		p1.setMessage(null);
		p1.setScore(0);
		p1.setLevel(1);
		createMessage("Anti-Virus Scanner Operational!");
	}
	
	public void reset()
	{
		createMessage("Formatting C:/",100);
		createMessage("Booting Android Environment....",100);
		initPlayers();
		timeAlive=0;
		shotOnTick=false;
		gameTick=0;
		shooting=false;
		godMode=false;
		createMessage("33%",100);
		activeBullets.clear();
		activeEnemies.clear();
		activeItems.clear();
		activeGhosts.clear();
		createMessage("66%",100);
		SLOW=0.25;
		firstTick=true;
		lastTick=true;
		dead=false;
		previousSystemTime=System.currentTimeMillis();
		systemTime=System.currentTimeMillis();
		msPerTick=-1.0;
		createMessage("100%",100);
		backMessage="";
		backMessageX=-9001;
		createMessage("All Android systems are running and operational!",200);
		//addTry();
	}
	
	public static void createMessage(String a,int b) //Custom
	{activeMessages.add(new Message(a,b));}
	public static void createMessage(String a) //System General
	{activeMessages.add(new Message(a,50));}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("onPause");
		v.pause();
		pmh.pause();
		purgeBitmaps();
		pause=true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		
		youarrowll = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowll);
		youarrowlr = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowlr);
		youarrowul = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowul);
		youarrowur = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowur);
		youarrowl = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowl);
		youarrowr = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowr);
		youarrowu = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowu);
		youarrowd = BitmapFactory.decodeResource(getResources(), R.drawable.youarrowd);
		shiftbar = BitmapFactory.decodeResource(getResources(), R.drawable.shiftbar);
		itemM = BitmapFactory.decodeResource(getResources(), R.drawable.itemm);
		itemS = BitmapFactory.decodeResource(getResources(), R.drawable.items);
		itemH = BitmapFactory.decodeResource(getResources(), R.drawable.itemh);
		itemClock = BitmapFactory.decodeResource(getResources(), R.drawable.itemclock);
		whiteglow = BitmapFactory.decodeResource(getResources(), R.drawable.whiteglow);
		staticRef = BitmapFactory.decodeResource(getResources(), R.drawable.staticref);
		//static_1 = BitmapFactory.decodeResource(getResources(), R.drawable.static_1);
		//static_2 = BitmapFactory.decodeResource(getResources(), R.drawable.static_2);
		//static_3 = BitmapFactory.decodeResource(getResources(), R.drawable.static_3);
		//static_4 = BitmapFactory.decodeResource(getResources(), R.drawable.static_4);
		v.resume();
		pmh.resume();
	}
	public class PlayerMovementHandler extends SurfaceView implements Runnable {
		
		Thread t = null;
		boolean isItOK = false;
		
		public PlayerMovementHandler(Context context) {
			super(context);
		}
		
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			System.out.println("PlayerMovementHandler Running!");
			while(isItOK == true){
				if(!dead){
					
					movementHandler();
					
					
					if(shotOnTick==false)
						shootingHandler();
					try {
						t.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		public void pause() {
			isItOK = false;
			System.out.println("pmh pause");
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}
		public void resume() {
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
		private void movementHandler() {
			
			final int MAX_NET = 80;
			int netX = (int) (x-finger1XRef);
			int netY = (int) (y-finger1YRef);
			
			
			if(netX> MAX_NET)
				netX=80;
			if(netX< -MAX_NET)
				netX=-80;
			if(netY> MAX_NET)
				netY=80;
			if(netY< -MAX_NET)
				netY=-80;
			
			p1.setVelX(netX/10);
			p1.setVelY(netY/10);
			
			//Move Player
			p1.setPosX(p1.getPosX()+p1.getVelX());
			p1.setPosY(p1.getPosY()+p1.getVelY());
			
		}
		private void shootingHandler() {
			shooting=false;
			shotOnTick=true;
			if(slow){
				//p1.getEquippedGun().subtractCoolDown(SLOW);
				//p1.getEquippedGun().subtractGunLife(SLOW);
				p1.getEquippedGun().subtractCoolDown(1.0);
				p1.getEquippedGun().subtractGunLife(1.0);
			}
			else{
				p1.getEquippedGun().subtractCoolDown(1.0);
				p1.getEquippedGun().subtractGunLife(1.0);
			}
			
			if(p1.getEquippedGun().getGunLife()<=0){
				p1.setEquippedGun(new Rifle());
			}
			
			if(!(finger2XRef==0 && finger2YRef==0)){
				if(p1.getEquippedGun().getCoolDown()<=0){
					shooting=true;
					int bulletVelX;
					int bulletVelY;
					
					double hyp = Math.sqrt(Math.pow((x2-finger2XRef),2)+Math.pow((y2-finger2YRef),2));
					
					bulletVelX= (int) (20/(hyp/(x2-finger2XRef)));
					bulletVelY= (int) (20/(hyp/(y2-finger2YRef)));
					
					p1.getEquippedGun().setCoolDown(p1.getEquippedGun().getFiringSpeed());
					
					if(!(bulletVelX==0 && bulletVelY==0)){
						activeBullets.add(new Bullet(p1.getPosX(),p1.getPosY(),bulletVelX,bulletVelY,p1.getEquippedGun().getBulletLife()));
						if(p1.getEquippedGun().getName().equals("Spreader")){
							double angle = Math.atan2(bulletVelY, bulletVelX);
							final double ANGLESHIFT = 0.3;
							hyp = Math.sqrt(Math.pow(bulletVelX, 2)+Math.pow(bulletVelY, 2));
								
							activeBullets.add(new Bullet(p1.getPosX(),p1.getPosY(),(int)(Math.cos(angle-ANGLESHIFT)*hyp),(int)(Math.sin(angle-ANGLESHIFT)*hyp),p1.getEquippedGun().getBulletLife()));
							activeBullets.add(new Bullet(p1.getPosX(),p1.getPosY(),(int)(Math.cos(angle+ANGLESHIFT)*hyp),(int)(Math.sin(angle+ANGLESHIFT)*hyp),p1.getEquippedGun().getBulletLife()));
									
						}
					}
				}
			}
		}
	}
	public class OurView extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;

		public OurView(Context context) {
			super(context);
			holder = getHolder();
		}
		@Override
		public void run() {
			System.out.println("OurView Running!");
		
			//reset();
		
			wonders.setLooping(true);
			wonders.start();
			while (isItOK == true) {
				if (!holder.getSurface().isValid()) {
					continue;
				}
				
				previousSystemTime = systemTime;
				
				Canvas c = holder.lockCanvas();
				
				gameTick++;
				shotOnTick=false;
				
				if(firstTick==true){
					SCREENWIDTH = c.getWidth();
					firstTick=false;
					p1.setPosX(this.getWidth()/2);
					p1.setPosY(this.getHeight()/2);
					System.out.println(this.getWidth()/2);
					System.out.println("Set Player Pos @"+p1.getPosX()+","+p1.getPosY());
				}
				
				if(shooting && dead==false){
					int m = (int) (Math.abs(p1.getHealth()-200)/(DEVICESCALE*c.getWidth()*10));
					int sX = (int)(Math.random()*(2*m)-m);
					int sY = (int)(Math.random()*(2*m)-m);
					c.translate((float)(sX*DEVICESCALE*c.getWidth()),(float)(sY*DEVICESCALE*c.getHeight()));
				}
			
			
			//Slow Handler
				slow=false;
				if(p1.getSlowCharge()>0){
					slow=true;
					//System.out.println("SLOW:"+p1.getSlowCharge());
					p1.decaySlowCharge();
				}
				
			//Background Color Update
				
				int BG_Red_ds   = BG_Red;
				int BG_Green_ds = BG_Green;
				int BG_Blue_ds  = BG_Blue;
				int BW = 20;
				int HEALTH = (int) Math.min(200, p1.getHealth());
				
				switch(BG_Step) //INCREMENT --> Speed of Color Change
				{
				case(1):
					BG_Green+=INCREMENT;
				
					if(BG_Green>=255){
						BG_Step++;
						BG_Green=255;}
					BG_Red_ds-=(255-BG_Green)*(1-(Math.max(HEALTH-BW,0)/200.0));
		    		BG_Green_ds=BG_Green;
		    		BG_Blue_ds+=BG_Green*(1-(Math.max(HEALTH-BW,0)/200.0));
					break;
				case(2):
					BG_Red-=INCREMENT;
				
					if(BG_Red<=0){
						BG_Step++;
						BG_Red=0;}
					BG_Red_ds=BG_Red;
			    	BG_Green_ds-=(255-BG_Red)*(1-Math.max(HEALTH-BW,0)/200.0);
			    	BG_Blue_ds +=BG_Red*(1-Math.max(HEALTH-BW,0)/200.0);
					break;
				case(3):
					BG_Blue+=INCREMENT;
				
					if(BG_Blue>=255){
						BG_Step++;
						BG_Blue=255;}
					BG_Red_ds+=BG_Blue*(1-Math.max(HEALTH-BW,0)/200.0);
	    			BG_Green_ds-=(255-BG_Blue)*(1-Math.max(HEALTH-BW,0)/200.0);
	    			BG_Blue_ds=BG_Blue;
					break;
				case(4):
					BG_Green-=INCREMENT;
				
					if(BG_Green<=0){
						BG_Step++;
						BG_Green=0;}
					BG_Red_ds+=BG_Green*(1-Math.max(HEALTH-BW,0)/200.0);
		    		BG_Green_ds=BG_Green;
		    		BG_Blue_ds-=(255-BG_Green)*(1-Math.max(HEALTH-BW,0)/200.0);
					break;
				case(5):
					BG_Red+=INCREMENT;
				
					if(BG_Red>=255){
						BG_Step++;
						BG_Red=255;}
					BG_Red_ds=BG_Red;
			    	BG_Green_ds+=BG_Red*(1-Math.max(HEALTH-BW,0)/200.0);
			    	BG_Blue_ds -=(255-BG_Red)*(1-Math.max(HEALTH-BW,0)/200.0);
					break;
				case(6):
					BG_Blue-=INCREMENT;
				
					if(BG_Blue<=0){
						BG_Step=1;
						BG_Blue=0;}
					BG_Red_ds-=(255-BG_Blue)*(1-Math.max(HEALTH-BW,0)/200.0);
	    			BG_Green_ds+=BG_Blue*(1-Math.max(HEALTH-BW,0)/200.0);
	    			BG_Blue_ds=BG_Blue;
					break;
				}
				
				if(dead){
					int deadColor = (int)(Math.random()*255);
					BG_Red_ds=deadColor;
					BG_Green_ds=deadColor;
					BG_Blue_ds=deadColor;
				}
				
				BG.setARGB(255, BG_Red_ds, BG_Green_ds, BG_Blue_ds);
				invBG.setARGB(255,255-BG_Red_ds, 255-BG_Green_ds, 255-BG_Blue_ds);
				c.drawARGB(255, BG_Red_ds, BG_Green_ds, BG_Blue_ds);
				
			//Paint More Seizure Stuff
				bgSpin+=3+((200-p1.getHealth())/30);
				if(bgSpin>360)
					bgSpin=0;
				
				int sRA = (c.getWidth()+c.getHeight())/2;
				
				Rect spinning = new Rect((c.getWidth()-sRA)/2,(c.getHeight()-sRA)/2,(c.getWidth()+sRA)/2,(c.getHeight()+sRA)/2);
				Paint BGlite = new Paint();
				BGlite.setARGB(80, 255, 255, 255);
				
				c.rotate(bgSpin,c.getWidth()/2, c.getHeight()/2);
				c.drawRect(spinning, BGlite);
				c.rotate(-bgSpin,c.getWidth()/2, c.getHeight()/2);		
				
			//Paint Setup
				Paint p = new Paint();
				p.setARGB(255, 0, 0, 0);
				
				Paint pBlue = new Paint();
				pBlue.setARGB(255, 0, 0, 255);
				
				Paint pRed = new Paint();
				pRed.setARGB(255, 255, 0, 0);
				
				Paint pGreen = new Paint();
				pGreen.setARGB(255, 0, 255, 0);
				
				Paint pWhite = new Paint();
				pWhite.setARGB(255, 255, 255, 255);
				
				Paint pWhiteGlow = new Paint();
				pWhiteGlow.setARGB((int) (200-p1.getHealth()), 255, 255, 255);
				
				Paint pShadow = new Paint();
				pShadow.setARGB(50, 0, 0, 0);
				
				Paint pFont = new Paint();
				pFont.setARGB(150, 0, 0, 0);
				pFont.setTextSize((float) (100*DEVICESCALE*c.getHeight()));
				
				Paint pScore = new Paint();
				pScore.setARGB(30, 0, 0, 0);
				pScore.setTextSize((float) (300*DEVICESCALE*c.getHeight()));
				//pScore.setTypeface(TFpixelade);
				
			//Draw backMessage
				if(backMessage != null && backMessageX > -2000)
				{
					backMessageX-= 30*DEVICESCALE*c.getWidth();
					c.drawText(backMessage, backMessageX, c.getHeight()/2-pFont.getTextSize()/2, pFont);
				}
				
			//Draw Score TODO
				if(p1.getHealth()<65){
					if((int)(Math.random()*p1.getHealth()/10)==0){
						//String s = p1.getScore()+"";
						c.drawText(p1.getScore()*8.1234+"",15,pScore.getTextSize()-40,pScore);
					}
					else{
						c.drawText(p1.getScore()+"",15,pScore.getTextSize()-40,pScore);
					}
				}
				else{
					if(p1.getHealth()>200){
						godMode=true;
						//pScore.setTextSize((float) (250*DEVICESCALE*c.getHeight()));
						createMessage("Super User Privilages Granted!",1);
						createMessage("Systems operating at "+p1.getHealth()/2.0+"%!!",1);
						c.drawText(p1.getScore()+"",0,pScore.getTextSize()-20,pScore);
					}
					else{
						godMode=false;
						c.drawText(p1.getScore()+"",0,pScore.getTextSize()-20,pScore);
					}
				}
				
			//Draw Static TODO
				if(p1.getHealth()<100){
					Paint pStatic = new Paint();
					pStatic.setAlpha(100-((int)p1.getHealth()));
					if((int)(Math.random()*2+1)==1){
						
						int xS = (int)(Math.random()*450+1);
						int yS = (int)(Math.random()*450+1);
						
						for(int x = 0; x<=(c.getWidth()/500)+1;x++)
							for(int y = 0; y<=(c.getHeight()/500)+1;y++)
								c.drawBitmap(staticRef, 500*x - xS, 500*y - yS, pStatic);
					}
				}
				
			//OffScreen Arrows
				if(p1.getPosX()<0)//Left
					if(p1.getPosY()<0)//Upper Left
						c.drawBitmap(youarrowul, 5, 5, null);
					else if(p1.getPosY()>c.getHeight())//Lower Left
						c.drawBitmap(youarrowll, 5, c.getHeight()-5-youarrowll.getHeight(), null);
					else//Left
						c.drawBitmap(youarrowl,5,p1.getPosY()-youarrowl.getHeight()/2,null);
				else if(p1.getPosX()>c.getWidth())//Right
					if(p1.getPosY()<0)//Upper Right
						c.drawBitmap(youarrowur, c.getWidth()-5-youarrowur.getWidth(),5, null);
					else if(p1.getPosY()>c.getHeight())//Lower Right
						c.drawBitmap(youarrowlr, c.getWidth()-5-youarrowlr.getWidth(), c.getHeight()-5-youarrowlr.getHeight(), null);
					else//Right
						c.drawBitmap(youarrowr, c.getWidth()-5-youarrowr.getWidth(),p1.getPosY()-youarrowr.getHeight()/2, null);
				else if(p1.getPosY()<0)//Up
					c.drawBitmap(youarrowu, p1.getPosX()-youarrowu.getWidth()/2, 5, null);
				else if(p1.getPosY()>c.getHeight())//Down
					c.drawBitmap(youarrowd, p1.getPosX()-youarrowd.getWidth()/2, c.getHeight()-5-youarrowd.getHeight(), null);
						
			//Draw Fingers
				c.drawCircle(x, y, 25, pShadow);//1st Finger
				c.drawCircle(x2, y2, 25, pShadow);//2nd Finger
				c.drawCircle(finger1XRef, finger1YRef, 50, pShadow);//1st Ref Finger
				c.drawCircle(finger2XRef, finger2YRef, 50, pShadow);//2nd Ref Finger
			
			//Console Text Generator
				if(p1.getHealth()<100 && !dead){
					if((int)(Math.random()*p1.getHealth()/10)==0)
						switch((int)(Math.random()*31)){
						case(1):createMessage("Java.lang.output: Error has occured!");break;
						case(2):createMessage("A fatal exception has ocurred at 0x0ffffff");break;
						case(3):createMessage("DRIVER_IRQL_NOT_LESS_OR_EQUAL");break;
						case(4):createMessage("MEMORY_MANAGEMENT failure!");break;
						case(5):createMessage("Computer Over - Virus = Very Yes");break;
						case(6):createMessage("Java.lang.output: Error has occured!");break;
						case(7):createMessage("** STOP: 0x00000019");break;
						case(8):createMessage("Android internal error has occured!");break;
						case(9):createMessage("An error with your device has been detected");break;
						case(10):createMessage("Root Privilages have been accepted!");break;
						case(11):createMessage("If this is your first time recieving this error, restart your device");break;
						case(12):createMessage("Molten Core Warning");break;
						case(13):createMessage("Dumping physical memory to disk, please wait");break;
						case(14):createMessage("**************");break;
						case(15):createMessage("*****-----");break;
						case(16):createMessage("Memory Failed at Address 00083g38ab389");break;
						case(17):createMessage("You system must shut down to avoid furthur damages!");break;
						case(18):createMessage("#####&&&&&&&&&&&////------------");break;
						case(19):createMessage("Corrupted Files Detected!!");break;
						case(20):createMessage("C0rrrpt3d F1les D##ect3d!@@");break;
						case(21):createMessage("An error at address 0x00008d669b has been detected!");if((int)(Math.random()*2)==1)break;
						case(22):createMessage("An error at address 0x0008320df9 has been detected!");if((int)(Math.random()*2)==1)break;
						case(23):createMessage("An error at address 0x008a1c7203 has been detected!");if((int)(Math.random()*2)==1)break;
						case(24):createMessage("An error at address 0x000938532a has been detected!");if((int)(Math.random()*2)==1)break;
						case(25):createMessage("An error at address 0x0000453be4 has been detected!");if((int)(Math.random()*2)==1)break;
						case(26):createMessage("An error at address 0x0094abd900 has been detected!");if((int)(Math.random()*2)==1)break;
						case(27):createMessage("An error at address 0x0000891e8a has been detected!");break;
						case(28):createMessage("@@@#########///////////////______________________________________________");break;
						case(29):createMessage((p1.getScore()*2.3145)+" errors have occured!");break;
						case(30):createMessage("Contact your system administrator if this problem persists.");break;
						case(31):createMessage("java.lang.nullpointerexception -> Chroma.java -> at line 20033");break;
						}
				}
			
			//Console Text
				//c.drawText("Current Task: ChromaCannon.apk - Process/sec:"+msPerTick, 10, 10, p);
				//c.drawText("x:"+x+" y:"+y+" x2:"+x2+" y2:"+y2+"    Slow:"+slow, 10, 20, p);
				//c.drawText("x:"+finger1XRef+" y:"+finger1YRef+" x2:"+finger2XRef+" y2:"+finger2YRef, 10, 30, p);
				
			//Draws Player Ghosts
				for(PlayerGhost a:activeGhosts) 
				{
					if(godMode)
						c.drawRect(a.getPosX()-p1.getSize(), a.getPosY()-p1.getSize(),a.getPosX()+p1.getSize(), a.getPosY()+p1.getSize(), pWhite);
					else
						c.drawRect(a.getPosX()-p1.getSize(), a.getPosY()-p1.getSize(),a.getPosX()+p1.getSize(), a.getPosY()+p1.getSize(), a.getColor());
					if(slow)
						a.decay(SLOW);
					else
						a.decay(1.0);
					if(a.getHealth()<=0){
						a.dead=true;
					}
				}
				
			//Draw Items
				for(Item a:activeItems)
				{
					c.drawBitmap(a.getTexture(), a.getPosX()-(a.getTexture().getWidth()/2), a.getPosY()-(a.getTexture().getHeight()/2), null);
				}
				
			//Draw Player
				
				Rect p1r = new Rect(p1.getPosX()-p1.getSize(),p1.getPosY()-p1.getSize(),p1.getPosX()+p1.getSize(),p1.getPosY()+p1.getSize());
				Rect p1r2 = new Rect(p1.getPosX()-p1.getSize()-10,p1.getPosY()-p1.getSize()-10,p1.getPosX()+p1.getSize()+10,p1.getPosY()+p1.getSize()+10);
				//c.drawBitmap(whiteglow, p1r2, p1r2, null);
				c.drawRect(p1r2, pWhiteGlow);
				c.drawRect(p1r, p);
				
			//Draw Enemies
				for(Man a:activeEnemies)
				{
					if(a.getType()=="Wizard")// Are you a Wizard?!
					{
						c.drawRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getPosX()+a.getSize(), a.getPosY()+a.getSize(), invBG);
						c.drawRect(a.getPosX()-a.getSize()+1, a.getPosY()-a.getSize()+1, a.getPosX()+a.getSize()-1, a.getPosY()+a.getSize()-1, BG);
					}
					else
					{ 
						/*
						if(a.getHealth()>1){
							Paint enemyGlow = new Paint();
							enemyGlow.setARGB(255,(int) (255/a.getMAXHEALTH() *a.getHealth()),(int) ((int) 255-(255/a.getMAXHEALTH() *a.getHealth())), 0);
							c.drawRect(a.getPosX()-a.getSize()-1, a.getPosY()-a.getSize()-1, a.getPosX()+a.getSize()+1, a.getPosY()+a.getSize()+1, enemyGlow);
						}
						else{
							c.drawRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getPosX()+a.getSize(), a.getPosY()+a.getSize(), p);
						}*/
						
						c.drawRect(a.getPosX()-a.getSize(), a.getPosY()-a.getSize(), a.getPosX()+a.getSize(), a.getPosY()+a.getSize(), p);
						c.drawRect((float)(a.getPosX()-a.getSize()+Math.min(a.getHealth(), 5))
								  ,(float)(a.getPosY()-a.getSize()+Math.min(a.getHealth(), 5))
								  ,(float)(a.getPosX()+a.getSize()-Math.min(a.getHealth(), 5))
								  ,(float)(a.getPosY()+a.getSize()-Math.min(a.getHealth(), 5)), invBG);
						
						//c.drawRect(a.getPosX()-a.getSize()+1, a.getPosY()-a.getSize()+1, a.getPosX()+a.getSize()-1, a.getPosY()+a.getSize()-1, invBG);
					}
				}
				
			//Draw Bullets
				for(Bullet a:activeBullets)
				{
					if(a.hasColor){
						//c.setColor(a.getColor());
						Rect bul = new Rect(a.getPosX()-4,a.getPosY()-4,a.getPosX()+4,a.getPosY()+4);
						c.drawRect(bul, a.getColor());
					}
					else{
						Rect bul = new Rect(a.getPosX()-4,a.getPosY()-4,a.getPosX()+4,a.getPosY()+4);
						c.drawRect(bul, p);
					}
				}
				
			//Draw Shift and Health Bars
				
				c.drawRect(c.getWidth()-shiftbar.getWidth()+5,5f,(float) (c.getWidth()-shiftbar.getWidth()+5+(162.0/p1.getEquippedGun().getMAXGUNLIFE()*p1.getEquippedGun().getGunLife())),shiftbar.getHeight()-10,pGreen);
				c.drawBitmap(shiftbar, c.getWidth()-shiftbar.getWidth(), 0, null);
				c.drawRect(c.getWidth()-shiftbar.getWidth()+5, 5f+45f, c.getWidth()-162+(int)Math.min((int) (p1.getHealth()/1.23), 162)-10, (shiftbar.getHeight()*2)-10, pRed);
				c.drawBitmap(shiftbar, c.getWidth()-shiftbar.getWidth(), shiftbar.getHeight(), null);
				if(p1.getHealth()<0){
					p1.setHealth(-1);
					c.drawText("You are currently dead.",this.getWidth()/2-50,this.getHeight()/2+12,p);
				}
				
			//Draw Player Messages
				if(p1.getMessage()!=null)
				{
					c.drawText(p1.getMessage().getMessage(), p1.getPosX()-7, p1.getPosY()-15,p);
					c.drawText("Score:"+p1.getScore(), p1.getPosX()-7, p1.getPosY()+25,p);
					p1.getMessage().decay();
					if(p1.getMessage().getLife() <=0)
						p1.setMessage(null);
				}
				
			//Draw Console Messages
				
				Paint pConsole = new Paint();
				pConsole.setARGB(175, 0, 0, 0);
				pConsole.setTextSize((float) (20*DEVICESCALE*c.getHeight()));
				
				int y=0;
				try
				{
					for(Message a:activeMessages)
					{
						c.drawText(a.getMessage(), 20, 20+(y*pConsole.getTextSize()),pConsole);
						a.decay();
						if(a.getLife() <=0)
							a.makeDead();
						y++;
					}
				}
				catch(ConcurrentModificationException e)
				{
					//createMessage("An unimportant error with activeMessages has occurred!",200);
				}
				
			//Paid Version Handler
				if(paidVersion == false){
					freeVersionThrottler();
				}
					
				
			//Overlay
				//c.drawText("Chroma Cannon Test Build",this.getWidth()/2-50,this.getHeight()/2-12,p);
				//c.drawText("Version:"+VERSION+" - Blue Barium",this.getWidth()/2-50,this.getHeight()/2,p);
			
			//msPerTick Visualizer
				//c.drawRect(300, 2, (float) (300+ msPerTick/100*(c.getWidth()-177)), 30, invBG);
				
			//Dead Handler
				if(p1.getHealth()<=0){
					
					pause=false;
					
					//createMessage("Player 1 has died!");
					p1.setHealth(0);
					if(lastTick==true){
						lastTick=false;
						//ohcomeon.start();
						createSplash(p1.getPosX(),p1.getPosY(),(int) (50*DEVICESCALE*c.getWidth()));
						createSplash(p1.getPosX(),p1.getPosY(),(int) (100*DEVICESCALE*c.getWidth()));
						createSplash(p1.getPosX(),p1.getPosY(),(int) (200*DEVICESCALE*c.getWidth()));
					}
					slow=true;
					dead=true;
					SLOW=0;
					bgSpin-=3+((200-p1.getHealth())/30);
					
					Paint shadowBox = new Paint();
					shadowBox.setARGB(230, 0, 0, 0);
					c.drawRect(0, 0, c.getWidth(), c.getHeight(), shadowBox);
					
					Paint text = new Paint();
					int sX;// = (int)(Math.random()*(2*m)-m);
					int sY;// = (int)(Math.random()*(2*m)-m);
					int SHAKE=(int) (6*DEVICESCALE*c.getHeight());
					text.setTextSize((float) (80*DEVICESCALE*c.getHeight()));
					
					text.setARGB(255, 255, 0, 0);
					sX = (int)(Math.random()*(2*SHAKE)-SHAKE);
					sY = (int)(Math.random()*(2*SHAKE)-SHAKE);
					if(!paidVersion && p1.getScore() >= 750)
						c.drawText("Free Version max score reached!", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					else
						c.drawText("You have been corrupted.", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					c.drawText("Your Score was: "+p1.getScore(), 10+(sX), (c.getHeight()/2)+(sY), text);
					c.drawText("Double Tap the Screen to Restart", 10+(sX), (float) ((c.getHeight()/2)+70*DEVICESCALE*c.getHeight())+(sY), text);
					
					text.setARGB(255, 0, 255, 0);
					sX = (int)(Math.random()*(2*SHAKE)-SHAKE);
					sY = (int)(Math.random()*(2*SHAKE)-SHAKE);
					if(!paidVersion && p1.getScore() >= 750)
						c.drawText("Free Version max score reached!", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					else
						c.drawText("You have been corrupted.", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					c.drawText("Your Score was: "+p1.getScore(), 10+(sX), (c.getHeight()/2)+(sY), text);
					c.drawText("Double Tap the Screen to Restart", 10+(sX), (float) ((c.getHeight()/2)+70*DEVICESCALE*c.getHeight())+(sY), text);
					
					text.setARGB(255, 0, 0, 255);
					sX = (int)(Math.random()*(2*SHAKE)-SHAKE);
					sY = (int)(Math.random()*(2*SHAKE)-SHAKE);
					if(!paidVersion && p1.getScore() >= 750)
						c.drawText("Free Version max score reached!", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					else
						c.drawText("You have been corrupted.", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					c.drawText("Your Score was: "+p1.getScore(), 10+(sX), (c.getHeight()/2)+(sY), text);
					c.drawText("Double Tap the Screen to Restart", 10+(sX), (float) ((c.getHeight()/2)+70*DEVICESCALE*c.getHeight())+(sY), text);
					
					text.setARGB(255, 255, 255, 255);
					if(!paidVersion && p1.getScore() >= 750)
						c.drawText("Free Version max score reached!", 10+(sX), (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight())+(sY), text);
					else
						c.drawText("You have been corrupted.", 10, (float) ((c.getHeight()/2)-70*DEVICESCALE*c.getHeight()), text);
					c.drawText("Your Score was: "+p1.getScore(), 10, (c.getHeight()/2), text);
					c.drawText("Double Tap the Screen to Restart", 10, (float) ((c.getHeight()/2)+70*DEVICESCALE*c.getHeight()), text);
				}
				
				//Draw Resume From Pause Screen
				if(pause){
					
					int sX;
					int sY;
					int SHAKE=(int) (6*DEVICESCALE*c.getHeight());
					sX = (int)(Math.random()*(2*SHAKE)-SHAKE);
					sY = (int)(Math.random()*(2*SHAKE)-SHAKE);
					
					Paint shadowBox = new Paint();
					shadowBox.setARGB(150, 0, 0, 0);
					c.drawRect(0, 0, c.getWidth(), c.getHeight(), shadowBox);
					Paint text = new Paint();
					text.setARGB(255, 255, 255, 255);
					text.setTextSize((float) (80*DEVICESCALE*c.getHeight()));
					c.drawText("Tap the screen to resume play!", 10, (c.getHeight()/2), text);
					c.drawText("Tap the screen to resume play!", 10+(sX), (c.getHeight()/2)+(sY), text);
				}
				
				
				if(!dead && !pause){
				createPlayerGhost();
				itemHandler();
				updateParticles();//Updates Pos of Bullets
				checkBulletCollision();
				checkItemCollision();
				if(makeRush)
					addRush(rushSize);
				//slowHandler();
				removeDeadParticles();
				aiHandler();
				}
				
				
				
				holder.unlockCanvasAndPost(c);
				
				systemTime=System.currentTimeMillis();
				msPerTick=systemTime-previousSystemTime;
			}
		}
		private void freeVersionThrottler() {
			if(p1.getScore() >= 750){
				p1.setScore(750);
				p1.setHealth(-9001);
			}
		}
		public int getChangingBGColor(){
			switch(BG_Step)
			{
			case(1):
				return 2;//"green";
			case(2):
				return 1;//"red";
			case(3):
				return 3;//"blue";
			case(4):
				return 2;//"green";
			case(5):
				return 1;//"red";
			case(6):
				return 3;//"blue";
			}
			return 2;//"green";
		}
		private void checkItemCollision() {
			for(Item a:activeItems)
			{
				int radius = a.getTexture().getWidth()/2;
				if(Math.abs(p1.getPosX()-a.getPosX())<radius)
					if(Math.abs(p1.getPosY()-a.getPosY())<radius){
						a.makeDead();
						switch(a.getType()){
						case 0://Slow Power Up
							p1.setSlowCharge(p1.getSlowCharge()+250);
							System.out.println("Set P1 Slow Charge to:"+p1.getSlowCharge());
							createMessage("Underclocking CPU to 250mhz...",200);
							break;
						case 1://Gun
							p1.setEquippedGun((Gun) a);
							createMessage("Overwriting plug-in : \"gun\" @ 0x00729123",200);
							break;
						case 2://Health
							p1.setHealth((int) (p1.getHealth()+60));
							createMessage("Service Pack Installed!",200);
							break;
						}
					}
			}
		}

		private void itemHandler() {
			if((int)(Math.random()*(1000)+1) ==10){
				
				int x = (int)(Math.random()*(this.getWidth())+1);
				int y = (int)(Math.random()*(this.getHeight())+1);
				System.out.println("Generating a new Item @"+x+","+y);
				switch((int)(Math.random()*(4)+3)){
				case 1://MachineGun
					MachineGun a = new MachineGun();
					a.setPosX(x);
					a.setPosY(y);
					a.setTexture(itemM);
					activeItems.add(a);
					break;
				case 2:
					Spreader b = new Spreader();
					b.setPosX(x);
					b.setPosY(y);
					b.setTexture(itemS);
					activeItems.add(b);
					break;
				case 3:
					Item c = new Item();
					c.setPosX(x);
					c.setPosY(y);
					c.setTexture(itemClock);
					c.setType(0);
					activeItems.add(c);
					break;
				case 4:
					Item d = new Item();
					d.setPosX(x);
					d.setPosY(y);
					d.setTexture(itemH);
					d.setType(2);
					activeItems.add(d);
					break;
				}
			}
		}
		
		private void createRandomGun(){
			int x = (int)(Math.random()*(this.getWidth())+1);
			int y = (int)(Math.random()*(this.getHeight())+1);
			System.out.println("Generating a new Item @"+x+","+y);
			switch((int)(Math.random()*(2)+1)){
			case 1://MachineGun
				MachineGun a = new MachineGun();
				a.setPosX(x);
				a.setPosY(y);
				a.setTexture(itemM);
				activeItems.add(a);
				break;
			case 2://Spreader
				Spreader b = new Spreader();
				b.setPosX(x);
				b.setPosY(y);
				b.setTexture(itemS);
				activeItems.add(b);
				break;
			}
		}
		
		@SuppressWarnings("unused")
		private void createItem(Item i){
			int x = (int)(Math.random()*(this.getWidth())+1); //TODO
			int y = (int)(Math.random()*(this.getHeight())+1);
			System.out.println("NOTHING HAPPENED BECAUSE I DIDN'T PROGRAM THIS YET!");
			//MachineGun a = new MachineGun();
			//a.setPosX(x);
			//a.setPosY(y);
			//a.setTexture(itemM);
			//activeItems.add(a);
		}
		
		public void createPlayerGhost()
		{
			Paint x = new Paint();
			x.setColor(invBG.getColor());
			activeGhosts.add(new PlayerGhost(p1.getPosX(),p1.getPosY(),255/ (10/3),x));
			if(p1.getHealth()<50)
			{
				int a=(int)(Math.random()*(p1.getHealth())+1);
				if(a==1)
					activeGhosts.add(new PlayerGhost(p1.getPosX(),p1.getPosY(),250,x));
			}
		}

		private void slowHandler() {
			if(slow==true){
				if(wonders.isPlaying()==true){
					int pos = wonders.getCurrentPosition() % wonders.getDuration();
					wonders.pause();
					//wonders.release();
					wondersSlow.start();
					wondersSlow.seekTo(pos*2);
					wondersSlow.setLooping(true);
					
				}
			}else if(wondersSlow.isPlaying()){
				int pos = wondersSlow.getCurrentPosition() % wondersSlow.getDuration();
				wondersSlow.pause();
				//wondersSlow.release();
				wonders.start();
				wonders.seekTo(pos/2);
				//wonders.setLooping(true);
				
			}
		}

		private void checkBulletCollision() {
			boolean createSplash = false;
			final int SPLASHRANGE = 30;
			int splashPosX=0;
			int splashPosY=0;
			int splashVelX=0;
			int splashVelY=0;
			
			for(Man a:activeEnemies) //Enemy
			{
				for(Particle b:activeBullets) //Bullet
				{
						if(b.getPosX()+1>a.getPosX()-a.getSize() && b.getPosX()-1<a.getPosX()+a.getSize())
						{
							if(b.getPosY()+1>a.getPosY()-a.getSize() && b.getPosY()-1<a.getPosY()+a.getSize())
							{
								a.subtractHealth(1.0);
								b.makeDead();
								
								if(a.getHealth() <= 0.0){
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
				}
				if(a.getPosX()+a.getSize()>p1.getPosX()-p1.getSize() && a.getPosX()-a.getSize()<p1.getPosX()+p1.getSize())
				{
					if(a.getPosY()+a.getSize()>p1.getPosY()-p1.getSize() && a.getPosY()-a.getSize()<p1.getPosY()+p1.getSize())
					{
						p1.setHealth((int)p1.getHealth()-1);
					}
				}
				if(a.getType()=="Spy")//Growing Spies!
				{
					int x=(int)(Math.random()*4);
					if(x==1)
						((Spy) a).grow();
				}
			}
			if(createSplash==true)
			{
				int newVelX1=0;
				int newVelY1=0;
				int newVelX2=0;
				int newVelY2=0;
				
				//Paint x = new Paint();
				activeBullets.add(new Bullet(splashPosX,splashPosY,splashVelX,splashVelY,SPLASHRANGE,invBG));
				
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
				activeBullets.add(new Bullet(splashPosX,splashPosY,newVelX1,newVelY1,SPLASHRANGE,invBG));
				activeBullets.add(new Bullet(splashPosX,splashPosY,newVelX2,newVelY2,SPLASHRANGE,invBG));
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
				rushSize=p1.getLevel()*2; //%%% 4
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
				createRandomGun();
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
				createRandomGun();
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
				createRandomGun();
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
				createRandomGun();
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
				createRandomGun();
				p1.setScore(p1.getScore()+1);
				makeRush=true;
				rushSize=50;
				break;
			case 850:
				p1.levelUp();
				p1.setMessage(new Message("Level 10!",100));
				createRandomGun();
				p1.setScore(p1.getScore()+1);
				makeRush=true;
				rushSize=75;
				break;
			case 1025:
				p1.levelUp();
				p1.setMessage(new Message("Level 11!",100));
				createRandomGun();
				p1.setScore(p1.getScore()+1);
				makeRush=true;
				rushSize=100;
				break;
			}
			if(p1.getScore()%(200+p1.getLevel()*8)==99){
				createRandomGun();
			}
		}
		private void aiHandler() {
			if(activeEnemies.size() < (int) (p1.getLevel()*1.1)) //Spawn Enemies //%%% *2
			{
				addEnemey();
			}
			for(Man a:activeEnemies) //Set Velocity of Enemies
			{
				if(gameTick%5==0){
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
				}
				
				if(slow){//Fixes immovable enemies
					double fixX=a.getVelX()*SLOW;
					double fixY=a.getVelY()*SLOW;
					if(fixX<0&&fixX>-1)
						fixX=-1; 
					else if(fixX>0&&fixX<1)
						fixX=1;
					if(fixY<0&&fixY>-1)
						fixY=-1;
					else if(fixY>0&&fixY<1)
						fixY=1;
					
					a.setPosX((int) (a.getPosX()+fixX));
					a.setPosY((int) (a.getPosY()+fixY));
				}
				else
				{
					a.setPosX(a.getPosX()+a.getVelX());
					a.setPosY(a.getPosY()+a.getVelY());
				}
				
			}
			
		}
		public void addEnemey()
		{
			int a =(int) (Math.random()*7+1);
			if(a==1){
				activeEnemies.add(new Spy(this.getWidth(),this.getHeight()));
			}
			else{
				activeEnemies.add(new Grunt(this.getWidth(),this.getHeight()));
			}
			
			a =(int) (Math.random()*750+1);
			if(a==50){
				activeEnemies.add(new Wizard(this.getWidth(),this.getHeight()));
			}
		}
		public void addRush(int a)
		{
			//createMessage(a+" enemy RUSH!");
			for(int i=0;i<a;i++){
				addEnemey();
				activeEnemies.size();
			}
			makeRush=false;
			rushSize=0;
		}
		private void updateParticles() {
			for(Bullet a:activeBullets)
			{
				if(slow)
					a.move(SLOW);
				else
					a.move(1.0);
				if(a.getPosX()<-10||a.getPosX()>this.getWidth()+10)
					a.makeDead();
				if(a.getPosY()<-10||a.getPosY()>this.getHeight()+10)
					a.makeDead();
				if(slow)
					a.decay(SLOW);
				else
					a.decay(1.0);
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
					//createMessage("A "+activeEnemies.get(i).getType()+" was killed!");
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
			for(int i=activeItems.size()-1;i>=0;i--)
			{
				if(activeItems.get(i).checkDead())
				{
					activeItems.remove(i);
				}
			}
		}
		public void createSplash(int x,int y, int r)
		{
			System.out.println("[Map] Creating Splash @X:"+x+" Y:"+y);
			for(int scanX=x-r;scanX<x+r;scanX+=10)
			{
				for(int scanY=y-r;scanY<y+r;scanY+=10)
				{
					if((int)(Math.random()*250+1)==10)
						activeGhosts.add(new PlayerGhost(scanX,scanY,200,getRandomColor()));
				}
			}
		}
		public Paint getRandomColor(){
			Paint p = new Paint();
			p.setARGB(255, (int)(Math.random()*255+1), (int)(Math.random()*255+1), (int)(Math.random()*255+1));
			return p;
		}
		public void pause() {
			System.out.println("ourView pause");
			isItOK = false;
			wonders.pause();
			
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			System.out.println("ourView resume");
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
	}
	
	public void purgeBitmaps(){
		youarrowll.recycle();
		youarrowlr.recycle();
		youarrowul.recycle();
		youarrowur.recycle();
		youarrowl.recycle();
		youarrowr.recycle();
		youarrowu.recycle();
		youarrowd.recycle();
		shiftbar.recycle();
		itemM.recycle();
		itemS.recycle();
		itemH.recycle();
		itemClock.recycle();
		whiteglow.recycle();
		staticRef.recycle();
		//static_1.recycle();
		//static_2.recycle();
		//static_3.recycle();
		//static_4.recycle();
		//static_5.recycle();
		//static_6.recycle();
		//static_7.recycle();
		//static_8.recycle();
		//static_9.recycle();
	}
	
	@Override
	protected void onStop() {
		
		System.out.println("ChromaCannon Session Ended!");
		super.onStop();
		//System.gc();
		purgeBitmaps();
	}


	//Touch Methods
	float x, y;
	float x2, y2;
	float finger1XRef,finger1YRef;
	float finger2XRef,finger2YRef;
	long doubleTap=0;
	@Override
	public boolean onTouch(View v, MotionEvent me) {
		
		//int action = MotionEventCompat.getActionMasked(me);
		
		pause=false;
		
		int index = MotionEventCompat.getActionIndex(me);
		
		if (me.getPointerCount() > 1) {
			x = (int)MotionEventCompat.getX(me, 0);
		    y = (int)MotionEventCompat.getY(me, 0);
		    x2 = (int)MotionEventCompat.getX(me, 1);
		    y2 = (int)MotionEventCompat.getY(me, 1);
		} 
		else{
		    x = (int)MotionEventCompat.getX(me, index);
		    y = (int)MotionEventCompat.getY(me, index);
		    x2 = -100;
		    y2 = -100;
		    finger2XRef=-100;
		    finger2YRef=-100;
		}
		//System.out.println(me.getAction());
		if(me.getAction()==0){//Finger 1 Down
			finger1XRef=x;
			finger1YRef=y;
			if(dead){
				if(System.currentTimeMillis()<doubleTap+500)
					reset();
				else
					doubleTap=System.currentTimeMillis();
			}
			//slow=false;
		}
		if(me.getAction()==1){
			x= y= x2= y2= finger1XRef=finger1YRef=finger2XRef=finger2YRef=-100;
			//slow=false;
		}
		if(me.getAction()==261){//Finger 2 Down
			finger2XRef=x2;
			finger2YRef=y2;
			//slow=false;
		}
		if(me.getAction()==262){//Finger 2 Up
			finger2XRef=-100;
			finger2YRef=-100;
			x2=-100;
			y2=-100;
			//slow=false;
		}
		
		/*
		if(me.getAction()==517){//Finger 3 Down
			slow=true;
		}
		if(me.getAction()==518){//Finger 3 Up
			slow=false;
		}
		
		if(me.getAction()==773){//Finger 4 Down
			createMessage("About to Reset!!");
		}
		if(me.getAction()==774){//Finger 4 Up
			reset();
		}
		*/
		return true;
	}

}
