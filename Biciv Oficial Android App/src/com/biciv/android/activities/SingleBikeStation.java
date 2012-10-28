package com.biciv.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.biciv.android.R;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class SingleBikeStation extends SherlockFragmentActivity {

	private TabHost mTabHost;
	private Fragment fragmentStats;
	private Fragment fragmentMap;
	
	private static String tabTagSTATS = "STATS";
	private static String tabTagMAP = "MAP";
	private static String tabTagSOCIAL = "SOCIAL";
	
	/*
	 * It put an empty view as a container of the fragment.
	 * */
	private class TabFactory implements TabHost.TabContentFactory {
		private final Context mContext;
		
		public TabFactory(Context context) {
			mContext = context;
		}
		
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlebikestation);
		
		initialiseTabHost(savedInstanceState);
	}

	/*
	* Setup TabHost
	*/
	private void initialiseTabHost(Bundle savedInstanceState) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		FragmentManager fm = this.getSupportFragmentManager();
		
		//****** tab1 start
		TabHost.TabSpec tabSpec_statistics = mTabHost.newTabSpec(tabTagSTATS);
		tabSpec_statistics.setIndicator(getString(R.string.singleBikeStation_tabNameStats));
		tabSpec_statistics.setContent(new TabFactory(this));
		mTabHost.addTab(tabSpec_statistics);
		
		fragmentStats = fm.findFragmentByTag(tabTagSTATS);
		if(fragmentStats == null)
			fragmentStats = Fragment.instantiate(this, SingleBikeStation_tabStatistics.class.getName(), savedInstanceState);
		//****** tab1 end
		
		//****** tab2 start
		TabHost.TabSpec tabSpec_map = mTabHost.newTabSpec(tabTagMAP);
		tabSpec_map.setIndicator(getString(R.string.singleBikeStation_tabNameMap));
		tabSpec_map.setContent(new TabFactory(this));
		mTabHost.addTab(tabSpec_map);
		
		fragmentMap = fm.findFragmentByTag(tabTagMAP);
		if(fragmentMap == null)
			fragmentMap = Fragment.instantiate(this, SingleBikeStation_tabMap.class.getName(), savedInstanceState);
		//****** tab2 end
		
		OnTabChangeListener listener = null;
		mTabHost.setOnTabChangedListener(listener=new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabTagToOpen) {
				Fragment newTab = null;
				if(tabTagToOpen == tabTagSTATS)
					newTab = fragmentStats;
				else
					newTab = fragmentMap;
				
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(android.R.id.tabcontent, newTab, tabTagToOpen);
				ft.commit();
				getSupportFragmentManager().executePendingTransactions();
			}
		});
		
		// Default first tab
		listener.onTabChanged(tabTagSTATS);
	}

}
