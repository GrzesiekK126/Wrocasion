package app.wrocasion.Events;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Events.TabsControl.EventsListTabs;
import app.wrocasion.JSONs.AddOrChangeUserCategories;
import app.wrocasion.JSONs.AllCategories;
import app.wrocasion.JSONs.ChangeCategoriesResponse;
import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsCategories extends Fragment implements View.OnClickListener{

    static GridView mGrid;
    int width, height;

    public static ArrayList<String> categoriesList;
    public static ArrayList<Boolean> isChecked;
    public static ArrayList<String> categoriesImages;
    public static int index = 0;

    private Button button;

    public static ArrayList<String> categoriesSelectedByUser;

    Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_categories,container,false);

        loadApps();

        categoriesList = new ArrayList<>();
        isChecked = new ArrayList<>();
        categoriesSelectedByUser = new ArrayList<>();
        categoriesImages = new ArrayList<>();

        mGrid = (GridView) v.findViewById(R.id.myGrid);
        final RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.loadingPanel);

        RestClient.get().getAllCategories(new Callback<List<AllCategories>>() {

            @Override
            public void success(List<AllCategories> allCategories, Response response) {
                for (int i = 0; i < 9; i++) {
                    categoriesList.add(i, allCategories.get(i).getNazwa());
                    isChecked.add(i, true);
                    categoriesImages.add(i,allCategories.get(i).getLinkDoObrazka());
                }
                rl.setVisibility(View.GONE);
                mGrid.setAdapter(new AppsAdapter(getActivity(), categoriesList, categoriesImages));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        mGrid.setColumnWidth(500);

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
        ArrayList<String> imageId;
        private LayoutInflater inflater=null;
        public AppsAdapter(FragmentActivity gridViewActivity, ArrayList<String> prgmNameList, ArrayList<String> prgmImages) {
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

            String imageUrl = "http://188.122.12.144:50000/" + categoriesImages.get(position);
            rowView.setTag(imageUrl);

            holder.tv.setText(result.get(position));
            Picasso.with(rowView.getContext())
                    .load(imageUrl)
                    .into(holder.img);

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(isChecked.get(position)){
                        isChecked.add(position, false);
                        holder.img.setBackgroundResource(R.color.LightGrey);
                        categoriesSelectedByUser.add(index, categoriesList.get(position));
                        String category = categoriesSelectedByUser.get(index);
                        Log.i("KATEGORIA",category);
                        index = index + 1;
                        Log.i("INDEX", String.valueOf(index));
                    } else{
                        isChecked.add(position, true);
                        holder.img.setBackgroundResource(R.color.Transparent);
                        if(categoriesSelectedByUser.size()>0){
                            categoriesSelectedByUser.remove(categoriesSelectedByUser.indexOf(categoriesList.get(position)));
                            index = index - 1;
                            Log.i("INDEX", String.valueOf(index));
                        }
                    }

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


            /*SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
            setCurrentLocation.setUserName("");

            RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {

                @Override
                public void success(List<GetEvents> events, Response response) {
                    Log.i("NAZWA", events.get(0).getNazwa());
                    String nazwa = events.get(0).getNazwa();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("NAZWA", error.getMessage());
                    error.printStackTrace();
                }
            });*/

            AddOrChangeUserCategories addOrChangeUserCategories = new AddOrChangeUserCategories();
            //addOrChangeUserCategories.setUser(String.valueOf(Profile.getCurrentProfile()));
            addOrChangeUserCategories.setUser("847379558710144");
            addOrChangeUserCategories.setCategories(categoriesSelectedByUser);

            RestClient.get().addOrChangeUserCategories(addOrChangeUserCategories, new Callback<ChangeCategoriesResponse>() {
                @Override
                public void success(ChangeCategoriesResponse changeCategoriesResponse, Response response) {
                    Toast.makeText(getActivity().getApplicationContext(), "Wybrano kategorie", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });

            Intent intent = new Intent(context, EventsListTabs.class);
            context.startActivity(intent);


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



