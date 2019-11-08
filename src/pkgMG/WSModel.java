package pkgMG;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pkgEnum.Game;
import pkgMover.Mover;

public class WSModel extends MinigameModel{
	
	Mover Bottle;
	Mover fullBottle;
	final int bottleImageWidth = 200;
	final int bottleImageHeight = 200;
	
	
	int waterLevel = 450;
	int maxDepth = 800;
	boolean filled = false;
	
	public WSModel() {
		g = Game.WATERSAMPLING;
		addObjects();
	}
	
	//public Mover(int x, int y, int imageWidth, int imageHeight, int xIncr, int yIncr, String value) {
	public void addObjects() {
<<<<<<< HEAD
		Bottle= new Bottle(600,50,0,10,"Bottle");
		fullBottle=new Bottle(600, backgroundHeight/2, 0, -8, "fullBottle");
		dns.add(Bottle);
=======
		Bottle= new Bottle(600,50,200,200,0,10,"Bottle");
		fullBottle=new Bottle(600, backgroundHeight/2, 200, 200, 0, -8, "fullBottle");
		movers.add(Bottle);
>>>>>>> branch 'Ryan' of https://github.com/CISC275-Fall2019/cisc275f19-project-cisc275f19-team-11-0
	}
	
	@Override
	public void update(MouseEvent me) {
		System.out.println(backgroundHeight);
		Bottle.move();
		System.out.println("y: "+Bottle.getY());
		if(Bottle.getY() >= backgroundHeight/2) {
			filled = true;
		}
		
		if(checkFill()) {
		fullBottle.move();
		}
	}

	/**
	 * Checks if bottle has been filled, adds new full bottle object to datanode list if so
	 * 
	 * @author AG
	 * 
	 * @return boolean true if bottle is full
	 */
	public boolean checkFill() {
		if (filled) {
			if(!movers.contains(fullBottle)) {
				movers.remove(Bottle);
				movers.add(fullBottle);
			}
			return true;
		}
		return false;
	}
	 
	public class Bottle extends Mover{

		public Bottle(int x, int y, int imageWidth, int imageHeight, int xIncr, int yIncr, String value) {
			super(x, y, imageWidth, imageHeight, xIncr, yIncr, value);
		}
	}
	
<<<<<<< HEAD
	class Bottle extends Mover {
		public Bottle(int x, int y, int xIncr, int yIncr, String value) {
			super(x, y, bottleImageWidth, bottleImageHeight, xIncr, yIncr, value);
		}
 	}
	
	
=======
	public class PHStrip extends Mover{

		public PHStrip(int x, int y, int imageWidth, int imageHeight, int xIncr, int yIncr, String value) {
			super(x, y, imageWidth, imageHeight, xIncr, yIncr, value);
		}
	}
>>>>>>> branch 'Ryan' of https://github.com/CISC275-Fall2019/cisc275f19-project-cisc275f19-team-11-0
}
