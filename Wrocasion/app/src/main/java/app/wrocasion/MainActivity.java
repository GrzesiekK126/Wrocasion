package app.wrocasion;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.FacebookSdk;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btnSkip, btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.start_activity);

        btnAccount = (Button) findViewById(R.id.goToAccount);
        btnAccount.setOnClickListener(this);

        btnSkip = (Button) findViewById(R.id.skip);
        btnSkip.setOnClickListener(this);

        if(Account.checkLogInFacebook()){
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.skip){
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.goToAccount){
            Account account = new Account();
            Intent intent = new Intent(this, Account.class);
            startActivity(intent);
        }

    }
}