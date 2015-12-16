package app.wrocasion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.JSONs.AllCategories;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.ResponseUserCategories;
import app.wrocasion.JSONs.RestAPI;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.JSONs.UserCategories;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GridViewActivity extends Fragment implements View.OnClickListener{

    static GridView mGrid;
    int width, height;
    //public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
    public static int [] prgmImages={R.drawable.ic_delete_black, R.drawable.ic_drafts_black, R.drawable.ic_email_black, R.drawable.ic_error_black,
                                    R.drawable.ic_home_black, R.drawable.ic_inbox_black, R.drawable.ic_send_black, R.drawable.ic_star_black,
                                    R.drawable.ic_star_rate_black};
    public static boolean [] isChecked = {false, false, false, false, false, false, false, false, false};

    public static ArrayList<String> prgmNameList;

    private Button button;

    ArrayList<String> categoriesSelectedByUser;

    static RestAdapter retrofit;
    RestAPI webService;
    Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid,container,false);

        loadApps();

        prgmNameList = new ArrayList<String>();

        mGrid = (GridView) v.findViewById(R.id.myGrid);

        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://188.122.12.144:50000/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        webService = retrofit.create(RestAPI.class);

        RestClient.get().getAllCategories(new Callback<List<AllCategories>>() {

            @Override
            public void success(List<AllCategories> allCategories, Response response) {
                for (int i = 0; i < 9; i++) {
                    prgmNameList.add(i, allCategories.get(i).getNazwa());
                }
                mGrid.setAdapter(new AppsAdapter(getActivity(), prgmNameList, prgmImages));
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        mGrid.setColumnWidth(width / 3);

        button = (Button) v.findViewById(R.id.goToEvents);
        button.setOnClickListener(this);

        categoriesSelectedByUser = new ArrayList<String>();


        context = getActivity();

        return v;
    }

    private List<ResolveInfo> mApps;

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public class AppsAdapter extends BaseAdapter {

        ArrayList<String> result;
        Context context;
        int [] imageId;
        private LayoutInflater inflater=null;
        public AppsAdapter(FragmentActivity gridViewActivity, ArrayList<String> prgmNameList, int[] prgmImages) {
            // TODO Auto-generated constructor stub
            result = prgmNameList;
            context = getActivity();
            imageId = prgmImages;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
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
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.grid_row, null);
            holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

            holder.tv.setText(result.get(position));
            holder.img.setImageResource(imageId[position]);

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(isChecked[position]){
                        isChecked[position] = false;
                        holder.img.setBackgroundResource(R.color.LightGrey);
                    } else{
                        isChecked[position] = true;
                        holder.img.setBackgroundResource(R.color.Transparent);
                    }
                    //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_SHORT).show();
                }
            });

            return rowView;
        }
        }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.goToEvents){

            double longtitude, latitude;
            LatLng pozycja = null;
            Location loc;
            /*latitude = pozycja.latitude;
            longtitude = pozycja.longitude;*/


            SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
            setCurrentLocation.setUserName("");

            RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

                @Override
                public void success(List<GetEvents> events, Response response) {
                    Log.i("NAZWA", events.get(0).getNazwa());
                    String nazwa = events.get(0).getNazwa();
                    System.out.println("NAZWA: " + nazwa);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("NAZWA", error.getMessage());
                    error.printStackTrace();
                }
            });
            /*Intent intent = new Intent(context, EventsListTabs.class);
            context.startActivity(intent);*/


        }
    }




        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }



    class CheckableLayout extends FrameLayout implements Checkable {
        public static boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
            if(checked){
                setBackgroundResource(R.color.LightGrey);
            } else{
                setBackgroundResource(R.color.Transparent);
            /*else{
                Random rand = new Random();
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                setBackgroundColor(Color.rgb(r,g,b));
                //setBackgroundResource(R.color.Transparent);
            }*/
            }
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }


