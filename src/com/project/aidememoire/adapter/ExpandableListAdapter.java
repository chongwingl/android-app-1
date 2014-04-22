package com.project.aidememoire.adapter;

import java.util.ArrayList;
import java.util.List;

import com.project.aidememoire.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
	
	private Activity activity;
	private List<String> parentGroup;
	private List<List<String>> childGroup;
	
	public ExpandableListAdapter(Activity activity){
		this.activity = activity;
		parentGroup = new ArrayList<String>();
		childGroup = new ArrayList<List<String>>();
		parentGroup.add("Ajouter");
		parentGroup.add("Filtrer");
		parentGroup.add("Trier");
		List<String> child1 = new ArrayList<String>();
		child1.add("Ajouter");
		child1.add("Filtrer");
		child1.add("Trier");
		childGroup.add(child1);
		List<String> child2 = new ArrayList<String>();
		child2.add("Ajouter");
		child2.add("Filtrer");
		child2.add("Trier");
		childGroup.add(child2);
		List<String> child3 = new ArrayList<String>();
		child3.add("Ajouter");
		child3.add("Filtrer");
		child3.add("Trier");
		childGroup.add(child3);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childGroup.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.menu_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.expandable_item)).setText(childGroup.get(groupPosition).get(childPosition));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childGroup.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentGroup.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parentGroup.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.menu_list_item, null);
		}
		((TextView)convertView.findViewById(R.id.expandable_item)).setText(parentGroup.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
