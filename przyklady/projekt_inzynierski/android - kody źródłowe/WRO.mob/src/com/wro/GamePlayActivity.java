package com.wro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pietras.wro.R;

import com.wro.adapters.ResourceAdapter;
import com.wro.backend.LocationInGame;
import com.wro.backend.LocationUtils;
import com.wro.backend.Resource;
import com.wro.backend.Session;
import com.wro.backend.TaskInGame;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GamePlayActivity extends FragmentActivity {
    private MyAdapter mAdapter;
    private ViewPager mPager;
    
    //UI
    public SupportMapFragment map_fragment;
    public ResCompatFragment res_fragment;
    TextView hint = null;
    public boolean isDialogShowed = false;
    GoogleMap google_map = null;
	
	//BL
	public Session session;
	int task_id;
	public TaskInGame riddle;
	int bundle_id;
	String riddle_name;
	String bundle_name;
	
	//GPS data
	boolean isGPSon = false;
	LatLng actual_position = null;
	
	//Internet access
	AsyncTask<String, String, String> mDataDownloadTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        mAdapter = new MyAdapter(getSupportFragmentManager());

        //BL init
  		session = (Session) getIntent().getSerializableExtra("Session");
  		task_id = (int) getIntent().getIntExtra("task_id", -1);
  		bundle_id = (int) getIntent().getIntExtra("bundle_id", -1);
  		riddle_name = getIntent().getStringExtra("name");
  		bundle_name = getIntent().getStringExtra("bundle_name");
        
        //Action bar setting
        final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Zagadka: " + riddle_name);
        
        //pager stuff
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager
			.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
				}
			});
        mPager.requestTransparentRegion(mPager);
        
        
        //actionbar tabs
      		for (int i = 0; i < mAdapter.getCount(); i++) {
      			actionBar.addTab(actionBar.newTab()
      					.setText(mAdapter.getPageTitle(i))
      					.setTabListener(new NavTabListener()));
      		}
        
  		hint = (TextView) findViewById(R.id.gameHint);
      		
  		//Connection
  		mDataDownloadTask = new DataDownloadTask(getApplicationContext(),task_id,session.getUserId(),this).execute(Session.SERVER_ADDRESS);
        
  		//Setting the GPS listener
        LocationManager loc_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener loc_listener = new PlayerLocationListener();
		loc_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc_listener);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_play, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == android.R.id.home){
			Intent backIntent = new Intent(this,RiddlesListActivity.class);
			backIntent.putExtra("id",bundle_id);
			backIntent.putExtra("Session", session);
			NavUtils.navigateUpTo(this, backIntent);
		}
		return super.onOptionsItemSelected(item);
	}

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
            	map_fragment = SupportMapFragment.newInstance();

            	google_map = map_fragment.getMap();
            	if( google_map != null){
            		google_map.setMyLocationEnabled(true);
            	}
            	
            	return map_fragment;
            case 1:
            	res_fragment = new ResCompatFragment();
				if(riddle != null)
					res_fragment.list = riddle.resources;
				return res_fragment;
            default:
                return null;
            }
        }
        
        @Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
    }

    //Task for downloading riddle data (like name, locations etc)
  	class DataDownloadTask extends AsyncTask<String, String, String>{
  	        
  			Context context;
  			int task_id;
  			int user_id;
  			GamePlayActivity parentActivity;
  			 
  			public DataDownloadTask(Context context, int task_id, int user_id, GamePlayActivity parentActivity) {
  				this.context = context;
  				this.task_id=task_id;
  				this.user_id=user_id;
  				this.parentActivity = parentActivity;
  			}
  			
  	        @Override
  	        protected String doInBackground(String... uri) {
  	            HttpURLConnection conn = null;
  	    		try {conn = (HttpURLConnection)new URL(uri[0]+"api/Values/riddle/"+task_id+"/"+session.getUserId()).openConnection();
  	
  	            	session.setConnectionCredentials(conn);
  	
  	            	conn.setConnectTimeout(5000);
  	            	conn.setDoInput(true);			
  	            	InputStream is = conn.getInputStream();
  	            	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
  	                return s.hasNext() ? s.next() : "";
  	            }
  	            catch(Exception e){
  	            	Log.d("LLL","Exception occured: " + e.getMessage());
  	            	runOnUiThread(new Runnable() {
  						//String message;
  						@Override
  						public void run() {
  							Session.logout(context);
  						}
  						
  						public Runnable init(String message){
  							//this.message = message;
  							return this;
  						}
  					}.init(e.getMessage()));
  	            	return null;
  	            }
  	            finally{
  	            	if(conn != null)
  	            		conn.disconnect();
  	            }
  	        }
  	        
  	        @Override
  	        protected void onPostExecute(String result) {
  				if (result != null){
  					Log.d("LLL",result);
  					parseRiddleData(result,context);

  					mDataDownloadTask = null;
  				}	
  	        }
  	        
  	        @Override
  	        protected void onCancelled() {
  	        	mDataDownloadTask = null;
  	        }
  	    }
  	
  	public void parseRiddleData(String data_to_parse,Context context){
  		String text = data_to_parse;
  		
  		try{
  			JSONObject jsonRiddleObject = new JSONObject(text);
  			riddle = new TaskInGame();
  			riddle.task_id = jsonRiddleObject.getInt("id");
  			riddle.name = jsonRiddleObject.getString("name");
  			riddle.type_id = jsonRiddleObject.getInt("type_id");
  			riddle.description_id = jsonRiddleObject.getInt("description_id");
  			riddle.min_number = jsonRiddleObject.getInt("minimum_to_find");
  			
  			JSONArray jsonLocationsArray = jsonRiddleObject.getJSONArray("locations");
  			riddle.locations = new ArrayList<LocationInGame>(jsonLocationsArray.length());
  			for(int i=0; i<jsonLocationsArray.length();i++){
  				JSONObject jsonLocation = jsonLocationsArray.getJSONObject(i);
  				LocationInGame location = new LocationInGame(
  						jsonLocation.getInt("id"),
  						jsonLocation.getDouble("latitude"), 
  						jsonLocation.getDouble("longitude"), 
  						jsonLocation.getString("name"), 
  						jsonLocation.getBoolean("is_found")
  				);
  				riddle.locations.add(location);
  			}
  			
  			JSONArray jsonResourceArray = jsonRiddleObject.getJSONArray("resources");
  			riddle.resources = new ArrayList<Resource>(jsonResourceArray.length());
  			for(int i=0; i<jsonResourceArray.length();i++){
  				JSONObject jsonResource = jsonResourceArray.getJSONObject(i);
  				Resource resource = new Resource(
  						jsonResource.getInt("id"), 
  						jsonResource.getInt("resource_type_id"), 
  						jsonResource.getString("path")
  				);
  				Log.d("LLL","Parsuje jsona i path="+resource.path);
  				riddle.resources.add(resource);
  			}
  			
  			Log.d("LLL","riddle= "+riddle);
  			if(riddle != null)
  				Log.d("LLL","riddle.resources= "+riddle.resources);	
  			Log.d("LLL","res_fragment= "+res_fragment);
  			if(res_fragment != null){
  				res_fragment.listview.setAdapter(  new ResourceAdapter( riddle.resources,this.getApplicationContext(),res_fragment.listview,res_fragment.empty_informer )  );
  				
  			}
  				
   			updateHint();
  			refreshMarkers();
  		}
  		catch(JSONException e){
  			Toast.makeText(getApplicationContext(), "Problem with loaded data", Toast.LENGTH_SHORT).show();
  			Session.logout(context);
  		}
  		
  	}//parseRiddleData

