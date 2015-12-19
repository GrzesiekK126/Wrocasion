package app.wrocasion.Events.TabsControl.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.wrocasion.Events.TabsControl.EventDetail;
import app.wrocasion.R;


public class PhotosTab extends Fragment {

    public static ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.photos_tab,container,false);

        img = (ImageView) v.findViewById(R.id.imageView2);
        img.setImageBitmap(EventDetail.image);

        return v;
    }
}