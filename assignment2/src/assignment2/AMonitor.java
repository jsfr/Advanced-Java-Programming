package assignment2;

public class AMonitor implements Monitor, Runnable {

	@Override
	public int pushReading(SensorReading sensorInput) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void processReading(SensorReading sensorInput) {
		// TODO Auto-generated method stub
		//Note that you need to push computed discomfort levels to the registered
		//subscribers using the registerDiscomfortLevel method in Subscriber interface
		
	}

	@Override
	public int registerSubscriber(int discomfortLevel, Subscriber subscriber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SensorReading getSensorReading() {
		// TODO Auto-generated method stub
		return null;
	}

	public void run() {
		SensorReading sensorInput = null;
		while(true) {
			sensorInput = getSensorReading();
			this.processReading(sensorInput);
		}
	}

}
