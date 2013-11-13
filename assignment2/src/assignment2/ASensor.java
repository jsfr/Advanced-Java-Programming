package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ASensor implements Sensor, Runnable {
    List<Monitor> monitors = new ArrayList<Monitor>(); //A sensor can push readings to one or many monitors

    public SensorReading generateSensorReading() {
        SensorReading sr = new SensorReading();
        Random rnd = new Random();
        double a = Math.min(1, Math.abs(rnd.nextGaussian()));
        double b = Math.min(1, Math.abs(rnd.nextGaussian()));
        Float temp = (float) a*50+10; //Gausian normal distribution between 10 and 60
        Float hum = (float) b*50+50;
        sr.setHumidity(hum);
        sr.setTemperature(temp);
        //System.out.println("Humidity: " + hum + "(" + b + "), Temp: " + temp + "(" + b + ")");
        return sr;
    }

    public void run() {
        SensorReading reading = null;
        while(true) {
            reading = this.generateSensorReading();
            synchronized(this){
                for (Monitor sm : monitors) {
                    sm.pushReading(reading);
                }
            }
            try {
                Thread.sleep(500); //sleep for 2 sec.
            } catch (InterruptedException e) {
                return; //just die
            }
        }

    }

    @Override
    public int registerMonitor(List<Monitor> sm) {
        synchronized(this){
            this.monitors.addAll(sm);
        }
        return 0;
    }

}
