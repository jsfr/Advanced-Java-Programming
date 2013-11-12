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
            System.out.println("Feeling so nice (^_-). Level 1");
            break;
        case 2:
            System.out.println("Still quite okay. Level 2");
            break;
        case 3:
            System.out.println("This is not nice. Level 3");
            break;
        case 4:
            System.out.println("It is uncomfortable. Level 4");
            break;
        case 5:
            System.out.println("It hate this planet! Level 5");
            break;
        default:
            System.out.println("Error:" + discomfortLevel);
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
