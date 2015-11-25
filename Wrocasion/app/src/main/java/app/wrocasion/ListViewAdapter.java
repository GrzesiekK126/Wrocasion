package app.wrocasion;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter{

    static String[] result;
    Context context;
    static int[] imageId;
    static int imageNumber;

    private static LayoutInflater inflater=null;
    public ListViewAdapter(FirstActivity mainActivity, String[] str, int[] img) {
        // TODO Auto-generated constructor stub
        result = str;
        context = mainActivity;
        imageId = img;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.eventDescription);
        holder.img=(ImageView) rowView.findViewById(R.id.eventImage);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
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
