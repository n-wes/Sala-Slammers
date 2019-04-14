
public class Quad1 {
	final private static int CHANNEL_SERVO_CONDUCTIVITY = 0;
	final private static int CHANNEL_INCLINOMETER = 1;
	
	
	public static SalaSlammers robot = new SalaSlammers();
	
	public static double conductance() {
		double adcCode = robot.getConductivity();
		System.out.println(adcCode);
		
		double slope = -0.0462;
		double yIntercept = 46.716;
		
		return slope * adcCode + yIntercept;
	}
	
	public static void getSoilConductance() {
		
		int startPos = 180;
		int endPos = 20;
		
		robot.runPCAServo(CHANNEL_SERVO_CONDUCTIVITY, endPos);
		robot.sleep(2000);
		
		System.out.println(conductance());
		// return the servo
		robot.sleep(1000);
		robot.runPCAServo(CHANNEL_SERVO_CONDUCTIVITY, startPos);
	}
	
	public static double getSlopeAngle() {
		robot.refreshAnalogPins();
		double adcCode = robot.getAnalogPin(CHANNEL_INCLINOMETER).getValue();
		
		
		double slope = -4.4238;
		double yIntercept = 474.3;
		
		System.out.println(adcCode);
		System.out.println((adcCode - yIntercept) / slope);
		
		return (adcCode - yIntercept) / slope;
	}
	
	public static void goToRamp() {
		robot.runForward(2000);
		robot.sleep(4000);
		robot.turnLeft();
		robot.sleep(3000);
		
		boolean isGood = false;
		robot.runForward(1000);
		
		while (true) {
			int dist = robot.getPing(robot.PING_PIN);
			int distToRamp = 30;
			if (dist < distToRamp) {
				robot.allPCAStop();
				isGood = true;
				break;
			}
		}
		robot.sleep(2000);
		
		if (isGood == false) {
			robot.runBackward(2000);
			
			while (true) {
				int dist = robot.getPing(5);
				int distToRamp = 30;
				if (dist < distToRamp) {
					robot.allPCAStop();
					break;
				}
			}
		}
		robot.sleep(2000);
		robot.runBackward(100);
		
		robot.sleep(3000);
		robot.turnRight(50);
		robot.sleep(2000);
		robot.runForward(100);
		robot.sleep(3000);
		robot.runForward(2500);
		robot.sleep(2000);
	}
	
	public static void goUpSlope() {
		robot.upSlope(0);
		robot.sleep(1000);
		
		while (true) {
			double groundAngle = 10;
			if (getSlopeAngle() <= groundAngle) {
				robot.allPCAStop();
				break;
			}
			robot.sleep(50);
		}
		
		robot.sleep(3000);
		robot.runBackward(100);
		robot.sleep(800);
		robot.turnRight(330);
		robot.sleep(4000);
		robot.runForward(200);
		
		robot.sleep(3000);
		robot.turnRight(1000);
		robot.sleep(1000);
	}
	
	public static void main(String[] args) {
		robot.setPort("COM5");
		robot.connect();
		
		goToRamp();
		goUpSlope();
		getSoilConductance();
		
		
		robot.close();
	}
}
