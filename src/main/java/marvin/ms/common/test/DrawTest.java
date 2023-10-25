package marvin.ms.common.test;

import java.util.Arrays;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.internal.ev3.EV3Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

/**
 * This class tests the raw capability's of the hardware to draw.
 * 
 * @author pizzadox9999
 */

public class DrawTest implements Testable {

    @Override
    public void run() {
        movementTest();
        drawPoint();
        drawCircle();
    }

    public static <T> T[] toArray(T... toArray) {
        return toArray;
    }

    private void movementTest() {
        EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);

        motorA.synchronizeWith(toArray(motorB));
        motorA.startSynchronization();
        motorA.setAcceleration(2000);
        motorA.rotateTo(1, true);
        motorA.stop();
        motorA.setAcceleration(6000);
        motorB.forward();
        motorB.stop();
        motorA.endSynchronization();
    }

    private void drawPoint() {
        // not yet implementet
    }

    private void drawCircle() {
        // not yet implementet
    }
}
