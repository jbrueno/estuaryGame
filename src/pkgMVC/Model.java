package pkgMVC;
import pkgEnum.Direction;
import pkgEnum.Game;
import pkgMG.*;
import pkgMGView.AMView;
import pkgMGView.HSCView;
import pkgMGView.LeaderboardView;
import pkgMGView.MainScreenView;
import pkgMGView.MinigameView;
import pkgMGView.SCView;
import pkgMGView.WSView;
import pkgMover.DataNode;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.input.MouseEvent;

/**
 * Class responsible for keeping track of the current MinigameModel <code>currGame</code> and passing in updates from the 
 * View into this <code>currGame</code>.
 * 
 * 
 * @author Ryan Peters
 * @see View
 *
 */
public class Model {
	private ArrayList<MinigameModel> minigames;
	private MinigameModel currGame;	
	
	/**
	 * Constructor that creates the list of MinigameModels <code>minigames</code>
	 * 
	 * @author Ryan Peters
	 * @see createModels()
	 */
	public Model() {
		createModels();
	}
	
	/**
	 * 
	 * @param g
	 * @param me
	 */
	public void update(Game g, MouseEvent me) {
		if (!isCurrGame(g)) {
			currGame = minigames.get(g.ordinal());
		}
		currGame.update(me);
	}	
	
	/**
	 * Checks is the passed in Game enum matches the <code>currGame</code>'s enum. Returns corresponding boolean
	 * 
	 * @author Ryan Peters
	 * @param g	Game enum to be checked against currGame Game enum
	 * @return	whether the parameter equals the currGame Game enum
	 */
	private boolean isCurrGame(Game g) {
		return currGame.getGame() == g;
	}
	
	/**
	 * Returns the list of DataNodes from the current MinigameModel being loaded (<code>currGame</code>)
	 * 
	 * @author Ryan Peters
	 * @return
	 * @see MinigameModel.getDataNodes()
	 */
	public ArrayList<DataNode> getDataNodes() {
		return currGame.getDataNodes();
	}
	
	/**
	 * Initializes and creates the list of MinigameModels to used in order to load the correct MinigameModel based on 
	 * Game enum.ordinal()
	 * <p>
	 * As default, sets the currGame to MainScreenModel as it is the first to be loaded
	 * <p>
	 * Note: adding must be done in this order so indexing by Game.ordinal() works
	 * 
	 * @author Ryan Peters
	 * @see MinigameModel
	 */
	private void createModels() {
		minigames = new ArrayList<MinigameModel>();
		
		minigames.add(new MainScreenModel());
		minigames.add(new AMModel());
		minigames.add(new HSCModel());
		minigames.add(new SCModel());
		minigames.add(new WSModel());
		minigames.add(new LeaderboardModel());
		
		currGame = minigames.get(0);	
	}
	
}
