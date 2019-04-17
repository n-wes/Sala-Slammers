import rxtxrobot.*;

public class SalaSlammers extends ArduinoUno {
	
	// Movement
	final protected int CHANNEL_LEFT_WHEEL = 15;
	final protected int CHANNEL_RIGHT_WHEEL = 14;
	
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
		int time = 720;
		
		turnLeft(time);
	}
	
	public void turnRight() {
		int time = 720;
		
		turnRight(time);
	}
	
	public void turnLeft(int time) {
		int speedLeft = -60;
		int speedRight = -60;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void turnRight(int time) {
		int speedLeft = 300;
		int speedRight = 300;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}


	
//	------------------------------------------------------------
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
}
