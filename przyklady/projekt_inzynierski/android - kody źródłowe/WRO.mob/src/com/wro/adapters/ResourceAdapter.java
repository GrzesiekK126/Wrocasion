package com.wro.adapters;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.pietras.wro.R;
import com.wro.backend.Resource;
import com.wro.backend.Session;


	public class ResourceAdapter extends BaseAdapter{
		ArrayList<Resource> list;
		Context context;
		
		TextView big = null;
		TextView firstLine= null;
		TextView secondLine = null;
		TextView empty_information = null;
		
		ListView list_view;
		
		public ResourceAdapter(ArrayList<Resource> l, Context c, ListView v, TextView e){
			this.list = l;
			this.context = c;
			this.list_view = v;
			this.empty_information = e;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			Resource resource_object = (Resource) getItem(position);
			
	        Log.d("LLL","resource_object: "+resource_object);

	        empty_information.setVisibility(View.INVISIBLE);
	        
	        View row;
	        
	        if( resource_object == null){
	        	row = inflater.inflate(R.layout.text_resource_view, parent,false);
	        	TextView text = (TextView) row.findViewById(R.id.textView);
        		text.setText("Brak zasobu");
	        }
	        
	        else{
	        	Log.d("LLL","resource_object path: "+resource_object.path);
		        switch( list.get(position).type ){
		        	case Resource.TEXT:
		        		Log.d("LLL","TEXT");
		        		row = inflater.inflate(R.layout.text_resource_view, parent,false);
		        		TextView text = (TextView) row.findViewById(R.id.textView);
		        		text.setText((String)list.get(position).resource_data);
		        		//if( list.get(position).resource_data == null)
		        		new ResourceDownloadTask(context,resource_object,text).execute(Session.SERVER_ADDRESS);
		        		break;
		        		
		        	case Resource.IMAGE:
		        		Log.d("LLL","IMAGE");
		        		row = inflater.inflate(R.layout.image_resource_view, parent,false);
		        		ImageView image_view = (ImageView) row.findViewById(R.id.imageView);
		        		Log.d("LLL","image_view: "+image_view);
		        		//image_view.setImageBitmap( (Bitmap) items.get(position).resource_data);
		        		//if( list.get(position).resource_data == null)
		        		new ResourceDownloadTask(context,resource_object,image_view).execute(Session.SERVER_ADDRESS);
		        		break;
		        	case Resource.SOUND:
		        		Log.d("LLL","SOUND");
		        		row = inflater.inflate(R.layout.sound_resource_view, parent,false);
		        		Button button = (Button) row.findViewById(R.id.buttonPlay);
		        		new ResourceDownloadTask(context,resource_object,button).execute(Session.SERVER_ADDRESS);
		        		break;
		        	case Resource.HTML:
		        		row = inflater.inflate(R.layout.web_resource_view, parent,false);
		        		WebView web_view = (WebView) row.findViewById(R.id.webView);
		        		new ResourceDownloadTask(context,resource_object,web_view).execute(Session.SERVER_ADDRESS);
		        		break;
		        	default:
		        		Log.d("LLL","default");
		        		row = inflater.inflate(R.layout.text_resource_view, parent,false);
			        	TextView text2 = (TextView) row.findViewById(R.id.textView);
		        		text2.setText("Zasób pusty 2");
		        		//if( list.get(position).resource_data == null)
		        		new ResourceDownloadTask(context,resource_object,text2).execute(Session.SERVER_ADDRESS);
		        		break;
		        }//switch
		        
	        }//else
			return row;
		}
		

		//Task for downloading resource file
		class ResourceDownloadTask extends AsyncTask<String, String, Object>{
	        
			Context context;
			Resource resource;
			
			Object viewToUpdate;
			
			public ResourceDownloadTask(Context context, Resource resource, Object viewToUpdate) {
				
				this.context = context;
				this.resource = resource;
				this.viewToUpdate = viewToUpdate;
			}
			
	        @Override
	        protected Object doInBackground(String... uri) {
	        	
	        	switch(resource.type){
	        	case Resource.IMAGE:
	        		try {
	    				Bitmap downloaded = BitmapFactory.decodeStream(
	    					new URL(Session.SERVER_ADDRESS +"files/"+ resource.path).openStream()
	    					);
	    				return downloaded;
	    			} catch (MalformedURLException e) {
	    				e.printStackTrace();
	    				return null;
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    				return null;
	    			}
	        	case Resource.TEXT:
	
	             	InputStream is;
					try {
						is = new URL(Session.SERVER_ADDRESS +"files/"+ resource.path).openStream();
						java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		                return s.hasNext() ? s.next() : "";
					} catch (MalformedURLException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
	        	case Resource.SOUND:
	        		return 1; //sound is played by stream so there's no need to download it
	        	case Resource.HTML:
	        		try {
						is = new URL(Session.SERVER_ADDRESS +"files/"+ resource.path).openStream();
						java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		                return s.hasNext() ? s.next() : "";
					} catch (MalformedURLException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
	        	default:
	        		return null;
	        	}
	        }
	        
	        @Override
	        protected void onPostExecute(Object result) {
	        	
	        	resource.resource_data = result;
	        	
	        	if(result==null){
	        		Toast.makeText(context, "Problem z pobraniem zasobów.", Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	switch(resource.type){
		        	case Resource.IMAGE:
		        		((ImageView)viewToUpdate).setImageBitmap( (Bitmap) result);
		        		break;
		        	case Resource.TEXT:
		        		((TextView)viewToUpdate).setText(result.toString());
		        		break;
		        	case Resource.SOUND:
		        		((Button)viewToUpdate).setTag( Session.SERVER_ADDRESS + "files/" + resource.path);
		        		((Button)viewToUpdate).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String path =(String) v.getTag();
								MediaPlayer mediaPlayer = new MediaPlayer();
								mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
								try {
									mediaPlayer.setDataSource(path);
								} catch (Exception e) {
									Toast.makeText(context,"Problem ze Ÿród³em dŸwiêku.", Toast.LENGTH_SHORT).show();
								}
								mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
							        @Override
							        public void onPrepared(MediaPlayer mp) {
							            mp.start();
							        }
							    });
							    mediaPlayer.setOnErrorListener(new OnErrorListener() {
							        @Override
							        public boolean onError(MediaPlayer mp, int what, int extra) {
							            return false;
							        }
							    });
							    mediaPlayer.prepareAsync();
							}
						});
		        		break;
		        	case Resource.HTML:
		        		((WebView)viewToUpdate).loadDataWithBaseURL("", result.toString(), "text/html", "UTF-8", "");
		        		break;
	        		default:
	        			break;
	        	}
	        	
	        	list_view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        }
	        
	        @Override
	        protected void onCancelled() {
	        }
	    }//task
		
	}//class
