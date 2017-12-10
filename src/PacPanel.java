import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PacPanel extends JPanel implements ActionListener, KeyListener {

	public static final int TIMER_DELAY = 50;
	
	/**
	 * scale which allows it to run on different monitors with different resolutions
	 */
	private double scale;
	
	private GameFrame frame;
	
	/**
	 * height of board array
	 */
	int boardHeight;
	
	/**
	 * length of board array
	 */
	int boardWidth;
	
	/**
	 * The pixel length of each "block"
	 */
	int blockDim;
	
	/**
	 * the pacman himself
	 */
	Pac pac;
	
	int[][] board;
	
	Timer timer;
	
	
	
	public PacPanel(double setScale, GameFrame setFrame, int boardPixSize) {
		/*
		 * Creating the "map" for pacman.
		 * Empty Space = 0
		 * Wall = 1
		 * Small Ball = 2
		 * Big Ball = 3
		 * 
		 */
		int[][] setBoard =
		  { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{ 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
			{ 1, 3, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 3, 1},
			{ 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1},
			{ 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
			{ 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1},
			{ 1, 2, 1, 1, 1, 2, 1, 2, 2 ,2, 1, 2, 2, 2, 1, 2, 1, 1, 1, 2, 1},
			{ 1, 2, 2, 2, 2, 2, 1, 1, 1, 0, 1, 0, 1, 1, 1, 2, 2, 2, 2, 2, 1},
			{ 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1},
			{ 0, 0, 0, 0, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 0, 0, 0, 0},
			{ 1, 1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1, 1},
			{ 0, 0, 0, 0, 0, 2, 0, 0, 1, 1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 0, 0},
			{ 1, 1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1, 1},
			{ 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0},
			{ 1, 1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1, 1},
			{ 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
			{ 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1},
			{ 1, 3, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 3, 1},
			{ 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1},
			{ 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1},
			{ 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1},
			{ 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1},
			{ 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} };
		
		board = setBoard;
		
		pac = new Pac(11, 8, this);
		
		scale = setScale;
		frame = setFrame;
		
		boardHeight = board.length;
		boardWidth = board[0].length;
		
		//Sets the dimensions of this panel proportional to the dimensions of the board array
		this.setSize( new Dimension( (int)(boardPixSize), (int) (boardPixSize * (double)boardHeight) / boardWidth) );
		this.setPreferredSize(this.getSize());
		
		
		blockDim = (int)((double)this.getWidth()/boardWidth);
				
		timer = new Timer(TIMER_DELAY, this);
		timer.start();
	}
	
	public void startTimer() {
		timer.start();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public int getBlockDim() {
		return blockDim;
	}
	
	public void actionPerformed(ActionEvent e) {
		pac.update();
		board[pac.getY()][pac.getX()] = 0;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		drawCurrentBoard(g);
		pac.paint(g);
	}	
	
	private void drawCurrentBoard(Graphics g) {
		super.paintComponent(g);
		
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				switch (board[y][x]) {
					case 1:
						//Blue
						g.setColor(Color.decode("#0000AA"));
						
						g.fillRect( (int)(blockDim * x), (int)(blockDim * y),
								(int)(blockDim), (int)(blockDim) );
						this.repaint();
						break;
					
					case 2:
						g.setColor(Color.YELLOW);
						
						g.fillOval( (blockDim * x) + (int)(blockDim/3.0), (blockDim * y) + (int)(blockDim/3.0), (int)(blockDim/3.0), (int)(blockDim/3.0));
						this.repaint();
						break;
						
					case 3:
						g.setColor(Color.ORANGE);
						
						g.fillOval( (blockDim * x) + (int)(blockDim/8.0), (blockDim * y) + (int)(blockDim/8.0), (int)(3 * blockDim/4.0), (int)(3 * blockDim/4.0));
						this.repaint();
						break;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
				
		switch (key.getKeyCode()) {
			case KeyEvent.VK_UP:
				pac.setOrientation("up");
				break;
			
			case KeyEvent.VK_RIGHT:
				pac.setOrientation("right");
				break;
				
			case KeyEvent.VK_DOWN:
				pac.setOrientation("down");
				break;
				
			case KeyEvent.VK_LEFT:
				pac.setOrientation("left");
				break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
