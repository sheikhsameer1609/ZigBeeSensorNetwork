package com.serial;

import java.util.HashMap;
import java.util.Map;

import com.serial.*;

public class ZigBeeModuleCollector {
	private static Map<String, ZigBeeModule> zigbeemap = new HashMap<String, ZigBeeModule>();

	public static Map<String, ZigBeeModule> getZigbeemap() {
		return zigbeemap;
	}

	public static void setZigbeemap(Map<String, ZigBeeModule> zigbeemap) {
		ZigBeeModuleCollector.zigbeemap = zigbeemap;
	}

	public static void addModule(String id, ZigBeeModule zm) {
		zigbeemap.put(id, zm);
	}

	public ZigBeeModuleCollector() {

	}

	public static Boolean isPresent(String zigbeemoduleid) {

		return getZigbeemap().containsKey(zigbeemoduleid);

	}

	public static ZigBeeModule getZigbeeModule(String zigbeemoduleid) {
		ZigBeeModule zigbee = null;
		if (isPresent(zigbeemoduleid)) {
			zigbee = zigbeemap.get(zigbeemoduleid);
		}
		return zigbee;
	}

}
