package com.wro.backend;

public class RiddleBundle {
	public int id;
	public String name;
	public int solved_count;
	public int all_riddles_count;
	
	public RiddleBundle(int id, String name, int solved, int all_riddles){
		this.id = id;
		this.name = name;
		this.solved_count = solved;
		this.all_riddles_count = all_riddles;
	}
}
