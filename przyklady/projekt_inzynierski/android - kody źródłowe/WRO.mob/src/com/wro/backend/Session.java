package com.wro.backend;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import org.apache.http.client.methods.HttpPost;
import com.wro.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

public class Session implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SERVER_ADDRESS = "http://wro.azurewebsites.net/";
	
	private String username;
	private String password;
	private int userId;
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Session(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public Session(String username, String password, int userId){
		this.username = username;
		this.password = password;
		this.userId = userId;
	}
	
	public void setCredentials(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public void setConnectionCredentials(HttpURLConnection connection){
		String cred = username + ":" + password;
		
		try {
			connection.setRequestProperty("Authorization", 
					"basic " + new String(
							Base64.encode(
									cred.getBytes("US-ASCII"),
									Base64.NO_WRAP
									)
			));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setConnectionCredentials(HttpPost http_post){
		String cred = username + ":" + password;
		try {
			http_post.setHeader("Authorization", 
					"basic " + new String(
							Base64.encode(
									cred.getBytes("US-ASCII"),
									Base64.NO_WRAP
									)
			));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logout(Context context){
		Intent intentLogout = new Intent(context, LoginActivity.class);
		try{
			intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentLogout);
		} catch (Exception e)
		{
			Log.e("LLL", e.toString());
		}
		
	}
	
}
