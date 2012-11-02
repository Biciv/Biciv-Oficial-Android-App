package com.biciv.android.activities;

import com.actionbarsherlock.app.SherlockFragment;
import com.biciv.android.R;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.managers.BikeStationManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class SingleBikeStation_tabMap extends SherlockFragment {
	
	private static View tabMapLayoutView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null)
			return null;
		
		/*
		 * Workarround to resolve the problem with MapView.
		 * If uses: return inflater.inflate(R.layout.singlebikestation_tabmap, container, false);
		 * It will throw an exception that MapActivy can only have only one MapView.
		 * With this the layout view is ever the SAME for this fragment.
		 * */
		if(tabMapLayoutView == null)
			tabMapLayoutView = getSherlockActivity().getLayoutInflater().inflate(R.layout.singlebikestation_tabmap, null);
		
		/*
		 * This resolve the problem with the views that are inside other.
		 * The tabMapLayoutView layout has parent (the previus container).
		 * A view can only be inside in only one parent.
		 * */
		ViewGroup parent = (ViewGroup) tabMapLayoutView.getParent();
		if(parent != null)
			parent.removeView(tabMapLayoutView);
		
		configMapView();
		
		return tabMapLayoutView;
	}
	
	private void configMapView(){
		MapView mapView = (MapView) tabMapLayoutView.findViewById(R.id.singleBikeStation_map);
		
		Integer bikeStationID = getSherlockActivity().getIntent().getExtras().getInt(SingleBikeStation.Params.BIKE_STATION_ID.toString());
		
		try {
			BikeStation bikeStation = new BikeStationManager().getBikeStation(bikeStationID);
			Double lat = bikeStation.getLat()*1E6;
			Double lng = bikeStation.getLng()*1E6;
			MapController mc = mapView.getController();
			mc.setCenter( new GeoPoint(lat.intValue(), lng.intValue()) );
			mc.setZoom(18);
		} catch (NotCachedBikeStation e) {
			e.toastMessage(getSherlockActivity());
			getSherlockActivity().finish();
		}
	}
	
}
