package com.wro.backend;

import com.google.android.gms.maps.model.LatLng;

public class LocationInGame {
	public int id;
	public double lat;
	public double lng;
	public String name;
	public boolean is_solved;
	
	public LocationInGame(int id, double lat, double lng, String name, boolean is_solved){
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.name = name;
		this.is_solved = is_solved;
	}
	
	public LatLng toLatLng(){
		return new LatLng(lat, lng);
	}
}
