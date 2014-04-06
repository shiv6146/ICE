package com.hawks.incaseofemergency;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Thread timer = new Thread()
		{
			public void run()
			{
				try{
					sleep(2000);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					Intent i = new Intent("com.hawks.incaseofemergency.CLIENTSERVER");
					startActivity(i);
				}
			}
		};
		timer.start();
	}

}

