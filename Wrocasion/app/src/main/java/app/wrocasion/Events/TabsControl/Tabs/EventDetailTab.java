package app.wrocasion.Events.TabsControl.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.wrocasion.Events.TabsControl.ListViewAdapter;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetailTab extends Fragment{

    static TextView textViewInfoTab;
    TextView eventTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.event_detail_tab,container,false);

        textViewInfoTab = (TextView) v.findViewById(R.id.textViewInfoTab);
        textViewInfoTab.setText(ListViewAdapter.eventName.get(ListViewAdapter.imageNumber));

        eventTitle = (TextView) v.findViewById(R.id.tvEventTitle);

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUserName("");

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {
                Log.i("NAZWA", events.get(0).getNazwa());
                eventTitle.setText(events.get(0).getNazwa());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


        return v;
    }

}