package com.wro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import com.pietras.wro.R;
import com.wro.backend.Session;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationFoundActivity extends Activity {

	//UI
	ImageView photoView;
	TextView messageText;
	Button okButton;

	//BL
	Session session;
	int task_id;
	int bundle_id;
	String bundle_name;
    String riddle_name;
	int location_id;
	String location_name;
	int number_of_found_locations;
	int minimum_number;
	
	Bitmap photo = null;
	
	//Connection
	private AsyncTask<String, String, String> mPostTask = null;
	private AsyncTask<String, String, String> mSolvedTask = null;
	private AsyncTask<String, String, String> mPhotoTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_found);
		
		//BL init
		session = (Session) getIntent().getSerializableExtra("Session");
		task_id = (int) getIntent().getIntExtra("task_id", -1);
		riddle_name = getIntent().getStringExtra("riddle_name");
		bundle_id = (int) getIntent().getIntExtra("bundle_id", -1);
		bundle_name = getIntent().getStringExtra("bundle_name");
		location_id = (int) getIntent().getIntExtra("location_id", -1);
		location_name = getIntent().getStringExtra("location_name");
		number_of_found_locations = getIntent().getIntExtra("found_locations", 0);
		minimum_number = getIntent().getIntExtra("min_number", 0);
				
		//UI init
		photoView = (ImageView) findViewById(R.id.photoImage);
		messageText = (TextView) findViewById(R.id.messageText);
		okButton = (Button) findViewById(R.id.okButton);
		
		messageText.setText(session.getUsername() + ", " + getString(R.string.you_found) + " " + location_name + ".");
		
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(photo == null){
					try{
						startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
					}
					catch(ActivityNotFoundException e){
						Toast.makeText(getApplicationContext(), getString(R.string.photo_taking_problem), Toast.LENGTH_SHORT).show();
					}
				}
				else{
					okButton.setClickable(false);
					okButton.setText("Czekaj...");
					mPhotoTask = new PhotoSendTask(getApplicationContext(),photo).execute(Session.SERVER_ADDRESS);
				}
				
			}
		});
		
		try{
			startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
		}
		catch(ActivityNotFoundException e){
			Toast.makeText(getApplicationContext(), getString(R.string.photo_taking_problem), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bundle_list, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Toast.makeText(getApplicationContext(), getString(R.string.first_send_photo), Toast.LENGTH_SHORT).show();
	}

	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && data != null){
			photo = (Bitmap)data.getExtras().get("data");
			int width = photo.getWidth();
			int height = photo.getHeight();
			int size = width > height ? height : width;
			Matrix m = new Matrix();
			float factor = 256f / (float)size;
			m.setScale(factor, factor);
			photo = Bitmap.createBitmap(photo, (width-size)/2, (height-size)/2, size, size, m, true);
			photoView.setImageBitmap(photo);			
		}
	}


	class LocationVisitedTask extends AsyncTask<String, String, String>{
        
		Context context;
		int user_id;
		String photo_path;
		
		public LocationVisitedTask(Context context, int user_id, String photo_path) {
			this.context = context;
			this.user_id = user_id;
			this.photo_path = photo_path;
		}
		
        @Override
        protected String doInBackground(String... uri) {
        	
        	JSONObject holder = new JSONObject();
        	StringEntity se = null;
            try {
				holder.put("user_id", user_id);
				holder.put("location_id", location_id);
				holder.put("photo_path", photo_path);
				se = new StringEntity(holder.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
           
            DefaultHttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(uri[0]+"api/Values/UserFoundLocation");
            http_post.setEntity(se);
            http_post.setHeader("Accept", "application/json");
            http_post.setHeader("Content-type", "application/json");
            session.setConnectionCredentials(http_post);
            ResponseHandler responseHandler = new BasicResponseHandler();
            Log.d("LLL","adres: "+http_post.getURI());
            Log.d("LLL","Content: "+ holder.toString() );
            try {
				return http_client.execute(http_post, responseHandler);
			} catch (ClientProtocolException e) {
				return "protocol problem";
			} catch (IOException e) {
				return "IO problem";
			}
        }
        
        @Override
        protected void onPostExecute(String result) {
			if (result != null){
				if( result.equals("protocol problem") || result.equals("IO problem")){
					Toast.makeText(getApplicationContext(), "Problem z przes³aniem informacji na serwer", Toast.LENGTH_SHORT).show();
					okButton.setClickable(true);
					okButton.setText("OK");
				}
				if(result.equals("\"done\"")){
					
					if(number_of_found_locations >= minimum_number){
						Toast.makeText(context, "Zagadka rozwi¹zana.", Toast.LENGTH_SHORT).show();
						mSolvedTask = new RiddleSolvedTask(getApplicationContext(),session.getUserId(),task_id).execute(Session.SERVER_ADDRESS);
					}
					else{
						//powrót do gry
						finish();
						Intent intentToGame = new Intent(context, GamePlayActivity.class);
						try{
							intentToGame.putExtra("task_id",task_id);
							intentToGame.putExtra("bundle_id",bundle_id);
							intentToGame.putExtra("bundle_name",bundle_name);
							intentToGame.putExtra("name", riddle_name);
							intentToGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intentToGame.putExtra("Session", session);
							context.startActivity(intentToGame);
						} catch (Exception e)
						{
							Log.e("LLL", e.toString());
						}
					}
				}
			}
			okButton.setClickable(true);
			okButton.setText("OK");
			mPostTask = null;
        }
        
        @Override
        protected void onCancelled() {
        	okButton.setClickable(true);
			okButton.setText("OK");
        	mPostTask = null;
        }
    }

	class RiddleSolvedTask extends AsyncTask<String, String, String>{
        
		Context context;
		int user_id;
		int riddle_id;
		
		public RiddleSolvedTask(Context context, int user_id, int riddle_id) {
			this.context = context;
			this.user_id = user_id;
			this.riddle_id = riddle_id;
		}
		
        @Override
        protected String doInBackground(String... uri) {
        	
        	JSONObject holder = new JSONObject();
        	StringEntity se = null;
            try {
				holder.put("user_id", user_id);
				holder.put("riddle_id", riddle_id);
				se = new StringEntity(holder.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            
            DefaultHttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(uri[0]+"api/Values/RiddleSolved");
            http_post.setEntity(se);
            http_post.setHeader("Accept", "application/json");
            http_post.setHeader("Content-type", "application/json");
            session.setConnectionCredentials(http_post);
            ResponseHandler responseHandler = new BasicResponseHandler();
            Log.d("LLL","adres2: "+http_post.getURI());
            Log.d("LLL","Content: "+ holder.toString() );
            try {
            	
				return http_client.execute(http_post, responseHandler);
			} catch (ClientProtocolException e) {
				return "protocol problem";
			} catch (IOException e) {
				return "IO problem";
			}
        }
        
        @Override
        protected void onPostExecute(String result) {
			if (result != null){
				if( result.equals("protocol problem") || result.equals("IO problem")){
					okButton.setClickable(true);
					okButton.setText("OK");
				}
				if(result.equals("\"done\"")){
					Intent intentToRiddlesList = new Intent(context, RiddlesListActivity.class);
					try{
						finish();
						intentToRiddlesList.putExtra("id",bundle_id);
						intentToRiddlesList.putExtra("name",bundle_name);
						intentToRiddlesList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentToRiddlesList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intentToRiddlesList.putExtra("Session", session);
						context.startActivity(intentToRiddlesList);
					} catch (Exception e)
					{
						Log.e("LLL", e.toString());
					}
				}
				else{
					okButton.setClickable(true);
					okButton.setText("OK");
					Toast.makeText(getApplicationContext(), getString(R.string.solve_riddle_server_error), Toast.LENGTH_SHORT).show();
				}
			}	
			mSolvedTask = null;
        }
        
        @Override
        protected void onCancelled() {
        	mSolvedTask = null;
        	okButton.setClickable(true);
			okButton.setText("OK");
        }
    }	

	class PhotoSendTask extends AsyncTask<String, String, String>{
        
		Context context;
		Bitmap photo;
		
		public PhotoSendTask(Context context, Bitmap photo) {
			this.context = context;
			this.photo = photo;
		}
		
        @Override
        protected String doInBackground(String... uri) {
        	
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		    photo.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		    byte[] b = baos.toByteArray();
		    String encoded = Base64.encodeToString(b,Base64.DEFAULT);
		  
        	JSONObject holder = new JSONObject();
        	StringEntity se = null;
            try {
				holder.put("encoded_data", encoded);
				se = new StringEntity(holder.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            
            DefaultHttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(uri[0]+"Bundle/UploadJpeg");
            http_post.setEntity(se);
            http_post.setHeader("Accept", "application/json");
            http_post.setHeader("Content-type", "application/json");
            session.setConnectionCredentials(http_post);
            ResponseHandler responseHandler = new BasicResponseHandler();
            Log.d("LLL","adres3: "+ http_post.getURI());
            Log.d("LLL","Content3: "+ holder.toString() );
            try {
				return http_client.execute(http_post, responseHandler);
			} catch (ClientProtocolException e) {
				return "protocol problem";
			} catch (IOException e) {
				return "IO problem";
			}
        }
        
        @Override
        protected void onPostExecute(String result) {
			if (result != null){
				if( result.equals("protocol problem") || result.equals("IO problem")){
					Toast.makeText(getApplicationContext(), "Problem z przes³aniem zdjêcia", Toast.LENGTH_SHORT).show();
					okButton.setClickable(true);
					okButton.setText("OK");
				}
				else{
					result = result.replace("\\\\", "\\");
					result = result.replace("\"", "");
					//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
					mPostTask = new LocationVisitedTask(getApplicationContext(),session.getUserId(),result).execute(Session.SERVER_ADDRESS);
				}
			}	
			mPhotoTask = null;
			okButton.setClickable(true);
			okButton.setText("OK");
        }
        
        @Override
        protected void onCancelled() {
        	mPhotoTask = null;
        	okButton.setClickable(true);
			okButton.setText("OK");
        }
    }

}
