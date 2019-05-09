// We have 2 solutions for navigating to the ramp (after crossing the bridge):
// 1: goToRamp() - can complete the task for all positions of the bridge, but takes more time
// 2: navigateToRamp() - need to know the relative position of the bridge first, faster, may be used when running out of time

public class Quad1 extends SalaSlammers{
	final private int CHANNEL_SERVO_CONDUCTIVITY = 0;
	final private int CHANNEL_INCLINOMETER = 1;
	
	Quad1() {
		super();
	}
		public double conductance() {
		double sum = 0;
		int numTimes = 3;
		
		double slope = -0.0462;
		double yIntercept = 46.716;

		for (int i = 0; i < numTimes; i++) {
			double adcCode = getConductivity();
			
			System.out.println("ADC code: " + adcCode);
			double value = slope * adcCode + yIntercept;
			System.out.println("Soil conductance: " + value);
			sum += value;
		}
		
		return sum / numTimes;
	}
	
	public void getSoilConductance() {
		refreshAnalogPins();
		refreshDigitalPins();
		int startPos = 170;
		int endPos = 0;
		
		runPCAServo(CHANNEL_SERVO_CONDUCTIVITY, endPos);
		sleep(2000);
		
		System.out.println("Conductance: " + conductance());
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
		System.out.println("Angle: " + ((adcCode - yIntercept) / slope + 44.2));
		
		return (adcCode - yIntercept) / slope + 44.2;
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
		runForward(-150, 150);
		sleep(2000);
		turnLeft(800);
		sleep(1500);
		
		runForward(200, 1000);
		sleep(2000);
		
		runForward(-200, 1500);
		sleep(3000);
		turnLeft(TURN_LEFT_TIME);
		sleep(1500);
		runForward(300, 2000);
		sleep(3000);
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

		double angleTheta = 90 - angleX;

		double d = 157;
		double r = 73;

		double angleAlpha = Math.asin(r * Math.sin(Math.toDegrees(angleTheta)) / d);
		angleAlpha = Math.toDegrees(angleAlpha);

		return 180 - (angleTheta + 90 - angleAlpha);
	}

	// 0: go backward, 1: forward
	public int turnPerpendicularRamp() {
		double turningAngle = findTurningAngle();
		System.out.println(turningAngle);

		int timeTurn = (int) (TURN_LEFT_TIME * turningAngle / 90.0);

		turnLeft(timeTurn);
		if (turningAngle < 80) return 0;
		else if (turningAngle > 100) return 1;
		return 2;
	}
	
	public void goToRamp() {

		int dir = turnPerpendicularRamp();
		sleep(3000);
		moveIRServo(-90);

		for (int i = 0; i < 15; i++) {
			if (dir == 2) break;

			if (dir == 1) runForward(150, 200);
			else runForward(-130, 200);

			if (isBeacon('Z')) {
				allPCAStop();
				System.out.println("Found Beacon Z!");
				break;
			}
			sleep(100);
		}

		sleep(2000);
		turnRight();
		sleep(500);
		findBeacon('Z');
	}
	
	public static void main(String[] args) {

		Quad1 robot = new Quad1();
		
		robot.runForward(200, 2200);
		robot.sleep(4000);
		robot.goToRamp();
		// robot.navigateToRamp();
		robot.goThroughSlope();
		robot.getSoilConductance();
		robot.close();
	}
}
