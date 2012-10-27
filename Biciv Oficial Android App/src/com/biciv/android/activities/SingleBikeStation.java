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

	/**
	* Setup TabHost
	*/
	private void initialiseTabHost(Bundle savedInstanceState) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		//****** tab1 start
		TabHost.TabSpec tabSpec_statistics = mTabHost.newTabSpec("STATS");
		tabSpec_statistics.setIndicator("Estadísticas");
		tabSpec_statistics.setContent(new TabFactory(this));
		String tagStats = tabSpec_statistics.getTag();
		
		FragmentManager fm = this.getSupportFragmentManager();
		fragmentStats = fm.findFragmentByTag(tagStats);
		if(fragmentStats == null)
			fragmentStats = Fragment.instantiate(this, SingleBikeStation_tabStatistics.class.getName(), savedInstanceState);
		
		mTabHost.addTab(tabSpec_statistics);
		//****** tab1 end
		
		//****** tab2 start
		TabHost.TabSpec tabSpec_map = mTabHost.newTabSpec("MAP");
		tabSpec_map.setIndicator("Mapa");
		tabSpec_map.setContent(new TabFactory(this));
		String tagMap = tabSpec_map.getTag();
		
		fm = this.getSupportFragmentManager();
		fragmentMap = fm.findFragmentByTag(tagMap);
		if(fragmentMap == null)
			fragmentMap = Fragment.instantiate(this, SingleBikeStation_tabMap.class.getName(), savedInstanceState);
		
		mTabHost.addTab(tabSpec_map);
		//****** tab2 end
		
		OnTabChangeListener listener = null;
		mTabHost.setOnTabChangedListener(listener=new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabTagToOpen) {
				Fragment newTab = null;
				if(tabTagToOpen == "STATS")
					newTab = fragmentStats;
				else
					newTab = fragmentMap;
				
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(android.R.id.tabcontent, newTab, tabTagToOpen);
				ft.commit();
				getSupportFragmentManager().executePendingTransactions();
			}
		});
		
		// Default to first tab
		listener.onTabChanged("STATS");
	}

}
