package app.wrocasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.wrocasion.JSONs.AllCategoriesJSON;
import app.wrocasion.JSONs.UserCategoriesJSON;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class Categories extends ListFragment implements View.OnClickListener{

    private Button button;
    ArrayList<String> categoriesList;
    ArrayList<String> categoriesChoiceByUser;

    RestAdapter retrofit;
    RestAdapter retrofitUserCategories;
    AllCategoriesJSON.WebServiceAllCategories webServiceAllCategories;
    UserCategoriesJSON.WebServiceUserCategories webServiceUserCategories;
    static String bodyString;
    static int pol;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categories,container,false);

        categoriesList = new ArrayList<String>();
        categoriesChoiceByUser = new ArrayList<String>();

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        /*retrofitUserCategories = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new StringConverter()).build();*/

        webServiceAllCategories = retrofit.create(AllCategoriesJSON.WebServiceAllCategories.class);
        webServiceUserCategories = retrofit.create(UserCategoriesJSON.WebServiceUserCategories.class);

        webServiceAllCategories.getData(new Callback<List<AllCategoriesJSON>>() {

            @Override
            public void success(List<AllCategoriesJSON> categoriesJSONs, Response response) {
                for (int i = 0; i < categoriesJSONs.size(); i++) {
                    categoriesList.add(i, categoriesJSONs.get(i).getNazwa());
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
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button4){
            UserCategoriesJSON userCategories = new UserCategoriesJSON();
            userCategories.setName(Account.getId(Profile.getCurrentProfile()));

            webServiceUserCategories.postData(userCategories, new Callback<UserCategoriesJSON>() {
                @Override
                public void success(UserCategoriesJSON myWebServiceResponse, Response response) {
                    //bodyString = Integer.toString(response.getStatus());
                    //bodyString = myWebServiceResponse.getName();
                    /*pol = response.getStatus();
                    bodyString = response.getReason();*/

                    //bodyString = new String(((TypedByteArray) response.getBody()).getBytes());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            EventsList eventsList = new EventsList();
            FragmentTransaction eventsListFragmentTransaction = getFragmentManager().beginTransaction();
            eventsListFragmentTransaction.replace(R.id.frame, eventsList);
            eventsListFragmentTransaction.commit();
            //button.setText(bodyString);
        }
    }
}


