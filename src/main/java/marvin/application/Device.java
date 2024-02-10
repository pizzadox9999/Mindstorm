package marvin.application;

import marvin.application.engine.Engine;

public interface Device {
    public void setup();
    public double getSize();
    public double getPosition();
    public double convertDegreeToMM(double degree);
    public double convertMMToDegree(double milliMeter);
    public Engine[] getEngines();
}
