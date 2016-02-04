package com.entity;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("threshold")
public class ThresholdValueSaver {
	@Id
	private int id =1;
	private String lightthresholdMax;
	public ThresholdValueSaver(String lightthresholdMax,
			String tempthresholdMax, String lightthresholdMin,
			String tempthresholdMin) {
		super();
		this.lightthresholdMax = lightthresholdMax;
		this.tempthresholdMax = tempthresholdMax;
		this.lightthresholdMin = lightthresholdMin;
		this.tempthresholdMin = tempthresholdMin;
	}
	public String getLightthresholdMax() {
		return lightthresholdMax;
	}
	public void setLightthresholdMax(String lightthresholdMax) {
		this.lightthresholdMax = lightthresholdMax;
	}
	public String getTempthresholdMax() {
		return tempthresholdMax;
	}
	public void setTempthresholdMax(String tempthresholdMax) {
		this.tempthresholdMax = tempthresholdMax;
	}
	public String getLightthresholdMin() {
		return lightthresholdMin;
	}
	public void setLightthresholdMin(String lightthresholdMin) {
		this.lightthresholdMin = lightthresholdMin;
	}
	public String getTempthresholdMin() {
		return tempthresholdMin;
	}
	public void setTempthresholdMin(String tempthresholdMin) {
		this.tempthresholdMin = tempthresholdMin;
	}
	private String tempthresholdMax;
	private String lightthresholdMin;
	private String tempthresholdMin;
	public ThresholdValueSaver() {
		// TODO Auto-generated constructor stub
	}


}
