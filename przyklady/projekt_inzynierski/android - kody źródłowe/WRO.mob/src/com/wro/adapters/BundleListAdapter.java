package com.wro.adapters;

import java.util.List;
import com.pietras.wro.R;
import com.wro.RiddlesListActivity;
import com.wro.backend.RiddleBundle;
import com.wro.backend.Session;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BundleListAdapter extends ArrayAdapter<RiddleBundle> {

	Context context;
	List<RiddleBundle> items;
	Session session; 
	public BundleListAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    this.context = context;
	}
	
	public BundleListAdapter(Context context, int resource, List<RiddleBundle> items, Session session) {
	    super(context, resource, items);
	    this.context = context;
	    this.items = items;
	    this.session = session;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View row_view = convertView;
	
	    if (row_view == null) {
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        row_view = vi.inflate(R.layout.bundle_element, null);
	    }
	
	    RiddleBundle bundle_object = getItem(position);
	
	    if (bundle_object != null) {
	
	        TextView name = (TextView) row_view.findViewById(R.id.bundle_element_name);
	        TextView left = (TextView) row_view.findViewById(R.id.bundle_element_left);
	        TextView right = (TextView) row_view.findViewById(R.id.bundle_element_right);
	
	        if (name != null) {
	        	name.setText(bundle_object.name);
	        }
	        if (left != null) {
	
	        	left.setText("xxx");
	        }
	        if (right != null) {
	
	        	right.setText(""+bundle_object.solved_count+"/"+bundle_object.all_riddles_count);
	        }
	        
	        row_view.setTag(position);
			row_view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if( items != null){
						RiddleBundle rb = items.get((Integer)v.getTag());
						Intent intentToRiddlesList = new Intent(context, RiddlesListActivity.class);
						try{
							intentToRiddlesList.putExtra("id",rb.id);
							intentToRiddlesList.putExtra("name",rb.name);
							intentToRiddlesList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intentToRiddlesList.putExtra("Session", session);
							context.startActivity(intentToRiddlesList);
						} catch (Exception e)
						{
							Log.e("LLL", e.toString());
						}
						//Toast.makeText(context,"You clicked: "+ rb.name, Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	    }
	
	    return row_view;
	}
}