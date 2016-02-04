package com.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.serial.ZigBeeModule;

@Entity("sensordata")
public class SensorDataEntity {

	public SensorDataEntity() {
		// TODO Auto-generated constructor stub
	}
	public SensorDataEntity(String id, List<ZigBeeModule> sensordata) {
		super();
		this.id = id;
		this.sensordata = sensordata;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<ZigBeeModule> getSensordata() {
		return sensordata;
	}
	public void setSensordata(List<ZigBeeModule> sensordata) {
		this.sensordata = sensordata;
	}
	public void addSensorData(ZigBeeModule zmb){
		sensordata.add(zmb);
	}
	
	@Id
	private String id;
	@Embedded
	public List<ZigBeeModule> sensordata = new ArrayList<ZigBeeModule>();

}
