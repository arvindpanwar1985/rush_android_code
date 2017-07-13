package com.hoffmans.rush.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackDriverBean extends BaseBean{

	@SerializedName("driver_id")
	@Expose
	private int driverId;

	@SerializedName("vehicle_type_id")
	@Expose
	private int vehicleTypeId;

	@SerializedName("latitude")
	@Expose
	private double latitude;

	@SerializedName("service_id")
	@Expose
	private int serviceId;

	@SerializedName("longitude")
	@Expose
	private double longitude;

	public void setDriverId(int driverId){
		this.driverId = driverId;
	}

	public int getDriverId(){
		return driverId;
	}

	public void setVehicleTypeId(int vehicleTypeId){
		this.vehicleTypeId = vehicleTypeId;
	}

	public int getVehicleTypeId(){
		return vehicleTypeId;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setServiceId(int serviceId){
		this.serviceId = serviceId;
	}

	public int getServiceId(){
		return serviceId;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
	public String toString(){
		return
				"Data{" +
						"driver_id = '" + driverId + '\'' +
						",vehicle_type_id = '" + vehicleTypeId + '\'' +
						",latitude = '" + latitude + '\'' +
						",service_id = '" + serviceId + '\'' +
						",longitude = '" + longitude + '\'' +
						"}";
	}
}