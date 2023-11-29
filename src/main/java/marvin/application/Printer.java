package marvin.application;

import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

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

    public void rotateWithoutHold(boolean rotateWithouthold) {
        printModule.rotateWithoutHold(rotateWithouthold);
        paperModule.rotateWithoutHold(rotateWithouthold);
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

    public double[] calcPointOnCircle(double radius, double angle) {
        double x = radius * Math.sin(Math.PI * 2 * angle / 360);
        double y = radius * Math.cos(Math.PI * 2 * angle / 360);

        if (Math.abs(x) == 1)
            y = 0;
        if (Math.abs(y) == 1)
            x = 0;

        return new double[] { x + printModule.getPosition(), y + paperModule.getPosition() };
    }
    
    /**
     * 
     * @param radius in mm
     */
    public void drawCircle(double radius) {
    	Engine printMotor = printModule.getEngines()[0];
        Engine paperMotor = paperModule.getEngines()[0];
        
        
    	paperModule.moveTo(radius * 2, paperMotor.getMaxSpeed()/2);
    	
    	
    	printMotor.forward();
    	printMotor.flt();
    	
    	printMotor.setAcceleration(100);
    	printModule.moveTo(radius, printMotor.getMaxSpeed());
    	
    	
    	
    	
    	
        int segment = 1;
        //moveToPosition(printModule.getPosition(), paperModule.getPosition() + radius);

        printModule.print(true);

        
        
        double segmentLength = (2 * Math.PI * radius) / 8;
        
        // first eighth
        double[] position = calcPointOnCircle(radius, 45 * segment);
        
        //velocity in deg per second
        final float velocityPrintMotor = printMotor.getMaxSpeed() / 2;

        final double timePrintMotor = printModule.convertMMToDegree(position[0] - printMotor.getPosition()) / velocityPrintMotor;
        
        final double velocityPaperMotor = paperModule.convertMMToDegree(position[1] - paperMotor.getPosition()) / timePrintMotor;
        final double accelerationPaperMotor = velocityPaperMotor / timePrintMotor;
        
        System.out.println(
                "currentX: " + printModule.getPosition() + " currentY: " + paperModule.getPosition()
                + "\npositionX: " + position[0] + " positionY: " + position[1]
                + "\nvelocityPrintMotor: " + velocityPrintMotor + " accelerationPrintMotor: " + printMotor.getAcceleration() + " timePrintMotor: " + timePrintMotor
                + "\nvelocityPaperMotor: " + velocityPaperMotor + " accelerationPaperMotor: " + accelerationPaperMotor       + " timePaperMotor: " + ((paperModule.convertMMToDegree(paperMotor.getPosition() - position[1]) / velocityPaperMotor)));
        
        startAndWait(printMotor.createEngineAction(new EngineAction() {
            @Override
            public void doAction(Engine engineInAction) {
                System.out.println("action 1 started");
                engineInAction.setSpeed(velocityPrintMotor);
                
                engineInAction.forward();
                
                Marvin.waitFor(timePrintMotor);
                engineInAction.stop();
                
                System.out.println("action 1 ended");
            }
        }), paperMotor.createEngineAction(new EngineAction() {
            @Override
            public void doAction(Engine engineInAction) {
                System.out.println("action 2 started");
                engineInAction.setSpeed(Math.round(velocityPaperMotor));
                engineInAction.setAcceleration(Math.round(Math.round(accelerationPaperMotor)));
                
                engineInAction.forward();
                
                Marvin.waitFor(timePrintMotor);
                engineInAction.stop();
                System.out.println("action 2 ended");
            }
        }));

        printModule.print(false);
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
    }

    public void startLineSegment() {
        printModule.print(true);
    }

    public void endLineSegment() {
        printModule.print(false);
    }

    public void drawContinousLineSegment(double startX, double startY, double endX, double endY)
            throws InterruptedException {
        Engine printMotor = printModule.getEngines()[0];
        Engine paperMotor = paperModule.getEngines()[0];

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

        float engineLongDinstanceSpeed = engineLongDistance.getMaxSpeed() / 8;
        double engineLongDinstanceTime = deviceLongDistance.convertMMToDegree(longDistance) / engineLongDinstanceSpeed;

        // calculate the speed of the engine with the short distance
        float engineShortDinstanceSpeed = (float) (deviceShortDistance.convertMMToDegree(shortDinstance)
                / engineLongDinstanceTime);

        printMotor.addSynchrozizedEngine(paperMotor);
        printMotor.startSynchronization();

        ((Movable) deviceLongDistance).moveTo(longDistancePosition, engineLongDinstanceSpeed);
        ((Movable) deviceShortDistance).moveTo(shortDistancePosition, engineShortDinstanceSpeed);

        printMotor.endSynchronization();

        printMotor.waitComplete();
        paperMotor.waitComplete();
    }
    
    public void drawCircleVer1() {
    	MovePilot movePilot = new MovePilot(null);
    }
}
