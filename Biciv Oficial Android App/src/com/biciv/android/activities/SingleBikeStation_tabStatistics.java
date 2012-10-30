package com.biciv.android.activities;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;
import com.biciv.android.R;
import com.biciv.android.entities.BikeStation.LastHour;
import com.biciv.android.managers.BikeStationManager;
import com.biciv.android.managers.Callback;
import com.biciv.android.managers.LastHourCallback;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
//Graph library: http://www.jjoe64.com/p/graphview-library.html
public class SingleBikeStation_tabStatistics extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
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
		if(bikeStationID == null){
			//TODO error.
		}
		
		new BikeStationManager().askLastHour(bikeStationID, new LastHourCallback() {
			
			@Override
			public void call(LastHour bikeStationLastHour) {
				GraphViewData [] graphData = new GraphViewData[60];
				
				ArrayList<Integer> lastHourSeriesRaw = bikeStationLastHour.getData();
				int lastHourSeriesRawSize = lastHourSeriesRaw.size();
				for(int i=0; i<lastHourSeriesRawSize; i++){
					graphData[i] = new GraphViewData(i, lastHourSeriesRaw.get(i));
				}
				
				GraphViewSeries lastHourSeries = new GraphViewSeries(graphData);
				GraphView lastHourView = new LineGraphView(getActivity(), "bla bla");
				lastHourView.addSeries(lastHourSeries);
				lastHourView.setViewPort(0, 59);
				
				int verticalLabelsSize = bikeStationLastHour.getCapacity()/4+1;
				String [] verticalLabels = new String[verticalLabelsSize];
				for(int i=0; i<verticalLabelsSize; i++){
					verticalLabels[i] = ""+i*4;
				}
				
				lastHourView.setVerticalLabels(verticalLabels);
				
				LinearLayout layout = (LinearLayout) getSherlockActivity().findViewById(R.id.singleBikeStation_StatsContainer);
				
				layout.removeAllViews();
				layout.addView(lastHourView);
			}
		}, new Callback() {
			
			@Override
			public void call() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
