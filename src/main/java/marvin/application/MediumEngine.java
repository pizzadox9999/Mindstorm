package marvin.application;

import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class MediumEngine extends Engine {
    static final float MOVE_P = 8f;
    static final float MOVE_I = 0.04f;
    static final float MOVE_D = 8f;
    static final float HOLD_P = 8f;
    static final float HOLD_I = 0.02f;
    static final float HOLD_D = 0f;
    static final int OFFSET = 1000;
    
    private static final int MAX_SPEED = 260*360/60;

    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public MediumEngine(TachoMotorPort port)
    {
        super(port, null, EV3SensorConstants.TYPE_MINITACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET, MAX_SPEED);
    }
    
    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public MediumEngine(Port port)
    {
        super(port, null, EV3SensorConstants.TYPE_NEWTACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET, MAX_SPEED);
    }
}
