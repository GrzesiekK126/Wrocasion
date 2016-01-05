package app.wrocasion.Events.TabsControl;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.wrocasion.FirstActivity;
import app.wrocasion.R;

public class ListViewAdapterAllEvents extends BaseAdapter{

    public static ArrayList<String> eventName;
    public static ArrayList<ArrayList<String>> eventCategories;
    Context context;
    public static ArrayList<Integer> imageId;
    public static int imageNumber;
    public static Holder holder=new Holder();


    private static LayoutInflater inflater=null;
    public ListViewAdapterAllEvents(FirstActivity mainActivity, ArrayList<String> str, ArrayList<Integer> img) {
        // TODO Auto-generated constructor stub
        eventName = str;
        context = mainActivity;
        imageId = img;
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
        public static TextView tv;
        public static ImageView img;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final View rowView;
        rowView = inflater.inflate(R.layout.row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.eventDescription);
        holder.img=(ImageView) rowView.findViewById(R.id.eventImage);
        holder.tv.setText(eventName.get(position));
        holder.img.setImageResource(imageId.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNumber = (int) getItem(position);
                Intent intent = new Intent(context, EventDetail.class);
                context.startActivity(intent);

            }
        });
        return rowView;
    }


}
