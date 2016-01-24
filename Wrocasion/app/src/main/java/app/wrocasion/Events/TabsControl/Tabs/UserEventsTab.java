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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Account;
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

    public static ArrayList<String> img;
    public static ArrayList<String> eventList;
    public static List<GetEvents> getUserEvents;
    public static ArrayList<Double> locationLatUser, locationLonUser;

    private ListView listView;
    private String accountName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.userEventList);

        eventList = new ArrayList<>();
        img = new ArrayList<>();
        locationLatUser = new ArrayList<>();
        locationLonUser = new ArrayList<>();


        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();

        if(Account.checkLoginToApp()){
            accountName = Account.getUsername();
            setCurrentLocation.setUsername(accountName);
        }else if(Account.checkLogInFacebook()){
            accountName = Account.getName(Profile.getCurrentProfile());
            setCurrentLocation.setUsername(accountName);
        } else{
            setCurrentLocation.setUsername("");
        }

        LatLng lokacja = MapTab.pobierzOstatniaLokalizacje(false, getApplicationContext());

        setCurrentLocation.setLongtitude(lokacja.latitude);
        setCurrentLocation.setLatitude(lokacja.longitude);


        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                getUserEvents = events;
                for (int i = 0; i < events.size(); i++) {
                    eventList.add(i, getUserEvents.get(i).getNazwa());
                    img.add(i,getUserEvents.get(i).getImage());
                    locationLatUser.add(i, getUserEvents.get(i).getLatitude());
                    locationLonUser.add(i, getUserEvents.get(i).getLongtitude());
                }
                listView.setAdapter(new ListViewAdapterUserEvents((FirstActivity) getActivity(), eventList, img, locationLatUser, locationLonUser));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        return v;
    }
}