//Nasluchiwanie pozycji GPS
  public class PlayerLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		
		actual_position = new LatLng(location.getLatitude(), location.getLongitude());
		
		updateHint();
		refreshMarkers();
		
		if( riddle == null || riddle.locations == null)
			return;
		
		LocationInGame close_location = LocationUtils.findClosestLocation(riddle.locations,actual_position);
		if(close_location!=null && isDialogShowed==false){
			FoundLocationDialogFragment.newInstance(session,riddle,bundle_id,bundle_name,close_location).show(getFragmentManager(),"");
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "GPS off", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "GPS on", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

  }//PlayerLocationListener

  public class NavTabListener implements ActionBar.TabListener{
	  @Override
		public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
			// When the given tab is selected, switch to the corresponding page in
			// the ViewPager.
			mPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab,
				FragmentTransaction fragmentTransaction) {
		}

		@Override
		public void onTabReselected(ActionBar.Tab tab,
				FragmentTransaction fragmentTransaction) {
		}
  }
  
  
  private void updateHint(){
	if( actual_position == null){
		hint.setText("Oczekiwanie na pozycjê GPS...");
		return;
	}
	if(riddle == null || riddle.locations == null || riddle.locations.isEmpty()){
		hint.setText("Oczekiwanie na pobranie danych...");
		return;
	}
	LinkedList<Float> distances;
	double angle;
	switch(riddle.type_id){
	case TaskInGame.DISTANCE_TABLE:
		
		distances = new LinkedList<Float>();
		
		for(LocationInGame location : riddle.locations){
			if( !(location.is_solved) ){
				distances.add( LocationUtils.distanceBetween(actual_position, new LatLng(location.lat,location.lng)) );
			}
		}
		
		Collections.sort(distances);
		
		String new_hint = "";
		
		for(int i=0; i<distances.size() && i<5;i++){
			new_hint = new_hint + LocationUtils.distanceToString(distances.get(i));
			if(i<distances.size()-1 && i<4)
				new_hint = new_hint + "\n";
		}
		
		hint.setText(new_hint);
		break;
	case TaskInGame.DIRECTION_AND_DISTANCE:
		
		distances = new LinkedList<Float>();
		
		LocationInGame nearest_location = null;
		float nearest_distance = Float.MAX_VALUE;
		
		for(LocationInGame location : riddle.locations){
			if( ! location.is_solved ){
				float distance = LocationUtils.distanceBetween(actual_position, new LatLng(location.lat,location.lng));
				
				if( distance < nearest_distance){
					nearest_location = location;
					nearest_distance = distance;
				}
			}
		}
		if(nearest_location != null){
			angle = LocationUtils.countAngle(actual_position, nearest_location.toLatLng());
			hint.setText("IdŸ na "+ LocationUtils.angleToString(angle) +".\nDo celu pozosta³o " + LocationUtils.distanceToString(nearest_distance)+".");
		}
		
		break;
	case TaskInGame.CLEAR_TARGETS:
		int number_of_found = 0;
		
		for(LocationInGame location : riddle.locations){
			if( location.is_solved ){
				number_of_found++;
			}
		}
		hint.setText("Znalaz³eœ "+number_of_found +"/"+ riddle.min_number+" miejsc.");
		break;
	default:
		hint.setText("Twoja wersja aplikacji nie obs³uguje takiej zagadki. Zaktualizuj.");
		break;
	}	
  }
  
  public void refreshMarkers(){
    	if( map_fragment == null)
    		return;
	  
    	google_map = map_fragment.getMap();
    	
  		if(google_map == null || riddle == null || riddle.locations == null){
  			return;
  		}

  		google_map.clear();
  		
  		if( actual_position != null){
  			Marker player_marker = google_map.addMarker(new MarkerOptions()
  		    .position(actual_position)
  		    .title(getString( R.string.you_are_here))
  		    );
  			player_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
  		}
  		
  		switch(riddle.type_id){
  		case TaskInGame.DISTANCE_TABLE:
  			for(LocationInGame location : riddle.locations){
  				if( location.is_solved ){
  					Marker location_marker = google_map.addMarker(new MarkerOptions()
  					    .position(new LatLng(location.lat,location.lng))
  					    .title(location.name)
  					    );
  					location_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
  				}
  	    	}
  			break;
  		case TaskInGame.DIRECTION_AND_DISTANCE:
  			for(LocationInGame location : riddle.locations){
  				if( location.is_solved ){
  					Marker location_marker = google_map.addMarker(new MarkerOptions()
  					    .position(new LatLng(location.lat,location.lng))
  					    .title(location.name)
  					    );
  					location_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
  				}
  	    	}
  			break;
  		case TaskInGame.CLEAR_TARGETS:
  			for(LocationInGame location : riddle.locations){
  				if( !(location.is_solved) ){
  					Marker location_marker = google_map.addMarker(new MarkerOptions()
  					    .position(new LatLng(location.lat,location.lng))
  					    .title(location.name)
  					    );
  					location_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
  				}
  	    	}
  			break;
  		default:
  			break;
  		}
  	}

}
