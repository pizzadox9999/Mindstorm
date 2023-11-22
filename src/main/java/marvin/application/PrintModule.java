package marvin.application;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class PrintModule implements Movable, Device, Finishable, Printhead {
    private LargeEngine printMotor;
    private MediumEngine pencilMotor;
    private Engine[] engines;
    private EV3TouchSensor touchSensor;

    private double wheelDiameter = 42f;
    private double wheelCircumference = 2 * 3.14f * (wheelDiameter / 2);
    private double gearRatio = 36 / 12;

    private boolean isUP = true;
    private double size = 0;
    private double position = 0;
    private boolean rotateWithoutHold = true;

    public PrintModule() {
        printMotor = new LargeEngine(MotorPort.C);
        pencilMotor = new MediumEngine(MotorPort.A);
        engines = new Engine[] { printMotor, pencilMotor };
        touchSensor = new EV3TouchSensor(SensorPort.S2);
    }

    @Override
    public void moveTo(double position) {
        moveTo(position, printMotor.getMaxSpeed());
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

        printMotor.setSpeed((float) velocity);
        if(rotateWithoutHold) {
            printMotor.rotateWithoutHold(- Math.round(Math.round(convertMMToDegree(newPosition))), false);
        } else {
            printMotor.rotate(- Math.round(Math.round(convertMMToDegree(newPosition))), false);
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
    
    public void rotateWithoutHold(boolean rotateWithouthold) {
    	this.rotateWithoutHold = rotateWithouthold;
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
        moveTo(size);
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
        
        System.out.println("printModuleSize: " + size);
    }
}
