package com.wro.backend;

import java.util.ArrayList;

import android.util.Log;

public class TaskInGame  {

	
	public int task_id;
	public String name;
	public int type_id;
	public int description_id;
	public int min_number;
	
	public ArrayList<LocationInGame> locations;
	public ArrayList<Resource> resources;
	
	public static final int DISTANCE_TABLE = 1;
	public static final int DIRECTION_AND_DISTANCE = 2;
	public static final int CLEAR_TARGETS = 3;
	
	public int getNumberOfFoundLocations(){
		if(locations == null)
			return 0;
		int count = 0;
		for(LocationInGame loc : locations){
			if(loc.is_solved){
				count++;
				Log.d("LLL","Location "+loc.name+"("+loc.id+") is found");
			}
		}
		return count;
	}
}
