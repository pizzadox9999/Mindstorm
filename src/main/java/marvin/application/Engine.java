package marvin.application;

import java.util.ArrayList;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.RegulatedMotor;

/**
 * The Engine class is used to provide additional functions the
 * BaseRegulatedMotor class is not supporting.
 * 
 * @author home
 *
 */
public class Engine extends BaseRegulatedMotor {
    private ArrayList<Engine> engines = new ArrayList<>();

    public Engine(Port port, MotorRegulator regulator, int typ, float moveP, float moveI, float moveD, float holdP,
            float holdI, float holdD, int offset, int maxSpeed) {
        super(port, regulator, typ, moveP, moveI, moveD, holdP, holdI, holdD, offset, maxSpeed);
    }

    public Engine(TachoMotorPort port, MotorRegulator regulator, int typ, float moveP, float moveI, float moveD,
            float holdP, float holdI, float holdD, int offset, int maxSpeed) {
        super(port, regulator, typ, moveP, moveI, moveD, holdP, holdI, holdD, offset, maxSpeed);
    }
    
    public Thread createEngineAction(final EngineAction engineAction) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                engineAction.doAction(Engine.this);
            }
        });
    }

    public void addSynchrozizedEngine(Engine engine) {
        engines.add(engine);
    }

    @Override
    public void startSynchronization() {
        if (!engines.isEmpty()) {
            synchronizeWith(engines.toArray(new RegulatedMotor[engines.size()]));
            engines.clear();
        }
        super.startSynchronization();
    }
    
    public void rotateWithoutHold(int angle, boolean immediateReturn) {
        rotateToWithoutHold(Math.round(reg.getPosition()) + angle, immediateReturn);
    }
    
    public void rotateToWithoutHold(int limitAngle, boolean immediateReturn) {
        reg.newMove(speed, acceleration, limitAngle, false, !immediateReturn);
    }

}
