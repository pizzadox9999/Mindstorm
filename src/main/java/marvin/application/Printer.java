package marvin.application;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Printer implements Finishable {
    private PaperModule paperModule;
    private PrintModule printModule;

    private boolean isSynchronized = false;

    public Printer() {
        paperModule = new PaperModule();
        printModule = new PrintModule();
        paperModule.setup();
        printModule.setup();
    }

    public void moveToPosition(double x, double y) {
        printModule.moveTo(x);
        paperModule.moveTo(y);
    }

    @Override
    public void finish() {
        paperModule.finish();
        printModule.finish();
    }

    public void drawBezier() {

    }

    public static double[] calcPointOnCircle(double radius, double angle) {
        return calcPointOnCircle(radius, angle, new double[] { 0, 0 });
    }

    public static double[] calcPointOnCircle(double radius, double angle, double[] offset) {
        double x = radius * Math.sin(Math.PI * 2 * angle / 360);
        double y = radius * Math.cos(Math.PI * 2 * angle / 360);

        if (Math.abs(x) == 1)
            y = 0;
        if (Math.abs(y) == 1)
            x = 0;

        return new double[] { x + offset[0], y + offset[1] };
    }

    /**
     * 
     * @param radius in mm
     */
    public void drawCircle(double radius) {
        printModule.print(true);

        Engine printMotor = printModule.getEngines()[0];
        Engine paperMotor = paperModule.getEngines()[0];

        float printSpeed = printMotor.getMaxSpeed() / 5, xSpeed = printSpeed;
        
        long printTime = Math.round(printModule.convertMMToDegree(radius) / printSpeed * 1000);
        
        printModule.move(radius, xSpeed, 6000, true);
        
        paperMotor.setAcceleration(6000);
        paperMotor.setSpeed(0);
        paperMotor.forward();
        
        long startTime = System.currentTimeMillis();
        long endTime = startTime + printTime;
        
        double currentYPosition = radius;
        
        while (System.currentTimeMillis() < endTime){
            long elapsedTime = System.currentTimeMillis() - startTime;
            double elapsedTimeInSeconds = elapsedTime / 1000;
            
            double xPosition = paperModule.convertDegreeToMM(xSpeed * elapsedTimeInSeconds );
            
            double omega = Math.acos(xPosition / radius);
            double yPosition = radius * Math.sin(omega);
            
            double yDistance = currentYPosition - yPosition;
            currentYPosition = currentYPosition - yDistance;
            double ySpeed = paperModule.convertMMToDegree(yDistance)  / elapsedTimeInSeconds;
            
            paperMotor.setSpeed(Math.round(ySpeed));
            paperMotor.forward();
        }
        
        paperMotor.stop();
        

        printModule.print(false);

        resetAcceleration();
        resetSpeed();
    }

    private static void startAndWait(Thread... threads) {
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * input parameter as millimeter
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void drawLine(double startX, double startY, double endX, double endY) {
        moveToPosition(startX, startY);
        startLineSegment();
        drawLineSegment(startX, startY, endX, endY);
        endLineSegment();
        resetAcceleration();
        resetSpeed();
    }

    public void startLineSegment() {
        printModule.print(true);
    }

    public void endLineSegment() {
        printModule.print(false);
    }

    public void drawLineSegment(double startX, double startY, double endX, double endY) {
        // x axis is the printMotor
        // y axis is the paperMotor

        Engine printMotor = printModule.getEngines()[0];
        Engine paperMotor = paperModule.getEngines()[0];

        printMotor.setAcceleration(450);
        paperMotor.setAcceleration(450);

        // determine the motor with the biggest distance to the target
        // this motor should use the max speed available the other
        // motors speed should be calculated from the travel time from
        // the first motor

        double xDistance = endX - startX;
        double yDistance = endY - startY;

        double longDistance = yDistance;
        double shortDinstance = xDistance;

        double longDistancePosition = endY;
        double shortDistancePosition = endX;

        Engine engineLongDistance = paperMotor;
        Engine engineShortDinstance = printMotor;
        Device deviceLongDistance = paperModule;
        Device deviceShortDistance = printModule;

        if (xDistance > yDistance) {
            engineLongDistance = printMotor;
            engineShortDinstance = paperMotor;
            longDistance = xDistance;
            shortDinstance = yDistance;
            deviceLongDistance = printModule;
            deviceShortDistance = paperModule;
            longDistancePosition = endX;
            shortDistancePosition = endY;
        }

        float engineLongDinstanceSpeed = engineLongDistance.getMaxSpeed() / 3;
        double engineLongDinstanceTime = deviceLongDistance.convertMMToDegree(longDistance) / engineLongDinstanceSpeed;

        // calculate the speed of the engine with the short distance
        float engineShortDinstanceSpeed = (float) (deviceShortDistance.convertMMToDegree(shortDinstance)
                / engineLongDinstanceTime);

        printMotor.addSynchronizedEngine(paperMotor);
        printMotor.startSynchronization();

        ((Movable) deviceLongDistance).moveTo(longDistancePosition, engineLongDinstanceSpeed);
        ((Movable) deviceShortDistance).moveTo(shortDistancePosition, engineShortDinstanceSpeed);

        printMotor.endSynchronization();

        printMotor.waitComplete();
        paperMotor.waitComplete();
    }

    public double[] getPosition() {
        return new double[] { printModule.getPosition(), paperModule.getPosition() };
    }

    public void setMaxSpeed() {
        paperModule.setMaxSpeed();
        printModule.setMaxSpeed();
    }

    public void resetSpeed() {
        paperModule.resetSpeed();
        printModule.resetSpeed();
    }

    public void resetAcceleration() {
        paperModule.resetAcceleration();
        printModule.resetAcceleration();
    }
}
