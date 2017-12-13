import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

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
		frame.setTitle("UI vs SubEx");
		
		//Set the game screen to a perfect rectangle
		panel = new PacPanel(scale, this, (int)(scale * gameWidth) );
		
		frame.add(panel);
		frame.addKeyListener(panel);
		
		frame.pack();	
		

		
	}
	
	public void win() {
		JLabel winLabel = new JLabel("You win the lawsuit!");
		winLabel.setFont(new Font("Consolas", Font.PLAIN, (int)(100 * scale)));
		winLabel.setVerticalAlignment((int) JFrame.CENTER_ALIGNMENT);
		winLabel.setHorizontalAlignment((int) JFrame.CENTER_ALIGNMENT);
		frame.add(winLabel);
		
		panel.setVisible(false);
		winLabel.setVisible(true);
		frame.repaint();
		winLabel.repaint();
	}
	
	public void lose() {
		JLabel loseLabel = new JLabel("You were sued!");
		loseLabel.setFont(new Font("Consolas", Font.PLAIN, (int)(100 * scale)));
		loseLabel.setVerticalAlignment((int) JFrame.CENTER_ALIGNMENT);
		loseLabel.setHorizontalAlignment((int) JFrame.CENTER_ALIGNMENT);
		frame.add(loseLabel);
		
		panel.setVisible(false);
		loseLabel.setVisible(true);
		frame.repaint();
		loseLabel.repaint();
	}
	
	public void run() {
		frame.setVisible(true);
	}

}
