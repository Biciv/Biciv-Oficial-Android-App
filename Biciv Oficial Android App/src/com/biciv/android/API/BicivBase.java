package com.biciv.android.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BicivBase {

	protected final static String host = "http://biciv.com/api";
	protected final static int readTimeout = 20000; //20 seconds.
	protected final static int connectTimeout = 15000; //15 seconds.

	protected String api_url;

	public BicivBase(String apiCall) {
		api_url = host+apiCall;
	}

	public String call() throws IOException{
		InputStream is = null;

		try {
			URL url = new URL(api_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(getReadTimeout());
			conn.setConnectTimeout(getConnectTimeout());
	
			conn.connect();
			is = conn.getInputStream();
			
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
			
			return total.toString();
		} finally {
			if(is != null)
				is.close();
		}
	}

	private static int getReadTimeout() {
		return readTimeout;
	}

	private static int getConnectTimeout() {
		return connectTimeout;
	}
	
}
