package marvin.application;

import java.util.Arrays;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.internal.ev3.EV3Battery;
import lejos.robotics.RegulatedMotor;

public class Marvin {
	static EV3LargeRegulatedMotor motorPaperMove;
	static EV3LargeRegulatedMotor motorPrintheadMove;
	public static void test() {
    	System.out.println("test");
    	motorPaperMove = new EV3LargeRegulatedMotor(MotorPort.A);
    	motorPrintheadMove = new EV3LargeRegulatedMotor(MotorPort.B);
    	
    	final EV3TouchSensor sensorPrinthead = new EV3TouchSensor(SensorPort.S2);
    	final EV3ColorSensor sensorPaper = new EV3ColorSensor(SensorPort.S1);
    	
    	final EV3Battery battery = new EV3Battery();
    	
    	
    	//getPaper
    	Runnable getPaper = new Runnable() {
			
			@Override
			public void run() {
				motorPaperMove.setSpeed(100 * battery.getVoltage());
		    	motorPaperMove.backward();
		    	
		    	sensorPaper.setCurrentMode(1); //1 one should be "red" mode
		    	
		    	float[] oneSample = new float[1];
		    	sensorPaper.getRedMode().fetchSample(oneSample, 0);
		    	while (oneSample[0] < .55) {
		    		try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    		sensorPaper.getRedMode().fetchSample(oneSample, 0);
		    		System.out.println("running sampling");
		    	}
		    	motorPaperMove.stop();
		    	System.out.println("motor stop");
		    	motorPaperMove.rotate(180);
			}
		};
    	
    	//get printhead
    	Runnable getPrinthead = new Runnable() {
			@Override
			public void run() {
				motorPrintheadMove.setSpeed(100 * battery.getVoltage());
		    	motorPrintheadMove.forward();
		    	
		    	sensorPrinthead.setCurrentMode(0);
		    	
		    	float[] oneSample = new float[1];
		    	sensorPrinthead.getTouchMode().fetchSample(oneSample, 0);
		    	while (oneSample[0] == 0) {
		    		try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    		sensorPrinthead.getTouchMode().fetchSample(oneSample, 0);
		    		System.out.println("running sampling");
		    	}
		    	motorPrintheadMove.stop();
			}
		};
		Thread thread1 = new Thread(getPaper);
		Thread thread2 = new Thread(getPrinthead);
		thread1.start();
		thread2.start();
		try {
			thread1.join();
			thread2.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		moveLine(100, 100, 200, 200);
	}
	
	static float umfangGroßesRad = (float) ((43.2f) * Math.PI);
	
	static int speed = 1;
	
	public static void moveLine(float x1, float y1, float x2, float y2) {
		startAndJoinThread(moveX(x1 - printHeadX, speed, -1), moveY(y1 - printHeadY, speed, -1));
		
		int dirX = -1;
		int dirY = -1;
		if(x2 - x1 > 0) dirX = 1;
		if(y2 - y1 > 0) dirY = 1;
		
		moveX(x2, speed, dirX).start();;
		moveY(y2, speed, dirY).start();;
		
	}
	
	public static void startAndJoinThread(Thread... threads) {
		for(Thread t : threads) {
			t.start();
		}
		for(Thread t : threads) {
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static float printHeadX = 0;
	static float printHeadY = 0;
	
	public static Thread moveX(final float distanceInMM, final int mmPerSec, int dir) {
		printHeadX = distanceInMM;
		return move(Math.round(distanceInMM), mmPerSec, motorPrintheadMove, dir);
	}
	
	public static Thread moveY(final float distanceInMM, final int mmPerSec, int dir) {
		printHeadY = distanceInMM;
		return move(Math.round(distanceInMM), mmPerSec, motorPaperMove, dir);
	}
	
	public static Thread move(final int distanceInMM, final int mmPerSec, final RegulatedMotor motor) {
		return move(distanceInMM, mmPerSec, motor, -1);
	}
	
	public static Thread move(final int distanceInMM, final int mmPerSec, final RegulatedMotor motor, final int dir) {
		// v = t/s
		// s = t/v
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("umfangGroßesRad: " + umfangGroßesRad);
				float angle = distanceInMM / umfangGroßesRad * 3 * 360;
				float speed = angle / (distanceInMM / mmPerSec);
				System.out.println("angle: " + angle);
				System.out.println("speed: " + speed);
				motor.setSpeed(Math.round(speed));
				motor.rotate(Math.round(angle) * dir);
			}
		});
		return thread;
	}
	
	public static void main(String[] args) {
		test();
	}
}