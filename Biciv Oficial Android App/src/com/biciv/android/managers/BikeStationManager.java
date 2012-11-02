package com.biciv.android.managers;

import com.biciv.android.activities.synchronization.BadSynchronization;
import java.util.ArrayList;
import com.biciv.android.dao.BikeStationDAO;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStations;
import com.biciv.android.entities.BikeStation;

public class BikeStationManager {
	
	public BikeStationManager(){}
	
	public long getLastFullSync(){
		return new BikeStationDAO().getLastFullSync();
	}
	
	public void forceSync() throws BadSynchronization{
		new BikeStationDAO().forceSync();
	}
	
	public BikeStation getBikeStation(int bikeStationID) throws NotCachedBikeStation{
		return new BikeStationDAO().getCachedBikeStation(bikeStationID);
	}
	
	public ArrayList<BikeStation> getBikeStations() throws NotCachedBikeStations{
		return new BikeStationDAO().getCachedBikeStations();
	}
	
	public void askLastHour(int bikeStationID, LastHourCallback onCall , Callback onError){
		new BikeStationDAO().getLastHour(bikeStationID, onCall, onError);
	}
}
