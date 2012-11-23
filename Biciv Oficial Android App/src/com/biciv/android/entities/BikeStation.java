package com.biciv.android.entities;

import java.util.ArrayList;

public class BikeStation {
	public Integer id;
	private String address = "";
	private double lat = 0.0;
	private double lng = 0.0;
	private boolean credit = false;
	private int available = 0;
	private int free = 0;
	private String lastCheck = "0/0/0 0:0:0";
	private ArrayList<Boolean> incidents;
	
	private boolean isFavourite = false;

	public boolean isFavourite() {
		return isFavourite;
	}

	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public BikeStation(int id, String address, double lat, double lng,
			boolean credit, int available, int free, String lastCheck,
			boolean outOfService, boolean brokenBikes, boolean bloquedSlots) {
		this.id = id;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.credit = credit;
		this.available = available;
		this.free = free;
		this.lastCheck = lastCheck;
		
		this.incidents.add(outOfService);
		this.incidents.add(brokenBikes);
		this.incidents.add(bloquedSlots);
	}
	
	public int getId() {
		return id;
	}



	public String getAddress() {
		return address;
	}



	public double getLat() {
		return lat;
	}



	public double getLng() {
		return lng;
	}



	public boolean isCredit() {
		return credit;
	}



	public int getAvailable() {
		return available;
	}



	public int getFree() {
		return free;
	}



	public String getLastCheck() {
		return lastCheck;
	}

	public boolean isOutOfService(){
		return incidents.get(0);
	}
	public boolean isBrokenBikes(){
		return incidents.get(1);
	}
	public boolean isBloquedSlots(){
		return incidents.get(2);
	}
	
	public static class LastHour {
		private String start = "0/0/0 0:0:0";
		private int capacity = 0;
		private ArrayList<Integer> data;
		
		public String getStart() {
			return start;
		}
		public int getCapacity() {
			return capacity;
		}
		public ArrayList<Integer> getData() {
			return data;
		}
		
		
	}
}
