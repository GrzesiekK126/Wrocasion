package app.wrocasion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

public class Account extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private ProfilePictureView profilePictureView;
    private TextView textView;

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

        getFacebookInfo();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button3){
            MainActivity.loginToFacebook();
            getFacebookInfo();
        }

    }

    private void getFacebookInfo(){
        if(MainActivity.checkLogIn()){
            textView.setText(MainActivity.getName(Profile.getCurrentProfile()));
            profilePictureView.setVisibility(View.VISIBLE);
            profilePictureView.setProfileId(MainActivity.getId(Profile.getCurrentProfile()));
            button.setText("Wyloguj");
        }
        else{
            textView.setText(R.string.logout);
            profilePictureView.setVisibility(View.INVISIBLE);
            button.setText("Zaloguj");
        }
    }
}
