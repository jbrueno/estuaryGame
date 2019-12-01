package pkgMGModel;

import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pkgEnum.Game;
import pkgEnum.GameState;
import pkgMover.Mover;

public class WSModel extends MinigameModel{
	 
	// WS_COLLECT
	Mover Bottle;
	final int bottleImageWidth = 268;
	final int bottleImageHeight = 100;
	final int bottleX = backgroundWidth/2;
	
	final int maxHeight = bottleImageHeight;
	final int maxDepth = backgroundHeight-bottleImageHeight-100;
	
	int waterLevel = backgroundHeight/2;
	final private int COLLECT_SCORING_RANGE = 91; //half the height of the gradient guide
	final private int CORRECT_LEVEL = 395; //center of the gradient guide
	final private int MAX_COLLECT_POINTS = 200;
	int deepLevel=backgroundHeight*4/5;
	boolean filled = false;
	
	
	// WS_PH
	
	Mover pHStrip;
	boolean gotStrip=false;
	final int pHStripWidth = 30;
	final int pHStripHeight = 100;
	
	final int pHMax=9;
	final int pHMin=5;
	int pH;
	boolean isDipped = false;
	
	Mover testTube;
	final int testTubeImageWidth = 500;
	final int testTubeSideFromBorder = 185;
	final int testTubeImageHeight = 500;
	final int testTubeX = backgroundWidth / 3;
	final int testTubeY = backgroundHeight - 315;
	final int testTubeLeftSide = 365; // x-coord
	final int testTubeRightSide = 485; // x-coord
	final int testTubeWaterLevel = 425; // y-coord
	final int testTubeBottom = 680;
	// left = 365x
	// right = 485x
	// water = 425y
	// bottom = 680y
	boolean labSet = false;
	
	public WSModel() {
		g = Game.WATERSAMPLING;
		//gs = GameState.WS_COLLECT;
		gs= GameState.WS_PHTUTORIAL;
		//gs = GameState.WS_PH; 
		addObjects(gs);
	}
	
	//public Mover(int x, int y, int imageWidth, int imageHeight, int xIncr, int yIncr, String value) {
	public void addObjects(GameState gs) {
		
		switch (gs) {
		case WS_COLLECT :
			Bottle = new Bottle(bottleX, maxHeight, 0, 15, "Bottle");
			movers.add(Bottle);
			break;
		case WS_PHTUTORIAL:
			setPH();
			pHStrip = new pHStrip(0, 0, 0, 0, "pHStrip");
			testTube = new testTube(testTubeX, testTubeY, 0, 0, "testtube");
			movers.add(testTube);
			break;
		case WS_PH : 
			setPH();
			pHStrip = new pHStrip(0, 0, 0, 0, "pHStrip");
			testTube = new testTube(testTubeX, testTubeY, 0, 0, "testtube");
			movers.add(testTube);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void update(MouseEvent me) {		
		// Switch statement to differentiate between GameStates {START, WS_COLLECT, WS_PH, WS_TEMP, FINISHED}
		switch (gs) {
		case START :
			gs = GameState.WS_COLLECT;
		case WS_COLLECT :
		
			//double startx, double starty, double endx, double endy)
			Bottle.move(bottleX, maxHeight, bottleX, maxDepth);
			
			//System.out.println(me.getEventType());
			
			//JUST FOR TUTORIAL TESTING///
			if (me.getEventType()==MouseEvent.MOUSE_CLICKED) {
				movers.remove(Bottle);
				gs=GameState.WS_PH;
			}
			/////////
			
			if(!filled && Bottle.getY()> waterLevel && me.getEventType() == MouseEvent.MOUSE_PRESSED) {
				System.out.println("FILLING BOTTLE");
				fillBottle();
			}
			if(filled && Bottle.getY()< waterLevel && me.getEventType() == MouseEvent.MOUSE_CLICKED){
				movers.remove(Bottle);
				gs=GameState.WS_PH;
			}
			
			break;
		case WS_PHTUTORIAL :
			if(!labSet) { // if lab is not set up
				addObjects(gs);
				labSet = true;
			}
			
			if(me.getEventType()==MouseEvent.MOUSE_CLICKED) {
				if(!gotStrip) {
					movers.add(pHStrip);
					movers.add(testTube);
					gotStrip=true;
				}
			}
			pHStrip.move(me.getX(),me.getY());
			dipStrip(); 
			
			if(me.getEventType()==MouseEvent.MOUSE_PRESSED) {
				gs=GameState.WS_PH;
			}
			break;
		case WS_PH :
			if(!labSet) { // if lab is not set up
				addObjects(gs);
				labSet = true;
			}
			
			if(me.getEventType()==MouseEvent.MOUSE_CLICKED) {
				if(!gotStrip) {
					movers.add(pHStrip);
					movers.add(testTube);
					gotStrip=true;
				}
			}
			pHStrip.move(me.getX(),me.getY());
			dipStrip(); 
			break;

		case WS_TEMP :
			break;

		default :
			break;
		}// end of switch
		
	}	
		
	
		
	/**
	 * Checks if bottle has been filled, adds new full bottle object to datanode list if so
	 * 
	 * @author AG
	 * 
	 * @return boolean true if bottle is full
	 */
	
	public void fillBottle() {
		filled=true;
		Bottle.setValue("fullBottle");
		score += calculateCollectSore();
	}
	
	public void setPH() {
		pH=r.nextInt((pHMax - pHMin) + 1) + pHMin;
	}
	
	/*
	 * Calculates the Collecting Water Score by mapping the Bottle's current height within the gradient image on the background.
	 * The closer the bottle is to the middle, the more points you get, maximum being <code>MAX_COLLECT_POINTS</code>.
	 * 
	 * @author Ryan Peters
	 * @returns	score
	 */
	private int calculateCollectSore() {
		if (Bottle.getY() < waterLevel) {return 0;}
		int cScore = (int) (MAX_COLLECT_POINTS -  (2 * Math.abs(CORRECT_LEVEL - Bottle.getY())));
		return (cScore < 0) ? 0 : cScore;
	}

	
	/**
	 * @author Abrenner
	 * logic for determining if pHStrip is within bounds of testtube and has been dipped in water
	 * changes boolean isDipped to true upon meeting criteria
	 */
	public void dipStrip() {
		// setting up logic for dipping pHStrip within testTube bounds
		if(pHStrip.getX() >= testTubeLeftSide &&
			pHStrip.getX() <= testTubeRightSide &&
			pHStrip.getY() >= testTubeWaterLevel &&
			 pHStrip.getY() <= testTubeBottom) {
						
					isDipped = true;
					changeColor(pH);
		} // end if
	}
	
	public void changeColor(int ph) {
		pHStrip.setValue("pHStrip"+ph);
		
	}
	
	
	// Movers related to WS
	class Bottle extends Mover {
		public Bottle(int x, int y, int xIncr, int yIncr, String value) {
			super(x, y, bottleImageWidth, bottleImageHeight, xIncr, yIncr, value);
		}
 	}
	 
	public class pHStrip extends Mover{

		public pHStrip(int x, int y, int xIncr, int yIncr, String value) {
			super(x, y, pHStripWidth, pHStripHeight, xIncr, yIncr, value);
		}

	}
	
	
	public class testTube extends Mover{
		public testTube(int x, int y, int xIncr, int yIncr, String value) {
			super(x, y, testTubeImageWidth, testTubeImageHeight, xIncr, yIncr, value);
		}
	}
	
	
}