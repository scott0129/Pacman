import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
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
	
	private BufferedImage img;
	
	
	public Pac(PacPanel setPanel, int setX, int setY) {
		

		try {
			img = ImageIO.read(new File(".\\UI_LOGO.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		panel = setPanel;
		
		xLoc = setX;
		yLoc = setY;
		xSub = 0;
		ySub = 0;
	}
	
	public String getOrient() {
		return orientation;
	}
	
	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}
	
	/**
	 * returns "literal" distance, which is the grid# plus subarray.
	 * @return xLoc + xSub.
	 */
	public double getLitX() {
		return xLoc + xSub;
	}
	
	/**
	 * returns "literal" distance, which is the grid# plus subarray
	 * @return yLoc + ySub.
	 */
	public double getLitY() {
		return yLoc + ySub;
	}
	
	
	public void setOrientation(String setOrient) {
		futureOrientation = setOrient;
		
		if ( !futureOrientation.equals(orientation) ) {
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
		
		//Takes care of the "loop around" in the tunnels to the left and right
		if (xLoc + xSub + delta[0] * .333 > 19.5) {
			xLoc = 0;
			xSub = 0.667;
		} else if (xLoc + xSub + delta[0] * .333 < 0.5) {
			xLoc = 19;
			xSub = .333;
		}
		
		//if there's no wall ahead, move forward. If there is a wall ahead, as long as you're moving towards the "center" of a block, keep going
		if ( noWallAhead || (xSub + delta[0] * .333 == 0 && ySub + delta[1] * .333 == 0)) {
			xSub += delta[0] * .333;
			ySub += delta[1] * .333;
		}
		
		// only move forward in the x direction of the y direction is both zeroed and there's no wall
		if (ySub == 0 && noWallAhead) {
			
			if (xSub > .5) {
				xLoc += 1;
				xSub = -.333;
			} else if (xSub < -.5) {
				xLoc -= 1;
				xSub = .333;
			}
		}
		
		// only move forward in the y direction of the x direction is both zeroed and there's no wall
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
		
		int size = panel.getBlockDim();	
		
		if (img == null) {
			
			g.setColor(Color.GREEN);
			
			g.fillOval( (int)( size * (xLoc + xSub)), 
					(int)( size * (yLoc + ySub)), size, size);
			
		} else {
			g.drawImage(img, (int)( size * (xLoc + xSub)), (int)( size * (yLoc + ySub + .1)), size, size, null);
		}

	}
}
