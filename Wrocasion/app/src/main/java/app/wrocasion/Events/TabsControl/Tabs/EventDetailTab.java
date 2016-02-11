package app.wrocasion.Events.TabsControl.Tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.Events.TabsControl.EventDetail;
import app.wrocasion.JSONs.ChangeCategoriesResponse;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.JoinToEvent;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EventDetailTab extends Fragment implements View.OnClickListener{

    private TextView tvDate, tvPrice, tvAddress, tvDescription, tvDetailDistance, tv, tvJoinToEvent;
    private Button joinToEventBtn;
    private boolean join = false;

    String accountName;
    public static List<GetEvents> eventsList;
    public static int index, eventID;
    public static ShareButton button;
    public static ShareLinkContent content;
    public static String urlToShare;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.event_detail_tab,container,false);



        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvDetailDistance = (TextView) v.findViewById(R.id.tvDetailDistance);
        tv = (TextView) v.findViewById(R.id.textView7);

        tvJoinToEvent = (TextView) v.findViewById(R.id.joinToEventTv);
        tvJoinToEvent.setOnClickListener(this);

        joinToEventBtn = (Button) v.findViewById(R.id.joinToEventbtn);
        joinToEventBtn.setOnClickListener(this);

        //button = (ShareButton) v.findViewById(R.id.action_share);

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

        /*setCurrentLocation.setLongtitude(51.1255273);
        setCurrentLocation.setLatitude(16.9943417);*/


        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {

                ((EventDetail) getActivity()).setActionBarTitle(eventsList.get(index).getNazwa());
                tvAddress.setText(eventsList.get(index).getStreet() + "\n" +
                        eventsList.get(index).getZipCode() + "  " +
                        eventsList.get(index).getCity());
                tvPrice.setText(String.valueOf(eventsList.get(index).getPrice()) + "zł");
                String s[] = eventsList.get(index).getData().split("T");
                tvDate.setText(s[0] + "\n" + s[1]);
                tvDescription.setText(eventsList.get(index).getDescription());
                eventID = events.get(index).getId();

                LatLng eventLocation = new LatLng(eventsList.get(index).getLongtitude(), eventsList.get(index).getLatitude());

                tvDetailDistance.setText("Odległość od Ciebie: " + String.valueOf(MapTab.getDistance(eventLocation)) + "km");


                //urlToShare = "https://developers.facebook.com";
                urlToShare = eventsList.get(index).getLink();
                button = new ShareButton(getActivity());
                button.setText("Share");
                //button.setBackgroundResource(R.drawable.share);
                content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(urlToShare))
                        .build();

                ((LinearLayout)(tv.getParent())).addView(button);

                button.setShareContent(content);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.joinToEventbtn || v.getId() == R.id.joinToEventTv){
            if(!join){
                if(Account.checkLoginToApp()){
                    joinToEventJSON(true, Account.getUsername());
                    joinToEventBtn.setBackgroundResource(R.drawable.heart_full);
                    tvJoinToEvent.setText(R.string.added_to_interesting);
                    join = true;
                }else if(Account.checkLogInFacebook()){
                    joinToEventJSON(true, Account.getName(Profile.getCurrentProfile()));
                    joinToEventBtn.setBackgroundResource(R.drawable.heart_full);
                    tvJoinToEvent.setText(R.string.added_to_interesting);
                    join = true;
                } else{
                    Toast.makeText(getApplicationContext(),"Funkcja dostępna tylko po zalogowaniu!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(Account.checkLoginToApp()){
                    joinToEventJSON(false, Account.getUsername());
                    joinToEventBtn.setBackgroundResource(R.drawable.heart_empty);
                    tvJoinToEvent.setText(R.string.add_to_interesting);
                    join = false;
                }else if(Account.checkLogInFacebook()){
                    joinToEventJSON(false, Account.getName(Profile.getCurrentProfile()));
                    joinToEventBtn.setBackgroundResource(R.drawable.heart_empty);
                    tvJoinToEvent.setText(R.string.add_to_interesting);
                    join = false;
                } else{
                    Toast.makeText(getApplicationContext(),"Funkcja dostępna tylko po zalogowaniu!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void joinToEventJSON(boolean join, String username){

        JoinToEvent joinToEvent = new JoinToEvent();
        joinToEvent.setUsername(username);
        joinToEvent.setTakingPart(join);
        joinToEvent.setEventIdToTakingPart(eventID);

        RestClient.get().joinToEvent(joinToEvent, new Callback<ChangeCategoriesResponse>() {
            @Override
            public void success(ChangeCategoriesResponse changeCategoriesResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }
}