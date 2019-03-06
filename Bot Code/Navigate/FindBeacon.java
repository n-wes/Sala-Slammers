import rxtxrobot.*;

public class FindBeacon {

	public static void main(String[] args) {
		
		RXTXRobot r = new ArduinoUno();
		r.setPort("COM5");
		r.setVerbose(true);
		r.connect();
		
		char beacon = ' ';
		int locationK = 200;
		int locationG = 200;
		
		int [] angles = {0,10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180};
		
		r.runPCAServo( 0 , angles[0] );
		
		for (int i = 0; i <= 18; i++) {
			
			System.out.println("Running Servo to next angle...");
			r.runPCAServo( 0 , angles[i] );
			
			System.out.println("Taking Samples...");
			beacon = TakeSamples(r);
			
			if (beacon == 'K') {
				locationK = i * 10;
			}
			else if (beacon == 'G') {
				locationG = i * 10;
			}
			
			System.out.println("That sample picked up - " + beacon + " at angle - " + (i * 10));
			
		}//end for loop

		System.out.println("beacon K: " + locationK + "\nbeacon G: " + locationG);
		
		r.close();
		
	}//end main 

	
	public static char TakeSamples(RXTXRobot r) {
		
		int k = 0;
		int g = 0;
		char returnChar = ' ';
		
		for (int samples = 1; samples <= 10; samples++) {
			
			char ch = r.getIRChar();
			
			if(ch == 'K') {
				k++;
			}
			else if(ch == 'G') {
				g++;
			}
					
		}//end for loop
		
		if(k == 10) {
			returnChar = 'K';
		}
		else if (g == 10) {
			returnChar = 'G';
		}
		else {
			returnChar = '0';
		}
		
		return returnChar;
		
	}//end function TakeSamples
	
}//end of class FindBeacon
