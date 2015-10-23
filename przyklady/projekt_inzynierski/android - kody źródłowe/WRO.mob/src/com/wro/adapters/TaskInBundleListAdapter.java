package com.wro.adapters;

import java.util.List;
import com.pietras.wro.R;
import com.wro.GamePlayActivity;
import com.wro.backend.Session;
import com.wro.backend.TaskInList;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TaskInBundleListAdapter extends ArrayAdapter<TaskInList> {

	Context context;
	List<TaskInList> items;
	int bundle_id;
	Session session; 
	String bundle_name;
	
	public TaskInBundleListAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    this.context = context;
	}
	
	public TaskInBundleListAdapter(Context context, int resource, List<TaskInList> items, Session session,int bundle_id,String bundle_name) {
	    super(context, resource, items);
	    this.context = context;
	    this.items = items;
	    this.session = session;
	    this.bundle_id = bundle_id;
	    this.bundle_name = bundle_name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View row_view = convertView;
	
	    if (row_view == null) {
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        row_view = vi.inflate(R.layout.bundle_element, null);
	    }
	
	    TaskInList task_object = getItem(position);
	
	    if (task_object != null) {
	
	        TextView name = (TextView) row_view.findViewById(R.id.bundle_element_name);
	        TextView left = (TextView) row_view.findViewById(R.id.bundle_element_left);
	        TextView right = (TextView) row_view.findViewById(R.id.bundle_element_right);
	
	        if (name != null) {
	        	name.setText(task_object.name);
	        }
	        if (left != null) {
	        	left.setText(task_object.getTypeName());
	        }
	        if (right != null) {
	        	right.setText(task_object.is_solved?"solved":"to do");
	        }
	        
	        row_view.setTag(position);
	        if( task_object.is_solved == true){
	        	row_view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(context,context.getString(R.string.already_solved), Toast.LENGTH_SHORT).show();
					}
				});
	        }
	        //Zakomentowane na czas demonstracji - ODKOMENTOWAC POTEM
	        else if( position>0 && items.get(position-1).is_solved == false ){
	        	row_view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(context,context.getString(R.string.first_solve_previous), Toast.LENGTH_SHORT).show();
					}
				});
	        }
	        else{
	        	row_view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if( items != null){
							TaskInList tin = items.get((Integer)v.getTag());
							Intent intentToGame = new Intent(context, GamePlayActivity.class);
							try{
								intentToGame.putExtra("task_id",tin.id);
								intentToGame.putExtra("bundle_id",bundle_id);
								intentToGame.putExtra("bundle_name",bundle_name);
								intentToGame.putExtra("name", tin.name);
								intentToGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intentToGame.putExtra("Session", session);
								context.startActivity(intentToGame);
							} catch (Exception e)
							{
								Log.e("LLL", e.toString());
							}
							//Toast.makeText(context,"You clicked: "+ tin.name, Toast.LENGTH_SHORT).show();
						}
						
					}
				});
	        }
			
	    }
	
	    return row_view;
	}
}