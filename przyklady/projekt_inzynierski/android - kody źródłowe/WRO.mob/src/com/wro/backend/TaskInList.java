package com.wro.backend;

public class TaskInList {
	public int id;
	public String name;
	public int type_id;
	public boolean is_solved;
	
	
	public TaskInList(int id, String name, int type_id, boolean is_solved) {
		super();
		this.id = id;
		this.name = name;
		this.type_id = type_id;
		this.is_solved = is_solved;
	}
	
	public String getTypeName(){
		switch (type_id){
		case 1:
			return "Tablica odleg³oœci";
		case 2:
			return "Kierunek i odleg³oœæ";
		case 3:
			return "Jasne cele";
		default:
			return "Nieznany typ";
		}
	}
}
