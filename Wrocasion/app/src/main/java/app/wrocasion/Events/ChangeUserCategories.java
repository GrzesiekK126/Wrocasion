package app.wrocasion.Events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.wrocasion.JSONs.AddOrChangeUserCategories;
import app.wrocasion.JSONs.ChangeCategoriesResponse;
import app.wrocasion.JSONs.ResponseUserCategories;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.UserCategories;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangeUserCategories extends Fragment implements View.OnClickListener{

    private Button button;
    ArrayList<String> allCategories;
    ArrayList<String> oldUserCategories;
    ArrayList<String> newUserCategories;
    ArrayList<Boolean> isChecked;
    GridView grid;

    int indexChange;

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_user_categories,container,false);

        allCategories = new ArrayList<>();
        allCategories = EventsCategories.categoriesList;
        oldUserCategories = new ArrayList<>();
        newUserCategories = new ArrayList<>();
        isChecked = new ArrayList<>();
        isChecked = EventsCategories.isChecked;

        grid = (GridView) v.findViewById(R.id.myGridChange);
        context = getActivity();

        final UserCategories userCategories = new UserCategories();
        userCategories.setName("847379558710144");
        RestClient.get().getUserCategories(userCategories, new Callback<ResponseUserCategories>() {
            @Override
            public void success(ResponseUserCategories responseUserCategories, Response response) {

                oldUserCategories = responseUserCategories.getCategories();
                indexChange = oldUserCategories.size();
                /*String category;
                for(int i=0; i<allCategories.size(); i++){
                    category = allCategories.get(i);
                    for(int j=0; j<oldUserCategories.size(); j++ ){
                        if(category == oldUserCategories.get(j)){

                        } else{

                        }
                    }
                }*/

                grid.setAdapter(new AppsAdapter(getActivity(), oldUserCategories, EventsCategories.categoriesImages));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        button = (Button) v.findViewById(R.id.changeCategories);
        button.setOnClickListener(this);

        return v;
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

            String imageUrl = "http://188.122.12.144:50000/" + oldUserCategories.get(position);
            rowView.setTag(imageUrl);

            holder.tv.setText(result.get(position));
            Picasso.with(rowView.getContext())
                    .load(imageUrl)
                    .into(holder.img);

            for(int i=0; i<isChecked.size(); i++){
                if(isChecked.get(i)==true){
                    holder.img.setBackgroundResource(R.color.LightGrey);
                } else{
                    holder.img.setBackgroundResource(R.color.Transparent);
                }
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(isChecked.get(position)){
                        isChecked.add(position, false);
                        holder.img.setBackgroundResource(R.color.LightGrey);
                        newUserCategories.add(EventsCategories.index, oldUserCategories.get(position));
                        indexChange = indexChange + 1;
                        Log.i("INDEX", String.valueOf(EventsCategories.index));
                    } else{
                        isChecked.add(position, true);
                        holder.img.setBackgroundResource(R.color.Transparent);
                        if(newUserCategories.size()>0){
                            newUserCategories.remove(EventsCategories.index - 1);
                            indexChange = indexChange - 1;
                            Log.i("INDEX", String.valueOf(EventsCategories.index));
                        }
                    }
                }
            });

            return rowView;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changeCategories){

            AddOrChangeUserCategories addOrChangeUserCategories = new AddOrChangeUserCategories();
            //addOrChangeUserCategories.setUser(String.valueOf(Profile.getCurrentProfile()));
            addOrChangeUserCategories.setUser("847379558710144");
            addOrChangeUserCategories.setCategories(newUserCategories);

            RestClient.get().addOrChangeUserCategories(addOrChangeUserCategories, new Callback<ChangeCategoriesResponse>() {
                @Override
                public void success(ChangeCategoriesResponse changeCategoriesResponse, Response response) {
                    Toast.makeText(getActivity().getApplicationContext(), "Zmieniono kategorie", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });

        }
    }
}


