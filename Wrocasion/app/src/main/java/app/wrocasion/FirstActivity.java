package app.wrocasion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button button;
    static ProfilePictureView profilePhoto;
    static TextView userName;
    static LoginButton hiddenLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        profilePhoto = (ProfilePictureView) findViewById(R.id.profile_image);
        userName = (TextView) findViewById(R.id.username);

        userName.setText(MainActivity.getName(Profile.getCurrentProfile()));
        profilePhoto.setProfileId(MainActivity.getId(Profile.getCurrentProfile()));

        hiddenLoginButton = (LoginButton) findViewById(R.id.loginButton);
        hiddenLoginButton.setOnClickListener(this);

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

                    case R.id.first:
                        Toast.makeText(getApplicationContext(), "First Selected", Toast.LENGTH_SHORT).show();
                        FirstItemFragment firstFragment = new FirstItemFragment();
                        FragmentTransaction firstFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        firstFragmentTransaction.replace(R.id.frame,firstFragment);
                        firstFragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.second:
                        Toast.makeText(getApplicationContext(),"Second Selected",Toast.LENGTH_SHORT).show();
                        SecondItemFragment secondFragment = new SecondItemFragment();
                        FragmentTransaction secondFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        secondFragmentTransaction.replace(R.id.frame,secondFragment);
                        secondFragmentTransaction.commit();
                        return true;

                    case R.id.third:
                        Toast.makeText(getApplicationContext(),"Third Selected",Toast.LENGTH_SHORT).show();
                        ThirdItemFragment thirdFragment = new ThirdItemFragment();
                        FragmentTransaction thirdFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        thirdFragmentTransaction.replace(R.id.frame,thirdFragment);
                        thirdFragmentTransaction.commit();
                        return true;

                    case R.id.categories:
                        Toast.makeText(getApplicationContext(),"Categories Selected",Toast.LENGTH_SHORT).show();
                        Categories categories = new Categories();
                        FragmentTransaction categoriesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        categoriesFragmentTransaction.replace(R.id.frame,categories);
                        categoriesFragmentTransaction.commit();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

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

    private void refreshActivity() {
        if(MainActivity.checkLogIn()==true){
            finish();
            startActivity(getIntent());
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
        if(v.getId()==R.id.button) {
            Intent intent = new Intent(this, Tabs.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.loginButton){
            /*if (MainActivity.checkLogIn() == true) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        userName.setText(MainActivity.getName(Profile.getCurrentProfile()));
                        profilePhoto.setProfileId(MainActivity.getId(Profile.getCurrentProfile()));

                    }
                }, 2250);
            } else{
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        userName.setText(MainActivity.getName(Profile.getCurrentProfile()));
                        profilePhoto.setProfileId(MainActivity.getId(Profile.getCurrentProfile()));

                    }
                }, 4000);
            }*/
        }
    }

    /*@Override
    public void onBackPressed() {

    }*/
}
