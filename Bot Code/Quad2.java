
public class Quad2 {
	
	public static SalaSlammers robot = new SalaSlammers();
	
	
	public static void main(String[] args) {
		robot.setPort("COM5");
		robot.connect();
		
		
		robot.runPCAServo(robot.CHANNEL_SERVO_IR, 0);
		
		robot.runForward(1350);
		robot.sleep(2500);
		
		for (int i = 0; i < 4; i++) {
			if (robot.isBeacon('S')) break;
			robot.runForwardSlow(120);
			robot.sleep(500);
		}
		robot.sleep(2800);
		robot.turnRight();
		robot.sleep(4000);
		
		robot.runForward(1500);
		robot.sleep(5000);
		robot.turnRight();
		robot.sleep(3000);
		robot.runForwardSlow(500);
		robot.sleep(2000);
		robot.turnRight();
		robot.sleep(2000);
		
		robot.runPCAServo(robot.CHANNEL_SERVO_IR, 125);
		
		for (int i = 0; i < 6; i++) {
			if (robot.isBeacon('G')) break;
			robot.runForwardSlow(200);
			robot.sleep(500);
		}
		robot.runForwardSlow(200);
			
		robot.sleep(3000);
		robot.turnLeft();
		robot.sleep(3000);
		robot.runForward(1800);
		robot.close();
	}
}
