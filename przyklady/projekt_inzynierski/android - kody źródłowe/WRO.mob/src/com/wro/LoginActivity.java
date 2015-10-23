package com.wro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pietras.wro.R;
import com.wro.backend.Session;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity{

    private AsyncTask<String, String, String> mAuthTask = null;
    private AsyncTask<String, String, String> mRegisterTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar action_bar = getActionBar();
		action_bar.setTitle("Zaloguj siê:");
        
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
       // populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        	showProgress(true);
        	mAuthTask = new LoginTask(email,password).execute(Session.SERVER_ADDRESS);
            
            /*mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
        }
    }
    
    public void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        	showProgress(true);
        	mRegisterTask = new RegisterTask(email,password).execute(Session.SERVER_ADDRESS);
            
            /*mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
        }
    }
    
    
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class LoginTask extends AsyncTask<String, String, String>{

    	private final String mEmail;
        private final String mPassword;

        LoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        
        @Override
        protected String doInBackground(String... uri) {
            Log.d("LLL","Connecting...");

            HttpURLConnection conn = null;
    		try {conn = (HttpURLConnection)new URL(uri[0]+"api/Values/login").openConnection();

    			session = new Session(mEmail, mPassword);
    			//session.setCredentials(mEmail, mPassword);
            	session.setConnectionCredentials(conn);

            	conn.setConnectTimeout(5000);
            	conn.setDoInput(true);			
            	
            	if( conn.getResponseCode() == 200){
            		InputStream is = conn.getInputStream();
                	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                    return s.hasNext() ? s.next() : "";
            	}         
            	else
            		return null;
            }
            catch(Exception e){
            	Log.d("LLL","Exception occured: " + e.getMessage());
            	return null;
            }
            finally{
            	if(conn != null)
            		conn.disconnect();
            }
        }
        
        @Override
        protected void onPostExecute(String result) {

            mAuthTask = null;
            showProgress(false);

            if (result != null) {
                
                int userId = Integer.parseInt(result);
                session.setUserId(userId);
                Log.d("LLL","logged. userId: "+userId);
            	finish();
            	Intent intent = new Intent(getApplicationContext(), BundleListActivity.class);
            	intent.putExtra("Session", session);
            	startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        
        @Override
        protected void onCancelled() {
        	mAuthTask = null;
            showProgress(false);
        }
    }

    class RegisterTask extends AsyncTask<String, String, String>{

    	private final String mEmail;
        private final String mPassword;

        RegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        
        @Override
        protected String doInBackground(String... uri) {
            Log.d("LLL","Connecting...");

            HttpURLConnection conn = null;
    		try {conn = (HttpURLConnection)new URL(uri[0]+"api/Values/register").openConnection();

    			session = new Session(mEmail, mPassword);
            	session.setConnectionCredentials(conn);

            	conn.setConnectTimeout(5000);
            	conn.setDoInput(true);			
            	
            	InputStream is = conn.getInputStream();
            	Log.d("LLL",is.toString());
            	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                return s.hasNext() ? s.next() : "";
            	
            }
            catch(Exception e){
            	
            	runOnUiThread(new Runnable() {
					String message;
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(),"Problem with logging", Toast.LENGTH_LONG).show();
					}
					
					public Runnable init(String message){
						this.message = message;
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

            mRegisterTask = null;
            showProgress(false);
            Log.d("LLL", "zawartosc: \"" + result + "\"");
            if (result.equals("\"done\"")){
            	 Toast.makeText(getApplicationContext(), getResources().getString(R.string.account_created), Toast.LENGTH_SHORT).show();
            }
            else if (result.equals("\"exist\"")){
            	Toast.makeText(getApplicationContext(), getResources().getString(R.string.account_exists), Toast.LENGTH_SHORT).show();
            }
            else if (result.equals("\"no\"")){
            	Toast.makeText(getApplicationContext(), getResources().getString(R.string.register_problem), Toast.LENGTH_SHORT).show();
            }
            else{
            	Toast.makeText(getApplicationContext(), getResources().getString(R.string.unknown_problem), Toast.LENGTH_SHORT).show();
            }
        }
        
        @Override
        protected void onCancelled() {
        	mRegisterTask = null;
            showProgress(false);
        }
    }

}