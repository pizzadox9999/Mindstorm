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
    
    private int standardAcceleration;
    private float standardSpeed;

    /**
     * paper size in mm
     */
    private double size;

    /**
     * position in mm
     */
    private double position = 0;

    private boolean isPaperEjected = false;

    public PaperModule() {
        paperMotor = new LargeEngine(MotorPort.B);
        paperSensor = new EV3ColorSensor(SensorPort.S1);

        engines = new Engine[] { paperMotor };
        
        standardAcceleration = paperMotor.getAcceleration();
        standardSpeed = paperMotor.getSpeed();
    }

    /**
     * @param position takes the position as mm
     */
    @Override
    public void moveTo(double position) {
        moveTo(position, paperMotor.getMaxSpeed());
    }

    public void moveTo(double position, double velocity) {
        moveTo(position, velocity, false);
    }

    public void moveTo(double position, double velocity, boolean returnImediate) {
        moveTo(position, velocity, paperMotor.getAcceleration(), returnImediate);
    }

    /**
     * Moves the paper module to the specified position.
     * @param position Takes the position in millimeter.
     */
    @Override
    public void moveTo(double position, double velocity, double acceleration, boolean returnImediate) {
        double wantedPosition = position;
        double currentPosition = this.position;
        double distance = wantedPosition - currentPosition;
        move(distance, velocity, acceleration, returnImediate);
    }
    
    public void moveMM(double distance, double velocity, double acceleration, boolean returnImediate) {
        move(distance, convertMMToDegree(velocity), convertMMToDegree(acceleration), returnImediate);
    }
    
    public void move(double distance, double velocity, double acceleration, boolean returnImediate) {
        paperMotor.setAcceleration(Math.round(Math.round(acceleration)));
        paperMotor.setSpeed(Math.round(velocity));
        move(distance, returnImediate);
    }
    
    /**
     * Moves the paper module about this distance.
     * @param distance In millimeter. 
     * @param returnImediate
     */
    public void move(double distance, boolean returnImediate) {
        double newPosition = position + distance;
        if (newPosition > size) {
            distance = size - position;
            System.out.println("Distance to big.");
        } else if (newPosition < 0) {
            distance = 0;
            System.out.println("Distance to small.");
        }
        
        int degrees = Math.round(Math.round(convertMMToDegree(distance)));
        if(1 > degrees && degrees > 0) {
            System.out.println("Degrees between 1 and 0.");
            degrees = 1;
        }
        
        paperMotor.rotate(degrees, returnImediate);

        this.position = newPosition;
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
        move(40, false);

        isPaperEjected = true;
    }
    
    public void setMaxSpeed() {
        paperMotor.setSpeed(paperMotor.getMaxSpeed());
    }
    
    public void resetSpeed() {
        paperMotor.setSpeed(standardSpeed);
    }
    
    public void resetAcceleration() {
        paperMotor.setAcceleration(standardAcceleration);
    }

    @Override
    public void finish() {
        ejectPaper();
    }
}
