package app.wrocasion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.ListViewAdapterAllEvents;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EventsRatingList extends Fragment{
    private ListView listView;
    static ArrayList<Integer> img;
    ArrayList<String> eventNameRatingList;

    public static List<GetEvents> getAllEvents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_rating_list,container,false);

        if(Profile.getCurrentProfile() != null || Account.checkLoginToApp()) {

            listView = (ListView) v.findViewById(R.id.eventsRatinglistView);

            eventNameRatingList = new ArrayList<>();
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
                    getAllEvents = events;
                    for (int i = 0; i < events.size(); i++) {
                        eventNameRatingList.add(i, getAllEvents.get(i).getNazwa());
                    }
                    listView.setAdapter(new ListViewAdapterEventsRating((FirstActivity) getActivity(), eventNameRatingList, img));
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });

        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Opcja dostępna tylko po zalogowaniu!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), FirstActivity.class);
            startActivity(intent);
        }


        return v;
    }
}
