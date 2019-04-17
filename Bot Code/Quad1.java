
public class Quad1 extends SalaSlammers{
	final private int CHANNEL_SERVO_CONDUCTIVITY = 0;
	final private int CHANNEL_INCLINOMETER = 1;
	
	Quad1() {
		super();
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
		int endPos = 20;
		
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
		
		System.out.println(adcCode);
		System.out.println((adcCode - yIntercept) / slope);
		
		return (adcCode - yIntercept) / slope;
	}
	
	public void goToRamp() {
		
		while (true) {
			int dist = getPing(PING_PIN);
			int distToRamp = 30;
			if (dist < distToRamp) {
				allPCAStop();
				break;
			}
		}
	}
	
	public void goUpSlope() {
		runForward(500, 0);
		sleep(2000);
		while (true) {
			double groundAngle = 10;
			if (getSlopeAngle() <= groundAngle) {
				allPCAStop();
				break;
			}
		}
		
		sleep(800);
		runForward(-150, 150);
		sleep(1500);
		turnRight();
		sleep(3000);
		runForward(-300, 1000);
		sleep(2000);
		
		runForward(200, 2000);
		
		turnRight(1300);
		sleep(3000);
		runForward(300, 2000);
		sleep(3000);
	}
	
	public static void main(String[] args) {

		Quad1 robot = new Quad1();
		
//		robot.goToRamp();
		robot.goUpSlope();
		robot.getSoilConductance();
	}
}
