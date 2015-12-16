package app.wrocasion.Events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
import app.wrocasion.JSONs.ResponseUserCategories;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.JSONs.UserCategories;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsCategories extends ListFragment implements View.OnClickListener{

    private Button button;
    ArrayList<String> categoriesList;
    ArrayList<String> categoriesSelectedByUser;

    static RestAdapter retrofit;
    RestAPI webServiceAllCategories;
    RestAPI webServiceSetCurrentLocation;
    RestAPI web;

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

        webServiceSetCurrentLocation = retrofit.create(RestAPI.class);

        web = retrofit.create(RestAPI.class);

       /* webServiceAllCategories.getAllCategories(new Callback<List<AllCategories>>() {

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
        });*/
        UserCategories userCategories = new UserCategories();
        userCategories.setName("847379558710144");
        RestClient.get().getUserCategories(userCategories, new Callback<List<ResponseUserCategories>>() {
            @Override
            public void success(List<ResponseUserCategories> responseUserCategories, Response response) {
                /*for (int i = 0; i < responseUserCategories.size(); i++) {
                    categoriesList.add(i, responseUserCategories.get(i).getNazwa());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, categoriesList);
                setListAdapter(adapter);
                setRetainInstance(true);*/

                Log.i("DUPA", responseUserCategories.get(0).getNazwa());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
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


        }
    }
}


