
public class Quad2 extends SalaSlammers {
	
	Quad2() {
		super();
	}
	
	public void work() 	{
		runPCAServo(CHANNEL_SERVO_IR, 0);
		
		runForward(200, 1500);
		
//		int distToWall = 90;
//		while (true) {
//			int dist = getPing(PING_PIN);
//			System.out.println(dist);
//			
//			if (dist > distToWall) {
//				allPCAStop();
//				break;
//			}
//		}
		sleep(2000);
		for (int i = 0; i < 8; i++) {
			if (isBeacon('S')) break;
			runForward(150, 120);
			sleep(500);
		}
		sleep(2800);
		turnRight();
		sleep(3000);
		
		runForward(250, 2200);
		sleep(5000);
		turnRight();
		sleep(3000);
		runForward(1000);
		sleep(2000);
		turnRight();
		sleep(2000);
		
		runPCAServo(CHANNEL_SERVO_IR, 125);
		
		for (int i = 0; i < 6; i++) {
			if (isBeacon('G')) break;
			runForward(200);
			sleep(500);
		}
		runForward(200);
			
		sleep(3000);
		turnLeft();
		sleep(3000);
		runForward(4800);
	}
	
	public static void main(String[] args) {
		Quad2 robot = new Quad2();
		robot.work();
		
		robot.close();
	}
}
