package com.project.aidememoire.fragment;

import java.util.List;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.listener.OnPageChange;
import com.project.aidememoire.model.Person;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment implements OnPageChange{

	private final static String TAG = "ListFragment";
	
	private ListView peopleListView;
	private PersonListAdapter adapter;
	
	private View fragmentView;
	private DatabaseApi dataBaseApi;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataBaseApi = new DatabaseApi(getActivity());
		
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.peopleListView);

		// TODO le chargement de la liste des personnes entr�es dans la BDD devrait se faire 
		//dans l'adapter pour �viter de charger toute la BDD avant d'afficher les donn�es
		adapter = new PersonListAdapter(getActivity(), dataBaseApi.fetchAllPersonAndMoneyCursor(), false);
		peopleListView.setAdapter(adapter);
		
		return fragmentView;
	}

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		adapter.notifyDataSetChanged();
		
		if(!dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
	}

	@Override
	public void onPageChanged() {
		Log.i(TAG, "Page has been left");
		
		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
		if(dataBaseApi.isOpen()){
			dataBaseApi.close();
		}
	}
	
	
}
