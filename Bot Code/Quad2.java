import com.sun.glass.ui.Robot;

public class Quad2 extends SalaSlammers {
	
	Quad2() {
		super();
	}
	
	public void work() 	{
		moveIRSensor(-90);
		runForward(200, 1100);
		
		sleep(2000);
		
		int beaconPos = 0;
		for (int i = 0; i < 10; i++) {
			if (isBeacon('S')) {
				beaconPos = i;
				break;
			}
			runForward(130, 150);
			sleep(500);
		}
		
//		runForward(130, 150);
		
		if (myGetPing(6) <= 15) {
			runForward(-130, 350);
		}
		
		sleep(3000);
		turnRight();
		sleep(2000);
		
//		findBeacon('S');
//		sleep(1000);
		
		runForward(200, 2200);
		sleep(3000);
		turnRight();
		sleep(3000);
		runForward(500);
		sleep(3000);
		
		int dist = myGetPing(6);
		
		System.out.println(dist);
		if (dist < 15) {
			runForward(-120, 300);
			sleep(2000);
		}
		turnRight(TURN_RIGHT_TIME);
		sleep(2000);
		
		runForward(150, 3000);
			
		sleep(5000);
		turnLeft();
		sleep(2000);
	}
	
	void goToReturningZone() {
		findBeacon('G');
		sleep(2000);
		runUntilHit(150, 20);
	}
	
	public static void main(String[] args) {
		Quad2 robot = new Quad2();
		robot.work();
		robot.goToReturningZone();
		
		robot.close();
	}
}
