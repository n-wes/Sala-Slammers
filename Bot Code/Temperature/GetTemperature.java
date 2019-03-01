import rxtxrobot.*;
/*
import rxtxrobot.ArduinoUno;
import rxtxrobot.RXTXRobot;
 */

public class GetTemperature {
	
	public static RXTXRobot robot = new ArduinoUno();
	
	public static double slope = -8.48;
	public static double intercept = 707.3;
	public static double elapsedTime = 0.0;
	public static double startTime = 0.0;
	
	public static double getThermistorReading() {
		 int sum = 0;
		 int readingCount = 100;
		 
		 //Read the analog pin values ten times, adding to sum each time
		 for (int i = 0; i < readingCount; i++) {
			
			 //Refresh the analog pins so we get new readings
			 robot.refreshAnalogPins();
			 int reading = robot.getAnalogPin(0).getValue();
			 sum += reading;
		 }
		 
		 //Return the average reading
		 return sum * 1.0 / readingCount;
	}
	
	public static double getTemperature(double thermistorReading) {
		return (thermistorReading - intercept) / slope;
	}
		
	public static void main(String[] args) {

		 //Connect to the arduino
		 robot = new ArduinoUno();
		 robot.setPort("COM5");
		 robot.connect();

		 startTime = System.currentTimeMillis();
		 
		 //Get the average thermistor reading
		 double thermistorReading = getThermistorReading();

		 //Print the results
		 System.out.println("The probe read the value: " + thermistorReading);
		 System.out.printf("The temperature is: %.2f C degrees.",getTemperature(thermistorReading));
		 
		 elapsedTime = System.currentTimeMillis() - startTime;
		 System.out.printf("\nAll operations took %.2f seconds.",(elapsedTime / 1000));
		 
		 //System.out.println("In volts: " + (thermistorReading * (5.0/1023.0)));
		 robot.close();
	}
}