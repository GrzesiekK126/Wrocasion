package com.wro.backend;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtils {
	
	public static final float CLOSE_ENOUGH_DISTANCE = 4; //meters
	
	public static float distanceBetween(LatLng latLng1, LatLng latLng2) {
		
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        return loc1.distanceTo(loc2);	//meters
    }
	
	public static String distanceToString(Float distance){
		if( distance < 1000)
			return "" + (int)((float)distance) + " metr�w";
		else
			return "" + String.format("%.1f", distance/1000) + " kilometr�w";
	}
	
	public static LocationInGame findClosestLocation(List<LocationInGame> locations, LatLng location_of_player){
		if(locations == null || location_of_player == null)
			return null;
		for( LocationInGame location : locations ){
			if(location.is_solved)
				continue;
			LatLng latlgn = new LatLng(location.lat, location.lng);
			if( distanceBetween(latlgn, location_of_player) < CLOSE_ENOUGH_DISTANCE ){
				return location;
			}
		}
		return null;
	}
	
	public static double countAngle(LatLng observer_location, LatLng target_location){
		return Math.toDegrees(Math.atan2(target_location.longitude-observer_location.longitude, target_location.latitude-observer_location.latitude));
	}
	
	public static String angleToString(double angle){
		
		if(angle < 0)
			angle+=360;
		
		if( angle < 22.5)
			return "p�noc";
		if( angle < 67.5)
			return "p�nocny wsch�d";
		if( angle < 111.5)
			return "wsch�d";
		if( angle < 157.5)
			return "po�udniowy wsch�d";
		if( angle < 202.5)
			return "po�udnie";
		if( angle < 247.5)
			return "po�udniowy zach�d";
		if( angle < 292.5)
			return "zach�d";
		if( angle < 337.5)
			return "p�nocny zach�d";
		return "p�noc";
	}
}
