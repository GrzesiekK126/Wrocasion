package app.wrocasion.Events;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.Account;
import app.wrocasion.FirstActivity;
import app.wrocasion.JSONs.AddOrChangeUserCategories;
import app.wrocasion.JSONs.AllCategories;
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
    ArrayList<String> allCategoriesList;
    ArrayList<String> oldUserCategories;
    ArrayList<String> newUserCategories;
    ArrayList<String> checkedCategories;
    ArrayList<String> allCategoriesImages;
    ArrayList<Boolean> isChecked;
    GridView grid;

    int width, height;

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_user_categories,container,false);

        if(Profile.getCurrentProfile() != null || Account.checkLoginToApp()) {
            allCategoriesList = new ArrayList<>();
            oldUserCategories = new ArrayList<>();
            newUserCategories = new ArrayList<>();
            checkedCategories = new ArrayList<>();
            allCategoriesImages = new ArrayList<>();

            isChecked = new ArrayList<>();

            grid = (GridView) v.findViewById(R.id.myGrid2);
            context = getActivity();
            final RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.loadingPanel2);

            RestClient.get().getAllCategories(new Callback<List<AllCategories>>() {

                @Override
                public void success(final List<AllCategories> allCategories, Response response) {
                    for (int i = 0; i < allCategories.size(); i++) {
                        allCategoriesList.add(i, allCategories.get(i).getNazwa());
                        isChecked.add(i, false);
                        allCategoriesImages.add(i, allCategories.get(i).getLinkDoObrazka());
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });

            final UserCategories userCategories = new UserCategories();
            userCategories.setName(Account.getId(Profile.getCurrentProfile()));
            RestClient.get().getUserCategories(userCategories, new Callback<ResponseUserCategories>() {
                @Override
                public void success(ResponseUserCategories responseUserCategories, Response response) {

                    oldUserCategories = responseUserCategories.getCategories();

                    for (int i = 0; i < oldUserCategories.size(); i++) {
                        for(int j=0; j<allCategoriesList.size(); j++){
                            if(oldUserCategories.get(i) == allCategoriesList.get(j)){
                                newUserCategories.add(j, allCategoriesList.get(j));
                                isChecked.add(j, true);
                            } else{
                                newUserCategories.add(j, allCategoriesList.get(j));
                                isChecked.add(j, false);
                            }
                        }
                    }

                    rl.setVisibility(View.GONE);
                    grid.setAdapter(new AppsAdapter(getActivity(), allCategoriesList, allCategoriesImages));

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });

            button = (Button) v.findViewById(R.id.changeCategories);
            button.setOnClickListener(this);
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Opcja dostępna tylko po zalogowaniu!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), FirstActivity.class);
            startActivity(intent);
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

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
            TextView tv, background;
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.grid_row_change, null);
            holder.tv=(TextView) rowView.findViewById(R.id.textViewChange);
            holder.img=(ImageView) rowView.findViewById(R.id.imageViewChange);
            holder.background = (TextView) rowView.findViewById(R.id.tvBackgroundChange);

            int size = width/3;
            holder.background.setMaxHeight(size);
            holder.background.setMaxWidth(size);
            holder.background.setMinimumHeight(size);
            holder.background.setMinimumWidth(size);

            holder.img.setMaxHeight(size - 5);
            holder.img.setMaxWidth(size - 5);
            holder.img.setMinimumHeight(size - 5);
            holder.img.setMinimumWidth(size - 5);

            /*String imageUrl = "http://188.122.12.144:50000/" + oldUserCategories.get(position);
            rowView.setTag(imageUrl);*/

            holder.tv.setText(result.get(position));
            if(isChecked.get(position)) {
                holder.background.setBackgroundResource(R.color.LightGrey);
            }
            /*Picasso.with(rowView.getContext())
                    .load(imageUrl)
                    .into(holder.img);*/



            /*for(int i=0; i<isChecked.size(); i++){
                if(isChecked.get(i)){
                    holder.background.setBackgroundResource(R.color.LightGrey);
                } else{
                    holder.background.setBackgroundResource(R.color.White);
                }
            }*/

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(isChecked.get(position)){
                        isChecked.add(position, true);
                        holder.background.setBackgroundResource(R.color.LightGrey);
                        /*newUserCategories.add(EventsCategories.index, oldUserCategories.get(position));
                        indexChange = indexChange + 1;
                        Log.i("INDEX", String.valueOf(EventsCategories.index));*/
                    } else{
                        isChecked.add(position, false);
                        holder.background.setBackgroundResource(R.color.White);
                        /*if(newUserCategories.size()>0){
                            newUserCategories.remove(EventsCategories.index - 1);
                            indexChange = indexChange - 1;
                            Log.i("INDEX", String.valueOf(EventsCategories.index));
                        }*/
                    }
                }
            });

            return rowView;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changeCategories){

            int ind = 0;
            for(int i=0; i<isChecked.size(); i++){
                if(isChecked.get(i)){
                    checkedCategories.add(ind, allCategoriesList.get(i));
                    ind++;
                }
            }

            AddOrChangeUserCategories addOrChangeUserCategories = new AddOrChangeUserCategories();
            //addOrChangeUserCategories.setUser(String.valueOf(Profile.getCurrentProfile()));
            addOrChangeUserCategories.setUser(Account.getId(Profile.getCurrentProfile()));
            addOrChangeUserCategories.setCategories(checkedCategories);

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


