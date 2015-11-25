package app.wrocasion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class EventsList extends Fragment {

    private ListView listView;
    public static int[] img = {R.drawable.krajobraz, R.drawable.zdjecie, R.drawable.groy};
    public static String[] str = {"Pierwszy", "Drugi", "Trzeci"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_list,container,false);

        listView = (ListView) v.findViewById(R.id.eventList);
        listView.setAdapter(new ListViewAdapter((FirstActivity) getActivity(), str, img));

        return v;
    }

}
