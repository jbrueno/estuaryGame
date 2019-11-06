package pkgMG;

import javafx.scene.input.MouseEvent;
import pkgEnum.Game;
import pkgMover.DataNode;
import pkgMover.FemaleHSC;
import pkgMover.MaleHSC;
import pkgMover.Mover;

public class HSCModel extends MinigameModel{
	
	public HSCModel() {
		g = Game.HSCCOUNT;
		createHSCrabs();
	}
	
	/**
	 * Adds 20 horshoecrabs (10 male/10 female) to the DataNode list <code>dns</code> located in the abstract
	 * class <code>MinigameModel</code> with random x,y starting location and random xIncr,yIncr within constricted ranges
	 * 
	 * @author Ryan Peters
	 * 
	 */
	private void createHSCrabs() {
		for (int i = 0; i < 10; i++) {
			dns.add(new MaleHSC(r.nextInt() % backgroundWidth, r.nextInt() % backgroundHeight,
					r.nextInt() % 5, r.nextInt() % 5));
		}
		
		for (int i = 0; i < 10; i++) {
			dns.add(new FemaleHSC(r.nextInt() % backgroundWidth, r.nextInt() % backgroundHeight,
					r.nextInt() % 5, r.nextInt() % 5));
		}
	}

	
	/**
	 * For each horseshoe crab, move() based on xIncr,yIncr
	 * 
	 * @author Ryan Peters
	 * @
	 */
	@Override
	public void update(MouseEvent me) {
		for (DataNode dn : dns) {
			Mover m = (Mover) dn;
			m.move();
		}
		
		//later handle click from mouseevent; maybe have crabs scatter from the light slowly
	}
	

}
