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
	private PacPanel panel;
		
	private String orientation;
	private String futureOrientation;
	
	private int moveState;
	
	public Pac(int setX, int setY, PacPanel setPanel) {
		moveState = 0;
		
		panel = setPanel;
		xLoc = (int)setX;
		yLoc = (int)setY;
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
		
		if (panel.getBoard()[yLoc + transl[1]][xLoc + transl[0]] != 1) {
			orientation = futureOrientation;
			return true;
		}
		
		return false;
	}
	
	
	
	public void update() {
		
		//Find next coordinates
		int[] transl = getTransform(orientation);
		int[] nextCoord = {xLoc + transl[0], yLoc + transl[1]};
		tryTurn();

		
		switch (moveState) {
			case 0:
				
				//Checks if there's no wall ahead
				boolean noWall = panel.getBoard()[ nextCoord[1] ][ nextCoord[0] ] != 1;
				
				if (noWall) {
					moveState = 1;
				}
				break;
				
			case 1:
				moveState++;
				break;
				
			case 2:
				moveState++;
				break;
				
			case 3:
				xLoc = nextCoord[0];
				yLoc = nextCoord[1];
				moveState = 0;

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
		
		
		g.fillOval( (int)( (xLoc) * size + transl[0] * moveState * (size/4.0)), 
				(int)( (yLoc) * size + transl[1] * moveState * (size/4.0)), size, size);

	}
}
