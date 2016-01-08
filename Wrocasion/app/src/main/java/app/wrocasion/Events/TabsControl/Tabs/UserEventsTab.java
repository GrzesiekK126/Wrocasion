package app.wrocasion.Events.TabsControl.Tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.ListViewAdapterUserEvents;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserEventsTab extends Fragment {

    private ListView listView;
    static ArrayList<Integer> img;
    ArrayList<String> eventList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.userEventList);

        eventList = new ArrayList<>();
        img = new ArrayList<>();
        img.add(0, R.drawable.krajobraz);
        img.add(1, R.drawable.groy);
        img.add(2, R.drawable.groy);
        img.add(3, R.drawable.krajobraz);

        LatLng lokacja = MapTab.pobierzOstatniaLokalizacje(false, getApplicationContext());

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUsername("");
        setCurrentLocation.setLongtitude(lokacja.latitude);
        setCurrentLocation.setLatitude(lokacja.longitude);

        Toast.makeText(getApplicationContext(), "LON: " + String.valueOf(lokacja.latitude) + " LAT: " + String.valueOf(lokacja.longitude), Toast.LENGTH_SHORT).show();

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                for (int i = 0; i < events.size(); i++) {
                    eventList.add(i, events.get(i).getNazwa());
                }
                listView.setAdapter(new ListViewAdapterUserEvents((FirstActivity) getActivity(), eventList, img));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });




        return v;
    }
}

