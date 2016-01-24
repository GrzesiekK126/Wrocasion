package app.wrocasion;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.wrocasion.Events.TabsControl.EventDetail;
import app.wrocasion.Events.TabsControl.Tabs.AllEventsTab;
import app.wrocasion.Events.TabsControl.Tabs.EventDetailTab;
import app.wrocasion.Events.TabsControl.Tabs.MapTab;

public class ListViewAdapterEventsRating extends BaseAdapter {

    public static ArrayList<String> eventNameRating;
    public static ArrayList<ArrayList<String>> eventCategories;
    Context context;
    public static ArrayList<String> imageRating;
    public static int imageNumber;
    public static Holder holder = new Holder();


    private static LayoutInflater inflater=null;
    public ListViewAdapterEventsRating(FirstActivity mainActivity, ArrayList<String> str, ArrayList<String> img) {
        // TODO Auto-generated constructor stub
        eventNameRating = str;
        context = mainActivity;
        imageRating = img;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventNameRating.size();
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
        public static TextView tv;
        public static ImageView img;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final View rowView;

        String imageUrl = "http://188.122.12.144:50000/" + imageRating.get(position);

        rowView = inflater.inflate(R.layout.events_rating_list_row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.tvEventsRatingList);
        holder.img=(ImageView) rowView.findViewById(R.id.ivEventsRatingList);
        holder.tv.setText(eventNameRating.get(position));

        Picasso.with(rowView.getContext())
                .load(imageUrl)
                .into(holder.img);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventsRating.eventName = eventNameRating.get(position);
                EventsRating.eventId = EventsRatingList.getAllEvents.get(position).getId();

                FirstActivity.accountNavigation = true;
                FirstActivity.menuItem = "eventsRating";
                Intent intent = new Intent(context, FirstActivity.class);
                context.startActivity(intent);

            }
        });
        return rowView;
    }

}
