package app.wrocasion;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondTab extends Fragment{

    private GoogleMap google_map;
    public SupportMapFragment map_fragment;
    private Marker mMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.second_tab,container,false);
        map_fragment = SupportMapFragment.newInstance();

        google_map = map_fragment.getMap();

        if( google_map != null){
            google_map.setMyLocationEnabled(true);
            //google_map.setOnMyLocationChangeListener(myLocationChangeListener);

        }

        return v;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = google_map.addMarker(new MarkerOptions().position(loc));
            if(google_map != null) {
                google_map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

}
