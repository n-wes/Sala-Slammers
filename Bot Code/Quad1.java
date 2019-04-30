
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
	
	public void navigateToRamp() {
		int curDist = myGetPing(6);
		System.out.println(curDist);
		
		turnLeft();
		sleep(3000);
		
		int distToWall = 90;
		if (curDist > distToWall || true) {
			runUntilHit(120, distToWall);
			sleep(2000);
		}
		else {
			runUntilHit(-120, distToWall);
			sleep(2000);
		}
		
		turnRight(500);
		sleep(2000);
	
		
		findBeacon('Z');
	}
	
	void goToRamp() {
		runForward(200, 1100);
		findBeacon('Z');
		runForward(200, 300);
		findBeacon('Z');
		runForward(200, 500);
	}
	
	public static void main(String[] args) {

		Quad1 robot = new Quad1();
		
//		robot.runPCAServo(0, 170);
//		robot.goToRamp();
		robot.runForward(200, 2200);
		robot.sleep(4000);
		robot.navigateToRamp();
		robot.goThroughSlope();
		robot.getSoilConductance();
		robot.close();
	}
}
