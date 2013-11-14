package assignment4.dataflow;

import assignment4.processors.*;

public class GenericDataflowHarness {
    public static void main(String[] args) {
        Node<StartSignal, SensorReading> sensor1 = new Node<StartSignal, SensorReading>(new SensorReadingGenerator());
        Node<StartSignal, SensorReadingExtended> sensor2 = new Node<StartSignal, SensorReadingExtended>(new SensorReadingExtendedGenerator());

        Node<? extends SensorReading, DiscomfortWarning> monitor1 = new Node<>(new DiscomfortProcessor());
        sensor1.subscribe(monitor1);
        sensor2.subscribe(monitor1);

        Node<DiscomfortWarning, Object> subscriber1 = new Node<DiscomfortWarning, Object>(new PrinterProcessor<DiscomfortWarning>("Printer 1"));

        sensor1.start();
        monitor1.start();
        subscriber1.start();
        sensor1.push(StartSignal.go);
    }
}
