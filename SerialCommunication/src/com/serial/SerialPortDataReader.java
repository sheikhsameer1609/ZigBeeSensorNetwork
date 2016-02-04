package com.serial;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.text.DateFormatter;

import com.boundary.DataHistoryBoundary;
import com.services.DataService;

public class SerialPortDataReader implements Runnable, SerialPortEventListener {
	
	static CommPortIdentifier portId;
	static Enumeration portList;
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;

	public void startSerial() {
		DataService.startGetService();
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals("COM3")) {
					// if (portId.getName().equals("/dev/term/a")) {

					try {
						serialPort = (SerialPort) portId.open("SimpleReadApp",
								2000);
					} catch (PortInUseException e) {
						System.out.println(e);
					}
					try {
						inputStream = serialPort.getInputStream();
					} catch (IOException e) {
						System.out.println(e);
					}
					try {
						serialPort.addEventListener(this);
					} catch (TooManyListenersException e) {
						System.out.println(e);
					}
					serialPort.notifyOnDataAvailable(true);
					try {
						serialPort.setSerialPortParams(38400,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
					} catch (UnsupportedCommOperationException e) {
						System.out.println(e);
					}
					readThread = new Thread(this);
					readThread.start();

				}
			}
		}
	}

	public SerialPortDataReader() {
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] readBuffer = new byte[100];
			byte[] read = new byte[4];
			byte[] readtem = new byte[4];			
			Byte readID;
			int numBytes = 0;
			
			try {
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);

				}
				read[0] = readBuffer[39];
				read[1] = readBuffer[40];
				read[2] = readBuffer[41];
				read[3] = readBuffer[42];
				readtem[0] = readBuffer[35];
				readtem[1] = readBuffer[36];
				readtem[2] = readBuffer[37];
				readtem[3] = readBuffer[38];
				readID = readBuffer[4];

				ByteBuffer buffer = ByteBuffer.wrap(read);
				buffer.order(ByteOrder.LITTLE_ENDIAN);

				ByteBuffer buffertemp = ByteBuffer.wrap(readtem);
				buffertemp.order(ByteOrder.LITTLE_ENDIAN);
				/*setZigbeeId(readID.intValue());
				
				setNewdata(true);*/
				
				/*setTemp(buffertemp.getShort());
				setLight(buffer.getShort());
				
				*/
				
				String l = String.valueOf(buffer.getShort());
				
				String t = String.valueOf(buffertemp.getShort());
				String id1 = String.valueOf(readID.intValue());
				
				SimpleDateFormat m = new SimpleDateFormat("hh:mm:ss");
				GregorianCalendar x =(GregorianCalendar) GregorianCalendar.getInstance();
				//String curenttime = m.format();
				
				addUpdateZigBeeModule(id1, l, t,new Date(x.getTimeInMillis()));
				
				//System.out.println("Id :" + id1 + ". Light: " + l + ". Temp:" + t );
							
				
			} catch (IOException e) {
				System.out.println(e);
			}
			break;
		}
	}

	private void addUpdateZigBeeModule(String zigbeemoduleid, String light2, String temp2, Date curenttime) {
		ZigBeeModule zigbeemodule;
		if (!ZigBeeModuleCollector.isPresent(zigbeemoduleid)) {
			 zigbeemodule = new ZigBeeModule(light2, temp2,curenttime);
			ZigBeeModuleCollector.addModule(zigbeemoduleid, zigbeemodule);
		} else {
			zigbeemodule = ZigBeeModuleCollector
					.getZigbeeModule(zigbeemoduleid);
			if (zigbeemodule != null) {
				zigbeemodule.setLightdata(light2);
				zigbeemodule.setTemperaturedata(temp2);
				zigbeemodule.setNewdata(true);
				zigbeemodule.setCurrenttime(curenttime);
			}
		}
		
		DataHistoryBoundary.saveToDatabase(zigbeemodule,zigbeemoduleid);
	}
}
