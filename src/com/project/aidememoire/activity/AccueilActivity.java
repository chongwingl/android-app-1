package com.project.aidememoire.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.TabsAdapter;
import com.project.aidememoire.fragment.AddFragment;
import com.project.aidememoire.fragment.ListFragment;

public class AccueilActivity extends FragmentActivity {
	
	private final static String TAG = "AideMemoireActivity";
	
	private TabsAdapter tabsAdapter;
	private ViewPager viewPager;
	private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewpager);
        setContentView(viewPager);
        
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        
        tabsAdapter = new TabsAdapter(this, fragmentManager, viewPager);
        tabsAdapter.addTab(bar.newTab().setText("Ajout"),
        		AddFragment.class, null);
        tabsAdapter.addTab(bar.newTab().setText("Liste"),
        		ListFragment.class, null);
        
        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }
    
}
