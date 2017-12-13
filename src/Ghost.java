import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Ghost {
	
	private static final int[] UP_DELTA = new int[] {0, -1};
	private static final int[] RIGHT_DELTA = new int[] {1, 0};
	private static final int[] DOWN_DELTA = new int[] {0, 1};
	private static final int[] LEFT_DELTA = new int[] {-1, 0};

	private static final int PANIC_DURATION = 100;
	
	public boolean panic;
	public int panicDelay;
	
	private Color ghostColor;
	private PacPanel panel;
	private Pac pacman;
	
	private String orientation;
	
	private int xLoc;
	private int yLoc;
	
	private double xSub;
	private double ySub;
	
	private int delay;
	
	private int chaseAlg;
	
	private BufferedImage img;
	
	public Ghost(PacPanel setPanel, Pac setPacman, Color setColor, int startX, int startY, int setDelay, int setChaseAlg) {
		
		try {
			img = ImageIO.read(new File(".\\SUBEX_LOGO.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		panic = false;
		panicDelay = 0;
		chaseAlg = setChaseAlg;
		
		orientation = "up";
		
		ghostColor = setColor;
		panel = setPanel;
		pacman = setPacman;
		
		delay = setDelay;
		
		xLoc = startX;
		yLoc = startY;
		xSub = 0;
		ySub = 0;
	}
	
	public boolean getPanic() {
		return panic;
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
	
	
	public void panic() {
		if (!panic) {
			orientation = oppositeOrientation(orientation);
		}
		
		panic = true;
		panicDelay = PANIC_DURATION;

	}
	
	public void setChase(int setNum) {
		chaseAlg = setNum;
	}
	
	public void update() {
		
		//Initial delay to give player a chance to move about
		if (delay > 0) {
			delay--;
			return;
		}
		
		if (panicDelay > 0) {
			panicDelay--;
		} else {
			panic = false;
		}
		
		if (chaseAlg == 0 || panic) {
			directTurning();
		} else if (chaseAlg == 1) {
			aheadTurning();
		}
		
		move();
	}

	private void directTurning() {
		//Check if piece is at intersection
		
		/**
		 * An array representing the distances for all 4 directions. 0 is up, 1 is right, 2 is down, 3 is left.
		 */
		double[] distances = new double[4];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = Double.MAX_VALUE;
		}
		
		//Fills up the array with all the distances between the potential turns and the pacman
		if (xSub == 0 && ySub == 0) {
			if (panel.getBoard()[ yLoc + UP_DELTA[1] ][ xLoc + UP_DELTA[0] ] != 1) {
				distances[0] = distBetween( xLoc + UP_DELTA[0], yLoc + UP_DELTA[1], pacman.getX(), pacman.getY());
			}
			
			if (panel.getBoard()[ yLoc + RIGHT_DELTA[1] ][ xLoc + RIGHT_DELTA[0] ] != 1) {
				distances[1] = distBetween( xLoc + RIGHT_DELTA[0], yLoc + RIGHT_DELTA[1], pacman.getX(), pacman.getY());
			}
			
			if (panel.getBoard()[ yLoc + DOWN_DELTA[1] ][ xLoc + DOWN_DELTA[0] ] != 1) {
				distances[2] = distBetween( xLoc + DOWN_DELTA[0], yLoc + DOWN_DELTA[1], pacman.getX(), pacman.getY());
			}
			
			if (panel.getBoard()[ yLoc + LEFT_DELTA[1] ][ xLoc + LEFT_DELTA[0] ] != 1) {
				distances[3] = distBetween( xLoc + LEFT_DELTA[0], yLoc + LEFT_DELTA[1], pacman.getX(), pacman.getY());
			}
			
			//The opposite way it's facing is MAX_VALUE so it never "turns 180 degrees around"
			distances[ String2NumDir(oppositeOrientation(orientation)) ] = Double.MAX_VALUE;
			
			int wayToTurn;
			
			if (!panic) {
				wayToTurn = findSmallestIndex(distances);
			} else {
				wayToTurn = findBiggestIndex(distances);
			}
			
			
			orientation = numDir2String(wayToTurn);
		}
	}
	
	private void aheadTurning() {
		//Check if piece is at intersection
		
		/**
		 * An array representing the distances for all 4 directions. 0 is up, 1 is right, 2 is down, 3 is left.
		 */
		double[] distances = new double[4];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = Double.MAX_VALUE;
		}
		
		int[] pacAheadCoords = pacAheadCoords();
		
		//Fills up the array with all the distances between the potential turns and the pacman
		if (xSub == 0 && ySub == 0) {
			if (panel.getBoard()[ yLoc + UP_DELTA[1] ][ xLoc + UP_DELTA[0] ] != 1) {
				distances[0] = distBetween( xLoc + UP_DELTA[0], yLoc + UP_DELTA[1], pacAheadCoords[0] , pacAheadCoords[1]);
			}
			
			if (panel.getBoard()[ yLoc + RIGHT_DELTA[1] ][ xLoc + RIGHT_DELTA[0] ] != 1) {
				distances[1] = distBetween( xLoc + RIGHT_DELTA[0], yLoc + RIGHT_DELTA[1], pacAheadCoords[0] , pacAheadCoords[1]);
			}
			
			if (panel.getBoard()[ yLoc + DOWN_DELTA[1] ][ xLoc + DOWN_DELTA[0] ] != 1) {
				distances[2] = distBetween( xLoc + DOWN_DELTA[0], yLoc + DOWN_DELTA[1], pacAheadCoords[0] , pacAheadCoords[1]);
			}
			
			if (panel.getBoard()[ yLoc + LEFT_DELTA[1] ][ xLoc + LEFT_DELTA[0] ] != 1) {
				distances[3] = distBetween( xLoc + LEFT_DELTA[0], yLoc + LEFT_DELTA[1], pacAheadCoords[0] , pacAheadCoords[1]);
			}
			
			//The opposite way it's facing is MAX_VALUE so it never "turns 180 degrees around"
			distances[ String2NumDir(oppositeOrientation(orientation)) ] = Double.MAX_VALUE;
			
			int wayToTurn;
			
			if (!panic) {
				wayToTurn = findSmallestIndex(distances);
			} else {
				wayToTurn = findBiggestIndex(distances);
			}
			
			
			orientation = numDir2String(wayToTurn);
		}
	}
	
	private int[] pacAheadCoords() {
		String direction = pacman.getOrient();
		
		switch (direction) {
			case "up": return new int[] {pacman.getX(), pacman.getY() - 5};
			case "right": return new int[] {pacman.getX() + 5, pacman.getY()};
			case "down": return new int[] {pacman.getX(), pacman.getY() + 5};
			case "left": return new int[] {pacman.getX() - 5, pacman.getY()};
		}
		
		return null;
	}
	
	
	public String oppositeOrientation(String inputOrient) {
		
		if (inputOrient == null) {
			return null;
		}
		
		switch (inputOrient) {
			case "up": return "down";
			case "right": return "left";
			case "down": return "up";
			case "left": return "right";
		}
		return null;
	}
	
	/**
	 * Returns the index of the smallest value.
	 * @param input the array to find the smallest value in.
	 * @return the index of the smallest value.
	 */
	public int findSmallestIndex(double[] input) {
		double smallestValue = input[0];
		int smallestIndex = 0;
		
		for (int i = 1; i < input.length; i++) {
			if (smallestValue > input[i]) {
				smallestIndex = i;
				smallestValue = input[i];
			}
		}
		
		return smallestIndex;
	}
	
	public int findBiggestIndex(double[] input) {
		double smallestValue = -1;
		int smallestIndex = -1;
		
		for (int i = 0; i < input.length; i++) {
			if (input[i] == Double.MAX_VALUE) {
				continue;
			}
			
			if (smallestValue < input[i]) {
				smallestIndex = i;
				smallestValue = input[i];
			}
		}
		
		return smallestIndex;
	}
	
	public int String2NumDir(String direction) {
		if (direction == null) {
			return 0;
		}
		
		switch (direction) {
			case "up": return 0;
			case "right": return 1;
			case "down": return 2;
			case "left": return 3;
		}
		return -1;
	}
	
	public String numDir2String(int direction) {
		switch (direction) {
			case 0: return "up";
			case 1: return "right";
			case 2: return "down";
			case 3: return "left";
		}
		return null;
	}
	
	public double distBetween(int x1, int y1, int x2, int y2) {
		return Math.sqrt( (x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2) );
	}
	
	
	
	public void move() {
		int[] delta = getTransform(orientation);
		
		//Takes care of the "loop around" in the tunnels to the left and right
		if (xLoc + xSub + delta[0] * .333 > 19.5) {
			xLoc = 0;
			xSub = 0.667;
		} else if (xLoc + xSub + delta[0] * .333 < 0.5) {
			xLoc = 19;
			xSub = .333;
		}
		
		boolean noWallAhead = panel.getBoard()[ (int)(yLoc + delta[1]) ][ (int)(xLoc + delta[0]) ] != 1;
		
		System.out.println(xLoc + xSub + " " + (yLoc + ySub) );
		
		//if there's no wall ahead, move forward. If there is a wall ahead, as long as you're moving towards the "center" of a block, keep going
		if ( noWallAhead || (xSub + delta[0] * .333 == 0 && ySub + delta[1] * .333 == 0)) {
			xSub += delta[0] * .333;
			ySub += delta[1] * .333;
		}

		if (noWallAhead) {
			// only move forward in the x direction of the y direction is both zeroed and there's no wall
			if (ySub == 0) {
				
				if (xSub > .5) {
					xLoc += 1;
					xSub = -.333;
				} else if (xSub < -.5) {
					xLoc -= 1;
					xSub = .333;
				}
			}
			
			// only move forward in the y direction of the x direction is both zeroed and there's no wall
			if (xSub == 0) {
				
				if (ySub > .5) {
					yLoc += 1;
					ySub = -.333;
				} else if (ySub < -.5) {
					yLoc -= 1;
					ySub = .333;
				}
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
		if (!panic) {
			g.setColor(ghostColor);
		} else {
			g.setColor(Color.BLUE);
			if ( panicDelay < 50 && (panicDelay / 5) % 2 == 0) {
				g.setColor(Color.WHITE);
			}
		}
		
		int size = panel.getBlockDim();
		
		if (img == null) {
			g.fillPolygon(new int[] { (int)(size * (xLoc + xSub) + size/2.0),	(int)(size * (xLoc + xSub)),	 (int)(size * (xLoc + xSub + 1)) } ,
						  new int[] { (int)(size * (yLoc + ySub)),	(int)(size * (yLoc + ySub + 1) ), (int)(size * (yLoc + ySub + 1) ) } ,	3);
		} else {
			g.fillOval( (int)( size * (xLoc + xSub)), 
					(int)( size * (yLoc + ySub)), size, size);
			g.drawImage(img, (int)( size * (xLoc + xSub)), (int)( size * (yLoc + ySub + .1)), size, size, null);
		}
	}
	
}
