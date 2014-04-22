package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.ExpandableListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class MenuFragment extends Fragment {
	
	private View fragmentView;
	private Context context;
	private ExpandableListAdapter expandableAdapter;
	private ExpandableListView expandableListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.context = getActivity();
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.menu_fragment, container, false);
		
		expandableListView = (ExpandableListView) fragmentView.findViewById(R.id.expandable_list);
		expandableAdapter = new ExpandableListAdapter(getActivity());
		expandableListView.setAdapter(expandableAdapter);
		
		return fragmentView;
	}
	
}
