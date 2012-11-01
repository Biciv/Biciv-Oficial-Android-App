package com.biciv.android.exceptions;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastedException extends Exception {
	
	public void toastMessage(Context context) {
		Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT).show();
	}

}
