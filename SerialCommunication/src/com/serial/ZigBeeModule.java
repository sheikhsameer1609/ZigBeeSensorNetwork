/**
 * 
 */
package com.serial;

import java.util.Date;

import com.google.code.morphia.annotations.Transient;

/**
 * @author sameer
 * 
 */
public class ZigBeeModule {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String lightdata;
	private String temperaturedata;
	private String humidity;
	public int getHumidity() {
		if(humidity!= null)
		return new Integer(humidity).intValue();
		else
			return 100;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	@Transient
	private boolean newdata;
	private Date currenttime;
	public Date getCurrenttime() {
		return currenttime;
	}
	

	public void setCurrenttime(Date currenttime) {
		this.currenttime = currenttime;//.setSeconds( Math.round(currenttime.getSeconds());
	}

	private int counter=0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isNewdata() {
		return newdata;
	}
	public void setNewdata(boolean newdata) {
		this.newdata = newdata;
	}

	public int getLightdata() {
		return new Integer(lightdata).intValue();
	}

	public void setLightdata(String lightdata) {
		this.lightdata = lightdata;
	}

	public int getTemperaturedata() {
		return new Integer(temperaturedata).intValue();
	}

	public void setTemperaturedata(String temperaturedata) {
		this.temperaturedata = temperaturedata;
	}

	/**
	 * This class represents the class of a zigbee module, so that to have
	 * scalability
	 */
	public ZigBeeModule() {

	}


	public ZigBeeModule(int id,String light, String temp,String humidity,Date curenttime) {
		this.id = id;
		this.lightdata = light;
		this.temperaturedata = temp;
		this.currenttime=curenttime;
		this.humidity=humidity;
	}
	public ZigBeeModule(String light, String temp,Date curenttime) {
		
		this.lightdata = light;
		this.temperaturedata = temp;
		this.newdata = true;
		this.counter = 0;
		this.currenttime=curenttime;
	}

}
