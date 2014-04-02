package com.project.aidememoire.adapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.project.aidememoire.fragment.AddFragment;
import com.project.aidememoire.fragment.ListFragment;
import com.project.aidememoire.listener.OnFragmentChange;

public class TabsAdapter extends FragmentPagerAdapter 
	implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

	private final static String TAG = "ViewPagerAdapter";
	
	private Activity activity;
    private ActionBar actionBar;
    private ViewPager viewPager;
    private ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
    
    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

	public TabsAdapter(Activity activity, FragmentManager fm, ViewPager viewPager) {
		super(fm);
		this.activity = activity; 
		this.actionBar = activity.getActionBar();
		this.viewPager = viewPager;
		this.viewPager.setAdapter(this);
		viewPager.setOnPageChangeListener(this);
	}

	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args){
		TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        tabs.add(info);
        actionBar.addTab(tab);
        notifyDataSetChanged();
	}
	
	@Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = tabs.get(position);
        return Fragment.instantiate(activity, info.clss.getName(), info.args);
    }
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
		OnFragmentChange fragment = (OnFragmentChange) this.instantiateItem(viewPager, position);
		if(fragment != null){
			fragment.OnFragmentVisible();
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		Object tag = tab.getTag();
        for (int i=0; i<tabs.size(); i++) {
            if (tabs.get(i) == tag) {
                viewPager.setCurrentItem(i);
            }
        }
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}

}
