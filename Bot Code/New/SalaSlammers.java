import rxtxrobot.*;

public class SalaSlammers extends ArduinoUno {
	
	// Movement
	final private int CHANNEL_LEFT_WHEEL = 2;
	final private int CHANNEL_RIGHT_WHEEL = 1;
	
	// Temperature
	final private double SLOPE = -8.48;
	final private double INTERCEPT = 707.3;
	final private int TEMP_READING_COUNT = 100;
	
	// Find gap
	final private int PING_PIN = 5;
	
	// Find beacons
	final private int CHANNEL_SERVO = 0;
	final private int BEACON_READING_COUNT = 3;

	
//	------------------------------------------------------
//	Constructor
	
	public SalaSlammers() {
		super();
	}

//	-----------------------------------------------------	
//	Movement
	
	public void runForward(int time) {
		int speedLeft = 385; 
		int speedRight = -102;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void upSlope(int time) {
		int speedLeft = 500; 
		int speedRight = -350;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void runBackward(int time) {
		int speedLeft = -98;
		int speedRight = 325;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void turnLeft(int time) {
		int speedLeft = -26;
		int speedRight = -26;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void turnRight(int time) {
		int speedLeft = 263;
		int speedRight = 263;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

//	----------------------------------------------------
//	Get temperature
	
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
		//System.out.println("The probe read the value: " + thermistorReading);
		 
		return (thermistorReading - INTERCEPT) / SLOPE;
	}

//	-------------------------------------------------------
//	Find Gap
	
	
	// Drive along the wall in time (ms)
	public void findGap(int time) {
		int distToWall = 30;

		runForward(0);
		int startTime = (int) System.currentTimeMillis();

		while (true) {
			int curDist = getPing(PING_PIN); 

			if (curDist <= distToWall) {
				System.out.print("X");
			}
			else {
				System.out.print("0");
			}

			int elapsedTime = (int) System.currentTimeMillis() - startTime;

			if (elapsedTime >= time) {
				allPCAStop();
				break;
				}
	 		}
		}

//	------------------------------------------------------------
//	Find beacons
	
	private boolean isBeacon(char beacon) {

		int cnt = 0;

		for (int sample = 1; sample <= BEACON_READING_COUNT; sample++) {
			char signal = getIRChar();
			//System.out.println(signal);
			if (signal == beacon) {
				cnt++;
			}
		}
	
		if (cnt == BEACON_READING_COUNT) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	private double findBeaconAngle(int startAngle, int endAngle, char beacon) {
		/*
		 * Find the beacon's angle by getting data from startAngle to endAngle
		 * We have a boolean array, then find the longest consecutive "true" subsequence
		 * Return the middle position of the subsequence
		 */
		
		boolean data[] = new boolean[20];
		int startIndex[] = new int[20]; //start position of the longest sub ending at i
		
		for (int angle = 0; angle <= 18; angle++) {
			data[angle] = false;
		}

		for (int angle = startAngle * 10; angle <= endAngle * 10; angle += 10) {
			runPCAServo(CHANNEL_SERVO, angle);
			data[angle / 10] = isBeacon(beacon);
			System.out.println(data[angle / 10]);
		}
		
		int lenLongestSub = -1, start = -1, end = -1; //details of the finding subsequence
		
		
		for (int i = 0; i <= 18; i++) {
			if (data[i] == true) {
				if (i == 0 || data[i - 1] == false) {
					startIndex[i] = i;
				}
				else {
					startIndex[i] = startIndex[i - 1];
				}
				
				if (lenLongestSub < i - startIndex[i] + 1) { //update maximum
					
					lenLongestSub = i - startIndex[i] + 1;
					start = startIndex[i];
					end = i;
					
				}
			}
		}

		return (start + end) / 2.0;
	}
	
	
	
	public void moveTowardBeacon(char beacon, int time) { // movement time ~ time * 3
		
		int[] timeTurn = {795, 780, 720, 690, 450, 300, 130, 40, -1, -1, -1, 40, 125, 230, 380, 550, 630, 700, 780};
		
		/*
		test timeTurn: 
		 
		robot.runPCAServo(CHANNEL_SERVO, 40);
		robot.sleep(1000);
		
		turnRight(timeTurn[4]);
		robot.sleep(300);
		robot.runPCAServo(CHANNEL_SERVO, 90);
		*/
		
		// stop once on the path
		int cnt = 0;
		
		while (cnt <= 2) {
			cnt++;
			double angle = 9;
			
			if (cnt == 1) {
				angle = findBeaconAngle(0, 18, beacon);
			}
			else if (cnt == 2) {
				angle = findBeaconAngle(6, 14, beacon);
			}
			
			//System.out.println(angle);
			
			if (angle <= 9) {
				int tmp = (int) angle * 2;
				
				if (tmp % 2 == 0) {
					turnRight(timeTurn[tmp / 2]);;
				}
				else {
					turnRight((timeTurn[tmp / 2] + timeTurn[tmp / 2 + 1]) / 2);
				}
			}
			else {
				int tmp = (int) angle * 2;
				
				if (tmp % 2 == 0) {
					turnLeft(timeTurn[tmp / 2]);;
				}
				else {
					turnLeft((timeTurn[tmp / 2] + timeTurn[tmp / 2 + 1]) / 2);
				}
			}
			sleep(300);
			runForward(time);
		}
	}
}
