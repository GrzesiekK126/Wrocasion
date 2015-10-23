package com.wro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pietras.wro.R;
import com.wro.adapters.BundleListAdapter;
import com.wro.backend.RiddleBundle;
import com.wro.backend.Session;
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

public class BundleListActivity extends Activity {

	//BL
	Session session;
	List<RiddleBundle> bundles = null;
	
	//UI
	TextView loaded_data;
	private ListView bundle_list; 
	
	//Connection
	private AsyncTask<String, String, String> mDownloadTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bundle_list);
		
		//BL init
		session = (Session) getIntent().getSerializableExtra("Session");
		//Toast.makeText(getApplication(), "Temporary fixed user:pass", Toast.LENGTH_SHORT).show();
		//session = new Session("mkpietras@gmail.com","test22");
		bundles = new ArrayList<RiddleBundle>();
		
		//UI init
		bundle_list = (ListView) findViewById(R.id.bundleList);
		ListAdapter customAdapter = new BundleListAdapter(this, R.layout.bundle_element, bundles,session);
		bundle_list.setAdapter(customAdapter);
		ActionBar action_bar = getActionBar();
		action_bar.setTitle("Wybierz pakiet zagadek:");
		
		//Connection
		mDownloadTask = new RequestTask(getApplicationContext()).execute(Session.SERVER_ADDRESS);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bundle_list, menu);
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
		return super.onOptionsItemSelected(item);
	}
	
	class RequestTask extends AsyncTask<String, String, String>{
        
		Context context;
		
		public RequestTask(Context context) {
			this.context = context;
		}
		
        @Override
        protected String doInBackground(String... uri) {
            HttpURLConnection conn = null;
    		try {conn = (HttpURLConnection)new URL(uri[0]+"api/Values/bundles/"+session.getUserId()).openConnection();

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
				bundle_list.setAdapter(new BundleListAdapter(context, R.layout.bundle_element, bundles,session));
				mDownloadTask = null;
			}	
        }
        
        @Override
        protected void onCancelled() {
        	mDownloadTask = null;
        }
    }

	public void parseData(String data_to_parse){
		//String text = loaded_data.getText().toString();
		String text = data_to_parse;
		JSONArray arr = null;
		try {
			arr = new JSONArray(text);
		} catch (JSONException e1) {
			Toast.makeText(getApplicationContext(), "Problem with loaded data", Toast.LENGTH_SHORT).show();
		}

		bundles = new ArrayList<RiddleBundle>();
		for (int i=0; i<arr.length(); i++){
			JSONObject jsonProductObject;
			try {
				jsonProductObject = arr.getJSONObject(i);
				int id = jsonProductObject.getInt("id");
				String name = jsonProductObject.getString("name");
				int solved_number = jsonProductObject.getInt("solved_riddles");
				int riddles_number = jsonProductObject.getInt("all_riddles");
				bundles.add(new RiddleBundle(id,name,solved_number,riddles_number));
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Problem with loaded data", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
