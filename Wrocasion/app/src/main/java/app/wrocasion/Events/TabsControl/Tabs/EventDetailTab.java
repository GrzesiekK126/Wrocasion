package app.wrocasion.Events.TabsControl.Tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.Events.TabsControl.EventDetail;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EventDetailTab extends Fragment{

    TextView tvDate, tvPrice, tvAddress, tvDescription, tvDetailDistance;
    ShareButton button;
    String urlToShare, accountName;
    public static List<GetEvents> eventsList;
    public static int index;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.event_detail_tab,container,false);



        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvDetailDistance = (TextView) v.findViewById(R.id.tvDetailDistance);

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

                LatLng eventLocation = new LatLng(eventsList.get(index).getLongtitude(), eventsList.get(index).getLatitude());

                tvDetailDistance.setText("Odległość od Ciebie: " + String.valueOf(MapTab.getDistance(eventLocation)) + "km");


                //urlToShare = "https://developers.facebook.com";
                urlToShare = eventsList.get(index).getLink();
                button = new ShareButton(getActivity());
                button.setText("Share");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(urlToShare))
                        .build();
                ((LinearLayout)(tvDescription.getParent())).addView(button);
                button.setShareContent(content);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


        return v;
    }

}