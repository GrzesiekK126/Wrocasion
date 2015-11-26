package app.wrocasion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import android.view.View.OnClickListener;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    static TextView txtSkip;
    static CallbackManager callbackManager;
    private static Context context;
    static ProfilePictureView profilePhotoStart;
    static TextView userNameStart, txtView4;
    static boolean logIn, navLogin = false;
    static Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.start_activity);

            profilePhotoStart = (ProfilePictureView) findViewById(R.id.profileImageStart);
            userNameStart = (TextView) findViewById(R.id.usernameStart);

            txtView4 = (TextView) findViewById(R.id.textView4);

            txtSkip = (TextView) findViewById(R.id.textView2);
            txtSkip.setOnClickListener(this);

            context = this;

            btn = (Button) findViewById(R.id.button2);
            btn.setOnClickListener(this);

        if (checkLogIn() == true) {
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
        }else {
            txtView4.setText(R.string.logout);
            userNameStart.setText("");
            profilePhotoStart.setVisibility(View.INVISIBLE);
            txtSkip.setText(R.string.skip);
        }
            //loginToFacebook();

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

    static String getFirstName(Profile profile){
        String name;
        if(profile!=null) {
            name = profile.getFirstName();
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
        /*accessTokenTracker.stopTracking();
        profileTracker.stopTracking();*/

    }

    static void loginToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        if(!checkLogIn()){

            LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (checkLogIn() == true) {
                        //userNameStart.setText("Witaj " + getFirstName(Profile.getCurrentProfile()) + "!");
                        userNameStart.setText(getName(Profile.getCurrentProfile()));
                        profilePhotoStart.setVisibility(View.VISIBLE);
                        profilePhotoStart.setProfileId(getId(Profile.getCurrentProfile()));
                        txtView4.setText("");
                        txtSkip.setText(R.string.next);
                        btn.setBackgroundResource(R.drawable.logout_button);

                        if(navLogin){
                            FirstActivity.profilePhoto.setVisibility(View.VISIBLE);
                            FirstActivity.userName.setText(getName(Profile.getCurrentProfile()));
                            FirstActivity.profilePhoto.setProfileId(getId(Profile.getCurrentProfile()));
                        }
                        else {
                            /*FirstActivity.profilePhoto.setVisibility(View.INVISIBLE);
                            FirstActivity.userName.setText(R.string.logout);*/
                        }
                    } else {
                        txtView4.setText(R.string.logout);
                        userNameStart.setText("");
                        profilePhotoStart.setVisibility(View.INVISIBLE);
                        txtSkip.setText(R.string.skip);
                        btn.setBackgroundResource(R.drawable.login_button);
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

    }
        else{
            LoginManager.getInstance().logOut();
            txtView4.setText(R.string.logout);
            userNameStart.setText("");
            profilePhotoStart.setVisibility(View.INVISIBLE);
            txtSkip.setText(R.string.skip);
            btn.setBackgroundResource(R.drawable.login_button);
        }
    }


    static boolean checkLogIn() {

        if (AccessToken.getCurrentAccessToken() != null) {
             logIn = true;
        } else {
             logIn = false;
        }
        return logIn;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView2) {
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.button2) {
            loginToFacebook();
        }
    }
}