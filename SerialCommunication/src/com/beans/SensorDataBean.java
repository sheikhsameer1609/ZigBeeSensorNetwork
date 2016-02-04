package com.beans;

import java.util.List;

import com.entity.SensorDataEntity;
import com.entity.ThresholdValueSaver;

public interface SensorDataBean {

	public void storeSensorData(SensorDataEntity sde);
		public List<SensorDataEntity> getSensorDataList();
		public SensorDataEntity getSensorData(String id) ;
		public ThresholdValueSaver getThresholdValue();
		public void storeThresholdValue(ThresholdValueSaver tvs);

}
