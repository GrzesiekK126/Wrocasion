package app.wrocasion.Events.TabsControl.Tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    static ArrayList<Integer> img;
    ArrayList<String> eventNameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.eventList);

        eventNameList = new ArrayList<>();
        img = new ArrayList<>();
        img.add(0, R.drawable.krajobraz);
        img.add(1, R.drawable.groy);
        img.add(2, R.drawable.groy);
        img.add(3, R.drawable.krajobraz);

        SetCurrentLocation setCurrentLocationAll = new SetCurrentLocation();
        setCurrentLocationAll.setUsername("");
        setCurrentLocationAll.setLongtitude(-1);
        setCurrentLocationAll.setLatitude(-1);

        Toast.makeText(getApplicationContext(), "LON: -1, LAT: -1", Toast.LENGTH_SHORT).show();

        RestClient.get().getEvents(setCurrentLocationAll, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                for (int i = 0; i < events.size(); i++) {
                    eventNameList.add(i, events.get(i).getNazwa());
                }
                listView.setAdapter(new ListViewAdapterAllEvents((FirstActivity) getActivity(), eventNameList, img));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });




        return v;
    }
}
