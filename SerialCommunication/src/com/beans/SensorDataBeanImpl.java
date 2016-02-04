package com.beans;

import java.net.UnknownHostException;
import java.util.List;

import com.entity.SensorDataEntity;
import com.entity.ThresholdValueSaver;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class SensorDataBeanImpl implements SensorDataBean {
	private static SensorDataBeanImpl instance = null;
	Mongo mongo;
	Morphia morph;
	Datastore ds;
	Datastore ds1;
	
	public SensorDataBeanImpl() {
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		morph = new Morphia();
		ds = morph.createDatastore(mongo, "sensornetwork", null, "".toCharArray());
		morph.map(SensorDataEntity.class).map(ThresholdValueSaver.class);
	}

	public void storeSensorData(SensorDataEntity sde) {
		ds.save(sde);
		
	}

	public List<SensorDataEntity> getSensorDataList() {
	return ds.createQuery(SensorDataEntity.class).asList();
		
	}
	public SensorDataEntity getSensorData(String id) {
		return ds.createQuery(SensorDataEntity.class).field("id").equal(id).get();
			
		}

	public ThresholdValueSaver getThresholdValue(){
		return ds.createQuery(ThresholdValueSaver.class).get();
	}
	public void storeThresholdValue(ThresholdValueSaver tvs){
		ds.save(tvs);
	}
	  
	   public static SensorDataBeanImpl getInstance() {
	      if(instance == null) {
	         instance = new SensorDataBeanImpl();
	      }
	      return instance;
	   }
	}


