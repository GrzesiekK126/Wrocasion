package app.wrocasion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.facebook.login.widget.ProfilePictureView;

import java.util.Arrays;

import app.wrocasion.JSONs.AddUser;
import app.wrocasion.JSONs.RemoveUser;
import app.wrocasion.JSONs.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Account extends AppCompatActivity implements View.OnClickListener{

    static Button facebookButton, loginButton, goToLogin, goToFacebookLogin,
            backLogin, backFacebook, backCreateAccount;
    static ProfilePictureView profilePictureView;
    static TextView textView;
    static Context context;
    static boolean logIn, isLogin;
    static CallbackManager callbackManager;
    private String blockCharacterSet;
    static EditText etUsername, etPassword;
    static TextView tvCreateAccount;

    LinearLayout login, facebookLogin, createAccount;
    RelativeLayout loginSelection, accountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginSelection = (RelativeLayout) findViewById(R.id.loginSelection);
        accountLayout = (RelativeLayout) findViewById(R.id.accountLayout);
        login = (LinearLayout) findViewById(R.id.login);
        facebookLogin = (LinearLayout) findViewById(R.id.facebookLogin);
        createAccount = (LinearLayout) findViewById(R.id.createAccount);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        facebookButton = (Button) findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(this);

        goToLogin = (Button) findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(this);

        goToFacebookLogin = (Button) findViewById(R.id.goToFacebookLogin);
        goToFacebookLogin.setOnClickListener(this);

        backLogin = (Button) findViewById(R.id.backLogin);
        backLogin.setOnClickListener(this);

        backFacebook = (Button) findViewById(R.id.backFacebook);
        backFacebook.setOnClickListener(this);

        backCreateAccount = (Button) findViewById(R.id.backCreateAccount);
        backCreateAccount.setOnClickListener(this);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvCreateAccount = (TextView) findViewById(R.id.create_account);
        tvCreateAccount.setOnClickListener(this);

        blockCharacterSet = getResources().getString(R.string.restricted_string);

        etPassword.setFilters(new InputFilter[] {filter});
        etUsername.setFilters(new InputFilter[] {filter});

        profilePictureView = (ProfilePictureView) findViewById(R.id.profileImageAccount);

        textView = (TextView) findViewById(R.id.textViewAccount);

        context = this;
        getFacebookInfo();

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.facebookButton){
            loginToFacebook();
        }
        else if(v.getId() == R.id.loginButton){
            loginToApp();
        }
        else if(v.getId() == R.id.goToLogin){
            login.setVisibility(View.VISIBLE);
            accountLayout.setVisibility(View.VISIBLE);
            loginSelection.setVisibility(View.GONE);
            facebookLogin.setVisibility(View.GONE);
            createAccount.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.goToFacebookLogin){
            facebookLogin.setVisibility(View.VISIBLE);
            accountLayout.setVisibility(View.VISIBLE);
            loginSelection.setVisibility(View.GONE);
            login.setVisibility(View.GONE);
            createAccount.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.backLogin || v.getId() == R.id.backFacebook || v.getId() == R.id.backCreateAccount){
            login.setVisibility(View.GONE);
            accountLayout.setVisibility(View.GONE);
            loginSelection.setVisibility(View.VISIBLE);
            facebookLogin.setVisibility(View.GONE);
            createAccount.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.create_account){
            login.setVisibility(View.GONE);
            accountLayout.setVisibility(View.VISIBLE);
            loginSelection.setVisibility(View.GONE);
            facebookLogin.setVisibility(View.GONE);
            createAccount.setVisibility(View.VISIBLE);
        }
    }

    void loginToApp() {

        etUsername.setError(null);
        etPassword.setError(null);

        if(etUsername.getText().toString().length() < 6){
            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_LONG).show();
        } else{

        }
        if(etPassword.getText().toString().length() < 6){
            etPassword.setError("Za krótkie hasło");
        } else{
            //jeżeli hasło jest inne niż w bazie danych, wywal inny error
        }

    }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    static void getFacebookInfo(){
        if(!checkLogIn()){
            facebookButton.setBackgroundResource(R.drawable.facebook_login_button);
            textView.setText(R.string.logout);
            profilePictureView.setVisibility(View.INVISIBLE);
        }
        else{
            facebookButton.setBackgroundResource(R.drawable.facebook_logout_button);
            textView.setText(getName(Profile.getCurrentProfile()));
            profilePictureView.setVisibility(View.VISIBLE);
            profilePictureView.setProfileId(getId(Profile.getCurrentProfile()));
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
                    AddUser addUser = new AddUser();
                    addUser.setName(getId(Profile.getCurrentProfile()));

                    RestClient.get().addUser(addUser, new Callback<AddUser>() {
                        @Override
                        public void success(AddUser myWebServiceResponse, Response response) {
                            Log.d("Account", myWebServiceResponse.getName());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
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
            RemoveUser removeUser = new RemoveUser();
            removeUser.setName(getId(Profile.getCurrentProfile()));

            RestClient.get().removeUser(removeUser, new Callback<RemoveUser>() {
                @Override
                public void success(RemoveUser myWebServiceResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
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

    public static String getId(Profile profile){
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
