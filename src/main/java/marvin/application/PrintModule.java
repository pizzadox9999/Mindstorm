package marvin.application;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

public class PrintModule implements Movable, Device, Finishable, Printhead {
    private LargeEngine printMotor;
    private MediumEngine pencilMotor;
    private Engine[] engines;
    private EV3TouchSensor touchSensor;

    private double wheelDiameter = 36.7;
    private double wheelCircumference = 2 * Math.PI * (wheelDiameter / 2);
    private double gearRatio = 36 / 12;

    private boolean isUP = true;
    private double size = 0;
    private double position = 0;
    
    private int standardAcceleration;
    private float standardSpeed;
    
    public PrintModule() {
        printMotor = new LargeEngine(MotorPort.C);
        pencilMotor = new MediumEngine(MotorPort.A);
        engines = new Engine[] { printMotor, pencilMotor };
        touchSensor = new EV3TouchSensor(SensorPort.S2);
        
        standardAcceleration = printMotor.getAcceleration();
        standardSpeed = printMotor.getSpeed();
    }
    
    public void calibrate() {
        float originalSpeed = printMotor.getSpeed();
        
        float speed = printMotor.getMaxSpeed();
        printMotor.setSpeed(speed);

        printMotor.forward();
        touchSensor.setCurrentMode(0);
        
        float[] oneSample = new float[1];
        touchSensor.getTouchMode().fetchSample(oneSample, 0);
        while (oneSample[0] == 0) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            touchSensor.getTouchMode().fetchSample(oneSample, 0);
        }        
        printMotor.stop();
        
        printMotor.setSpeed(originalSpeed);
        double originalPosition = position;
        position = 0;
        moveTo(originalPosition);
    }

    /**
     * @param position takes the position as mm
     */
    @Override
    public void moveTo(double position) {
        moveTo(position, printMotor.getMaxSpeed());
    }

    public void moveTo(double position, double velocity) {
        moveTo(position, velocity, false);
    }

    public void moveTo(double position, double velocity, boolean returnImediate) {
        moveTo(position, velocity, printMotor.getAcceleration(), returnImediate);
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
        printMotor.setAcceleration(Math.round(Math.round(acceleration)));
        printMotor.setSpeed(Math.round(velocity));
        move(distance, returnImediate);
    }
    
    public int degreeCorrection = 0;
    
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
            System.out.println("Distance to small.");
            distance = 0;
        }
        
        int degrees = Math.round(Math.round(convertMMToDegree(distance)));
        if(1 > degrees && degrees > 0) {
            System.out.println("Degrees between 1 and 0.");
            degrees = 1;
        }
        
        printMotor.rotate(- (degrees + degreeCorrection), returnImediate);

        this.position = newPosition;
    }

    @Override
    public double getSize() {
        return size;
    }

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

    @Override
    public void print(boolean print) {
        if (print && isUP) {
            pencilMotor.rotateTo(90);
            isUP = false;
        } else if (!isUP) {
            pencilMotor.rotate(-90);
            isUP = true;
        }
    }

    @Override
    public void finish() {
        print(false);
        calibrate();
        moveTo(size);
    }
    
    public void setMaxSpeed() {
        printMotor.setSpeed(printMotor.getMaxSpeed());
    }
    
    public void resetSpeed() {
        printMotor.setSpeed(standardSpeed);
    }
    
    public void resetAcceleration() {
        printMotor.setAcceleration(standardAcceleration);
    }

    @Override
    public void setup() {
        pencilMotor.setSpeed(pencilMotor.getMaxSpeed());
        float speed = printMotor.getMaxSpeed();
        printMotor.setSpeed(speed);

        printMotor.forward();
        touchSensor.setCurrentMode(0);
        
        double startTime = System.currentTimeMillis();
        
        float[] oneSample = new float[1];
        touchSensor.getTouchMode().fetchSample(oneSample, 0);
        while (oneSample[0] == 0) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            touchSensor.getTouchMode().fetchSample(oneSample, 0);
        }
        double endTime = System.currentTimeMillis();
        printMotor.stop();

        double timeInMillis = endTime - startTime;
        double time = timeInMillis / 1000;
        
        double sizeInDegree = speed * time;
        size = convertDegreeToMM(sizeInDegree);
    }
}
