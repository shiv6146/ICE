package com.hawks.incaseofemergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ClientServer extends Activity implements LocationListener {

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	TextView txtLat;
	String lat;
	String provider;
	protected String latitude,longitude; 
	protected boolean gps_enabled,network_enabled;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_server);
		Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
		startActivityForResult(gpsOptionsIntent,0);
		
	}
		


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		txtLat = (TextView) findViewById(R.id.textview1);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		Log.d("loc", "hi");
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		txtLat = (TextView) findViewById(R.id.textview1);
		latitude = String.valueOf(location.getLatitude()); 
		longitude = String.valueOf(location.getLongitude());
		Log.d("Flash", "GEOCODE");
		Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
		Log.d("Flash", "GEOCODE1");
		try {
        	  
			  //Place your latitude and longitude
			  List<Address> addresses = geocoder.getFromLocation(37.423247,-122.085469, 1);
        	 
        	  if(addresses != null) {
        	  
        		  Address fetchedAddress = addresses.get(0);
        		  StringBuilder strAddress = new StringBuilder();
        	   
        		  for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
        			  	strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
        		  }
        	   
        		  Log.d("I am at: ", strAddress.toString());
        	  }
        	  
         
        }
        	  catch (IOException e) {
         		 // TODO Auto-generated catch block
         		 e.printStackTrace();
         		 Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
 		}
		try {
			Log.d(null,"before conn in b1");
			Connect c=new Connect();
			Log.d(null,"after conn in b1");
			c.post();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d(null, "b1CPE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(null, "b1IOE");
		}
		Connect c=new Connect();
		c.execute();
		Intent i = new Intent("com.hawks.incaseofemergency.CONTACTS");
		startActivity(i);
		finish();
	}



	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.d("chg", "before");
		
		Log.d("chg", "before1");
		txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
		Log.d("chg", "after");
		Log.d("location", String.valueOf(location.getLatitude()));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitude","disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitude","enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("Latitude","status");
	}

	class Connect extends AsyncTask<Void,Void,Void>{
		HttpPost post = new HttpPost("http://sastra-ice.comxa.com/index.php");
		HttpClient client=new DefaultHttpClient();
		StringBuilder sb=null;
		protected Void doInBackground(Void... v){
			try {
				Log.d(null,"before post conn");
				
				Log.d(null,"after post conn");
				post();
				Log.d(null,"after post");
				response();
				Log.d(null,"after response");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public void response() throws ClientProtocolException, IOException{
			HttpResponse response=client.execute(post);
			Log.d(null,"after client execute");
		    sb=inputStreamToString(response.getEntity().getContent());
		    Log.d("res", sb.toString());
		}
		public StringBuilder inputStreamToString(InputStream is) throws IOException{
			String line="";
			StringBuilder total=new StringBuilder();
			BufferedReader rd=new BufferedReader(new InputStreamReader(is));
			while((line=rd.readLine())!=null)
			{
				total.append(line);
			}
			return total;
		}
		public void post() throws ClientProtocolException, IOException{
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("lat",latitude));
			pairs.add(new BasicNameValuePair("long",longitude));
			Log.d(null,"in post-before");
			post.setEntity(new UrlEncodedFormEntity(pairs));
			Log.d(null,"in post-after");
			//Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
			
		}
			
	}

}


