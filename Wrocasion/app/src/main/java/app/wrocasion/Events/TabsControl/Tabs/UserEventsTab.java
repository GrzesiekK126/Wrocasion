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

import com.facebook.Profile;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.Events.EventsCategories;
import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.Events.TabsControl.ListViewAdapterUserEvents;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.JSONs.SetCurrentLocationWithCategories;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserEventsTab extends Fragment {

    public static ArrayList<String> img, eventList, categoriesUser;
    public static List<GetEvents> getUserEvents;
    public static ArrayList<Double> locationLatUser, locationLonUser;

    private ListView listView;
    private String accountName;
    private RelativeLayout relativeLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.userEventList);

        relativeLayout = (RelativeLayout) v.findViewById(R.id.loadingPanelUserList);

        eventList = new ArrayList<>();
        img = new ArrayList<>();
        locationLatUser = new ArrayList<>();
        locationLonUser = new ArrayList<>();
        categoriesUser = new ArrayList<>();

        if(Account.checkLoginToApp()){
            accountName = Account.getUsername();
            sendJSON(accountName);
        }else if(Account.checkLogInFacebook()){
            accountName = Account.getId(Profile.getCurrentProfile());
            sendJSON(accountName);
        } else{
            sendJSONEvents();
        }

        return v;
    }

    void sendJSON(String name){

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();

        LatLng lokacja = MapTab.pobierzOstatniaLokalizacje(false, getApplicationContext());

        setCurrentLocation.setUsername(name);
        setCurrentLocation.setLongtitude(lokacja.latitude);
        setCurrentLocation.setLatitude(lokacja.longitude);

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {

                getUserEvents = events;
                for (int i = 0; i < getUserEvents.size(); i++) {
                    eventList.add(i, getUserEvents.get(i).getNazwa());
                    img.add(i, getUserEvents.get(i).getImage());
                    locationLatUser.add(i, getUserEvents.get(i).getLatitude());
                    locationLonUser.add(i, getUserEvents.get(i).getLongtitude());
                    //Toast.makeText(getApplicationContext(), getUserEvents.get(i).getCategories(), Toast.LENGTH_SHORT).show();
                    categoriesUser.add(i, getUserEvents.get(i).getCategories());
                    //Log.i("KATEGORIEuser", getUserEvents.get(i).getCategories());
                }
                relativeLayout.setVisibility(View.GONE);
                listView.setAdapter(new ListViewAdapterUserEvents((FirstActivity) getActivity(), eventList, img, locationLatUser, locationLonUser, categoriesUser));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    void sendJSONEvents(){

        SetCurrentLocationWithCategories setCurrentLocationWithCategories = new SetCurrentLocationWithCategories();

        setCurrentLocationWithCategories.setUsername("");
        setCurrentLocationWithCategories.setCategoriesList(EventsCategories.categoriesSelectedByUser);
        setCurrentLocationWithCategories.setLatitude(-1);
        setCurrentLocationWithCategories.setLongtitude(-1);

        RestClient.get().getEventsCategories(setCurrentLocationWithCategories, new Callback<List<GetEvents>>() {
            @Override
            public void success(List<GetEvents> getEventses, Response response) {
                getUserEvents = getEventses;
                for (int i = 0; i < getUserEvents.size(); i++) {
                    eventList.add(i, getUserEvents.get(i).getNazwa());
                    img.add(i, getUserEvents.get(i).getImage());
                    locationLatUser.add(i, getUserEvents.get(i).getLatitude());
                    locationLonUser.add(i, getUserEvents.get(i).getLongtitude());
                    //Toast.makeText(getApplicationContext(), getUserEvents.get(i).getCategories(), Toast.LENGTH_SHORT).show();
                    categoriesUser.add(i, getUserEvents.get(i).getCategories());
                    //Log.i("KATEGORIEuser", getUserEvents.get(i).getCategories());
                }
                relativeLayout.setVisibility(View.GONE);
                listView.setAdapter(new ListViewAdapterUserEvents((FirstActivity) getActivity(), eventList, img, locationLatUser, locationLonUser, categoriesUser));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}

