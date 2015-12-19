package app.wrocasion;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import app.wrocasion.Events.ChangeUserCategories;
import app.wrocasion.Events.EventsCategories;
import app.wrocasion.Events.TabsControl.EventsListTabs;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    static ProfilePictureView profilePhoto;
    static TextView userName;
    Context context;
    static boolean exit = false;

    LatLng pozycja = null;
    Location loc;
    double lat,lon;

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

        profilePhoto = (ProfilePictureView) findViewById(R.id.profile_image);
        userName = (TextView) findViewById(R.id.username);

        getProfileInfo();

        context = this;

        EventsCategories eventsCategories = new EventsCategories();

        if(Profile.getCurrentProfile() != null) {
            Intent intent = new Intent(context, EventsListTabs.class);
            context.startActivity(intent);
        } else{
            FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
            categoriesFragmentTransaction.replace(R.id.frame, eventsCategories);
            categoriesFragmentTransaction.commit();
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

                    case R.id.add_or_change_user_categories:
                        ChangeUserCategories changeUserCategories = new ChangeUserCategories();
                        FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        categoriesFragmentTransaction.replace(R.id.frame, changeUserCategories);
                        categoriesFragmentTransaction.commit();
                        return true;

                    case R.id.about:
                        Toast.makeText(getApplicationContext(),"Second Selected",Toast.LENGTH_SHORT).show();
                        SecondItemFragment secondFragment = new SecondItemFragment();
                        FragmentTransaction secondFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        secondFragmentTransaction.replace(R.id.frame,secondFragment);
                        secondFragmentTransaction.commit();
                        return true;

                    case R.id.feedback:
                        Toast.makeText(getApplicationContext(),"Third Selected",Toast.LENGTH_SHORT).show();
                        ThirdItemFragment thirdFragment = new ThirdItemFragment();
                        FragmentTransaction thirdFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        thirdFragmentTransaction.replace(R.id.frame,thirdFragment);
                        thirdFragmentTransaction.commit();
                        return true;

                    case R.id.events_categories:
                        /*ChangeUserCategories changeUserCategories = new ChangeUserCategories();
                        FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        categoriesFragmentTransaction.replace(R.id.frame, changeUserCategories);
                        categoriesFragmentTransaction.commit();*/
                        return true;

                    case R.id.user_account:
                        Intent intent = new Intent(context, Account.class);
                        startActivity(intent);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

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

    }

    private void getProfileInfo() {
        if(Account.checkLogIn()) {
            profilePhoto.setVisibility(View.VISIBLE);
            userName.setText(Account.getName(Profile.getCurrentProfile()));
            profilePhoto.setProfileId(Account.getId(Profile.getCurrentProfile()));
        }
        else {
            profilePhoto.setVisibility(View.INVISIBLE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

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
            Toast.makeText(this, "Press Back again to Exit.",
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
