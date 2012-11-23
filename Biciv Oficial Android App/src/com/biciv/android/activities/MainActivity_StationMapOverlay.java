package com.biciv.android.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.MotionEvent;

import com.biciv.android.activities.SingleBikeStation.Params;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class MainActivity_StationMapOverlay extends ItemizedOverlay<MainActivity_StationMapMarker> {

	private ArrayList<MainActivity_StationMapMarker> mOverlays = new ArrayList<MainActivity_StationMapMarker>();
	private Context mContext;
	private boolean   isPinch  =  false;
	

	public MainActivity_StationMapOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected MainActivity_StationMapMarker createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(MainActivity_StationMapMarker overlay) {
        mOverlays.add(overlay);
        populate();
    }
	
	@Override
    public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, false);
    }
	
	@Override
	protected final boolean onTap(int index) {
		if ( isPinch ){
	        return false;
	    }else{
	    	Bundle sendBundle = new Bundle();
	        sendBundle.putInt(SingleBikeStation.Params.BIKE_STATION_ID.toString(), createItem(index).getStationID());
	        
	        Intent i = new Intent(mContext, SingleBikeStation.class);
	        i.putExtras(sendBundle);
	        mContext.startActivity(i);
	    }		
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView)
	{
	    int fingers = e.getPointerCount();
	    if( e.getAction()==MotionEvent.ACTION_DOWN ){
	        isPinch=false;  // Touch DOWN, don't know if it's a pinch yet
	    }
	    if( e.getAction()==MotionEvent.ACTION_MOVE && fingers==2 ){
	        isPinch=true;   // Two fingers, def a pinch
	    }
	    return super.onTouchEvent(e,mapView);
	}
}
