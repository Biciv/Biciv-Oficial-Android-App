package com.biciv.android.activities;

import com.actionbarsherlock.app.SherlockFragment;
import com.biciv.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

//http://thepseudocoder.wordpress.com/2011/10/04/android-tabs-the-fragment-way/
public class SingleBikeStation_tabStatistics extends SherlockFragment {

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
}
