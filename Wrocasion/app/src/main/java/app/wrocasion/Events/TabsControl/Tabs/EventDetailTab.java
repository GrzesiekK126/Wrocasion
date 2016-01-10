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

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import java.util.List;

import app.wrocasion.Events.TabsControl.EventDetail;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetailTab extends Fragment{

    TextView tvDate, tvPrice, tvAddress, tvDescription;
    ShareButton button;
    String urlToShare;
    public static List<GetEvents> eventsList;
    public static int index;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.event_detail_tab,container,false);



        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUsername("");
        setCurrentLocation.setLatitude(62.11);
        setCurrentLocation.setLongtitude(12.23);

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

            @Override
            public void success(List<GetEvents> events, Response response) {

                ((EventDetail) getActivity()).setActionBarTitle(eventsList.get(index).getNazwa());
                tvAddress.setText(eventsList.get(index).getStreet() + "\n" +
                        eventsList.get(index).getZipCode() + "  " +
                        eventsList.get(index).getCity());
                tvPrice.setText(String.valueOf(eventsList.get(index).getPrice()) + "zł");
                tvDate.setText(eventsList.get(index).getData());
                tvDescription.setText(eventsList.get(index).getDescription());


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