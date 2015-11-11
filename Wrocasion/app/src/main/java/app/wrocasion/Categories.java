package app.wrocasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Categories extends Fragment {

    private ImageButton imgView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categories,container,false);

        imgView = (ImageButton)v.findViewById(R.id.imageButton);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsList eventsList = new EventsList();
                FragmentTransaction eventsListFragmentTransaction = getFragmentManager().beginTransaction();
                eventsListFragmentTransaction.replace(R.id.frame, eventsList);
                eventsListFragmentTransaction.commit();
            }
        });



        return v;
    }

}
