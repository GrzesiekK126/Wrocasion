package app.wrocasion.Events.TabsControl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsListTabs extends Fragment {

    ViewPager pager;
    ViewPagerAdapterEventList adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wybrane","Wszystkie"};
    int Numboftabs = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_events_list_tabs,container,false);

        adapter = new ViewPagerAdapterEventList(getActivity().getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.viewpagerEventsTabs);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.eventsTabs);
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

        PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayoutUserEventsTab);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventsListTabs eventsListTabs = new EventsListTabs();
                FragmentTransaction categoriesFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                categoriesFragmentTransaction.replace(R.id.frame, eventsListTabs);
                categoriesFragmentTransaction.commit();
            }
        });

        pullRefreshLayout.setRefreshing(false);

        return v;
    }

}
