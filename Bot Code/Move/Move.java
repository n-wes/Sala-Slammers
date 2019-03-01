import rxtxrobot.*;

public class Move {
	
	public static RXTXRobot robot = new ArduinoUno();
	
	private static int CHANNEL_LEFT_WHEEL = 0;
	private static int CHANNEL_RIGHT_WHEEL = 1;
	
	public static void runForward(int time) {
		int speedLeft = 325; 
		int speedRight = -90;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void turnLeft(int time) {
		int speedLeft = -60;
		int speedRight = -60;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void turnRight(int time) {
		int speedLeft = 260;
		int speedRight = 260;
		
		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void main(String[] args) {
		robot.setPort("COM4");
		robot.connect();

		//runForward(3000);
		
		//turnLeft(2000);
		
		robot.close();
	}
}
