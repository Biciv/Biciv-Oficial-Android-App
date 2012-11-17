package com.biciv.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.actionbarsherlock.app.SherlockFragment;
import com.biciv.android.R;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStations;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.managers.BikeStationManager;
import com.biciv.android.module.LoadBikeStationsOnMapAsync;
import com.biciv.android.module.LocationService;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class MainActivity_tabStationsMap extends SherlockFragment {
	public static View mapViewLayout;
	private LoadBikeStationsOnMapAsync task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist.  The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed.  Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		
		if(mapViewLayout == null) {
			mapViewLayout = getSherlockActivity().getLayoutInflater().inflate(R.layout.mainactivity_tabstationsmap, null);
		}
		
		ViewGroup parent = (ViewGroup) mapViewLayout.getParent();
		if(parent != null)
			parent.removeView(mapViewLayout);
		
		configMap();
		
		return mapViewLayout;
	}
	
	private void configMap(){
		MapView mapView = (MapView) mapViewLayout.findViewById(R.id.mainActivity_map);
		
		MapController mc = mapView.getController();
		
		LocationService locationService = new LocationService(getSherlockActivity());
		Location current = locationService.getCurrentLocation();
		
		if(current == null) mc.setCenter( new GeoPoint((int) (39.47818 * 1E6), (int) (-0.38354* 1E6)) );
		else mc.setCenter( new GeoPoint((int) current.getLatitude(), (int) current.getLongitude()) );

		mc.setZoom(18);		
	}
	

	@Override
	public void onPause() {
		super.onPause();
		task.close();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadStationsMarkers();
	}

	public void loadStationsMarkers(){
		try {
			MapView mapView = (MapView) mapViewLayout.findViewById(R.id.mainActivity_map);
			
			HashMap<Integer, BikeStation> bikeStations = new BikeStationManager().getBikeStations();
			task = new LoadBikeStationsOnMapAsync(getSherlockActivity(), mapView, bikeStations);
			task.execute();			
		} catch (NotCachedBikeStations e) {
			e.printStackTrace();
		}
	}
}
