
public class Quad3 extends SalaSlammers {
	// Inclinometer
	final private int CHANNEL_INCLINOMETER = 1;
		
	// Drive along the wall in time (ms)
	// type = 0: forward, 1: backward
	
	public void findGap(int time, int type) {
		int distToWall = 60;

		if (type == 0) runForward(250, 0);
		else runForward(-250, 0);
		
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
		findGap(5500, 0);
		sleep(100);
		runForward(-150, 300);
		sleep(2000);
		
		turnRight();
		sleep(2000);
		
		runForward(200, 1300);
		sleep(2800);
		runForward(-150, 200);
		turnLeft();
		sleep(2000);
		
		findGap(5500, 1);
		sleep(500);
		runForward(200, 50);
		sleep(2000);
		
		turnRight();
		sleep(2000);
		
		runForward(200, 1300);
		sleep(2000);
	}
	
	public void navigateToRamp() {
		turnLeft();
		sleep(3000);
		
		runUntilHit(250, 15);
		
		sleep(2000);
		runForward(-200, 2500);
		sleep(3000);
		turnRight(400);
		sleep(2000);
		findBeacon('Y');
		runForward(200, 1000);
		findBeacon('Y');
	}
	
	public void goUpSlope() {
		sleep(2000);
		runForward(400, 1500);
		runForward(80, 0);
		
		sleep(2000);

		double slope = -4.4238;
		double yIntercept = 474.3;

		for (int i = 0; i < 20; i++) {
			refreshAnalogPins();
			double adcCode = getAnalogPin(CHANNEL_INCLINOMETER).getValue();
			System.out.println("ADC Code: " + adcCode);
			System.out.println("Angle: " + (((adcCode - yIntercept) / slope) + 45.2)) ;
			sleep(100);
		}
		
		allPCAStop();
	}
	

	public static void main(String[] args) {
		Quad3 robot = new Quad3();
		robot.passWalls();
		robot.navigateToRamp();
		robot.goUpSlope();
		
		robot.close();
	}
}

