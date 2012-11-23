package com.biciv.android.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biciv.android.R;
import com.biciv.android.entities.BikeStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MainActivity_StationMapMarker extends OverlayItem {
	private int mStationID;
	private int mAvailableBikes;
	private int mAvailableHollows;
	private Context mContext;
	
	public MainActivity_StationMapMarker(GeoPoint pt, String label, String snippet, BikeStation bikeStation) {
		super(pt, label, snippet);
		this.mAvailableBikes = bikeStation.getAvailable();
		this.mAvailableHollows = bikeStation.getFree();
		this.mStationID = bikeStation.getId();
	}
	
	public int getStationID(){
		return this.mStationID;
	}

}
