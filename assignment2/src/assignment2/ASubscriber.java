package assignment2;

import java.util.LinkedList;
import java.util.Queue;

public class ASubscriber implements Subscriber, Runnable {

    Queue<Integer> readings = new LinkedList<Integer>();

    public void run() {
        while (true) {
            int discomfortLevel = this.getDiscomfortWarning();
            this.processDiscomfortWarning(discomfortLevel);
        }
    }

    @Override
    public void pushDiscomfortWarning(int discomfortlevel) {
        synchronized(this) {
            if (readings.size() < 50) {
                readings.add(Integer.valueOf(discomfortlevel));
                notify();
            }
        }
    }

    @Override
    public void processDiscomfortWarning(int discomfortLevel) {
        switch (discomfortLevel) {
        case 1:
            System.out.println("Feeling so nice (^_-)");
            break;
        case 2:
            System.out.println("Still quite okay.");
            break;
        case 3:
            System.out.println("This is not nice.");
            break;
        case 4:
            System.out.println("It is uncomfortable.");
            break;
        case 5:
            System.out.println("It hate this planet!");
            break;
        case 0:
            System.out.println("getDiscomfortWarning was interrupted while waiting.");
        default:
            System.out.println("Something weird and undefined happen with the weather (and the program).");
        }
    }

    @Override
    public int getDiscomfortWarning() {
        synchronized(this) {
            while (readings.peek() == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    return 0;
                }
            }
            return readings.poll().intValue();
        }
    }



}
