import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Pac {
	
	private static final int FRAME_DELAY = 50;
	
	private int xLoc;
	private int yLoc;
	
	/**
	 * "Subcoordinate" that is within each "block". Within each block, there can be 3 different locations
	 */
	private double xSub;
	private double ySub;
	
	private PacPanel panel;
		
	private String orientation;
	private String futureOrientation;
	
	private int moveState;
	
	public Pac(int setX, int setY, PacPanel setPanel) {
		moveState = 0;
		
		panel = setPanel;
		
		xLoc = setX;
		yLoc = setY;
		xSub = 0;
		ySub = 0;
	}
	
	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}
	
	public void setOrientation(String setOrient) {
		futureOrientation = setOrient;
		
		if (!futureOrientation.equals(orientation) && moveState == 0) {
			tryTurn();
		}
	}
	
	private boolean tryTurn() {
		int[] transl = getTransform(futureOrientation);
		
		if (panel.getBoard()[yLoc + transl[1]][xLoc + transl[0]] != 1 
				&& xSub == 0 && ySub == 0) {
			orientation = futureOrientation;
			return true;
		}
		
		return false;
	}
	
	
	
	public void update() {

		tryTurn();
		
		move();
	}
	
	public void move() {
		int[] delta = getTransform(orientation);
		
		boolean noWallAhead = panel.getBoard()[ (int)(yLoc + delta[1]) ][ (int)(xLoc + delta[0]) ] != 1;
		
		System.out.println(xLoc + xSub + " " + (yLoc + ySub) );
		
		if (xLoc + xSub + delta[0] * .333 > 19.5) {
			xLoc = 0;
			xSub = 0.667;
		} else if (xLoc + xSub + delta[0] * .333 < 0.5) {
			xLoc = 19;
			xSub = .333;
		}
		
		if ( (xSub + delta[0] * .333 == 0 && ySub + delta[1] * .333 == 0) || noWallAhead) {
			xSub += delta[0] * .333;
			ySub += delta[1] * .333;
		}
		
		if (ySub == 0 && noWallAhead) {
			
			if (xSub > .5) {
				xLoc += 1;
				xSub = -.333;
			} else if (xSub < -.5) {
				xLoc -= 1;
				xSub = .333;
			}
		}
		
		if (xSub == 0 && noWallAhead) {
			
			if (ySub > .5) {
				yLoc += 1;
				ySub = -.333;
			} else if (ySub < -.5) {
				yLoc -= 1;
				ySub = .333;
			}
		}
		
	}
	
	private int[] getTransform(String direction) {
		if (direction == null) {
			return new int[] {0, 0};
		}
		
		switch (direction) {
			case "up":
				return new int[] {0, -1};
				
			case "right":
				return new int[] {1, 0};
				
			case "down":
				return new int[] {0, 1};
				
			case "left": 
				return new int[] {-1,0};
				
			default:
				return null;
		}

	}
	
	public void paint(Graphics g) {
		
		g.setColor(Color.GREEN);
		int size = panel.getBlockDim();
		
		int[] transl = getTransform(orientation);
		
		
		g.fillOval( (int)( size * (xLoc + xSub)), 
				(int)( size * (yLoc + ySub)), size, size);

	}
}
