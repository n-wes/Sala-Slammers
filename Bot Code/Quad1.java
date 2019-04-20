
public class Quad1 extends SalaSlammers{
	final private int CHANNEL_SERVO_CONDUCTIVITY = 0;
	final private int CHANNEL_INCLINOMETER = 1;
	
	Quad1() {
		super();
	}
	
	public double findTurningAngle() {
		double angleX = 0;
		
		for (int i = 0; i <= 120; i += 5) {
			runPCAServo(CHANNEL_SERVO_IR, i);
			char beacon = getIRChar();
			System.out.println(i + ": " + beacon);
			if (beacon == 'Z') {
				angleX = i;
				break;
			}
		}
		
		double angleTheta = 90 - 1.5 * angleX;
		
		double d = 157;
		double r = 73;
		
		double angleAlpha = Math.asin(r * Math.sin(angleTheta) / d);
		
		return 180 - (angleTheta + 90 - angleAlpha);
	}
	
	// 0: go backward, 1: forward
	public int turnPerpendicularRamp() {
		double turningAngle = findTurningAngle();
		System.out.println(turningAngle);
		
		int timeTurn = (int) (TURN_LEFT_TIME * turningAngle / 90.0);
		if (turningAngle < 80) {
			timeTurn = timeTurn * 7 / 6;
		}
		
		turnLeft(timeTurn);
		if (turningAngle < 80) return 0;
		else if (turningAngle > 100) return 1;
		return 2;
	}
	
	public double conductance() {
		double adcCode = getConductivity();
		System.out.println(adcCode);
		
		double slope = -0.0462;
		double yIntercept = 46.716;
		
		return slope * adcCode + yIntercept;
	}
	
	public void getSoilConductance() {
		
		int startPos = 180;
		int endPos = 0;
		
		runPCAServo(CHANNEL_SERVO_CONDUCTIVITY, endPos);
		sleep(2000);
		
		System.out.println(conductance());
		// return the servo
		sleep(1000);
		runPCAServo(CHANNEL_SERVO_CONDUCTIVITY, startPos);
	}
	
	public double getSlopeAngle() {
		refreshAnalogPins();
		double adcCode = getAnalogPin(CHANNEL_INCLINOMETER).getValue();
		
		
		double slope = -4.4238;
		double yIntercept = 474.3;
		
		System.out.println("ADC code for angle: " + adcCode);
		System.out.println("Angle: " + (adcCode - yIntercept) / slope + 45);
		
		return (adcCode - yIntercept) / slope + 45;
	}
	
	public void goToRamp() {
		
		int dir = turnPerpendicularRamp();
		sleep(3000);
		runPCAServo(CHANNEL_SERVO_IR, 0);

		for (int i = 0; i < 15; i++) {
			if (dir == 2) break;
			
			if (dir == 1) runForward(200);
			else runForward(-130, 200);
			
			if (isBeacon('Z')) {
				allPCAStop();
				System.out.println("Found Beacon Z!");
				break;
			}
			sleep(100);
		}
		if (dir == 0) {
			runForward(-150, 800);
		}
		
		sleep(2000);
		turnRight(500);
		sleep(500);
	}
	
	public void goThroughSlope() {
		runForward(500, 0);
		sleep(2000);
		
		while (true) {
			double rampAngle = 10;
			if (getSlopeAngle() <= rampAngle) {
				allPCAStop();
				break;
			}
		}
		
		sleep(800);
		runForward(300, 1000);
		runForward(-150, 150);
		sleep(2000);
		turnLeft();
		sleep(1500);
		
		runForward(200, 1000);
		sleep(2000);
		
		runForward(-200, 3000);
		sleep(3000);
		turnLeft(500);
		sleep(1500);
		runForward(300, 2000);
		sleep(3000);
	}
	
	public static void main(String[] args) {

		Quad1 robot = new Quad1();
		
		robot.goToRamp();
		robot.findBeacon('Z');
		robot.goThroughSlope();
		robot.getSoilConductance();
//		System.out.println(robot.getSlopeAngle());
		robot.close();
	}
}
