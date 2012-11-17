package com.biciv.android.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biciv.android.R;
import com.biciv.android.activities.MainActivity_StationMapMarker;
import com.biciv.android.activities.MainActivity_StationMapOverlay;
import com.biciv.android.entities.BikeStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class LoadBikeStationsOnMapAsync {	
	private MapView mapView;
	private Context mContext;
	private HashMap<Integer, BikeStation> bikeStations;
	private List<Overlay> mapOverlays;

	public LoadBikeStationsOnMapAsync(Context context, MapView mapView, HashMap<Integer, BikeStation> bikeStations){
		this.mapView = mapView;
		this.mContext = context;
		this.bikeStations = bikeStations;
		this.mapOverlays = mapView.getOverlays();
		
		if(!this.mapOverlays.isEmpty()) 
	    { 
			this.mapOverlays.clear();
	    }
	}
	
	public void execute(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
		CreateBikeStationsBitmapsAsyncTask task = new CreateBikeStationsBitmapsAsyncTask();
		task.execute(inflater, bikeStations);		
	}

	private class CreateBikeStationsBitmapsAsyncTask extends AsyncTask<Object, Object, Void>{

		protected void onPreExecute() {
		}
		
		@Override
		protected Void doInBackground(Object... params) {
			LayoutInflater layoutInflater = (LayoutInflater) params[0];
			HashMap<Integer, BikeStation> bikeStations = (HashMap<Integer, BikeStation>) params[1];

			Iterator<Entry<Integer, BikeStation>> iter = bikeStations.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<Integer, BikeStation> entry = iter.next();
				BikeStation bikeStation = entry.getValue();
				publishProgress(entry.getKey(), LoadBikeStationsOnMapAsync.getBikeStationDefaultMarker(layoutInflater, bikeStation));
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Object... values) {
			Integer keyBikeStation = (Integer) values[0];
			Bitmap bitmap = (Bitmap) values[1];
			
			BikeStation bikeStation = bikeStations.get(keyBikeStation);
			
			Drawable drawOverlay = new BitmapDrawable(mContext.getResources(), bitmap);
			
			GeoPoint CENTER_POINT = new GeoPoint((int) (bikeStation.getLat() * 1E6), (int) (bikeStation.getLng()* 1E6));
			MainActivity_StationMapMarker mapMarker = new MainActivity_StationMapMarker(CENTER_POINT, "", "", bikeStation);

			MainActivity_StationMapOverlay itemizedOverlay = new MainActivity_StationMapOverlay(drawOverlay, mContext);

			itemizedOverlay.addOverlay(mapMarker);
			
			mapOverlays.add(itemizedOverlay);
		}

		protected void onPostExecute(Void a) {
			mapView.invalidate();
		}
	}
	
	public static Bitmap getBikeStationDefaultMarker(LayoutInflater inflater, BikeStation bikeStation) {
		Bitmap viewCapture = null;
        
        LinearLayout markerLayout = (LinearLayout) inflater.inflate(R.layout.mainactivity_tabstationsmap_marker, null);
		
        TextView avaiableBikes = (TextView) markerLayout.findViewById(R.id.mainactivity_tabstationmap_marker_availableBikes);
		avaiableBikes.setText(""+bikeStation.getAvailable());
		
		TextView availableHollows = (TextView) markerLayout.findViewById(R.id.mainactivity_tabstationmap_marker_availableHollows);
		availableHollows.setText(""+bikeStation.getFree());
		
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
        
        return viewCapture;
	}	
}
