package com.project.aidememoire.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.aidememoire.fragment.AccueilFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	/**
	 * The fragment list.
	 */
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public ViewPagerAdapter(Context context, FragmentManager fm) {

		super(fm);

		Fragment f = (Fragment) Fragment.instantiate(context, AccueilFragment.class.getName());
		fragmentList.add(f);
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
