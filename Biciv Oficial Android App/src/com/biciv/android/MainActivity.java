package com.biciv.android;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.biciv.android.activities.SingleBikeStation;

public class MainActivity extends SherlockActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent singleBikeStationActivity = new Intent(this, SingleBikeStation.class);
        singleBikeStationActivity.putExtra(SingleBikeStation.Params.BIKE_STATION_ID.toString(), 264);
        startActivity(singleBikeStationActivity);
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}
    
    

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
}
