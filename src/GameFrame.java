import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameFrame {
	
	/**
	 * Relative "width" of the game screen.
	 */
	private int gameWidth = 1500;
	
	private int spawnX = 10;
	private int spawnY = 8;
	
	private double scale;
	private JFrame frame;
	private PacPanel panel;
	
	public GameFrame(double sizePctScreen) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Set a "scale" factor so that the size of the game is always proportional to the computer's screen
		scale = (sizePctScreen * screenSize.getHeight())/2160;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set the game screen to a perfect rectangle
		panel = new PacPanel(scale, this, (int)(scale * gameWidth) );
		
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.pack();	
		
	}
	
	public void run() {
		frame.setVisible(true);
	}

}
