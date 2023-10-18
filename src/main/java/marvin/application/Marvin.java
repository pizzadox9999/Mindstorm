package marvin.application;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.internal.ev3.EV3Battery;
import lejos.robotics.RegulatedMotor;
import marvin.ms.application.Application;
import marvin.ms.application.MarvinToolkit;

public class Marvin extends Application {
	private boolean run = true;

	public static void test() {
    	System.out.println("test");
    	final EV3LargeRegulatedMotor motorPaperMove = new EV3LargeRegulatedMotor(MotorPort.A);
    	final EV3LargeRegulatedMotor motorPrintheadMove = new EV3LargeRegulatedMotor(MotorPort.B);
    	
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
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(getPaper);
		executorService.execute(getPrinthead);
		
		//move paper 
		
		float umfangGroßesRad = 4.32f;

		int papier1cmProGrad = (int) (360 * (1/umfangGroßesRad)) * 3;		
    }

	public static void move(final int distance, final int mmSec, final RegulatedMotor motor) {
		// v = t/s
		// s = t/v
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long time = distance / mmSec;
				motor.setSpeed(250);
				motor.forward();
				try {
					Thread.sleep(time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (run) {
			System.out.println("Console: ");
			String nextLine = scanner.nextLine().toLowerCase();
			if (nextLine.equals("stop")) {
				run = false;
			} else {
				System.out.println("Your input was not recognized");
			}
		}
	}

	private String getLanguageValue(String key) {
		return MarvinToolkit.getDefaulToolkit().getLanguagePack().get(key);
	}

	public static void main(String[] args) {
		test();
	}
}
