package app.wrocasion.Events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.JSONs.AddOrChangeUserCategories;
import app.wrocasion.JSONs.AllCategories;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.R;
import app.wrocasion.Events.TabsControl.EventsListTabs;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsCategories extends ListFragment implements View.OnClickListener{

    private Button button;
    ArrayList<String> categoriesList;
    ArrayList<String> categoriesSelectedByUser;
    ArrayList<String> addOrChangeUserCategoriesList;

    RestAdapter retrofit;
    RestAPI webServiceAllCategories;
    RestAPI webServiceAddOrChangeUserCategories;

    static String bodyString;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_categories,container,false);

        categoriesList = new ArrayList<String>();
        categoriesSelectedByUser = new ArrayList<String>();
        addOrChangeUserCategoriesList = new ArrayList<String>();

        context = getActivity();

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        webServiceAllCategories = retrofit.create(RestAPI.class);

        webServiceAddOrChangeUserCategories = retrofit.create(RestAPI.class);

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

            AddOrChangeUserCategories addOrChangeUserCategories = new AddOrChangeUserCategories();
            addOrChangeUserCategories.setUser(Account.getId(Profile.getCurrentProfile()));

            webServiceAddOrChangeUserCategories.addOrChangeUserCategories(addOrChangeUserCategories, new Callback<AddOrChangeUserCategories>() {
                @Override
                public void success(AddOrChangeUserCategories addOrChangeUserCategories, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            Intent intent = new Intent(context, EventsListTabs.class);
            context.startActivity(intent);


        }
    }
}


