package com.biciv.android.activities;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class MainActivity_MapOverlay extends ItemizedOverlay<MainActivity_MapMarker> {

	private ArrayList<MainActivity_MapMarker> mOverlays = new ArrayList<MainActivity_MapMarker>();

	public MainActivity_MapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected MainActivity_MapMarker createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(MainActivity_MapMarker overlay) {
        mOverlays.add(overlay);
        populate();
    }
	
	@Override
    public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, false);
    }

}
