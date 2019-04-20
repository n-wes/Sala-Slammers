
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
	
	public void downSlope() {
		int time = 200;
		int speedLeft = 208;
		int speedRight = 208;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
		sleep(1000);
	}
	
	public void navigateTempBox() {
		runForward(-200, 2000);
		sleep(2000);
		turnRight();
		runPCAServo(CHANNEL_SERVO_IR, 60);
		findBeacon('K');
		turnRight(50);
		sleep(100);
		runForward(300, 2000);
		sleep(2000);
		System.out.println("Temperature: " + measureTemperature());
	}
	
	public void goToBridge() {
		sleep(2000);
		runPCAServo(CHANNEL_SERVO_IR, 0);
		
		runForward(-150, 200);
		sleep(2000);
		turnLeft();
		sleep(2000);
		
		while(true) {
			if (isBeacon('N')) break;
			runForward(200);
			sleep(500);
		}		
		
		sleep(2000);
		turnRight();
		sleep(2000);
		runForward(200, 0);
		
		while (true) {
			int dist = getPing(PING_PIN);
			int distToBrigde = 30;
			if (dist < distToBrigde) {
				allPCAStop();
				break;
			}
		}
		runForward(100);
		sleep(2000);
		turnRight();
		sleep(2000);
//		runForward(4000);
	}
	
	public static void main(String[] args) {
		Quad4 robot = new Quad4();
		
		robot.downSlope();
		robot.navigateTempBox();
		robot.goToBridge();
		
		robot.close();
	}
}
