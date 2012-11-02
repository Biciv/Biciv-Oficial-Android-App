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

public class MainActivity_MapMarker extends OverlayItem {
	private int mAvailableBikes;
	private int mAvailableHollows;
	private Context mContext;
	
	public MainActivity_MapMarker(GeoPoint pt, String label, String snippet, BikeStation bikeStation, Context context) {
		super(pt, label, snippet);
		
		this.mAvailableBikes = bikeStation.getAvailable();
		this.mAvailableHollows = bikeStation.getFree();
		this.mContext = context;
				
	}

	public Drawable getDefaultMarker() {
		Bitmap viewCapture = null;
        Drawable drawOverlay = null;
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        
        LinearLayout markerLayout = (LinearLayout) inflater.inflate(R.layout.mainactivity_tabstationsmap_marker, null);
		
        TextView avaiableBikes = (TextView) markerLayout.findViewById(R.id.mainactivity_tabstationmap_marker_availableBikes);
		avaiableBikes.setText(""+mAvailableBikes);
		
		TextView availableHollows = (TextView) markerLayout.findViewById(R.id.mainactivity_tabstationmap_marker_availableHollows);
		availableHollows.setText(""+mAvailableHollows);
		
		// we need to enable the drawing cache
		markerLayout.setDrawingCacheEnabled(true);
		
		// this is the important code
        // Without it the view will have a dimension of 0,0 and the bitmap
        // will be null
		markerLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());
        
        // we need to build our drawing cache
        markerLayout.buildDrawingCache(true);
        
        viewCapture = Bitmap.createBitmap(markerLayout
                .getDrawingCache());

        markerLayout.setDrawingCacheEnabled(false);
        drawOverlay = new BitmapDrawable(mContext.getResources(),viewCapture);
        
        return drawOverlay;
	}

}
