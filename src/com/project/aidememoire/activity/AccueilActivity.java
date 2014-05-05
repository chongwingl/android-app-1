package com.project.aidememoire.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.project.aidememoire.R;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.fragment.ListFragment;
import com.project.aidememoire.fragment.MenuFragment;

public class AccueilActivity extends FragmentActivity {
	
	private final static String TAG = "AideMemoireActivity";

	private FragmentManager fragmentManager = getSupportFragmentManager();
	private MenuFragment menuFragment;
	private ListFragment listFragment;
	
	private DatabaseApi dataBaseApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        dataBaseApi = DatabaseApi.getInstance(this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        listFragment = new ListFragment();
        fragmentTransaction.add(R.id.main_container, listFragment);
        
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){            
            menuFragment = new MenuFragment();
            fragmentTransaction.add(R.id.second_container, menuFragment);
    	}
        
        fragmentTransaction.commit();
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(menuFragment != null){
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(menuFragment);
			fragmentTransaction.commit();
			menuFragment = null;
		}
		
		super.onSaveInstanceState(outState);
	}
}
