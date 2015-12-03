package app.wrocasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import app.wrocasion.JSONs.UserCategories;

public class ThirdItemFragment extends ListFragment {

    static ArrayList<String> id = new ArrayList<String>();
    static ArrayList<String> nazwy = new ArrayList<String>();
    static ArrayList<String> linki = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_item_fragment,container,false);

        ArrayAdapter<String> adapterId = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, id);
        setListAdapter(adapterId);
        setRetainInstance(true);

        ArrayAdapter<String> adapterNazwy = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nazwy);
        setListAdapter(adapterNazwy);
        setRetainInstance(true);

        ArrayAdapter<String> adapterLinki = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, linki);
        setListAdapter(adapterLinki);
        setRetainInstance(true);


        return v;
    }

    public static void setResponse(List<UserCategories> myWebServiceResponse){

        /*for (int i = 0; i < myWebServiceResponse.size(); i++) {
            id.add(i, myWebServiceResponse.get(i).getId());
            nazwy.add(i, myWebServiceResponse.get(i).getName());
            linki.add(i, myWebServiceResponse.get(i).getLinkDoObrazka());
        }*/

    }
}
