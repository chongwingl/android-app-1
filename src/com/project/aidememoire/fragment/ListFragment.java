package com.project.aidememoire.fragment;

import java.util.List;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.listener.OnPageChange;
import com.project.aidememoire.model.Person;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment implements OnPageChange{

	private final static String TAG = "ListFragment";
	
	private List<Person> persons;
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
		persons = dataBaseApi.fetchAllPerson();

		// TODO le chargement de la liste des personnes entrées dans la BDD devrait se faire 
		//dans l'adapter pour éviter de charger toute la BDD avant d'afficher les données
		adapter = new PersonListAdapter(getActivity(), R.layout.person_list, persons);
		peopleListView.setAdapter(adapter);
		
		return fragmentView;
	}

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		if(!dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
		persons = dataBaseApi.fetchAllPerson();

		if(persons.size() > adapter.getCount()){
			List<Person> temp = persons;
			temp.removeAll(adapter.getItems());
			adapter.addAll(temp);
		}
	}

	@Override
	public void onPageChanged() {
		Log.i(TAG, "Page has been left");
		if(dataBaseApi.isOpen()){
			dataBaseApi.close();
		}
	}
	
	
}
