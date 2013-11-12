package assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AMonitor implements Monitor, Runnable {
	
	int sumHumidity = 0;
	int sumTemperature = 0;
	int numElements = 0;
	
	private Queue<SensorReading> readings = new LinkedList<SensorReading>();
	private Map<Integer, List<Subscriber>> ss = new HashMap<Integer, List<Subscriber>>();
	

	@Override
	public int pushReading(SensorReading sensorInput) {
		synchronized(this) {
			if(readings.size() < 50) {
				readings.add(sensorInput);
				notify();
			}
		}
		return 0;
	}

	@Override
	public void processReading(SensorReading sensorInput) {
		int avgHumidity = 0;
		int avgTemperature = 0;
		synchronized(this) {
			sumHumidity += sensorInput.getHumidity();
			sumTemperature += sensorInput.getTemperature();
			numElements++;
		
			avgHumidity = (int) Math.floor(sumHumidity/numElements);
			avgTemperature = (int) Math.floor(sumTemperature/numElements);
		}
		
		double discomfortLevel1 = avgTemperature/10;
		double discomfortLevel2 = (avgHumidity/10)-4;
		
		int discomfortLevel = (int) Math.floor(Math.min(5,Math.max(discomfortLevel1, discomfortLevel2)));
		
		for(int i = discomfortLevel; i < 6; i++) {
			for(Subscriber s : ss.get(i)) {
				s.pushDiscomfortWarning(discomfortLevel);
			}
		}
	}

	@Override
	public int registerSubscriber(int discomfortLevel, Subscriber subscriber) {
		if(ss.get(discomfortLevel).equals(null)){
			ss.put(discomfortLevel, new ArrayList<Subscriber>());
		}
		List<Subscriber> l = ss.get(discomfortLevel);
		l.add(subscriber);
		return 0;
	}

	@Override
	public SensorReading getSensorReading() {
		SensorReading sensorReading = null;
		
		synchronized(this) {
			while(sensorReading.equals(null)) {
				try {
					wait();
					sensorReading = readings.poll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//Now sensorReading is no longer null		
		return sensorReading;
	}

	public void run() {
		SensorReading sensorInput = null;
		while(true) {
			sensorInput = getSensorReading();
			this.processReading(sensorInput);
		}
	}

}
