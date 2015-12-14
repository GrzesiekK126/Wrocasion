package app.wrocasion.Events.TabsControl;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsListTabs extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapterEventList adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wszystkie","Wybrane"};
    int Numboftabs = 2;

    RestAdapter retrofit;
    RestAPI webServiceSetCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ViewPagerAdapterEventList(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.viewpagerEventsTabs);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.eventsTabs);
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.Grey);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        webServiceSetCurrentLocation = retrofit.create(RestAPI.class);

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUserName("");

        webServiceSetCurrentLocation.getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                Log.d("NAZWA:", events.get(0).getNazwa());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}