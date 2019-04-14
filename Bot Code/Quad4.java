
public class Quad4 {
	
	
	// Temperature
	final private static double SLOPE = -8.48;
	final private static double INTERCEPT = 707.3;
	final private static int TEMP_READING_COUNT = 50;
	final private static int CHANNEL_SERVO_TEMP = 0;
	
	public static SalaSlammers robot = new SalaSlammers();
	
	
	
	private static double getThermistorReading() {
		 int sum = 0;
		 
		 for (int i = 0; i < TEMP_READING_COUNT; i++) {
			
			 //Refresh the analog pins so we get new readings
			 robot.refreshAnalogPins();
			 int reading = robot.getAnalogPin(0).getValue();
			 sum += reading;
		 }
		 
		 return sum * 1.0 / TEMP_READING_COUNT;
	}
	
	public static double temperature() {
		double thermistorReading = getThermistorReading();
		System.out.println("The probe read the value: " + thermistorReading);
		 
		return (thermistorReading - INTERCEPT) / SLOPE;
	}

	public static double measureTemperature() {
		
		int startPos = 180;
		int endPos = 20;
		
		robot.runPCAServo(CHANNEL_SERVO_TEMP, endPos);
		robot.sleep(2000);
		
		double result = temperature();
		
		// return the servo
		robot.sleep(1000);
		robot.runPCAServo(CHANNEL_SERVO_TEMP, startPos);
		robot.sleep(3000);
		return result;
	}
	
	public static void turnLeftAround(int time) {
		int speedLeft = -38;
		int speedRight = -38;
		
		robot.runTwoPCAMotor(robot.CHANNEL_LEFT_WHEEL, speedLeft, robot.CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void downSlope() {
		int time = 400;
		int speedLeft = 308;
		int speedRight = 308;
		
		robot.runTwoPCAMotor(robot.CHANNEL_LEFT_WHEEL, speedLeft, robot.CHANNEL_RIGHT_WHEEL, speedRight, time);
		robot.sleep(1000);
	}
	
	public static void navigateTempBox() {
		robot.runForward(300);
		robot.runPCAServo(robot.CHANNEL_SERVO_IR, 60);
		while (true) {
			if (robot.isBeacon('K')) break;
			turnLeftAround(20);
			robot.sleep(1000);
		}
		
		robot.runForward(2000);
		robot.sleep(2000);
		System.out.println("Temperature: " + measureTemperature());
	}
	
	public static void goToBridge() {
		robot.sleep(2000);
		robot.runPCAServo(robot.CHANNEL_SERVO_IR, 0);
		
		robot.runBackward(100);
		robot.sleep(2000);
		robot.turnLeft();
		robot.sleep(2000);
		
		while(true) {
			if (robot.isBeacon('N')) break;
			robot.runForwardSlow(200);
			robot.sleep(500);
		}		
		
		robot.sleep(3000);
		robot.turnRight();
		robot.sleep(3000);
		robot.runForward(2500);
		while (true) {
			int dist = robot.getPing(robot.PING_PIN);
			int distToBrigde = 30;
			if (dist < distToBrigde) {
				robot.allPCAStop();
				break;
			}
		}
		robot.runForward(100);
		robot.sleep(5000);
		robot.turnRight();
		robot.sleep(3000);
		robot.runForward(4000);
	}
	
	public static void main(String[] args) {
		robot.setPort("COM5");
		robot.connect();
		
		downSlope();
		navigateTempBox();
		goToBridge();
		
		robot.close();
	}
}
