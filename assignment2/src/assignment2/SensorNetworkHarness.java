package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorNetworkHarness {

    private static ASensor[] sensors = new ASensor[10];
    private static ASubscriber[] subscribers = new ASubscriber[10];
    private static List<Monitor> monitors = new ArrayList<Monitor>();

    /**
     * @param args
     */
    public static void main(String[] args) {
    	Random r = new Random();
    	switch(r.nextInt(3)) {
    		case 0:
    			System.out.println("Invoking network1");
    			network1();
    			break;
    		case 1:
    			System.out.println("Invoking network2");
    			network2();
    			break;
    		case 2:
    			System.out.println("Invoking network3");
    			network3();
    			break;
    	}
    }
    
    private static void network1() {
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
    
    private static void network2() {
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
    }
    
    private static void network3() {
    	
    	// 3 sensors, 2 monitors, 2 subscribers (Connected like in the diagram)
    	for (int i = 0; i < 2; i++) {
            monitors.add(new AMonitor());
            new Thread((Runnable)monitors.get(i)).start();
        }
        
        for (int i = 0; i < 3; i++) {
	        sensors[i] = new ASensor();
	        //sensors[i].registerMonitor(monitors);
	        new Thread(sensors[i]).start();
        }
        List<Monitor> m1 = new ArrayList<Monitor>();
        m1.add(monitors.get(0));
        List<Monitor> m2 = new ArrayList<Monitor>();
        m2.add(monitors.get(1));
        
        sensors[0].registerMonitor(m1);
        sensors[2].registerMonitor(m2); 
        sensors[1].registerMonitor(monitors);
        
        for (int i = 0; i < 2; i++) {
            subscribers[i] = new ASubscriber();
            
            //monitors.get(i % 5).registerSubscriber(5, subscribers[i]);
            new Thread(subscribers[i]).start();
        }
        
        monitors.get(0).registerSubscriber(4, subscribers[0]);
        monitors.get(1).registerSubscriber(4, subscribers[0]);
        monitors.get(1).registerSubscriber(5, subscribers[1]);
    }
}
