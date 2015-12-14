package app.wrocasion.Events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.JSONs.AllCategories;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.SetCurrentLocationWithCategories;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsCategories extends ListFragment implements View.OnClickListener{

    private Button button;
    ArrayList<String> categoriesList;
    ArrayList<String> categoriesSelectedByUser;

    RestAdapter retrofit;
    RestAPI webServiceAllCategories;
    RestAPI webServiceSetCurrentLocationWithCategories;

    static String bodyString;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_categories,container,false);

        categoriesList = new ArrayList<String>();
        categoriesSelectedByUser = new ArrayList<String>();

        context = getActivity();

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        webServiceAllCategories = retrofit.create(RestAPI.class);

        webServiceSetCurrentLocationWithCategories = retrofit.create(RestAPI.class);

        webServiceAllCategories.getAllCategories(new Callback<List<AllCategories>>() {

            @Override
            public void success(List<AllCategories> allCategories, Response response) {
                //if(Profile.getCurrentProfile()==null) {
                for (int i = 0; i < allCategories.size(); i++) {
                    categoriesList.add(i, allCategories.get(i).getNazwa());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, categoriesList);
                setListAdapter(adapter);
                setRetainInstance(true);


            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        button = (Button) v.findViewById(R.id.button4);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(l.getCheckedItemCount() == 0){
            //button.setVisibility(View.INVISIBLE);
        } else {
            //button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button4){

            /*double longtitude, latitude;
            LatLng pozycja = null;
            Location loc;
            latitude = pozycja.latitude;
            longtitude = pozycja.longitude;*/

            SetCurrentLocationWithCategories setCurrentLocationWithCategories = new SetCurrentLocationWithCategories();
            /*setCurrentLocationWithCategories.setUserName(Account.getId(Profile.getCurrentProfile()));
            setCurrentLocationWithCategories.setLongtitude(longtitude);
            setCurrentLocationWithCategories.setLatitude(latitude);*/
            setCurrentLocationWithCategories.setUserName("");
            setCurrentLocationWithCategories.setLongtitude(12.23);
            setCurrentLocationWithCategories.setLatitude(62.11);
            setCurrentLocationWithCategories.setCategories(categoriesSelectedByUser);

            webServiceSetCurrentLocationWithCategories.getEvents(setCurrentLocationWithCategories, new Callback<List<GetEvents>>() {

                @Override
                public void success(List<GetEvents> getEvents, Response response) {
                    button.setText(getEvents.get(0).getNazwa());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            /*Intent intent = new Intent(context, EventsListTabs.class);
            context.startActivity(intent);*/


        }
    }
}


