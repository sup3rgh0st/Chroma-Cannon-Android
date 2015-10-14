package com.pixulted.chromacannon;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity
{
	
	static MediaPlayer introecho;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//setRequestedOrientation(0);
		//ourSong.start();
		
		introecho = MediaPlayer.create(Splash.this, R.raw.introecho);
		introecho.start();
		
		Thread timer =  new Thread()
		{
			public void run()
			{
				try
				{
					sleep(4000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					Intent i = new Intent("android.intent.action.MainActivity");
					startActivity(i);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onResume() {
		introecho.start();
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		introecho.pause();
		super.onPause();
		finish();
	}
	

}
