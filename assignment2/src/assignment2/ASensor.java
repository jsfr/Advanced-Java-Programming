package assignment2;

import java.util.List;

public class ASensor implements Sensor, Runnable {
	List<Monitor> monitors; //A sensor can push readings to one or many monitors
	
	public SensorReading generateSensorReading() {
		// TODO Auto-generated method stub
		return null;
	}

	public void run() {
		SensorReading reading = null;
		while(true) {
			reading = this.generateSensorReading();
			for (Monitor sm : monitors) {
				sm.pushReading(reading);
			}
			//Thread.sleep() ??
		}
		
	}

	@Override
	public int registerMonitor(List<Monitor> sm) {
		// TODO Auto-generated method stub
		return 0;
	}

}
