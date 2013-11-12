package assignment2;

import java.util.ArrayList;
import java.util.List;

public class SensorNetworkHarness {

    private static ASensor[] sensors = new ASensor[10];
    private static ASubscriber[] subscribers = new ASubscriber[10];
    private static List<Monitor> monitors = new ArrayList<Monitor>();

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	/*
    	// 1 sensor, 5 monitors, 10 subscribers (all should post same output)
        for (int i = 0; i < 5; i++) {
            monitors.add(new AMonitor());
            new Thread((Runnable)monitors.get(i)).start();
        }
        
        for (int i = 0; i < 1; i++) {
	        sensors[i] = new ASensor();
	        sensors[i].registerMonitor(monitors);
	        new Thread(sensors[i]).start();
        }
        
        for (int i = 0; i < 10; i++) {
            subscribers[i] = new ASubscriber();
            monitors.get(i % 5).registerSubscriber(5, subscribers[i]);
            new Thread(subscribers[i]).start();
        }
        */
        
        // 2 sensors 1 monitor, 2 subscriber (Both should post the same, but two post each time)
        for (int i = 0; i < 1; i++) {
            monitors.add(new AMonitor());
            new Thread((Runnable)monitors.get(i)).start();
        }
        
        for (int i = 0; i < 2; i++) {
	        sensors[i] = new ASensor();
	        sensors[i].registerMonitor(monitors);
	        new Thread(sensors[i]).start();
        }
        
        for (int i = 0; i < 2; i++) {
            subscribers[i] = new ASubscriber();
            monitors.get(0).registerSubscriber(5, subscribers[i]);
            new Thread(subscribers[i]).start();
        }
    }
}
