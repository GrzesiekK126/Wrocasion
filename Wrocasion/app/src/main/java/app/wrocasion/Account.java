package app.wrocasion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import app.wrocasion.JSONs.LoginResponse;
import app.wrocasion.JSONs.LoginUser;
import app.wrocasion.JSONs.RestClient;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Account extends AppCompatActivity implements View.OnClickListener{

    static Button facebookButton, loginButton, backCreateAccount;
    static ProfilePictureView profilePictureView;
    static TextView textView,tvCreateAccount, tvAppLogin;
    static Context context;
    static boolean logIn, isLogin;
    static CallbackManager callbackManager;
    static EditText etUsername, etPassword;
    static SweetAlertDialog sweetAlertDialog;

    private String blockCharacterSet;
    private boolean loginApp;

    LinearLayout login, facebookLogin, createAccount, appLogin, tvAppLoginLayout, editTextsLayout;
    RelativeLayout accountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountLayout = (RelativeLayout) findViewById(R.id.accountLayout);
        login = (LinearLayout) findViewById(R.id.login);
        facebookLogin = (LinearLayout) findViewById(R.id.facebookLogin);
        createAccount = (LinearLayout) findViewById(R.id.createAccount);
        appLogin = (LinearLayout) findViewById(R.id.appLogin);
        tvAppLoginLayout = (LinearLayout) findViewById(R.id.tvAppLoginLayout);
        editTextsLayout = (LinearLayout) findViewById(R.id.editTextsLayout);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        facebookButton = (Button) findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(this);

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
        tvAppLogin = (TextView) findViewById(R.id.tvAppLogin);

        context = this;
        getFacebookInfo();

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.facebookButton){
            loginToFacebook();
            login.setVisibility(View.VISIBLE);
            appLogin.setVisibility(View.GONE);
            facebookLogin.setVisibility(View.VISIBLE);
        }
        else if(v.getId() == R.id.loginButton){
            if(loginApp){
                loginApp = false;
                login.setVisibility(View.VISIBLE);
                facebookLogin.setVisibility(View.VISIBLE);
                appLogin.setVisibility(View.VISIBLE);
                createAccount.setVisibility(View.GONE);
                editTextsLayout.setVisibility(View.VISIBLE);
                tvAppLoginLayout.setVisibility(View.GONE);
                loginButton.setText(R.string.login_button);
            }else {
                loginToApp();
                login.setVisibility(View.VISIBLE);
                appLogin.setVisibility(View.VISIBLE);
                facebookLogin.setVisibility(View.INVISIBLE);
                editTextsLayout.setVisibility(View.GONE);
                tvAppLoginLayout.setVisibility(View.VISIBLE);
            }

        }
        else if(v.getId() == R.id.backCreateAccount){
            login.setVisibility(View.VISIBLE);
            facebookLogin.setVisibility(View.VISIBLE);
            appLogin.setVisibility(View.VISIBLE);
            createAccount.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.create_account){
            login.setVisibility(View.GONE);
            createAccount.setVisibility(View.VISIBLE);
        }
    }

    void loginToApp() {

        etUsername.setError(null);
        etPassword.setError(null);

        if(etUsername.getText().toString().length() > 20){
            etUsername.setError("Za długa nazwa użytkownika");
        } else{
            if(etPassword.getText().toString().length() < 6){
                etPassword.setError("Za krótkie hasło");
            } else{
                final LoginUser loginUser = new LoginUser();
                loginUser.setName(etUsername.getText().toString());
                loginUser.setPassword(etPassword.getText().toString());

                RestClient.get().loginUser(loginUser, new Callback<LoginResponse>() {
                    @Override
                    public void success(LoginResponse loginResponse, Response response) {
                        if(loginResponse.getMessage().equals("Password is incorrect")){
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Błąd!");
                            sweetAlertDialog.setContentText("Wpisano złe hasło!");
                            sweetAlertDialog.show();

                        } else if(loginResponse.getMessage().equals("Username is incorrect")){
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Błąd!");
                            sweetAlertDialog.setContentText("Wpisano złą nazwę użytkownika!");
                            sweetAlertDialog.show();
                        } else{
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Sukces!");
                            sweetAlertDialog.setContentText("Zalogowano poprawnie!");
                            sweetAlertDialog.show();
                            tvAppLogin.setText("Zalogowany jako " + loginUser.getName());
                            loginButton.setText(R.string.logout_button);
                        }
                        loginApp = true;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
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
        if(!checkLogInFacebook()){
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
        if(!checkLogInFacebook()){
            LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    getFacebookInfo();
                    isLogin = true;
                    LoginUser loginUser = new LoginUser();
                    loginUser.setName(getId(Profile.getCurrentProfile()));
                    loginUser.setPassword("");

                    RestClient.get().loginUser(loginUser, new Callback<LoginResponse>() {
                        @Override
                        public void success(LoginResponse loginResponse, Response response) {
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Sukces!");
                            sweetAlertDialog.setContentText("Zalogowano poprawnie!");
                            sweetAlertDialog.show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Błąd!");
                            sweetAlertDialog.setContentText("Wystąpił problem z zalogowaniem za pomocą Facebooka!");
                            sweetAlertDialog.show();
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
            /*RemoveUser removeUser = new RemoveUser();
            removeUser.setName(getId(Profile.getCurrentProfile()));

            RestClient.get().removeUser(removeUser, new Callback<RemoveUser>() {
                @Override
                public void success(RemoveUser myWebServiceResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });*/

            LoginManager.getInstance().logOut();
            getFacebookInfo();
            isLogin = false;
        }
    }

    static boolean checkLogInFacebook() {

        if (AccessToken.getCurrentAccessToken() != null) {
            logIn = true;
        } else {
            logIn = false;
        }
        return logIn;
    }

    public boolean isLoginApp() {
        return loginApp;
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
