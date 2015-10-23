package com.wro;

import java.util.ArrayList;
import com.pietras.wro.R;
import com.wro.adapters.ResourceAdapter;
import com.wro.backend.Resource;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class ResCompatFragment  extends Fragment{
	
	TextView empty_informer = null;
	public ListView listview = null;
	String[] wyniki;
	//baza
	public ArrayList<Resource> list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_res, container, false);
	}//onCreateView

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//Toast.makeText(getActivity().getApplicationContext(),"ResFragment onActivityCreated", Toast.LENGTH_SHORT).show();
		super.onActivityCreated(savedInstanceState);

		empty_informer =  (TextView) getActivity().findViewById(R.id.txtMsgG);
		listview = (ListView) getActivity().findViewById(R.id.listViewG);

		empty_informer.setVisibility(View.VISIBLE);
	}//onActivityCreated

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity().getApplicationContext(),"ResFragment onActivityCreated", Toast.LENGTH_SHORT).show();
		if(((GamePlayActivity)getActivity()).riddle != null)
			list = ((GamePlayActivity)getActivity()).riddle.resources;
		if(list != null)
			listview.setAdapter(  new ResourceAdapter( list,getActivity().getApplicationContext(),listview,empty_informer )  );
		listview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		super.onResume();
	}

}
