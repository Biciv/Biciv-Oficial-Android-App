package com.biciv.android.module;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationService {

	private LocationManager locationManager;
	private String provider;

	public LocationService(Activity activity) {
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria(); 
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
		criteria.setAltitudeRequired(false); 
		criteria.setBearingRequired(false); 
		criteria.setCostAllowed(true); 
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		provider = locationManager.getBestProvider(criteria,true);
	}
	
	public Location getCurrentLocation() {	 
		Location location = null;
		
		if(provider != null)
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		return location;
	} 

}
