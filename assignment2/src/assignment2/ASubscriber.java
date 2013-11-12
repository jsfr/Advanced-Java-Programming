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
    	String name = Thread.currentThread().getName();
        switch (discomfortLevel) {
        case 1:
            System.out.println("Feeling so nice (^_-). Level 1, thread name: " + name);
            break;
        case 2:
            System.out.println("Still quite okay. Level 2, thread name: " + name);
            break;
        case 3:
            System.out.println("This is not nice. Level 3, thread name: " + name);
            break;
        case 4:
            System.out.println("It is uncomfortable. Level 4, thread name: " + name);
            break;
        case 5:
            System.out.println("It hate this planet! Level 5, thread name: " + name);
            break;
        default:
            System.out.println("Error:" + discomfortLevel + ", thread name: " + name);
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
