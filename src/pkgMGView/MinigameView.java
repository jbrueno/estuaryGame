package pkgMGView;


//testing branch
//test
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

//import java.awt.Font;
import javafx.scene.paint.Color;
import pkgEnum.Direction;
import pkgEnum.GameState;
import pkgEnum.Game;
import pkgMover.DataNode;
import pkgMover.Mover;

public abstract class MinigameView {
	Image background;
	int backgroundWidth = 1280;
	int backgroundHeight = 768;

	MouseEvent me;
	Canvas c;
	GraphicsContext gc;
	Group root;
	Scene scene;
	Game game;
	final Game theGame; // final in each subclass
	boolean areButtonsMade = false;
	Text scoreBox = new Text();
	Button btnReturn;
	Random r = new Random();

	Image gameOver;
	int goX = (backgroundWidth / 2) - 250;
	int goY = (backgroundHeight / 2) - 150;
	int goWidth = 500;
	int goHeight = 300;
	int backToMainWidth = 500;
	int backToMainHeight = 50;
	int buffer = 25;
	// ** need to modify update() to update total Score by getting sum of MiniGames
	// **
	int currScore; // individual miniGameScore , may be easier to track by making individual attributes in each subclass
	int totalScore; // Overall score, sum of miniGameScores
	Label scoreLabel = new Label();
	int scoreLabelWidth = 150;
	int scoreLabelHeight = 100;

	Label displayTime = new Label();
	int displayTimeX = (backgroundWidth / 2) - 150;
	int displayTimeY = 15;
	
	/////TUTORIAL STUFF//////
	Label tutorialLabel;
	int tutorialX = 0;
	int tutorialY = backgroundHeight*2/5;
	int tutorialStep = 0;
	
	Button btnPlay;
	final double btnPlayX = backgroundWidth*4/5;
	final double btnPlayY = backgroundHeight*3/5;
	boolean btnPlayAdded=false;
	boolean play=false;
	boolean isBackToMainDrawn = false;
	/////STUFF FOR TUTORIALS////

	ArrayList<DataNode> dns = new ArrayList<DataNode>();
	ArrayList<Button> buttonList = new ArrayList<Button>();;
	
	//pre-loaded images database
	HashMap<String,Image> imgDB = new HashMap<String,Image>();
	
	//TUTORIAL
	abstract void drawTutorial(int step);
	abstract void updateTutorialStep(MouseEvent me);
	
	public abstract void update(ArrayList<Mover> movers, GameState gs, int score, int time);
	abstract void startTimer(int ms);
	abstract void stopTimer();
	//abstract void setUpListeners();
	abstract void draw(ArrayList<Mover> movers);
	abstract void importImages();

