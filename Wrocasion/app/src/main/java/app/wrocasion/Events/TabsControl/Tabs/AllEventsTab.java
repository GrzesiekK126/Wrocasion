package app.wrocasion.Events.TabsControl.Tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.Events.TabsControl.ListViewAdapterAllEvents;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AllEventsTab extends Fragment {

    private ListView listView;
    private RelativeLayout relativeLayout;
    static ArrayList<String> img, eventNameList, categoriesAll;
    static ArrayList<Double> locationLat, locationLon;

    public static List <GetEvents> getAllEvents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.eventList);

        relativeLayout = (RelativeLayout) v.findViewById(R.id.loadingPanelAllList);

        eventNameList = new ArrayList<>();
        img = new ArrayList<>();
        locationLat = new ArrayList<>();
        locationLon = new ArrayList<>();
        categoriesAll = new ArrayList<>();

        SetCurrentLocation setCurrentLocationAll = new SetCurrentLocation();
        setCurrentLocationAll.setUsername("");
        setCurrentLocationAll.setLongtitude(-1);
        setCurrentLocationAll.setLatitude(-1);

        RestClient.get().getEvents(setCurrentLocationAll, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {

            getAllEvents = events;
            for (int i = 0; i < getAllEvents.size(); i++) {
                eventNameList.add(i, getAllEvents.get(i).getNazwa());
                img.add(i, getAllEvents.get(i).getImage());
                locationLat.add(i, getAllEvents.get(i).getLatitude());
                locationLon.add(i, getAllEvents.get(i).getLongtitude());
                //Toast.makeText(getApplicationContext(), getAllEvents.get(i).getCategories(), Toast.LENGTH_SHORT).show();
                categoriesAll.add(i, getAllEvents.get(i).getCategories());
                //Log.i("KATEGORIEall", getAllEvents.get(i).getCategories());
            }
                relativeLayout.setVisibility(View.GONE);
            listView.setAdapter(new ListViewAdapterAllEvents((FirstActivity) getActivity(), eventNameList, img, locationLat, locationLon, categoriesAll));

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        return v;
    }
}
