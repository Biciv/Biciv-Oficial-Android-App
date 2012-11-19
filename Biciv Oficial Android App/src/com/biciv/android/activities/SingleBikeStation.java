package com.biciv.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.biciv.android.R;
import com.biciv.android.activities.synchronization.AsyncSystem;
import com.biciv.android.activities.synchronization.ISyncSystem;
import com.biciv.android.activities.synchronization.SyncSystemSyncTypes;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.managers.BikeStationManager;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class SingleBikeStation extends SherlockFragmentActivity implements ISyncSystem {

	private TabHost mTabHost;
	private Fragment fragmentStats;
	private Fragment fragmentMap;

	private static String tabTagSTATS = "STATS";
	private static String tabTagMAP = "MAP";
	private static String tabTagSOCIAL = "SOCIAL"; //TODO
	
	private Integer bikeStationID;
	
	private AsyncSystem asyncSystem;
	
	public static enum Params {
		BIKE_STATION_ID
	};

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
		
		Bundle params = getIntent().getExtras();
		if(params == null) {
			Toast.makeText(this, "Something wrong with params", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		bikeStationID = params.getInt(Params.BIKE_STATION_ID.toString());
		if(bikeStationID == null) {
			Toast.makeText(this, "Something wrong with params", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		initialiseTabHost(savedInstanceState);
		
		try {
			BikeStation bikeStation = new BikeStationManager().getBikeStation(bikeStationID);
			setBikeStationData(bikeStation);
		} catch (NotCachedBikeStation e) {
			e.toastMessage(this);
			finish();
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.singlebikestation, menu);
		return true;
	}
	
	public void syncNow(MenuItem menuItem){
		asyncSystem.syncNow();
	}
	
	public void setBikeStationData(BikeStation bikeStation){
		String title = bikeStation.getId()+" - "+bikeStation.getAddress();
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setTitle(title);
		
		ProgressBar progressBikesAndSlots = (ProgressBar) findViewById(R.id.singleBikeStation_progressBikesAndSlots);
		progressBikesAndSlots.setMax(bikeStation.getAvailable()+bikeStation.getFree());
		progressBikesAndSlots.setProgress(bikeStation.getAvailable());
		
		TextView availableBikesTV = (TextView) findViewById(R.id.singleBikeStation_availableBikes);
		availableBikesTV.setText(""+bikeStation.getAvailable());
		
		TextView freeSlotsTV = (TextView) findViewById(R.id.singleBikeStation_availableSlots);
		freeSlotsTV.setText(""+bikeStation.getFree());
	}

	/*
	 * Setup TabHost
	 */
	private void initialiseTabHost(Bundle savedInstanceState) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();

		//****** tab1 start
		TabHost.TabSpec tabSpec_statistics = mTabHost.newTabSpec(tabTagSTATS);
		tabSpec_statistics.setIndicator(getString(R.string.singleBikeStation_tabNameStats));
		tabSpec_statistics.setContent(new TabFactory(this));
		mTabHost.addTab(tabSpec_statistics);

		fragmentStats = getSupportFragmentManager().findFragmentByTag(tabTagSTATS);
		if(fragmentStats == null)
			fragmentStats = Fragment.instantiate(this, SingleBikeStation_tabStatistics.class.getName(), savedInstanceState);
		//****** tab1 end

		//****** tab2 start
		TabHost.TabSpec tabSpec_map = mTabHost.newTabSpec(tabTagMAP);
		tabSpec_map.setIndicator(getString(R.string.singleBikeStation_tabNameMap));
		tabSpec_map.setContent(new TabFactory(this));
		mTabHost.addTab(tabSpec_map);

		fragmentMap = getSupportFragmentManager().findFragmentByTag(tabTagMAP);
		if(fragmentMap == null) {
			fragmentMap = Fragment.instantiate(this, SingleBikeStation_tabMap.class.getName(), savedInstanceState);
		}//****** tab2 end

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

	@Override
	public void onSync(SyncSystemSyncTypes syncType) {
		try {
			BikeStation bikeStation = new BikeStationManager().getBikeStation(bikeStationID);
			setBikeStationData(bikeStation);
		} catch (NotCachedBikeStation e) {
			e.toastMessage(this);
			finish();
			return;
		}
	}

	@Override
	public void onError(SyncSystemSyncTypes syncType) {
		Toast.makeText(SingleBikeStation.this, "Error al sincronizar.", Toast.LENGTH_SHORT).show();
		//TODO
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		asyncSystem.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		asyncSystem = new AsyncSystem(this);
		asyncSystem.start();
	}
}
