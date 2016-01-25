package app.wrocasion.Events.TabsControl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.Tabs.AllEventsTab;
import app.wrocasion.Events.TabsControl.Tabs.EventDetailTab;
import app.wrocasion.Events.TabsControl.Tabs.MapTab;
import app.wrocasion.Events.TabsControl.Tabs.UserEventsTab;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.R;

public class ListViewAdapterUserEvents extends BaseAdapter {

    public static ArrayList<String> eventName;
    public static ArrayList<ArrayList<String>> eventCategories;
    public static ArrayList<Double> locLat;
    public static ArrayList<Double> locLon;
    public static ArrayList<String> imageId;
    public static ArrayList<String> categories;
    public static int imageNumber;
    public static Holder holder=new Holder();

    Context context;

    private static LayoutInflater inflater=null;
    public ListViewAdapterUserEvents(FirstActivity mainActivity, ArrayList<String> str, ArrayList<String> img, ArrayList<Double> lat, ArrayList<Double> lon, ArrayList<String> cat) {
        // TODO Auto-generated constructor stub
        eventName = str;
        context = mainActivity;
        imageId = img;
        locLat = lat;
        locLon = lon;
        categories = cat;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static class Holder
    {
        public static TextView tv, tvDistance, tvCategories;
        public static ImageView img;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final View rowView;

        rowView = inflater.inflate(R.layout.row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.eventDescription);
        holder.img=(ImageView) rowView.findViewById(R.id.eventImage);
        holder.tv.setText(eventName.get(position));
        holder.tvDistance = (TextView) rowView.findViewById(R.id.tvDistanceEventList);
        holder.tvCategories = (TextView) rowView.findViewById(R.id.tvCategories);

        int listCat = 0;
        for(int j=0; j<categories.get(position).length(); j++){
            if (categories.get(position).charAt(j) == ';') {
                listCat++;
            }
        }

        if(listCat > 0){
            String categoriesSplit[] = categories.get(position).split(";");
            StringBuilder categoriesList = new StringBuilder();
            for (int i = 0; i < categoriesSplit.length; i++) {
                categoriesList.append(categoriesSplit[i] + "\n");
            }
            holder.tvCategories.setText(categoriesList);
        } else{
            holder.tvCategories.setText(categories.get(position));
        }

        LatLng eventLocation = new LatLng(locLon.get(position), locLat.get(position));

        holder.tvDistance.setText("Odległość od Ciebie: " + String.valueOf(MapTab.getDistance(eventLocation)) + "km");


        String imageUrl = "http://188.122.12.144:50000/" + imageId.get(position);

        rowView.setTag(imageUrl);

        Picasso.with(rowView.getContext())
                .load(imageUrl)
                .into(holder.img);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetail.who = 2;
                EventDetailTab.eventsList = UserEventsTab.getUserEvents;
                MapTab.events = UserEventsTab.getUserEvents;
                imageNumber = (int) getItem(position);
                EventDetailTab.index = imageNumber;
                MapTab.index = imageNumber;
                EventDetail.imgNumber = imageNumber;
                Intent intent = new Intent(context, EventDetail.class);
                context.startActivity(intent);

            }
        });
        return rowView;
    }


}
