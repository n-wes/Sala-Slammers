
public class Quad4 extends SalaSlammers{
	
	
	// Temperature
	final private double SLOPE = -8.48;
	final private double INTERCEPT = 707.3;
	final private int TEMP_READING_COUNT = 20;
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
		
		int startPos = 170;
		int endPos = 0;
		
		runPCAServo(CHANNEL_SERVO_TEMP, endPos);
		sleep(5000);
		
		double result = temperature();
		
		// return the servo
		sleep(1000);
		runPCAServo(CHANNEL_SERVO_TEMP, startPos);
		return result;
	}
	
	public void downSlope() {
		int time = 200;
		int speedLeft = 208;
		int speedRight = 208;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
		sleep(1000);
	}
	
	public void navigateTempBox() {
		runForward(-150, 1000);
		sleep(2000);
		turnRight(1000);
		moveIRSensor(0);
		findBeacon('K');
//		turnRight(50);
//		sleep(100);
		runUntilHit(200, 10);
		sleep(1000);
		System.out.println("Temperature: " + measureTemperature());
	}
	
	public void goToBridge() {
		sleep(2000);
		
		runForward(-150, 200);
		sleep(2000);
		turnLeft();
		sleep(2000);
		
		runUntilHit(220, 15);
		sleep(2000);
		
		runForward(-150, 800);
		sleep(2000);
		
		turnRight();
		sleep(2000);
		findBeacon('N');
		sleep(2000);
		
		runUntilHit(150, 30);
		sleep(3000);
	}
	
	public void crossBridge() {
		runForward(-150, 200);
		sleep(2500);
		
		turnRight(TURN_RIGHT_TIME + 300);
		sleep(3000);
		
		runForward(-200, 800);
		sleep(3000);
		
		runForward(200, 4500);

	}
	
	public static void main(String[] args) {
		Quad4 robot = new Quad4();
		
		robot.downSlope();
		robot.navigateTempBox();
		robot.goToBridge();
		robot.crossBridge();
		System.out.println(robot.temperature());
		
		robot.close();
	}
}
