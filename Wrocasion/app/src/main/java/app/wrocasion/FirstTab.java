package app.wrocasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstTab extends Fragment{

    static TextView textViewInfoTab;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.first_tab,container,false);

        textViewInfoTab = (TextView) v.findViewById(R.id.textViewInfoTab);
        textViewInfoTab.setText(ListViewAdapter.result[ListViewAdapter.imageNumber]);

        return v;
    }

}