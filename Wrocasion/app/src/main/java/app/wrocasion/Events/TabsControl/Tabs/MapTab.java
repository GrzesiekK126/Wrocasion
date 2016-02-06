package app.wrocasion.Events.TabsControl.Tabs;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MapTab extends Fragment implements GoogleMap.OnCameraChangeListener{

    private GoogleMap google_map = null; //uważać czy nie jest nullem!!!!!!!!

    LatLng aktualna_pozycja = null;

    private float cameraZoom = 13.0f;

    public static List<GetEvents> events;
    public static int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.map_tab, container, false);

        PobierzMape();
        ustawSluchaczaLokalizacji();

        return v;
    }

    private void PobierzMape(){
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {
                        google_map = googleMap;
                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.setMyLocationEnabled(true);

                        ustawWszystkoNaMapie();
                    }
                }
            });
        }
    }

    //
    private void ustawWszystkoNaMapie(){

        //znacznik z miejscem wydarzenia


        /*LatLng lokacja = pobierzOstatniaLokalizacje(false,getApplicationContext());
        wstawZnacznik(lokacja);*/

        LatLng lokacja = new LatLng(events.get(index).getLongtitude(), events.get(index).getLatitude());
        wstawZnacznik(lokacja);
        //zoom kamery na pozycje użytkownika
        google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lokacja, cameraZoom));
    }

    private void wstawZnacznik(LatLng pozycjaZnacznika){
        if(google_map != null)
            google_map.addMarker(new MarkerOptions().position(pozycjaZnacznika));
    }

    private void ustawSluchaczaLokalizacji(){
        //Setting the GPS listener
        LocationManager loc_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        android.location.LocationListener loc_listener = (android.location.LocationListener) new SluchaczLokalizacji();
        loc_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) loc_listener);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        cameraZoom = cameraPosition.zoom;
    }

    public class SluchaczLokalizacji implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(getApplicationContext(), "nowa lok. GPS", Toast.LENGTH_SHORT).show();
            aktualna_pozycja = new LatLng(location.getLatitude(), location.getLongitude());

            if(google_map != null){
                google_map.addMarker(new MarkerOptions().position(aktualna_pozycja));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "GPS off", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "GPS on", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "Nowy status", Toast.LENGTH_SHORT).show();
        }

    }//PlayerLocationListener


    public static LatLng pobierzOstatniaLokalizacje(boolean enabledProvidersOnly, Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location utilLocation = null;
        List<String> providers = manager.getProviders(enabledProvidersOnly);
        for(String provider : providers){
            utilLocation = manager.getLastKnownLocation(provider);
            if(utilLocation != null) {
                return new LatLng(utilLocation.getLatitude(), utilLocation.getLongitude());
            }
        }
        return null;
    }


    public static double getDistance(LatLng eventLocation){

        double distance, distanceInKm;
        //LatLng latLng = new LatLng(51.1255273,16.9943417);
        distance = SphericalUtil.computeDistanceBetween(pobierzOstatniaLokalizacje(false, getApplicationContext()), eventLocation);
        //distance = SphericalUtil.computeDistanceBetween(latLng, eventLocation);
        distanceInKm = round(distance/1000, 2);

        return distanceInKm;
    }

    private static double round(double value, int places) {
        return (double) Math.round(value * Math.pow(10, places))
                / Math.pow(10, places);
    }
}



