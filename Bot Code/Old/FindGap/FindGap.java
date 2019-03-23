import rxtxrobot.*;

public class FindGap {
	public static RXTXRobot robot = new ArduinoUno();
	
	final private static int CHANNEL_LEFT_WHEEL = 2;
	final private static int CHANNEL_RIGHT_WHEEL = 1;
	final private static int PING_PIN = 5;
	
	public static void runForward(int time) {
		int speedLeft = 315; 
		int speedRight = -50;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
		
	public static void findGap() {
		int distToWall = 40;
		int time = 5000;
		
		runForward(0);
		int startTime = (int) System.currentTimeMillis();
		
		while (true) {
			int ping = robot.getPing(PING_PIN); 
			
			if (ping <= distToWall) {
				System.out.print("X");
			}
			else {
				System.out.print("0");
			}
			
			int elapsedTime = (int) System.currentTimeMillis() - startTime;

			if (elapsedTime >= time) {
				robot.allPCAStop();
				break;
			}
 		}
	}
	
	public static void main(String[] args) {
		robot.setPort("COM4");
		robot.connect();
		findGap();
	
		robot.close();
	}
}
