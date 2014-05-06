package com.project.aidememoire.fragment;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PartialListAdapter;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class InfosFragment extends Fragment {
	
	private View fragmentView;
	private Context context;
	private TextView title;
	
	private ListView partialListView;
	private PartialListAdapter adapter;
	
	private DatabaseApi databaseApi;
	
	public final static String PERSON_MONEY = "person_money";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		databaseApi = DatabaseApi.getInstance(context);
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.infos_layout, container, false);
		
		Bundle bundle = getArguments();
		Person person = (Person) bundle.get(ListFragment.PERSON);
		
		title = (TextView) fragmentView.findViewById(R.id.infos_title);
		title.setText(R.string.infosfragment_title);
		title.setTextAppearance(context, R.style.TitleFontStyle);
		
		((TextView) fragmentView.findViewById(R.id.infos_name)).setText(person.getName().substring(0, 1).toUpperCase() + person.getName().substring(1));
		((TextView) fragmentView.findViewById(R.id.infos_surname)).setText(person.getSurname().substring(0, 1).toUpperCase() + person.getSurname().substring(1));
		
		partialListView = (ListView) fragmentView.findViewById(R.id.infosListView);
		adapter = new PartialListAdapter(context);
		partialListView.setAdapter(adapter);
		
		if(databaseApi.open()){
			Cursor cursor = databaseApi.fetchMoneyOfPersonCursor(person);
			List<Money> money = databaseApi.fromDataToMoneys(cursor);
			
			person.getMoney().clear();
			person.addMoneys(money);
			
			((TextView) fragmentView.findViewById(R.id.infos_credit)).setText(String.valueOf(person.getAllCredit()));
			((TextView) fragmentView.findViewById(R.id.infos_dette)).setText(String.valueOf(person.getAllDette()));
			
			adapter.swapCursor(cursor);
		}
		
		return fragmentView;
	}

}
