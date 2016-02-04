package com.services;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beans.SensorDataBeanImpl;
import com.entity.SensorDataEntity;
import com.entity.ThresholdValueSaver;
import com.serial.ZigBeeModule;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class DataService {
	
	public static void startGetService(){
		Spark.get("/", new Route() {
			
			public Object handle(Request arg0, Response arg1) {
				
				SensorDataBeanImpl sdbi = SensorDataBeanImpl.getInstance();
				List<SensorDataEntity>  zb = sdbi.getSensorDataList();//.().get(sdbi.getSensorData("1").getSensordata().size() - 1);
				JSONObject data = new JSONObject();
				
				JSONArray arr = new JSONArray();
				
				for(SensorDataEntity a: zb){
					ZigBeeModule zig = a.getSensordata().get(a.getSensordata().size() -1);
					ThresholdValueSaver th = sdbi.getThresholdValue();
				 JSONObject obj = new JSONObject(zig);
				 
				try {
					obj.put("id", a.getId());
					obj.put("thresholdLightMax", new Integer(th.getLightthresholdMax()).intValue());
					obj.put("thresholdTempMax", new Integer(th.getTempthresholdMax()).intValue());
					obj.put("thresholdLightMin", new Integer(th.getLightthresholdMin()).intValue());
					obj.put("thresholdTempMin", new Integer(th.getTempthresholdMin()).intValue());
					System.out.println("////////////////"+ obj.toString());
					arr.put(obj);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				System.out.println("#################3: "+ arr.toString());
				try {
					data.put("data", arr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data.toString();
			}
		});
	}

}
