package app.wrocasion;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Categories extends ListFragment implements View.OnClickListener{

    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categories,container,false);

        String[] values = new String[] { "Spektakle", "Koncerty", "Warsztaty",
                "Wystawy", "Taniec", "Teatr Piosenki", "Inne"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, values);
        setListAdapter(adapter);
        setRetainInstance(true);

        button = (Button) v.findViewById(R.id.button4);
        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(l.getCheckedItemCount() == 0){
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button4){
            EventsList eventsList = new EventsList();
            FragmentTransaction eventsListFragmentTransaction = getFragmentManager().beginTransaction();
            eventsListFragmentTransaction.replace(R.id.frame, eventsList);
            eventsListFragmentTransaction.commit();
        }
    }
}
