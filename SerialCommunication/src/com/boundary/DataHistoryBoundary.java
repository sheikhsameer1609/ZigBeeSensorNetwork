package com.boundary;

import java.util.ArrayList;
import java.util.List;

import com.beans.SensorDataBean;
import com.beans.SensorDataBeanImpl;
import com.entity.SensorDataEntity;
import com.entity.ThresholdValueSaver;
import com.example.javawwo.WwoClient;
import com.serial.ZigBeeModule;

public class DataHistoryBoundary {
private static SensorDataBean sdbean = new SensorDataBeanImpl();
	public DataHistoryBoundary() {
		
	}
	
	public static void saveToDatabase(ZigBeeModule zbm,String zigbeeid){
		WwoClient wwoclient = new WwoClient();
		zbm.setHumidity(wwoclient.getHumidity());
		SensorDataEntity sde =sdbean.getSensorData(zigbeeid);
		if(sde !=null)
		sde.addSensorData(zbm);
		else{
			List<ZigBeeModule> zbmlist= new ArrayList<ZigBeeModule>();
			zbmlist.add(zbm);
			sde = new SensorDataEntity(zigbeeid,zbmlist);
		}
			
		sdbean.storeSensorData(sde);
		
		
	}
	public static ThresholdValueSaver getThresholdValue(){
		return sdbean.getThresholdValue();
	}
	
	public static void saveThresholdToDatabase(String lightMax,String tempMax, String lightMin,String tempMin){
		sdbean.storeThresholdValue(new ThresholdValueSaver(lightMax,tempMax,lightMin,tempMin));
	}

}
