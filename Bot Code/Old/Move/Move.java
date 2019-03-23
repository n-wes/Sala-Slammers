import rxtxrobot.*;

//12.1 V

public class Move {
	
	public static RXTXRobot robot = new ArduinoUno();
	
	final private static int CHANNEL_LEFT_WHEEL = 2;
	final private static int CHANNEL_RIGHT_WHEEL = 1;
	
	public static void runForward(int time) {
		int speedLeft = 385; 
		int speedRight = -107;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void upSlope(int time) {
		int speedLeft = 500; 
		int speedRight = -350;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void runBackward(int time) {
		int speedLeft = -98;
		int speedRight = 325;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	//720 for 90 deg
	public static void turnLeft(int time) {
		int speedLeft = -26;
		int speedRight = -26;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void turnRight(int time) {
		int speedLeft = 263;
		int speedRight = 263;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void main(String[] args) {
		robot.setPort("COM4");
		robot.connect();
		
		//runForward(6000);
		turnRight(720);
		
		robot.close();
	}
}
