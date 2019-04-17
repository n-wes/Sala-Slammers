
public class Quad3 extends SalaSlammers {
	// Inclinometer
	final private int CHANNEL_INCLINOMETER = 1;
		
	// Drive along the wall in time (ms)
	// type = 0: forward, 1: backward
	
	public void findGap(int time, int type) {
		int distToWall = 40;

		if (type == 0) runForward(0);
		else runForward(-150, 0);
		
		int startTime = (int) System.currentTimeMillis();

		while (true) {
			int curDist = getPing(PING_PIN); 

			System.out.println(curDist);
			if (curDist > distToWall) {
				allPCAStop();
				break;
			}
			int elapsedTime = (int) System.currentTimeMillis() - startTime;

			if (elapsedTime >= time) {
				allPCAStop();
				break;
			}
		}
	}
	
	
	public void passWalls() {
		runPCAServo(CHANNEL_SERVO_IR, 120);
		findGap(5500, 0);
		sleep(100);
		runForward(-150, 50);
		sleep(2000);
		
		turnRight();
		sleep(2000);
		
		runForward(350);
		sleep(2800);

		turnLeft(440);
		sleep(2800);
		
		findGap(5500, 1);
		sleep(500);
		runForward(20);
		sleep(2800);
		
		turnRight();
		sleep(2800);
		
		runForward(1500);
	}
	
	public void goUpSlope() {
		runForward(200, 3000);
		sleep(3000);
		runForward(400, 1500);
		runForward(80, 0);
		
		sleep(2000);
		
		double slope = -4.4238;
		double yIntercept = 474.3;

		for (int i = 0; i < 20; i++) {
			refreshAnalogPins();
			double adcCode = getAnalogPin(CHANNEL_INCLINOMETER).getValue();
			System.out.println("ADC Code: " + adcCode);
			System.out.println("Angle: " + (adcCode - yIntercept) / slope);
			sleep(100);
		}
		
		allPCAStop();
	}
	

	public static void main(String[] args) {
		Quad3 robot = new Quad3();
//		robot.passWalls();
		robot.goUpSlope();
	}
}
