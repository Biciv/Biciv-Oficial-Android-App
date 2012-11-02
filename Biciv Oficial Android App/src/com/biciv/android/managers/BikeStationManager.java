package com.biciv.android.managers;

import com.biciv.android.activities.synchronization.BadSynchronization;
import com.biciv.android.dao.BikeStationDAO;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.dao.BikeStationDAO.NotCachedLastHour;
import com.biciv.android.entities.BikeStation;

public class BikeStationManager {
	
	public BikeStationManager(){}
	
	public long getLastFullSync(){
		return new BikeStationDAO().getLastFullSync();
	}
	
	public long getLastLastHourSync(int bikeStationID){
		return new BikeStationDAO().getLastLastHourSync(bikeStationID);
	}
	
	public void forceSync() throws BadSynchronization{
		new BikeStationDAO().forceSync();
	}
	
	public BikeStation getBikeStation(int bikeStationID) throws NotCachedBikeStation{
		return new BikeStationDAO().getCachedBikeStation(bikeStationID);
	}
	
	public void lastHourSync(int bikeStationID) throws BadSynchronization {
		new BikeStationDAO().lastHourSync(bikeStationID);
	}
	
	public BikeStation.LastHour getLastHour(int bikeStationID) throws NotCachedLastHour{
		return new BikeStationDAO().getCachedLastHourBikeStation(bikeStationID);
	}
}
