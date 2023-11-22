package marvin.application;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class PaperModule implements Movable, Device, Finishable {
    private LargeEngine paperMotor;
    private Engine[] engines = null;
    private EV3ColorSensor paperSensor;

    private double wheelDiameter = 43.2f;
    private double wheelCircumference = 2 * 3.14f * (wheelDiameter / 2);
    private double gearRatio = 36 / 12;

    /**
     * paper size in mm
     */
    private double size;

    /**
     * position in mm
     */
    private double position = 0;

    private boolean isPaperEjected = false;
    private boolean rotateWithoutHold = true;

    public PaperModule() {
        paperMotor = new LargeEngine(MotorPort.B);
        paperSensor = new EV3ColorSensor(SensorPort.S1);

        engines = new Engine[] { paperMotor };
    }

    /**
     * @param position takes the position as mm
     */
    @Override
    public void moveTo(double position) {
        moveTo(position, paperMotor.getMaxSpeed());
    }
    
    public void rotateWithoutHold(boolean rotateWithouthold) {
    	this.rotateWithoutHold = rotateWithouthold;
    }

    /**
     * @param position takes the position as mm
     */
    @Override
    public void moveTo(double position, double velocity) {
    	if (position > size)
            position = size;
        if (position < 0)
            position = 0;
        
        double wantedPosition = position;
        double currentPosition = this.position;
        double newPosition = wantedPosition - currentPosition;

        paperMotor.setSpeed((float) velocity);
        if (rotateWithoutHold) {
            paperMotor.rotateWithoutHold((int) Math.round(convertMMToDegree(newPosition)), false);
        } else {
            paperMotor.rotate((int) Math.round(convertMMToDegree(newPosition)), false);
        }
        
        this.position = position;
    }
    
    public void movePosition(double position) {
    	if (position > size)
            position = size;
        if (position < 0)
            position = 0;
        
        this.position = position;
    }

    @Override
    public void setup() {
        paperSensor.setCurrentMode(1); // one should be good
        float[] colorData = new float[1];

        // wait for paper to be inserted
        paperSensor.fetchSample(colorData, 0);
        while (colorData[0] < .3) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paperSensor.getRedMode().fetchSample(colorData, 0);
        }

        // wait extra second that paper is properly inserted by the user
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
        }

        float speed = paperMotor.getMaxSpeed();
        paperMotor.setSpeed(speed);

        // offset paper because off printerModule
        paperMotor.rotate((int) Math.round(convertMMToDegree(65)), false);

        // paper is inserted
        long startTime = System.currentTimeMillis();
        paperMotor.forward();

        // wait for paper to be "read"
        paperSensor.fetchSample(colorData, 0);
        while (colorData[0] > .3) {
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paperSensor.getRedMode().fetchSample(colorData, 0);
        }
        paperMotor.stop();
        long endTime = System.currentTimeMillis();

        // calculate paper size
        float timeInMillis = endTime - startTime;
        float time = timeInMillis / 1000;
        double angle = speed * time;
        size = convertDegreeToMM(angle);

        // after setup position size = position
        position = size;

        moveTo(0);
    }

    /**
     * size in mm
     */
    @Override
    public double getSize() {
        return size;
    }

    /**
     * position in mm
     */
    @Override
    public double getPosition() {
        return position;
    }

    @Override
    public double convertDegreeToMM(double degree) {
        return degree / gearRatio / 360 * wheelCircumference;
    }

    @Override
    public double convertMMToDegree(double milliMeter) {

        return (milliMeter * gearRatio * 360) / wheelCircumference;
    }

    @Override
    public Engine[] getEngines() {
        return engines;
    }

    public void ejectPaper() {
        if (isPaperEjected)
            return;
        moveTo(size);
        paperMotor.forward();
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
        }

        isPaperEjected = true;
    }

    @Override
    public void finish() {
        ejectPaper();
    }
}
