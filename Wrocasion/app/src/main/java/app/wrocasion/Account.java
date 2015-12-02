package app.wrocasion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Arrays;

import app.wrocasion.JSONs.AddUserJSON;
import app.wrocasion.JSONs.AllCategoriesJSON;
import app.wrocasion.JSONs.RemoveUserJSON;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Account extends AppCompatActivity implements View.OnClickListener{

    static Button button;
    static ProfilePictureView profilePictureView;
    static TextView textView;
    static Context context;
    static boolean logIn, isLogin;
    static CallbackManager callbackManager;

    static RestAdapter retrofit;
    static AddUserJSON.WebServiceAddUser webServiceAddUser;
    static RemoveUserJSON.WebServiceRemoveUser webServiceRemoveUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(this);

        profilePictureView = (ProfilePictureView) findViewById(R.id.profileImageAccount);

        textView = (TextView) findViewById(R.id.textViewAccount);

        context = this;
        getFacebookInfo();

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        webServiceAddUser = retrofit.create(AddUserJSON.WebServiceAddUser.class);
        webServiceRemoveUser = retrofit.create(RemoveUserJSON.WebServiceRemoveUser.class);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button3){
            loginToFacebook();
        }

    }

    static void getFacebookInfo(){
        if(!checkLogIn()){
            //Toast.makeText(context,"Nie weszło",Toast.LENGTH_SHORT).show();
            button.setBackgroundResource(R.drawable.login_button);
            textView.setText(R.string.logout);
            profilePictureView.setVisibility(View.INVISIBLE);
        }
        else{
            //Toast.makeText(context,"Weszło",Toast.LENGTH_SHORT).show();
            button.setBackgroundResource(R.drawable.logout_button);
            textView.setText(getName(Profile.getCurrentProfile()));
            profilePictureView.setVisibility(View.VISIBLE);
            profilePictureView.setProfileId(getId(Profile.getCurrentProfile()));

            /*FirstActivity.profilePhoto.setVisibility(View.VISIBLE);
            FirstActivity.userName.setText(getName(Profile.getCurrentProfile()));
            FirstActivity.profilePhoto.setProfileId(getId(Profile.getCurrentProfile()));*/
        }
    }

    static void loginToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        if(!checkLogIn()){
            LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    getFacebookInfo();
                    isLogin = true;
                    AddUserJSON addUser = new AddUserJSON();
                    addUser.setName(getId(Profile.getCurrentProfile()));

                    webServiceAddUser.postData(addUser, new Callback<AddUserJSON>() {
                        @Override
                        public void success(AddUserJSON myWebServiceResponse, Response response) {
                            Log.d("Account", myWebServiceResponse.getName());
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
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
            RemoveUserJSON removeUser = new RemoveUserJSON();
            removeUser.setName(getId(Profile.getCurrentProfile()));

            webServiceRemoveUser.postData(removeUser, new Callback<RemoveUserJSON>() {
                @Override
                public void success(RemoveUserJSON myWebServiceResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            LoginManager.getInstance().logOut();
            getFacebookInfo();
            isLogin = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
