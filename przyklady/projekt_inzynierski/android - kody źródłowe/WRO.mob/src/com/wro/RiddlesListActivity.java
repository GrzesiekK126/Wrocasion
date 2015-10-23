package com.wro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pietras.wro.R;
import com.wro.adapters.TaskInBundleListAdapter;
import com.wro.backend.Session;
import com.wro.backend.TaskInList;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RiddlesListActivity extends Activity {

	//BL
	Session session;
	ArrayList<TaskInList> riddles = null;
	int bundle_id;
	String bundle_name;
	
	//UI
	TextView loaded_data;
	private ListView riddle_list; 
	ActionBar action_bar;
	
	//Connection
	private AsyncTask<String, String, String> mDownloadTask = null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_riddles_list);
		
		//BL init
		session = (Session) getIntent().getSerializableExtra("Session");
		riddles = new ArrayList<TaskInList>();
		bundle_id = getIntent().getIntExtra("id", -1);
		bundle_name = getIntent().getStringExtra("name");
		
		//UI init
		riddle_list = (ListView) findViewById(R.id.riddleList);
		ListAdapter customAdapter = new TaskInBundleListAdapter(this, R.layout.bundle_element, riddles,session,bundle_id,bundle_name);
		riddle_list.setAdapter(customAdapter);
		action_bar = getActionBar();
		action_bar.setDisplayHomeAsUpEnabled(true);
		action_bar.setTitle(bundle_name);

		//Connection
		mDownloadTask = new RequestTask(getApplicationContext(),bundle_id).execute(Session.SERVER_ADDRESS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.riddles_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_logout) {
        	Toast.makeText(getApplicationContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
        	startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        	return true;
		}
		else if(id == android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	/*@Override
	public void onBackPressed() {
		finish();
    	Intent intent = new Intent(getApplicationContext(), BundleListActivity.class);
    	intent.putExtra("Session", session);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
	}*/
	
	
	class RequestTask extends AsyncTask<String, String, String>{
        
		Context context;
		int bundle_id;
		
		public RequestTask(Context context, int bundle_id) {
			this.context = context;
			this.bundle_id = bundle_id;
		}
		
        @Override
        protected String doInBackground(String... uri) {
            HttpURLConnection conn = null;
    		try {conn = (HttpURLConnection)new URL(uri[0]+"api/Values/bundle/"+bundle_id+"/"+session.getUserId()).openConnection();

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
				parseData(result);
				riddle_list.setAdapter(new TaskInBundleListAdapter(context, R.layout.bundle_element, riddles,session,bundle_id,bundle_name));
				mDownloadTask = null;
			}	
        }
        
        @Override
        protected void onCancelled() {
        	mDownloadTask = null;
        }
    }

	public void parseData(String data_to_parse){
		String text = data_to_parse;
		
		JSONObject root = null;
		JSONArray rids = null;
		try{
			root = new JSONObject(text);

			riddles = new ArrayList<TaskInList>();
			
			//Toast.makeText(getApplicationContext(), "Bundle: "+root.getString("name"),Toast.LENGTH_SHORT).show();
			
			rids = root.getJSONArray("tasks");
		
			for (int i=0; i<rids.length(); i++){
				JSONObject jsonProductObject;
				jsonProductObject = rids.getJSONObject(i);
				int id = jsonProductObject.getInt("id");
				String name = jsonProductObject.getString("name");
				int type_id = jsonProductObject.getInt("type_id");
				boolean is_solved = jsonProductObject.getBoolean("is_solved");
				riddles.add(new TaskInList(id, name, type_id, is_solved));
			}
		} catch (JSONException e1) {
			Toast.makeText(getApplicationContext(), "Problem with loaded data", Toast.LENGTH_SHORT).show();
		}
	}
}
