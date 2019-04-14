
public class Quad3 {
	public static SalaSlammers robot = new SalaSlammers();
	
	
	// Inclinometer
	final private static int CHANNEL_INCLINOMETER = 1;
		
		
	// Drive along the wall in time (ms)
	// type = 0: forward, 1: backward
	
	public static void findGap(int time, int type) {
		int distToWall = 40;

		if (type == 0) robot.runForward(0);
		else robot.runBackward(0);
		
		int startTime = (int) System.currentTimeMillis();

		while (true) {
			int curDist = robot.getPing(robot.PING_PIN); 

			System.out.println(curDist);
			if (curDist > distToWall) {
				robot.allPCAStop();
				break;
			}
			int elapsedTime = (int) System.currentTimeMillis() - startTime;

			if (elapsedTime >= time) {
				robot.allPCAStop();
				break;
			}
		}
	}
	

	public static double getSlopeAngle() {
		robot.refreshAnalogPins();
		double adcCode = robot.getAnalogPin(CHANNEL_INCLINOMETER).getValue();
		
		
		double slope = -4.4238;
		double yIntercept = 474.3;
		
		System.out.println(adcCode);
		
		return (adcCode - yIntercept) / slope;
	}
	
	
	public static void passWalls() {
		robot.runPCAServo(robot.CHANNEL_SERVO_IR, 120);
		findGap(5500, 0);
		robot.sleep(100);
		robot.runBackward(50);
		robot.sleep(2000);
		
		robot.turnRight();
		robot.sleep(2000);
		
		robot.runForward(350);
		robot.sleep(2800);

		robot.turnLeft(440);
		robot.sleep(2800);
		
		findGap(5500, 1);
		robot.sleep(500);
		robot.runForward(20);
		robot.sleep(2800);
		
		robot.turnRight();
		robot.sleep(2800);
		
		robot.runForward(1500);
	}
	
	public static void goUpSlope() {
		robot.runForward(2500);
		robot.sleep(1000);
		
		double angle = 0;
		
		for (int i = 0; i < 10; i++) {
			angle = getSlopeAngle();
			robot.sleep(100);
		}
		System.out.println("Angle: " + getSlopeAngle());
		
		robot.close();
	}
	

	public static void main(String[] args) {
		robot.setPort("COM5");
		robot.connect();
		
		passWalls();
		goUpSlope();
		

	}
}
