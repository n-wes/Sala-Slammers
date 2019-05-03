import rxtxrobot.*;

public class SalaSlammers extends ArduinoUno {

	// Movement
	final protected int CHANNEL_LEFT_WHEEL = 15;
	final protected int CHANNEL_RIGHT_WHEEL = 14;
	final protected int TURN_LEFT_TIME = 680;
	final protected int TURN_RIGHT_TIME = 680;

	// Find beacons
	final protected int CHANNEL_SERVO_IR = 1;
	final protected int BEACON_READING_COUNT = 1;

	// Ping sensor
	final protected int PING_PIN = 5;


	//	------------------------------------------------------
	//	Constructor

	public SalaSlammers() {
		super();
		setPort("COM9");
		connect();
	}

	//	-----------------------------------------------------	
	//	Movement



	public void runForward(int speed, int time) {
		int speedZero = 120;

		int speedLeft = speedZero + speed;
		int speedRight = speedZero - speed;

		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

	public void runForward(int time) {
		runForward(150, time);
	}

	public void turnLeft() {
		turnLeft(TURN_LEFT_TIME);
	}

	public void turnRight() {
		turnRight(TURN_RIGHT_TIME);
	}

	public void turnLeft(int time) {
		int speedLeft = -60;
		int speedRight = -60;

		allPCAStop();
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

	public void turnRight(int time) {
		int speedLeft = 300;
		int speedRight = 300;

		allPCAStop();
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

	public void runUntilHit(int speed, int distToWall) {
		runForward(speed, 0);
		while (true) {
			int dist = myGetPing(6); 
			System.out.println("ping 6: " + dist);
			if (speed > 0) {
				if (dist < distToWall) {
					allPCAStop();
					break;
				}
			}
			else {
				if (dist > distToWall) {
					allPCAStop();
					break;
				}
			}
			sleep(20);
		}
	}


	//	------------------------------------------0	------------------
	//	Find beacons

	public boolean isBeacon(char beacon) {

		int cnt = 0;

		for (int sample = 1; sample <= BEACON_READING_COUNT; sample++) {
			char signal = getIRChar();
			System.out.println(signal);
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


	public void turnRightAround(int time) {
		int speedLeft = 270;
		int speedRight = 270;

		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}

	public void turnLeftAround(int time) {
		int speedLeft = -18;
		int speedRight = -18;

		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}


	public void findBeacon(char beacon) {
		int angle = 0;
		for (int i = 40; i >= -30; i -= 5) {
			moveIRSensor(i);
			if (getIRChar() == beacon) {
				angle = i;
				break;
			}
			sleep(200);
		}
		System.out.println("Beacon angle: " + angle);

		moveIRSensor(0);
		while (true) {
			if (getIRChar() == beacon) break;
			if (angle < 0) turnRightAround(10);
			else turnLeftAround(10);

			sleep(50);
		}
		System.out.println("Found");
	}

	// IR Sensor

	// move IR Sensor to an angle: 0 = front, -90 = right, 90 = left  
	public void moveIRSensor(int angle) {
		double slope = 7.0 / 9;
		double yIntercept = 100.67;

		int transformedAngle = (int) (slope * angle + yIntercept); 
		runPCAServo(CHANNEL_SERVO_IR, transformedAngle);
	}

	// Ping 6
	public int myGetPing(int channel) {
		if (channel == 6) {
			int dist = getPing(6);
			while (dist > 2000 || dist <= 0) {
				dist = getPing(6);
				sleep(20);
			}
			return dist;
		}
		return getPing(channel);
	}

}
