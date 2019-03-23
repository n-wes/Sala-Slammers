public class Test {
	public static SalaSlammers robot = new SalaSlammers();
	
	
	public static void main(String[] args) {
		robot.setPort("COM5");
		robot.connect();
		
		// Do something here
		robot.runForward(1000);
		robot.sleep(1000);
		robot.findGap(1000);
		
		robot.close();
	}
}
