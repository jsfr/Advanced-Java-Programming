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
        for (int i = 0; i < 1; i++) {
            monitors.add(new AMonitor());
            new Thread((Runnable)monitors.get(i)).start();
        }

        for (int i = 0; i < 1; i++) {
            sensors[i] = new ASensor();
            sensors[i].registerMonitor(monitors);
            new Thread(sensors[i]).start();

            subscribers[i] = new ASubscriber();
            monitors.get(i % 2).registerSubscriber(5, subscribers[i]);
            new Thread(subscribers[i]).start();
        }
    }
}
