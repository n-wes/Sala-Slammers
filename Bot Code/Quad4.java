
public class Quad4 extends SalaSlammers{
	
	
	// Temperature
	final private double SLOPE = -8.48;
	final private double INTERCEPT = 707.3;
	final private int TEMP_READING_COUNT = 50;
	final private int CHANNEL_SERVO_TEMP = 0;
	
	private double getThermistorReading() {
		 int sum = 0;
		 
		 for (int i = 0; i < TEMP_READING_COUNT; i++) {
			
			 //Refresh the analog pins so we get new readings
			 refreshAnalogPins();
			 int reading = getAnalogPin(0).getValue();
			 sum += reading;
		 }
		 
		 return sum * 1.0 / TEMP_READING_COUNT;
	}
	
	public double temperature() {
		double thermistorReading = getThermistorReading();
		System.out.println("The probe read the value: " + thermistorReading);
		 
		return (thermistorReading - INTERCEPT) / SLOPE;
	}

	public double measureTemperature() {
		
		int startPos = 180;
		int endPos = 20;
		
		runPCAServo(CHANNEL_SERVO_TEMP, endPos);
		sleep(2000);
		
		double result = temperature();
		
		// return the servo
		sleep(1000);
		runPCAServo(CHANNEL_SERVO_TEMP, startPos);
		sleep(3000);
		return result;
	}
	
	public void turnLeftAround(int time) {
		int speedLeft = -38;
		int speedRight = -38;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void downSlope() {
		int time = 400;
		int speedLeft = 308;
		int speedRight = 308;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
		sleep(1000);
	}
	
	public void navigateTempBox() {
		runForward(300);
		runPCAServo(CHANNEL_SERVO_IR, 60);
		while (true) {
			if (isBeacon('K')) break;
			turnLeftAround(20);
			sleep(1000);
		}
		
		runForward(2000);
		sleep(2000);
		System.out.println("Temperature: " + measureTemperature());
	}
	
	public void goToBridge() {
		sleep(2000);
		runPCAServo(CHANNEL_SERVO_IR, 0);
		
		runForward(-150, 100);
		sleep(2000);
		turnLeft();
		sleep(2000);
		
		while(true) {
			if (isBeacon('N')) break;
			runForward(200);
			sleep(500);
		}		
		
		sleep(3000);
		turnRight();
		sleep(3000);
		runForward(2500);
		while (true) {
			int dist = getPing(PING_PIN);
			int distToBrigde = 30;
			if (dist < distToBrigde) {
				allPCAStop();
				break;
			}
		}
		runForward(100);
		sleep(5000);
		turnRight();
		sleep(3000);
		runForward(4000);
	}
	
	public static void main(String[] args) {
		Quad4 robot = new Quad4();
		
		robot.downSlope();
		robot.navigateTempBox();
		robot.goToBridge();
		
	}
}
