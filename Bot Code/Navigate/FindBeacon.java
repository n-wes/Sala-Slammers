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

	public static int findBeaconAngle(int startAngle, int endAngle, char beacon) {

		boolean data[] = new boolean[20];
		int prefixSum[] = new int[20];
		
		for (int angle = 0; angle <= 18; angle++) {
			data[angle] = false;
		}

		for (int angle = startAngle * 10; angle <= endAngle * 10; angle += 10) {
			robot.runPCAServo(CHANNEL_SERVO, angle);
			data[angle / 10] = isBeacon('K');
			System.out.println(data[angle / 10]);
		}

		if (data[0] == true)
			prefixSum[0] = 1;
		else
			prefixSum[0] = 0;

		for (int i = 1; i <= 18; i++) {
			prefixSum[i] = prefixSum[i - 1];
			if (data[i] == true)
				prefixSum[i]++;
		}

		for (int len = 18; len >= 0; len--) {
			for (int i = 0; i <= 18 - len; i++) {
				if (i > 0) {
					if (prefixSum[i + len] - prefixSum[i - 1] == len + 1) {
						return (i + len / 2);
					}
				} else if (i == 0 && prefixSum[len] == len + 1) {
					return len / 2;
				}
			}
		}
		return -1;
	}

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

	public static void main(String[] args) {

		robot.setPort("COM4");
		robot.connect();
		
		int[] timeTurn = {785, 780, 720, 690, 450, 300, 130, 40, -1, -1}; 
		
		int cnt = 0;
		while (cnt <= 2) {
			cnt++;
			int angle = 9;
			
			if (cnt == 1) {
				angle = findBeaconAngle(0, 18, 'K');
			}
			else if (cnt == 2) {
				angle = findBeaconAngle(6, 12, 'K');
			}
			
			System.out.println(angle);
			
			if (angle <= 9) {
				turnRight(timeTurn[angle]);
			}
			else {
				turnLeft(timeTurn[18 - angle]);
			}
			robot.sleep(300);
			runForward(2000);
			
		}
		
		
		
		robot.close();
	}

}