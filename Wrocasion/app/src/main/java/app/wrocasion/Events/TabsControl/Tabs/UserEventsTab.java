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

import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.ResponseUserCategories;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.UserCategories;
import app.wrocasion.Events.TabsControl.ListViewAdapter;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserEventsTab extends Fragment {

    private ListView listView;
    public static int[] img = {R.drawable.krajobraz, R.drawable.zdjecie, R.drawable.groy};
    public static String[] str = {"Pierwszy", "Drugi", "Trzeci"};

    RestAdapter retrofit;
    RestAPI webServiceUserCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_events_list,container,false);

        listView = (ListView) v.findViewById(R.id.userEventList);
        listView.setAdapter(new ListViewAdapter((EventsListTabs) getActivity(), str, img));

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        webServiceUserCategories = retrofit.create(RestAPI.class);

        UserCategories userCategories = new UserCategories();
        userCategories.setName(Account.getId(Profile.getCurrentProfile()));

        webServiceUserCategories.getUserCategories(userCategories, new Callback<List<ResponseUserCategories>>() {
            @Override
            public void success(List<ResponseUserCategories> myWebServiceResponse, Response response) {
                if (response.getStatus() == 200) {

                    Toast.makeText(getActivity(), "Code: " + response.getStatus(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "REST: Error unknown. Code: " + response.getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return v;
    }
}

