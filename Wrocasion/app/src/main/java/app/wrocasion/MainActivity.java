package app.wrocasion;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import android.view.View.OnClickListener;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private TextView txtSkip;
    static LoginButton loginButton;
    static CallbackManager callbackManager;
    static AccessTokenTracker accessTokenTracker;
    static ProfileTracker profileTracker;
    static boolean loginFacebook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.start_activity);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        txtSkip = (TextView) findViewById(R.id.textView2);
        txtSkip.setOnClickListener(this);

        loginToFacebook();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static String getName(Profile profile){
        String name;
        if(profile!=null) {
            name = profile.getName();
        }
        else {
            name = null;
        }
        return name;
    }

    static String getId(Profile profile){
        String id;
        if(profile!=null) {
            id = profile.getId();
        }
        else {
            id = null;
        }
        return id;
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.textView2){
           Intent intent = new Intent(this, FirstActivity.class);
           startActivity(intent);

        }
        /*else if(v.getId()==R.id.login_button){
           if(loginFacebook) {
               Intent intent = new Intent(this, FirstActivity.class);
               startActivity(intent);
           }
       }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        getName(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    static void loginToFacebook(){
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {



            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginFacebook = true;

    }


}