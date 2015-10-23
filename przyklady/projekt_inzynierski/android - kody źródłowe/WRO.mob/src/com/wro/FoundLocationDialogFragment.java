package com.wro;

import com.pietras.wro.R;
import com.wro.backend.LocationInGame;
import com.wro.backend.Session;
import com.wro.backend.TaskInGame;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FoundLocationDialogFragment  extends DialogFragment {

    
	Session session;
	TaskInGame riddle;
	int bundle_id;
	String bundle_name;
	LocationInGame close_location;
	
	public static FoundLocationDialogFragment newInstance(Session session,TaskInGame riddle,int bundle_id,String bundle_name,LocationInGame close_location){
		FoundLocationDialogFragment frag = new FoundLocationDialogFragment();
		frag.session = session;
		frag.riddle = riddle;
		frag.bundle_id = bundle_id;
		frag.close_location = close_location;
		frag.bundle_name = bundle_name;
		
		frag.setCancelable(false);
		
		return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		((GamePlayActivity)getActivity()).isDialogShowed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.found_location)
               .setPositiveButton(R.string.photo_prompt, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
						Intent intentToGame = new Intent(getActivity(), LocationFoundActivity.class);
						try{
							//getActivity().finish();
							intentToGame.putExtra("task_id",riddle.task_id);
							intentToGame.putExtra("bundle_id",bundle_id);
							intentToGame.putExtra("bundle_name",bundle_name);
							intentToGame.putExtra("riddle_name",riddle.name);
							intentToGame.putExtra("location_id",close_location.id);
							intentToGame.putExtra("location_name",close_location.name);
							intentToGame.putExtra("Session", session);
							intentToGame.putExtra("found_locations", riddle.getNumberOfFoundLocations()+1);
							intentToGame.putExtra("min_number", riddle.min_number);
							intentToGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getActivity().startActivity(intentToGame);
						} catch (Exception e)
						{
							Log.e("LLL", e.toString());
						}
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
	}
	
}