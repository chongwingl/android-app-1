package com.project.aidememoire.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.project.aidememoire.R;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.fragment.ListFragment;

public class AccueilActivity extends FragmentActivity {
	
	private final static String TAG = "AideMemoireActivity";

	private FragmentManager fragmentManager = getSupportFragmentManager();

	private DatabaseApi dataBaseApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        dataBaseApi = DatabaseApi.getInstance(this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        ListFragment fragment = new ListFragment();
        fragmentTransaction.add(R.id.main_container, fragment, "Liste");
        fragmentTransaction.commit();

    }
    
    @Override
	protected void onDestroy() {
    	if(dataBaseApi.isOpen()){
    		dataBaseApi.close();
    	}
		super.onDestroy();
	}

	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
}
