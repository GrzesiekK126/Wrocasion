package com.wro.backend;

public class Resource {
	public int id;
	public int type;
	public String path;
	
	public Object resource_data =null;
	
	public Resource(int id, int type, String path){
		this.id = id;
		this.type = type;
		this.path = path;
	}
	
	public static final int TEXT = 1;
	public static final int HTML = 2;
	public static final int IMAGE = 3;
	public static final int SOUND = 4;
}
