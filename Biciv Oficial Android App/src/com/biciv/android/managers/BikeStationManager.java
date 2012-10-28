package com.biciv.android.managers;

import com.biciv.android.dao.BikeStationDAO;
import com.biciv.android.dao.BikeStationDAO.NotCachedBikeStation;
import com.biciv.android.entities.BikeStation;

public class BikeStationManager {
	
	public BikeStationManager(){}
	
	public void forceSync(Callback onSyncEnds, Callback onError){
		new BikeStationDAO().forceSync(onSyncEnds, onError);
	}
	
	public BikeStation getBikeStation(int bikeStationID) throws NotCachedBikeStation{
		return new BikeStationDAO().getCachedBikeStation(bikeStationID);
	}
}
