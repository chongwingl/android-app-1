package com.project.aidememoire.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.ViewPagerAdapter;
import com.project.aidememoire.fragment.AddFragment;
import com.project.aidememoire.fragment.ListFragment;

public class AccueilActivity extends FragmentActivity {
	
	private final static String TAG = "AideMemoireActivity";
	
	// le numero de la ligne saisie
	//private int mLineNumber = 1;
	// la base de donn�es
	//private DataBaseAdapter mDbHelper;
	//private ListView peopleListView;
	
	private ViewPagerAdapter viewPagerAdapter;
	private ViewPager viewPager;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private Fragment addFragment;
	private Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPagerAdapter = new ViewPagerAdapter(getBaseContext(),
				fragmentManager);
		viewPager.setAdapter(viewPagerAdapter);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }
    
}



/************** Manipulation de la BDD *****************/

/*mDbHelper = new DataBaseAdapter(this);
mDbHelper.open();
mDbHelper.addCreditLine("Martin", "Pierre", 1234567890, 1234);
mDbHelper.addCreditLine("Durand", "Pauline", 987654321, 765);
mDbHelper.addDetteLine("Dupont", "Paul", 1235679, 987);
Cursor c = mDbHelper.fetchAllPeople();

while (c.moveToNext()){
	Log.i(TAG, "People : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
}

c = mDbHelper.fetchAllCredit();

while (c.moveToNext()){
	Log.i(TAG, "Cr�dit : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
}

c = mDbHelper.fetchAllDette();

while (c.moveToNext()){
	Log.i(TAG, "Dette : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
}

//peopleListView = (ListView) findViewById(R.id.peopleListView);*/
