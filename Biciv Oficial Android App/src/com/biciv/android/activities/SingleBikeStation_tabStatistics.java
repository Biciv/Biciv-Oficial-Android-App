package com.biciv.android.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.biciv.android.R;
import com.biciv.android.activities.synchronization.AsyncSystem;
import com.biciv.android.activities.synchronization.ISyncSystem;
import com.biciv.android.activities.synchronization.SyncSystemSyncTypes;
import com.biciv.android.dao.BikeStationDAO.NotCachedLastHour;
import com.biciv.android.entities.BikeStation;
import com.biciv.android.entities.BikeStation.LastHour;
import com.biciv.android.managers.BikeStationManager;
import com.biciv.android.managers.Callback;
import com.biciv.android.managers.LastHourCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
//Graph library: http://www.jjoe64.com/p/graphview-library.html
public class SingleBikeStation_tabStatistics extends SherlockFragment implements ISyncSystem {

	private AsyncSystem asyncSystem;

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
		
		return inflater.inflate(R.layout.singlebikestation_tabstatistics, container, false);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		prepareStats();
	}
	
	private void prepareStats(){
		Integer bikeStationID = getSherlockActivity().getIntent().getExtras().getInt(SingleBikeStation.Params.BIKE_STATION_ID.toString());
		
		try {
			BikeStation.LastHour lastHour = new BikeStationManager().getLastHour(bikeStationID);
			
			GraphViewData [] graphData = new GraphViewData[60];
			
			ArrayList<Integer> lastHourSeriesRaw = lastHour.getData();
			int lastHourSeriesRawSize = lastHourSeriesRaw.size();
			for(int i=0; i<lastHourSeriesRawSize; i++){
				graphData[i] = new GraphViewData(i, lastHourSeriesRaw.get(i));
			}
			
			GraphViewSeries lastHourSeries = new GraphViewSeries(graphData);
			GraphView lastHourView = new LineGraphView(getActivity(), "bla bla");
			lastHourView.addSeries(lastHourSeries);
			lastHourView.setViewPort(0, 59);
			
			/*int verticalLabelsSize = lastHour.getCapacity()/4+1;
			String [] verticalLabels = new String[verticalLabelsSize];
			for(int i=0; i<verticalLabelsSize; i++){ //TODO
				verticalLabels[i] = ""+i*4;
			}
			
			lastHourView.setVerticalLabels(verticalLabels);*/
			
			LinearLayout layout = (LinearLayout) getSherlockActivity().findViewById(R.id.singleBikeStation_StatsContainer);
			
			layout.removeAllViews();
			layout.addView(lastHourView);
		} catch (NotCachedLastHour e) {
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		
		asyncSystem.close();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		Integer bikeStationID = getSherlockActivity().getIntent().getExtras().getInt(SingleBikeStation.Params.BIKE_STATION_ID.toString());
		
		asyncSystem = new AsyncSystem(this);
		asyncSystem.startLastHour(bikeStationID);
	}

	@Override
	public void onSync(SyncSystemSyncTypes syncType) {
		prepareStats();
	}

	@Override
	public void onError(SyncSystemSyncTypes syncType) {
		// TODO Auto-generated method stub
		
	}
}
