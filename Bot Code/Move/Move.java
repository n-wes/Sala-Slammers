import rxtxrobot.*;

public class Move {
	
	public static RXTXRobot robot = new ArduinoUno();
	
	private static int CHANNEL_LEFT_WHEEL = 0;
	private static int CHANNEL_RIGHT_WHEEL = 1;
	
	public static void runForward(int time) {
		int speedLeft = 325; 
		int speedRight = -100;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void upSlope(int time) {
		int speedLeft = 500; 
		int speedRight = -350;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	//720 for 90 deg
	public static void turnLeft(int time) {
		int speedLeft = -28;
		int speedRight = -28;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void turnRight(int time) {
		int speedLeft = 258;
		int speedRight = 258;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void main(String[] args) {
		robot.setPort("COM4");
		robot.connect();

		//runForward(3000);
		
		//turnLeft(720);
		//
		robot.sleep(3000);
		turnRight(720);
		
		
		robot.close();
	}
}
