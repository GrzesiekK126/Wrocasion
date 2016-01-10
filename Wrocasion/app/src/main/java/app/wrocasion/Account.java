package app.wrocasion;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.baoyz.widget.PullRefreshLayout;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.regex.Pattern;

import app.wrocasion.Events.ChangeUserCategories;
import app.wrocasion.Events.EventsCategories;
import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.JSONs.AddUser;
import app.wrocasion.JSONs.LoginResponse;
import app.wrocasion.JSONs.LoginUser;
import app.wrocasion.JSONs.RestClient;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Account extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    static Button facebookButton, loginButton, backCreateAccount, createAccountButton, logoutButton;
    static ProfilePictureView profilePictureView;
    static TextView tvCreateAccount, tvAppLogin, tvSkipLogin, textViewLoggedIn, tvLogin, userName;
    static Context context;
    static boolean logIn, isLoginToFacebook, loginApp, loginToApp, exit = false;
    static CallbackManager callbackManager;
    static EditText etUsername, etPassword, etCreateUsername, etCreatePassword, etEmail;
    static SweetAlertDialog sweetAlertDialog;
    static String userLoginToApp = "";

    private String blockCharacterSet;

    static LinearLayout login, facebookLogin, createAccount, appLogin, tvAppLoginLayout, editTextsLayout,
            loggedIn;
    RelativeLayout accountLayout;
    boolean validEmail = false, validUsername = false, validPassword = false;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_account);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        accountLayout = (RelativeLayout) findViewById(R.id.accountLayout);
        login = (LinearLayout) findViewById(R.id.login);
        facebookLogin = (LinearLayout) findViewById(R.id.facebookLogin);
        createAccount = (LinearLayout) findViewById(R.id.createAccount);
        appLogin = (LinearLayout) findViewById(R.id.appLogin);
        tvAppLoginLayout = (LinearLayout) findViewById(R.id.tvAppLoginLayout);
        editTextsLayout = (LinearLayout) findViewById(R.id.editTextsLayout);
        loggedIn = (LinearLayout) findViewById(R.id.loggedIn);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        facebookButton = (Button) findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(this);

        backCreateAccount = (Button) findViewById(R.id.backCreateAccount);
        backCreateAccount.setOnClickListener(this);

        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(this);

        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCreateUsername = (EditText) findViewById(R.id.etCreateUsername);
        etCreatePassword = (EditText) findViewById(R.id.etCreatePassword);
        etEmail = (EditText) findViewById(R.id.etEmail);

        tvCreateAccount = (TextView) findViewById(R.id.create_account);
        tvCreateAccount.setOnClickListener(this);
        tvSkipLogin = (TextView) findViewById(R.id.skipLogin);
        tvSkipLogin.setOnClickListener(this);

        blockCharacterSet = getResources().getString(R.string.restricted_string);

        etPassword.setFilters(new InputFilter[]{filter});
        etUsername.setFilters(new InputFilter[]{filter});

        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_image_account);
        userName = (TextView) findViewById(R.id.username_account);

        tvAppLogin = (TextView) findViewById(R.id.tvAppLogin);
        textViewLoggedIn = (TextView) findViewById(R.id.textViewLoggedIn);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        context = this;

        if (checkLoginToApp()) {
            getAppLoginInfo();
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.GONE);
            setVisibilityLoggedIn(View.VISIBLE);
        } else if (checkLogInFacebook()) {
            getFacebookInfo();
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.GONE);
            setVisibilityLoggedIn(View.VISIBLE);
        } else {
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.VISIBLE);
            setVisibilityLoggedIn(View.GONE);
        }

        PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(context, Account.class);
                startActivity(intent);
                /*Account account = new Account();
                FragmentTransaction accountFragmentTransaction = getSupportFragmentManager().beginTransaction();
                accountFragmentTransaction.replace(R.id.frame, account);
                accountFragmentTransaction.commit();*/
            }
        });

        pullRefreshLayout.setRefreshing(false);


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view_account);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.events_tabs:
                        FirstActivity.accountNavigation = true;
                        FirstActivity.menuItem = "eventsListTabs";
                        Intent intent = new Intent(context, FirstActivity.class);
                        startActivity(intent);

                        return true;

                    case R.id.add_or_change_user_categories:
                        FirstActivity.accountNavigation = true;
                        FirstActivity.menuItem = "changeUserCategories";
                        Intent intent2 = new Intent(context, FirstActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.user_account:
                        FirstActivity.accountNavigation = true;
                        FirstActivity.menuItem = "account";
                        Intent intent3 = new Intent(context, FirstActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.app_rating:
                        FirstActivity.accountNavigation = true;
                        FirstActivity.menuItem = "appRating";
                        Intent intent4 = new Intent(context, FirstActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.about:
                        FirstActivity.accountNavigation = true;
                        FirstActivity.menuItem = "about";
                        Intent intent5 = new Intent(context, FirstActivity.class);
                        startActivity(intent5);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_account);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.facebookButton) {
            loginToFacebook();

            /*EventsCategories eventsCategories = new EventsCategories();
            FragmentTransaction categoriesFragmentTransaction = FirstActivity.fragmentManager.beginTransaction();
            categoriesFragmentTransaction.replace(R.id.frame, eventsCategories);
            categoriesFragmentTransaction.commit();*/

        } else if (v.getId() == R.id.loginButton) {
            loginToApp();
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.GONE);
            setVisibilityLoggedIn(View.VISIBLE);
        } else if (v.getId() == R.id.logout) {
            if (checkLoginToApp()) {
                logoutApp();
                etUsername.setText(null);
                etPassword.setText(null);
                loginApp = false;
                setVisibilityCreateLayout(View.GONE);
                setVisibilityLoginLayout(View.VISIBLE);
                setVisibilityLoggedIn(View.GONE);
            } else if (checkLogInFacebook()) {
                logoutFacebook();
                setVisibilityCreateLayout(View.GONE);
                setVisibilityLoginLayout(View.VISIBLE);
                setVisibilityLoggedIn(View.GONE);
            }
        } else if (v.getId() == R.id.backCreateAccount) {
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.VISIBLE);
            setVisibilityLoggedIn(View.GONE);
        } else if (v.getId() == R.id.create_account) {
            setVisibilityCreateLayout(View.VISIBLE);
            setVisibilityLoginLayout(View.GONE);
            setVisibilityLoggedIn(View.GONE);
            etCreateUsername.setText(null);
            etCreatePassword.setText(null);
            etEmail.setText(null);
        } else if (v.getId() == R.id.createAccountButton) {
            createAccount();
            setVisibilityCreateLayout(View.GONE);
            setVisibilityLoginLayout(View.GONE);
            setVisibilityLoggedIn(View.VISIBLE);
        } else if (v.getId() == R.id.skipLogin) {
            FirstActivity.accountNavigation = true;
            FirstActivity.menuItem = "eventsCategories";
            Intent intent5 = new Intent(context, FirstActivity.class);
            startActivity(intent5);
        }
    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    public static boolean isValidEmail(CharSequence target) {
        return EMAIL_ADDRESS_PATTERN.matcher(target).matches();
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


    static void getFacebookInfo() {

        if(checkLogInFacebook()) {
            logoutButton.setBackgroundResource(R.drawable.facebook_button_logout);
            tvLogin.setText(getName(Profile.getCurrentProfile()));
            profilePictureView.setVisibility(View.VISIBLE);
            userName.setText(Account.getName(Profile.getCurrentProfile()));
            profilePictureView.setProfileId(Account.getId(Profile.getCurrentProfile()));
        } else{
            profilePictureView.setVisibility(View.INVISIBLE);
            userName.setText(R.string.logout);
        }
        //profilePictureView.setVisibility(View.VISIBLE);
        //profilePictureView.setProfileId(getId(Profile.getCurrentProfile()));
    }

    static void getAppLoginInfo() {
        if(checkLoginToApp()) {
            String username = null;
            BaseHelper baseHelper = new BaseHelper(context);
            Cursor cursor = baseHelper.getUserFromDatabase();
            while (cursor.moveToNext()) {
                int nr = cursor.getInt(0);
                username = cursor.getString(1);
            }
            profilePictureView.setVisibility(View.GONE);
            userName.setText(username);
            tvLogin.setText(username);
            logoutButton.setBackgroundResource(R.drawable.app_button_logout);
        } else {
            userName.setText(R.string.logout);
        }
    }


    void createAccount() {
        etCreateUsername.setError(null);
        etCreatePassword.setError(null);
        etEmail.setError(null);

        if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Niepoprawny adres email");
            validEmail = false;
        } else {
            validEmail = true;
        }
        if (etCreatePassword.getText().toString().length() < 6) {
            etCreatePassword.setError("Za krótkie hasło");
            validPassword = false;
        } else {
            validPassword = true;
        }
        if (etCreateUsername.getText().toString().isEmpty()) {
            etCreateUsername.setError("Pole wymagane");
            validUsername = false;
        } else {
            validUsername = true;
        }
        if (validEmail && validUsername && validPassword) {
            final AddUser addUser = new AddUser();
            addUser.setName(etCreateUsername.getText().toString());
            addUser.setPassword(etCreatePassword.getText().toString());
            addUser.setEmail(etEmail.getText().toString());

            RestClient.get().addUser(addUser, new Callback<LoginResponse>() {
                @Override
                public void success(LoginResponse loginResponse, Response response) {
                    if (loginResponse.getMessage().toString().equals("User with that name already exists")) {
                        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Błąd!");
                        sweetAlertDialog.setContentText("Użytkownik o tej nazwie juz istnieje!");
                        sweetAlertDialog.show();
                    } else if (loginResponse.getMessage().toString().equals("Add new user by registartion")) {
                        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Sukces!");
                        sweetAlertDialog.setContentText("Zalogowano poprawnie!");
                        sweetAlertDialog.show();

                        loginApp = true;

                        BaseHelper baseHelper = new BaseHelper(context);
                        baseHelper.addUserToDatabase(addUser.getName());

                        getAppLoginInfo();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
        FirstActivity.accountNavigation = true;
        FirstActivity.menuItem = "eventsCategories";
        Intent intent5 = new Intent(context, FirstActivity.class);
        startActivity(intent5);
    }

    void loginToApp() {

        etUsername.setError(null);
        etPassword.setError(null);

        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError("Pole wymagane");
        } else {
            if (etPassword.getText().toString().length() < 6) {
                etPassword.setError("Za krótkie hasło");
            } else {
                final LoginUser loginUser = new LoginUser();
                loginUser.setName(etUsername.getText().toString());
                loginUser.setPassword(etPassword.getText().toString());

                RestClient.get().loginUser(loginUser, new Callback<LoginResponse>() {
                    @Override
                    public void success(LoginResponse loginResponse, Response response) {
                        if (loginResponse.getMessage().equals("Password is incorrect")) {
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Błąd!");
                            sweetAlertDialog.setContentText("Wpisano złe hasło!");
                            sweetAlertDialog.show();

                        } else if (loginResponse.getMessage().equals("Username is incorrect")) {
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Błąd!");
                            sweetAlertDialog.setContentText("Wpisano złą nazwę użytkownika!");
                            sweetAlertDialog.show();
                        } else {
                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Sukces!");
                            sweetAlertDialog.setContentText("Zalogowano poprawnie!");
                            sweetAlertDialog.show();

                            BaseHelper baseHelper = new BaseHelper(context);
                            baseHelper.addUserToDatabase(loginUser.getName());

                            getAppLoginInfo();

                            FirstActivity.accountNavigation = true;
                            FirstActivity.menuItem = "eventsCategories";
                            Intent intent5 = new Intent(context, FirstActivity.class);
                            startActivity(intent5);

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

    void logoutApp() {
        context.deleteDatabase("database.db");
        loginToApp = false;
        Toast.makeText(context, "Poprawnie usunięto", Toast.LENGTH_SHORT).show();
    }

    void loginToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        if (!checkLogInFacebook()) {
            LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    isLoginToFacebook = true;
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

                            getFacebookInfo();

                            setVisibilityCreateLayout(View.GONE);
                            setVisibilityLoginLayout(View.GONE);
                            setVisibilityLoggedIn(View.VISIBLE);

                            FirstActivity.accountNavigation = true;
                            FirstActivity.menuItem = "eventsCategories";
                            Intent intent5 = new Intent(context, FirstActivity.class);
                            startActivity(intent5);

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
    }

    void logoutFacebook() {
        LoginManager.getInstance().logOut();
        getFacebookInfo();
        isLoginToFacebook = false;
    }


    public static boolean checkLogInFacebook() {

        if (AccessToken.getCurrentAccessToken() != null) {
            logIn = true;
        } else {
            logIn = false;
        }
        return logIn;
    }

    public static boolean checkLoginToApp() {

        try {
            BaseHelper baseHelper = new BaseHelper(context);
            Cursor cursor = baseHelper.getUserFromDatabase();
            while (cursor.moveToNext()) {
                int nr = cursor.getInt(0);
                userLoginToApp = cursor.getString(1);
            }
        } catch (NullPointerException e) {
            userLoginToApp = "";
        }

        if (userLoginToApp.equals("") || userLoginToApp.isEmpty()) {
            loginToApp = false;
        } else {
            loginToApp = true;
            Toast.makeText(context, "Login: " + userLoginToApp, Toast.LENGTH_SHORT).show();
        }

        return loginToApp;
    }


    static void setVisibilityLoginLayout(int i) {
        login.setVisibility(i);
        appLogin.setVisibility(i);
        facebookLogin.setVisibility(i);
        tvCreateAccount.setVisibility(i);
        tvSkipLogin.setVisibility(i);
    }

    static void setVisibilityCreateLayout(int i) {
        createAccount.setVisibility(i);
        etEmail.setVisibility(i);
        etCreateUsername.setVisibility(i);
        etCreatePassword.setVisibility(i);
        createAccountButton.setVisibility(i);
        backCreateAccount.setVisibility(i);
    }

    static void setVisibilityLoggedIn(int i) {
        loggedIn.setVisibility(i);
        textViewLoggedIn.setVisibility(i);
        tvLogin.setVisibility(i);
        logoutButton.setVisibility(i);
    }


    static String getName(Profile profile) {
        String name;
        if (profile != null) {
            name = profile.getName();
        } else {
            name = null;
        }
        return name;
    }

    static String getFirstName(Profile profile) {
        String name;
        if (profile != null) {
            name = profile.getFirstName();
        } else {
            name = null;
        }
        return name;
    }

    public static String getId(Profile profile) {
        String id;
        if (profile != null) {
            id = profile.getId();
        } else {
            id = null;
        }
        return id;
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        getName(profile);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Account Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.wrocasion/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        /*accessTokenTracker.stopTracking();
        profileTracker.stopTracking();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Account Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.wrocasion/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Naciśnij wstecz ponownie,\n aby zamknąć aplikację",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
