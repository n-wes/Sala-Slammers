import rxtxrobot.*;

public class FindBeacon {
	public static RXTXRobot robot = new ArduinoUno();

	final private static int CHANNEL_LEFT_WHEEL = 2;
	final private static int CHANNEL_RIGHT_WHEEL = 1;
	final static int CHANNEL_SERVO = 0;

	public static boolean isBeacon(char beacon) {

		int readingCount = 3;
		int cnt = 0;

		for (int sample = 1; sample <= readingCount; sample++) {
			char signal = robot.getIRChar();
			System.out.println(signal);
			if (signal == beacon) {
				cnt++;
			}
		}

		if (cnt == readingCount) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Find the beacon's angle by getting data from startAngle to endAngle
	 * We have a boolean array, then find the longest consecutive "true" subsequence
	 * Return the middle of the subsequence
	 */

	public static double findBeaconAngle(int startAngle, int endAngle, char beacon) {

		boolean data[] = new boolean[20];
		int startIndex[] = new int[20]; //start position of the longest sub ending at i
		
		for (int angle = 0; angle <= 18; angle++) {
			data[angle] = false;
		}

		for (int angle = startAngle * 10; angle <= endAngle * 10; angle += 10) {
			robot.runPCAServo(CHANNEL_SERVO, angle);
			data[angle / 10] = isBeacon('K');
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

	public static void runForward(int time) {
		int speedLeft = 385;
		int speedRight = -110;

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

	// 720 for 90 deg
	public static void turnLeft(int time) {
		if (time == -1) return;
		
		int speedLeft = -26;
		int speedRight = -26;

		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

	public static void turnRight(int time) {
		if (time == -1) return;
		int speedLeft = 263;
		int speedRight = 263;

		robot.runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public static void moveTowardBeacon(char beacon) {
		
		int[] timeTurn = {785, 780, 720, 690, 450, 300, 130, 40, -1, -1, -1, 40, 125, 230, 380, 550, 630, 700, 780}; 
		
		int cnt = 0;
		
		while (cnt <= 2) {
			cnt++;
			double angle = 9;
			
			if (cnt == 1) {
				angle = findBeaconAngle(0, 18, beacon);
			}
			else if (cnt == 2) {
				angle = findBeaconAngle(6, 12, beacon);
			}
			
			System.out.println(angle);
			
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
			robot.sleep(300);
			runForward(1800);
		}

	}

	public static void main(String[] args) {

		robot.setPort("COM4");
		robot.connect();
		
		moveTowardBeacon('K');
				
		
		robot.close();
	}

}