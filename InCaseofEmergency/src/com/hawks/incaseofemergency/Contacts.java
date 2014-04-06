package com.hawks.incaseofemergency;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Contacts extends Activity implements OnClickListener{
	public int PICK_CONTACT = 0;
	public int SERVER = 0; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		Button b = (Button)findViewById(R.id.pc);
		b.setOnClickListener(this);
		Log.d("call", "hello");
		Log.d("calling", "hey");
		Log.d("called", "conn");
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        MyReceiver mReceiver = new MyReceiver (this);
        registerReceiver(mReceiver, filter);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		startActivityForResult(i, PICK_CONTACT);
		 
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			Uri uri = data.getData();
			if(uri != null){
				Cursor c = null;
				try{
					c = getContentResolver().query(uri, new String[]{
							ContactsContract.CommonDataKinds.Phone.NUMBER
					}, null, null, null);
					
					if(c != null && c.moveToFirst()){
						String num = c.getString(0);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(c != null){
						c.close();
					}
				}
			}
		}
	}
	
}
class MyReceiver extends BroadcastReceiver {
    static int countPowerOff=0;
    private Activity activity=null;
    public MyReceiver (Activity activity)
    {
    this.activity=activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

      Log.v("onReceive", "Power button is pressed.");

     if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) 
{
    countPowerOff++;  
    
} 
     else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
    	 if(countPowerOff==2){
    		 Log.v("2power", "power 2");
    	 }
    	 if(countPowerOff==3){
    		 Log.v("3power", "power 3");
    	 }
    	 if(countPowerOff==4){
    		 Log.v("4power", "power 4");
    	 }
     }

}
	
}

