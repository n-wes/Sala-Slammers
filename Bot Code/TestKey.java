import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
public class TestKey extends SalaSlammers{
	
	public TestKey() {
		
		super();
		JFrame frame = new JFrame();
		frame.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	        }

	        @Override
	        public void keyPressed(KeyEvent e) {
	        	switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
						runForward(250, 0);
					}
					else runForward(150, 0);
					break;
					
				case KeyEvent.VK_LEFT:
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
						turnLeft(0);
					}
					else turnLeftAround(0);
					break;
				
				case KeyEvent.VK_RIGHT:
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
						turnRight(0);
					}
					else turnRightAround(0);
					break;
				case KeyEvent.VK_DOWN:
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
						runForward(-250, 0);
					}
					else runForward(-150, 0);
					break;
				
				case KeyEvent.VK_ENTER:
					close();
					frame.setVisible(false);
					break;
				
				default:
					allPCAStop();
					break;
				}
	        }

	        @Override
	        public void keyReleased(KeyEvent e) {
	        	allPCAStop();
	        }
	    });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
        frame.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		TestKey robot = new TestKey();
//		robot.runForward(200, 0);
	}
}