package com.biciv.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.actionbarsherlock.app.SherlockFragment;
import com.biciv.android.R;
import com.biciv.android.activities.synchronization.LoadBikeStationsOnMapAsync;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStations;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.managers.BikeStationManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class MainActivity_tabStationsMap extends SherlockFragment {
	public static Activity activity;
	public static MapView mapView;
	@Deprecated
	public static List<Overlay> mapOverlays;

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
		if(activity == null) activity = getSherlockActivity();
		if(mapView == null) {
			mapView = (MapView) activity.getLayoutInflater().inflate(R.layout.mainactivity_tabstationsmap, container, false);
			mapView.setBuiltInZoomControls(true);
			
			MapController mc = mapView.getController();
			mc.setCenter( new GeoPoint((int) (39.47818 * 1E6), (int) (-0.38354* 1E6)) );
			mc.setZoom(18);
		}
		
		ViewGroup parent = (ViewGroup) mapView.getParent();
		if(parent != null)
			parent.removeView(mapView);
		loadStationsMarkers();
		
		return mapView;
	}
	
	public void loadStationsMarkers(){
		try {
			HashMap<Integer, BikeStation> bikeStations = new BikeStationManager().getBikeStations();
			LoadBikeStationsOnMapAsync task = new LoadBikeStationsOnMapAsync(activity, mapView, bikeStations);
			task.execute();			
		} catch (NotCachedBikeStations e) {
			e.printStackTrace();
		}
	}
}
