package com.biciv.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.biciv.android.R;
import com.biciv.android.R.layout;
import com.biciv.android.R.menu;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStations;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.managers.BikeStationManager;
import com.biciv.android.managers.Callback;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainActivity extends SherlockFragmentActivity {
	
	private TabHost mTabHost;
	
	private OnTabChangeListener listener;
	
	private Fragment fragmentFavorites;
	private Fragment fragmentStationsMap;
	
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        
        initialiseTabHost(savedInstanceState);
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.mainactivity, menu);
		
		return true;
	}
	
	/**
	* Setup TabHost
	*/
	private void initialiseTabHost(Bundle savedInstanceState) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		//****** tab1 start
		TabHost.TabSpec tabSpec_stationsmap = mTabHost.newTabSpec("STATIONSMAP");
		tabSpec_stationsmap.setIndicator("Estaciones");
		tabSpec_stationsmap.setContent(new TabFactory(this));
		String tagStats = tabSpec_stationsmap.getTag();
		
		FragmentManager fm = this.getSupportFragmentManager();
		fragmentStationsMap = fm.findFragmentByTag(tagStats);
		if(fragmentStationsMap == null)
			fragmentStationsMap = Fragment.instantiate(this, MainActivity_tabStationsMap.class.getName(), savedInstanceState);
		
		mTabHost.addTab(tabSpec_stationsmap);
		//****** tab1 end
		
		//****** tab2 start
		TabHost.TabSpec tabSpec_fav = mTabHost.newTabSpec("FAV");
		tabSpec_fav.setIndicator("Favoritas");
		tabSpec_fav.setContent(new TabFactory(this));
		String tagMap = tabSpec_fav.getTag();
		
		fm = this.getSupportFragmentManager();
		fragmentFavorites = fm.findFragmentByTag(tagMap);
		if(fragmentFavorites == null)
			fragmentFavorites = Fragment.instantiate(this, MainActivity_tabStationsMap.class.getName(), savedInstanceState);
		
		mTabHost.addTab(tabSpec_fav);
		//****** tab2 end
		
		listener = null;
		mTabHost.setOnTabChangedListener(listener=new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabTagToOpen) {
				Fragment newTab = null;
				if(tabTagToOpen == "STATIONSMAP")
					newTab = fragmentStationsMap;
				else
					newTab = fragmentFavorites;
				
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(android.R.id.tabcontent, newTab, tabTagToOpen);
				ft.commit();
				getSupportFragmentManager().executePendingTransactions();
			}
		});
		
		// Default to first tab
		listener.onTabChanged("STATIONSMAP");
	}
    
    

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
	
	public void syncNow(MenuItem menuItem){
		Callback onSyncEnds = new Callback() {
			@Override
			public void call() {
				MainActivity_tabStationsMap fragment = (MainActivity_tabStationsMap) fragmentStationsMap;
				fragment.loadStationsMarkers();
			}
		};
		Callback onSyncError = new Callback() {
			@Override
			public void call() {
				Toast.makeText(MainActivity.this, "Error al sincronizar.", Toast.LENGTH_SHORT).show();
				//TODO
			}
		};
		new BikeStationManager().forceSync(onSyncEnds, onSyncError);
	}
	
	
}
