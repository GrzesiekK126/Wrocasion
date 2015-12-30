package app.wrocasion.Events.TabsControl.Tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.ResponseUserCategories;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.JSONs.UserCategories;
import app.wrocasion.Events.TabsControl.ListViewAdapter;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserEventsTab extends Fragment {

    private ListView listView;
    static ArrayList<Integer> img;
    ArrayList<String> eventNameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.userEventList);

        eventNameList = new ArrayList<>();
        img = new ArrayList<>();
        img.add(0, R.drawable.krajobraz);
        img.add(1,R.drawable.groy);
        img.add(2,R.drawable.groy);

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUserName("");

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                for (int i = 0; i < events.size(); i++) {
                    eventNameList.add(i, events.get(i).getNazwa());
                }
                listView.setAdapter(new ListViewAdapter((FirstActivity) getActivity(), eventNameList, img));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return v;
    }
}

