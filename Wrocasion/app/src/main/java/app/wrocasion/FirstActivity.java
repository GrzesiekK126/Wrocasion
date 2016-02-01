package app.wrocasion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.wrocasion.Events.ChangeUserCategories;
import app.wrocasion.Events.EventsCategories;
import app.wrocasion.Events.TabsControl.EventsListTabs;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout mainLayout, startLayout;
    private Button btnSkip, btnAccount;

    static ProfilePictureView profilePhoto;
    static TextView userName, loginAs;
    static Context context;

    public static boolean exit = false, accountNavigation = false;
    public static String menuItem;

    String userLoginToApp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //sprawdz GPS
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            buildAlertMessageNoGps();
        }//koniec sprawdz GPS
        //sprawdzanie sieci


//koniec sprawdzenia sieci


        if(!isConnectingToInternet()){
            buildAlertMessageNoInternetConnection();
        } else{
            //Toast.makeText(this, "Internet is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }

            profilePhoto = (ProfilePictureView) findViewById(R.id.profile_image);

        userName = (TextView) findViewById(R.id.username);
        loginAs = (TextView) findViewById(R.id.loginAs);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        startLayout = (LinearLayout) findViewById(R.id.startLayout);

        btnAccount = (Button) findViewById(R.id.goToAccount);
        btnAccount.setOnClickListener(this);

        btnSkip = (Button) findViewById(R.id.skip);
        btnSkip.setOnClickListener(this);

        getProfileInfo();

        context = this;

        if(accountNavigation){
            accountNavigation = false;
            mainLayout.setVisibility(View.VISIBLE);
            startLayout.setVisibility(View.GONE);
            getProfileInfo();

            switch(menuItem){

                case "eventsCategories":
                    EventsCategories eventsCategories = new EventsCategories();
                    FragmentTransaction eventsCategoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    eventsCategoriesFragmentTransaction.replace(R.id.frame, eventsCategories);
                    eventsCategoriesFragmentTransaction.commit();
                    break;
                case "eventsListTabs":
                    EventsListTabs eventsListTabs = new EventsListTabs();
                    FragmentTransaction eventsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    eventsFragmentTransaction.replace(R.id.frame, eventsListTabs);
                    eventsFragmentTransaction.commit();
                    break;
                case "changeUserCategories":
                    EventsCategories changeUserCategories = new EventsCategories();
                    //ChangeUserCategories changeUserCategories = new ChangeUserCategories();
                    FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    categoriesFragmentTransaction.replace(R.id.frame, changeUserCategories);
                    categoriesFragmentTransaction.commit();
                    break;
                case "account":
                    Intent intent = new Intent(context, Account.class);
                    startActivity(intent);
                    break;
                case "eventsRating":
                    EventsRating eventsRating = new EventsRating();
                    FragmentTransaction eventsRatingTransaction = getSupportFragmentManager().beginTransaction();
                    eventsRatingTransaction.replace(R.id.frame,eventsRating);
                    eventsRatingTransaction.commit();
                    break;
                case "appRating":
                    AppRating appRating = new AppRating();
                    FragmentTransaction appRatingTransaction = getSupportFragmentManager().beginTransaction();
                    appRatingTransaction.replace(R.id.frame,appRating);
                    appRatingTransaction.commit();
                    break;
                case "about":
                    About about = new About();
                    FragmentTransaction aboutFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    aboutFragmentTransaction.replace(R.id.frame,about);
                    aboutFragmentTransaction.commit();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Coś poszło źle", Toast.LENGTH_SHORT).show();
                    break;

            }
        }else {
            if (Profile.getCurrentProfile() != null || Account.checkLoginToApp()) {
                /*mainLayout.setVisibility(View.VISIBLE);
                startLayout.setVisibility(View.GONE);
                EventsListTabs eventsListTabs = new EventsListTabs();
                FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                categoriesFragmentTransaction.replace(R.id.frame, eventsListTabs);
                categoriesFragmentTransaction.commit();*/
                mainLayout.setVisibility(View.GONE);
                startLayout.setVisibility(View.VISIBLE);
                getProfileInfo();
            } else {
                mainLayout.setVisibility(View.GONE);
                startLayout.setVisibility(View.VISIBLE);
            }
        }

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    case R.id.events_tabs:
                        EventsListTabs eventsListTabs = new EventsListTabs();
                        FragmentTransaction eventsFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        eventsFragmentTransaction.replace(R.id.frame, eventsListTabs);
                        eventsFragmentTransaction.commit();
                        return true;

                    case R.id.add_or_change_user_categories:
                        //ChangeUserCategories changeUserCategories = new ChangeUserCategories();
                        EventsCategories changeUserCategories = new EventsCategories();
                        FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        categoriesFragmentTransaction.replace(R.id.frame, changeUserCategories);
                        categoriesFragmentTransaction.commit();
                        return true;

                    case R.id.about:
                        About about = new About();
                        FragmentTransaction aboutFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        aboutFragmentTransaction.replace(R.id.frame,about);
                        aboutFragmentTransaction.commit();
                        return true;

                    case R.id.app_rating:
                        AppRating appRating = new AppRating();
                        FragmentTransaction appRatingTransaction = getSupportFragmentManager().beginTransaction();
                        appRatingTransaction.replace(R.id.frame,appRating);
                        appRatingTransaction.commit();
                        return true;

                    case R.id.events_rating:
                        EventsRatingList eventsRating = new EventsRatingList();
                        FragmentTransaction eventsRatingTransaction = getSupportFragmentManager().beginTransaction();
                        eventsRatingTransaction.replace(R.id.frame,eventsRating);
                        eventsRatingTransaction.commit();
                        return true;

                    case R.id.user_account:
                        Intent intent = new Intent(context, Account.class);
                        startActivity(intent);
                        /*Account account = new Account();
                        FragmentTransaction accountFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        accountFragmentTransaction.replace(R.id.frame, account);
                        accountFragmentTransaction.commit();*/
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Coś poszło źle",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        }
             );

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

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

        //refreshActivity();
        BaseHelper baseHelper = new BaseHelper(this);
        Cursor cursor = baseHelper.getUserFromDatabase();
        while(cursor.moveToNext()){
            int nr = cursor.getInt(0);
            userLoginToApp = cursor.getString(1);
        }
        Log.d("DATABASE: ", userLoginToApp);

    }

    private void getProfileInfo() {
        if(Account.checkLogInFacebook()) {
            profilePhoto.setVisibility(View.VISIBLE);
            userName.setText(Account.getName(Profile.getCurrentProfile()));
            profilePhoto.setProfileId(Account.getId(Profile.getCurrentProfile()));
            loginAs.setVisibility(View.VISIBLE);
        }
        else if(Account.checkLoginToApp()){
            BaseHelper baseHelper = new BaseHelper(this);
            Cursor cursor = baseHelper.getUserFromDatabase();
            while(cursor.moveToNext()){
                int nr = cursor.getInt(0);
                userLoginToApp = cursor.getString(1);
            }
            profilePhoto.setVisibility(View.INVISIBLE);
            userName.setText(userLoginToApp);
            loginAs.setVisibility(View.VISIBLE);
        } else{
            profilePhoto.setVisibility(View.INVISIBLE);
            loginAs.setVisibility(View.INVISIBLE);
            userName.setText(R.string.logout);
        }
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
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.skip){
            mainLayout.setVisibility(View.VISIBLE);
            startLayout.setVisibility(View.GONE);
            EventsCategories eventsCategories = new EventsCategories();
            FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
            categoriesFragmentTransaction.replace(R.id.frame, eventsCategories);
            categoriesFragmentTransaction.commit();
        }
        else if(v.getId() == R.id.goToAccount){
            mainLayout.setVisibility(View.VISIBLE);
            startLayout.setVisibility(View.GONE);
            Intent intent = new Intent(context, Account.class);
            startActivity(intent);
            /*Account account = new Account();
            FragmentTransaction accountFragmentTransaction = getSupportFragmentManager().beginTransaction();
            accountFragmentTransaction.replace(R.id.frame, account);
            accountFragmentTransaction.commit();*/
        }

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

    //tworzenie alertu dotyczącego dostępu do GPS
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aby aplikacja działała poprawnie potrzebujesz połączenia GPS, czy chcesz go teraz właczyć?")
                .setCancelable(false)
                .setPositiveButton("Włącz", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Nie włączaj", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




    //tworzenie alertu dotyczącego dostępu do internetu
    public void buildAlertMessageNoInternetConnection(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Nie masz połączenia z internetem, aby korzystać z aplikacji włącz internet.")
                .setCancelable(false)
                .setPositiveButton("Włącz", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Nie włączaj", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }


    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

}
