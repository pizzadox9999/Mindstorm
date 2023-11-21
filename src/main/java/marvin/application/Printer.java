package marvin.application;

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
    
    /**
     * input parameter as millimeter
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
    
    public void drawLineSegment(double startX, double startY, double endX, double endY) {
        //x axis is the printMotor
        //y axis is the paperMotor
        
        Engine printMotor = printModule.getEngines()[0];
        Engine paperMotor = paperModule.getEngines()[0];
        
        //determine the motor with the biggest distance to the target
        //this motor should use the max speed available the other 
        //motors speed should be calculated from the travel time from 
        //the first motor
        
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
        
        if(xDistance > yDistance ) {
            engineLongDistance = printMotor;
            engineShortDinstance = paperMotor;
            longDistance = xDistance;
            shortDinstance = yDistance;
            deviceLongDistance = printModule;
            deviceShortDistance = paperModule;
            longDistancePosition = endX;
            shortDistancePosition = endY;
        }
        
        float engineLongDinstanceSpeed = engineLongDistance.getMaxSpeed();
        double engineLongDinstanceTime = deviceLongDistance.convertMMToDegree(longDistance) / engineLongDinstanceSpeed;
        
        //calculate the speed of the engine with the short distance
        float engineShortDinstanceSpeed = (float) (deviceShortDistance.convertMMToDegree(shortDinstance) / engineLongDinstanceTime);
        
        printMotor.addSynchrozizedEngine(paperMotor);
        printMotor.startSynchronization();
        
        ((Movable) deviceLongDistance).moveTo(longDistancePosition, engineLongDinstanceSpeed);
        ((Movable) deviceShortDistance).moveTo(shortDistancePosition, engineShortDinstanceSpeed);
        
        printMotor.endSynchronization();
        
        printMotor.waitComplete();
        paperMotor.waitComplete();
    }
}