	// EVENTHANDLER!! This sees all mouse events in minigames
	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			me = e;
		}
	};

	public MinigameView(Game theGame) {
		this.theGame = theGame;
	}
	
	/**
	 * Finds and returns image associated with given Mover object
	 * @param pkgName package to determine where image is located
	 * @param m Mover - used to grab value attribute (name of image)
	 * @return Image img - image associated with that particular Mover object
	 */
	public Image loadImage(Mover m) {
		return loadImage(m.getValue(), "Mover/");
	}
	
	public Image loadBackgroundImage(String bgName) {
		return loadImage(bgName,"backgrounds/");
	}
	
	public Image loadButtonImage(String btnName) {
		return loadImage(btnName,"Buttons/");
	}
	public Image loadImage(String key, String dir) {
		Image img;
		if (imgDB.containsKey(key)) {
			return imgDB.get(key);
		} 
		System.out.println(dir + key + ".png");
		img = new Image(dir + key + ".png");
		imgDB.put(key, img);
		return img;
	}

	
	void setUpListeners() {
		
		btnReturn = new Button("Return");
		btnReturn.setLayoutX(0);
		btnReturn.setLayoutY(0);
		btnReturn.setOnAction(e -> {
			clearFX();
			game = Game.MAINSCREEN;
		});
		root.getChildren().add(btnReturn);
	}

	public double getAngle(Direction d) {
		double angle = 0;
		switch (d) {
		case NORTH: {
			angle = -60.0;
			break;
		}
		case NORTHEAST: {
			angle = -30.0;
			break;
		}
		case EAST: {
			angle = 0.0;
			break;
		}
		case SOUTHEAST: {
			angle = 30.0;
			break;
		}
		case SOUTH: {
			angle = 60.0;
			break;
		}
		case SOUTHWEST: {
			// isFlipped = true;
			angle = -30.0;
			break;
		}
		case WEST: {
			// isFlipped = true;
			break;
		}
		case NORTHWEST: {
			// isFlipped = true;
			angle = 30.0;
			break;
		}
		}
		return angle;
	}

	public Group getRoot() {
		return root;
	}

	public Scene getScene() {
		return scene;
	}

	public MouseEvent getMouseEvent() {
		return me;
	}

	/**
	 * Clears the current FX being displayed.
	 * <p>
	 * GC is wiped and root is cleared of all children (Buttons, etc) which includes
	 * canvas to which gc draws upon. This is handled in <code>View</code> class
	 * under <code>getGame()</code>
	 * 
	 * @author Ryan Peters
	 * @see View
	 * @see View.getGame()
	 */
	public void clearFX() {
		gc.clearRect(0, 0, backgroundWidth, backgroundHeight);
		root.getChildren().clear();
		areButtonsMade = false;
	}
	/*
	 * public void draw(Mover m) { gc.drawImage(loadImage("Mover", m.getValue()),
	 * m.getX(), m.getY(), m.getImageWidth(), m.getImageWidth()); }
	 */

	public void draw(Mover m) {
		gc.drawImage(loadImage(m), m.getTranslatedX(), m.getTranslatedY(), m.getImageWidth(), m.getImageHeight());
	}

	public GraphicsContext getGC() {
		return this.gc;
	}

	public Game getTheGame() {
		return theGame;
	}

	public void resetGameAttribute() {
		game = theGame;
	}

	public Game getGame() {
		return this.game;
	}

	public ArrayList<DataNode> getDataNodes() {
		return dns;
	}

	public int getTotalScore() {
		return totalScore;
	}

	// The following Score methods can be used for both total Score and current
	// score for a particular miniGame
	/**
	 * @author Abrenner method to draw up a brand new scoreLabel with score of zero
	 *         for any miniGameView
	 */
	/*
	 * public void createScoreLabel() { scoreLabel.setLayoutX(scoreLabelX);
	 * scoreLabel.setLayoutY(scoreLabelY); scoreLabel.setFont(new Font("Arial",
	 * 30)); scoreLabel.setTextFill(Color.PEACHPUFF); scoreLabel.setText("Score: " +
	 * 0); root.getChildren().add(scoreLabel); }
	 */

	/**
	 * @author Abrenner Overloading createScoreLabel()
	 * @param score int - current score for particular view method will be called
	 *              when score is not assumed to be zero (loading MainScreenView
	 *              with total score)
	 */
	public void createScoreLabel(int score) {
		scoreLabel.setLayoutX(backgroundWidth - scoreLabelWidth - buffer);
		scoreLabel.setLayoutY(buffer/2);
		scoreLabel.setFont(new Font("Arial", 30));
		scoreLabel.setTextFill(Color.PEACHPUFF);
		scoreLabel.setText("Score: " + score);
		scoreLabel.setTextAlignment(TextAlignment.CENTER);
		scoreLabel.setStyle("-fx-font-weight: bold");
		root.getChildren().remove(scoreLabel);
		root.getChildren().add(scoreLabel);
	}

	/**
	 * @author Abrenner updates scoreLabel to display the current score
	 */
	public void updateScoreLabel(int score) {
		scoreLabel.setText("Score: " + score);
	}

	/**
	 * @author Abrenner 
   * removes the scoreLabel from the view may not need if we
	 *         include label in method clearFX()
	 * 
	 * removes the scoreLabel from the view
	 */
	public void removeScoreLabel() {
		root.getChildren().remove(scoreLabel);
	}

	/**
	 * Draws the gameOver image on the middle of the view
	 * @author jbrueno
	 */
	public void drawGameOver() {
		gc.drawImage(gameOver, goX, goY, goWidth, goHeight);
	}

	/**
	 * Creates and adds a timer to the view.  Depending on the time remaining, displays the time
	 * in seconds instead of milliseconds by parsing the time int.
	 * 
	 * @author jbrueno
	 * @param time
	 */
	public void createTimer(int time) {
		if (time >= 100) {
			time = Integer.parseInt(("" + time).substring(0, 2));
		} else if (time >= 10) {
			time = Integer.parseInt(("" + time).substring(0, 1));
		} else if(time > 0){
			time = 1;
		} else {
			time = 0;
		}
		displayTime.setLayoutX(displayTimeX);
		displayTime.setLayoutY(displayTimeY);
		displayTime.setFont(new Font("Arial", 30));
		displayTime.setTextFill(Color.WHITE);
		displayTime.setText("Time Remaining: " + time);
		root.getChildren().remove(displayTime);
		root.getChildren().add(displayTime);
	}
	
	/**
	 * Displays a button that returns the user back to the MainScreen once the game is over
	 * Drawn in the bottom right corner and pulsates to draw attention to.
	 * 
	 * @author jbrueno, Ryan Peters
	 */
	public void backToMainButton() {
		Button backToMain = new Button("Back to Main Screen");
		backToMain.setLayoutX(backgroundWidth/2 - backToMainWidth/2);
		backToMain.setLayoutY(backgroundHeight - backToMainHeight*2 - buffer);
		backToMain.setPrefWidth(backToMainWidth);
		backToMain.setPrefHeight(backToMainHeight);
		backToMain.setTextAlignment(TextAlignment.CENTER);
		backToMain.setStyle("-fx-background-color: blue;-fx-font-weight: bold;-fx-font-size: 30;-fx-text-fill: white");
		backToMain.setOnAction(e -> {
			game = Game.MAINSCREEN;
		});
		
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), backToMain);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.5);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
		root.getChildren().add(backToMain);
	}
	
	/*
	 * Prevents a button from being clicked
	 * 
	 * @author Ryan Peters
	 */
	public void disableButton(Button b) {
		b.setDisable(true);
	}

	public void addButtons(ArrayList<Button> buttons) {
		for(Button b : buttons) {
			root.getChildren().add(b);
		}
	}
	
	public void removeButtons(ArrayList<Button> buttons) {
		for(Button b : buttons) {
			root.getChildren().remove(b);
		}
	}
	
	
	/**
	 * Removes all children from <code>root</code> that aren't the canvas. 
	 * <p>
	 * Used for switching between FX's within the MinigameViews themselves rather than from one game to the other. Since <code>canvas</code>
	 * is stored locally in View, we have no ability to access it, so we handle it this way instead.
	 * 
	 * @author Ryan Peters
	 */
	public void clearRootExceptCanvas() {
		Iterator<Node> it = root.getChildren().iterator();
		while (it.hasNext()) {
			Node n = it.next();
			if (!(n instanceof Canvas)) {
				it.remove();
			}
		}
	}
	
/////TUTORIAL STUFF//////
	public void setUpTutorial() {
		tutorialLabel = new Label();
		tutorialLabel.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY ,Insets.EMPTY )));
		tutorialLabel.setFont(new Font("Arial", 25));
		root.getChildren().add(tutorialLabel);
	}
	
	public void drawPlayButton() {
		btnPlay=new Button();
		btnPlay.setText("Let's play!");
		btnPlay.setLayoutX(btnPlayX);
		btnPlay.setLayoutY(btnPlayY);
		btnPlay.setOnMouseClicked(e -> {
			me=e;
			play=true;
		});
		root.getChildren().add(btnPlay);
	}
	/////STUFF FOR TUTORIALS////
	

}