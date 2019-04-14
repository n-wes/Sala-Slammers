import rxtxrobot.*;

public class SalaSlammers extends ArduinoUno {
	
	// Movement
	final public int CHANNEL_LEFT_WHEEL = 15;
	final public int CHANNEL_RIGHT_WHEEL = 14;
	
	// Find beacons
	final public int CHANNEL_SERVO_IR = 1;
	final private int BEACON_READING_COUNT = 1;
	
	// Ping sensor
	final public int PING_PIN = 5;
	
	
//	------------------------------------------------------
//	Constructor
	
	public SalaSlammers() {
		super();
	}

//	-----------------------------------------------------	
//	Movement
	
	public void runForward(int time) {
		int	speedLeft = 415; 
		int	speedRight = -165;
					
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void upSlope(int time) {
		int speedLeft = 540; 
		int speedRight = -295;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void runForwardSlow(int time) {
		int	speedLeft = 305; 
		int	speedRight = -50;
					
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void runBackward(int time) {
		int speedLeft = -120;
		int speedRight = 315;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void turnLeft() {
		int time = 420;
		
		turnLeft(time);
	}
	
	public void turnRight() {
		int time = 350;
		
		turnRight(time);
	}
	
	public void turnLeft(int time) {
		int speedLeft = -58;
		int speedRight = -58;
		
		runTwoPCAMotor(CHANNEL_LEFT_WHEEL, speedLeft, CHANNEL_RIGHT_WHEEL, speedRight, time);
	}
	
	public void turnRight(int time) {
		int speedLeft = 308;
		int speedRight = 308;
		
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
