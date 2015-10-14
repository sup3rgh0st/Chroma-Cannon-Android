/*
 *    Chroma Cannon
 *     Baby Barium
 *  
 *  (c) Ryan Magliola
 */

package com.pixulted.chromacannon;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{//, OnPreparedListener {
	Button bStart, bOptions;
	TextView tvVersion;
	
	MediaPlayer menuloop;
	
	boolean toHelp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(0);
		
		System.out.println("MainActivity onCreate");
		
		Splash.introecho.start();
		menuloop = MediaPlayer.create(MainActivity.this, R.raw.menuloop);
		menuloop.setLooping(true);
		
		initializeVars();
		if(ChromaCannon.paidVersion == true)
			tvVersion.setText("Version: 1.0 Beta : Paid Version! Thank You!");
		else
			tvVersion.setText("Version: 1.0 Beta : Free Version!");
	}

	private void initializeVars() {
		bStart = (Button) findViewById(R.id.bStart);
		bOptions = (Button) findViewById(R.id.bOptions);
		tvVersion = (TextView) findViewById(R.id.tvVersion);
		bStart.setOnClickListener(this);
		bOptions.setOnClickListener(this);
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bStart:
			toHelp = false;
			Intent i = new Intent("com.pixulted.chromacannon.ChromaCannon");
			startActivity(i);
			menuloop.seekTo(1);
			menuloop.pause();
			break;
		case R.id.bOptions:
			toHelp = true;
			Intent j = new Intent("com.pixulted.chromacannon.Help");
			startActivity(j);
			break;
		}
	}

	@Override
	protected void onPause() {
		System.out.println("MainActivity onPause");
		if(!toHelp){
			if(menuloop.isPlaying() == true)
				menuloop.pause();
			if(Splash.introecho.isPlaying() == true)
				Splash.introecho.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		System.out.println("MainActivity onResume");/*
		try {
			menuloop.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if(!toHelp)
			menuloop.start();
		super.onResume();
	}
/*
	@Override
	public void onPrepared(MediaPlayer player) {
		System.out.println("onPrepared");
		player.start();
		
	}*/
}
