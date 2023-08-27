import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Image;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;	
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;

// This class creates the GamePanel JPanel and adds other panels onto
// it with a border layout. This class also contains the runGame() method
// which starts the actual game by calling methods such as initialStatsValues() and
// statsController()
public class GamePanel extends JPanel
{
	private CardLayout mainCards; // this is a cardLayout object that holds my cardLayout object for the main card layout so that I can show different cards 
	private MainHolderPanel mainHolder; // this is an object of my MainHolderPanel class which is a JPanel that is the holder panel for my card layout
										// this is passed in so that I can show different cards using this class
	private SheetMusicPanel sheetMusicPan; // this is an object of my sheetMusicPanel class, it is needed in order to call that class from other classes
	private int level; // keeps track of the level the player is on
	private int stageNumber; // keeps track of the stage number the player is on in a level
	private int runningScore; // keeps track of the players score
	private int lessTime; // keeps track of by what factor the player's amount of time should decrease per stage (depends on their level)
	private int numLost; // keeps track of the number of times the player has lost a stage
	private int pauseForInst; // keeps track of when the player pauses the game to go to the instructions panel
	private GameData gd; // an object of the GameData class that stores important values for the game to use
	public GamePanel(CardLayout cl, MainHolderPanel mhp, GameData gdIn)
	{
		// Setting the field variables of this class to equal
		// the values passed in by the MainHolderPanel class
		
		mainCards = cl;
		mainHolder = mhp;
		gd = gdIn;
		
		setLayout(new BorderLayout());
		sheetMusicPan = new SheetMusicPanel(); // Creating the panel holding the notes and sheet music
		sheetMusicPan.setPreferredSize(new Dimension(1100,450));
		
		KeyboardPanel keyboardPan = new KeyboardPanel(); // Creating the panel holding the drawing of the keyboard
		keyboardPan.setPreferredSize(new Dimension(1100,350));
		
		add(keyboardPan, BorderLayout.SOUTH); 
		add(sheetMusicPan, BorderLayout.CENTER);
		
		level = 0;
		stageNumber = 0;
		runningScore = 0;
		lessTime = 0;
		numLost = 0;
		pauseForInst = 0;
	}
// this method is called by the start button of the game 
// on the start panel, as well as in the instructions panel. 
// this method calls initialStatsValues() which sets up a new
// player's score, level, and stage number. This method also
// calls statsController() which is what calls other methods for
// the actual game to work.
	public void runGame()
	{	
		initialStatsValues();
		statsController();
	}
// This method sets all of the player's stats (score, level, stage number) to the starting values since they 
// started a new game. It also stores the starting values for pauseForInst (pausing for instructions), 
// the score, and the numLost (number of times the player has lost a stage)
	public void initialStatsValues()
	{
		level = 1; 
		stageNumber = 1;
		runningScore = 0;
		lessTime = 0;
		numLost = 0;
		pauseForInst = 0;
		// Setting important initial values for info that is stored
		// in the GameData class throughout a game
		gd.setResumed(false);
		gd.setPauseForInst(pauseForInst);
		gd.setFinalScore(runningScore);
		gd.setNumLost(numLost);
	}
// This method first checks if the player has completed 5 stages of the game,
// in which case the level # is incremented and the stage # is set back to 1.
// This method also checks if the level # is higher than 3, meaning that
// the amount of time the player gets to play the notes is decreased. Finally, 
// this method calls the runTimer() method of the sheetMusicPanel class
// which starts a stage.
	public void statsController()
	{
		if (stageNumber == 6) // If the player finished their fifth stage, they move onto the next level,
							  // get points for it, and their stage number is 1 again
		{
			level++;
			runningScore += 20;
			stageNumber = 1;
			if (level > 3) // If the player is on a level higher than 3 they get less time for each level
			{
				lessTime ++;
			}
		}
		sheetMusicPan.runTimer(); // the method that starts a new stage is called
	}

// this class is a JPanel that contains the sheet music. The notes are
// randomly generated and drawn on the screen each stage. This panel
// also listens for KeyEvents and determines whether or not the player
// has played the correct note.
class SheetMusicPanel extends JPanel implements KeyListener, ActionListener
{
		private Image staffImage; // an Image object that will contain the image of the music staff so that it can be drawn
		private String staffImageName; // the string object containing the name of the music staff image's file so that it can be loaded and drawn
		private Color lightBlue; // a color that is used to fill the background of this panel
		private int [] generatedNote; // an array containing a number randomly generated that corresponds to a note
		private int [] heightNote; // an array containing the y values of the notes when they are drawn 
		private char [] searchNote; // the key codes for the notes that were generated, used to see if the player "played" the right note by typing
		private boolean [] match; // the array containing whether or not the player played the right note for each generated note
		private char userTyped; // the character the player typed
		private boolean lostStage; // the boolean letting us know whether or not the player lost the stage
		private int noteNum; // the number indicating what number the note is out of five notes generated (i.e. the first note, second note, etc.)
		private boolean wonStageEarly; // a boolean letting us know whether or not the player played all the notes correctly before the timer ran out
		private int numNotePlayed; // the numerical value of the note being played (the first note, the second note, etc.)
		private boolean resumed; // checks if the game is being resumed after passing the quiz
		
		private Timer stageTimer; // an instance of a timer, this is the timer for a stage, it is used to start and stop the timer
		private int tenthSecond; // this is the variables determining how many tenths of a second have passed in a stage
		private int totalTime; // this indicates the total time that the timer is giving the player
		private double secondsDecimal; // this is the double value that shows how many seconds (to the tenth place) are left for the player
		private boolean outTime; // this is the boolean value indicating whether or not the timer has run out of time
		
		public SheetMusicPanel()
		{
			// Initializing all of the field variables in this class
			staffImage = null;
			staffImageName = new String("staffImage.PNG");
			lightBlue = new Color(194,246,255);
			setBackground(lightBlue);
			generatedNote = new int []{0,0,0,0,0};
			heightNote = new int []{0,0,0,0,0};
			searchNote= new char []{'-','-','-','-','-'};
			userTyped = '-';
			match = new boolean[] {false,false,false,false,false};
			lostStage = false;
			noteNum = 0;
			wonStageEarly = false;
			numNotePlayed = 0;
			resumed = false;
			
			addKeyListener(this);
		
			stageTimer = new Timer (100, this); // tenth second delay between events
			totalTime = 60;
			tenthSecond = 0;
			secondsDecimal = 0.0;
			outTime = false;
		}
// This method is called when a new stage starts. This method calls initialValues()
// which sets up starting values for a stage, and then getstaffImage()
// which loads the image of the music staff. generateNote() is called, which
// generates the random notes that the player must play depending on the level #
// and the timer stageTimer starts so that the player now has an amount of time
// that they must complete the stage in.
		public void runTimer()
		{
			initialValues();
			getStaffImage();
			generateNote();
			stageTimer.start();
		}
// This method sets the initial values of variables for a new stage.
// These are all key variables whose values change during gameplay.
		public void initialValues()
		{
			outTime = false;
			totalTime = (int)(60 * Math.pow(0.9,lessTime)); // The higher the level (after Level 3) the less time to play a stage
			//totalTime = 2; // for quiz testing ONLY
			tenthSecond = 0;
			secondsDecimal = -5.5; // Negative because the timer stops when this value equals 0, so the timer wouldn't start if this
								   // value were initialized to 0 
			
			generatedNote = new int []{0,0,0,0,0};
			heightNote = new int []{0,0,0,0,0};
			match = new boolean[] {false,false,false,false,false};
			searchNote= new char []{'-','-','-','-','-'};
			userTyped = '-';
			lostStage = false;
			noteNum = 0;
			wonStageEarly = false;
			numNotePlayed = 0;
			resumed = false;
		}
		
// This method loads the image of the music staff so that it can be drawn to screen later on
		public void getStaffImage() 
		{
			// This try catch block loads the image so that it can be 
			// drawn in the paintComponent() method later on
			File imageFile = new File(staffImageName);
			try
			{
				staffImage = ImageIO.read(imageFile);
			}
			catch(IOException e)
			{
				System.err.println("\n\n" + staffImageName + " can't be found.\n\n");
				e.printStackTrace(); 
			}
		}
// This method uses Math.random to generate random numbers and store them in the 
// generatedNote array. Then, the values in the generatedNote array are passed
// into the getDimensions() method of the GameData class, which returns
// the y coordinate value to draw a note at. These y coordinate values 
// are stored in the heightNote array. Then, the values of the 
// heightNote array are passed into the setSearchNote() method
// of the GameData class, which returns the computer keyboard key character
// that corresponds to the note in question. This value is then stored in 
// the searchNote array to use in the keyTyped method later on. This method
// also generates different numbers, thus notes, depending on the levels.
// Level 1 generates notes numbered 1 through 5 (notes that are on the
// treble clef), Level 2 generates notes numbered 6 through 10 (bass cleff),
// and level 3 and up generate numbers 1 through 10 (any note on either
// bass or treble clef).
		public void generateNote()
		{
			initialValues();
			if (level == 1) // If this is the first level only notes in treble clef will be generated
			{
				for(int i = 0; i < generatedNote.length; i++)
				{
					generatedNote[i] = (int)(Math.random()*4 + 1); // each int corresponding to a note is added to the array
				}
				for(int i = 0; i < generatedNote.length; i++)
				{
					heightNote[i] = (gd.getDimensions(generatedNote[i])); // the height each note should be drawn at is 
																		  // received from the GameData class and then stored in an array for
																		  // reference when drawing the notes later
					searchNote[i] = gd.setSearchNote(heightNote[i]); // the height of each note is passed to a method in the GameData class
																	 // that returns the key on the computer keyboard that corresponds with
																	 // that note, these values are then stored in an array to be used by 
																	 // keyListener later on to see if the player played the correct note
				}
			}
			else if (level == 2) // if the player is on level 2 only notes in bass clef are generated
			{
				for(int i = 0; i < generatedNote.length; i++)
				{
					generatedNote[i] = (int)(Math.random()*5 + 6);
				}
				for(int i = 0; i < generatedNote.length; i++)
				{
					heightNote[i] = (gd.getDimensions(generatedNote[i]));
					searchNote[i] = gd.setSearchNote(heightNote[i]);
				}
			}
			else // if the player is on any level other than 1 and 2 notes can be in either bass or treble clef
			{
				for(int i = 0; i < generatedNote.length; i++)
				{
					generatedNote[i] = (int)(Math.random()*9 + 1);
				}
				for(int i = 0; i < generatedNote.length; i++)
				{
					heightNote[i] = (gd.getDimensions(generatedNote[i]));
					searchNote[i] = gd.setSearchNote(heightNote[i]);
				}
			}
			this.repaint();
		}
// This method checks whether or not the player has played the correct note.
// If the player plays the right note and time is not up, they may move on to the next note.
// If the player plays the wrong note and time is not up, they have to keep retrying that one note.
// This method also determines whether the stage has been lost by the player. If the 
// player completed all of the notes before time ran out, the timer stops and a new stage
// begins. If the player did not complete all of the notes before time ran out, the player loses
// the stage (and thus the game (unless they pass the quiz)). Also, this method
// calls repaint() because the paintComponent method will check if the 
// given note has been played correctly or not, if not then the blue colored
// indicator remains on the note, otherwise it moves on to the next note. This 
// method also checks for if the player has lost a stage for the first time
// in one game, in which case they are given the quiz. If they pass the quiz,
// a boolean value is passed into the GameData class' setResumed and then
// that value is gotten here using getResumed. If getResumed is true then
// the player may resume their game, but first a pop up appears telling the
// player to press any key to resume. This method also checks for if the player
// has lost a second stage in the game, in which case their game is over
// and they are brought to the intro screen of the quiz (which tells them 
// what note they got wrong) and then they move on to the closing screen
		public void playGame()
		{
			if(numNotePlayed < 5 && outTime == false && match[numNotePlayed] == false) // if the player got the note wrong the blue note remains the same
			{
				lostStage = false;
				this.repaint();
			}
			// if the player played the correct note then
			// the player can move onto the next note and the 
			// blue note moves on to be the next note the player
			// has to play
			else if(numNotePlayed < 5 && outTime == false && match[numNotePlayed] == true) 
			{
				lostStage = false;
				this.repaint();
				numNotePlayed++;
			}
			else if(outTime == false && noteNum == 5) // if the player plays all of the notes in 1 stage before time is up they win the stage and move on to the next one
			{
				wonStageEarly = true;
				lostStage = false;
				runningScore += 10; // the player gets points for completing the stage
				stageNumber++; // the player moves on to the next stage
				statsController(); // the method checking to increase the level and resetting the values for a stage is called
			}
			else if(outTime == true && noteNum < 5 && numNotePlayed < 5) // if the player ran out of time and didn't play all the notes they lose the stage
			{
				lostStage = true;
			}
			if(lostStage == true) // also happends when player presses the key to resume the game, which is why later check for == 3 not == 2
			{
				numLost++; // the number indicating how many times the player lost a stage in one game is incremented
			}
			if(numLost == 1) // brings the player to the quiz if they lose a stage for the first time
			{
				repaint();
				gd.setFinalScore(runningScore);
				mainCards.show(mainHolder, "quiz holder panel");
			}
			if(lostStage == true && gd.getResumed() == true) // shows the player the sign for resuming the game after they pass the quiz
			{
				repaint();
				stageNumber++;
				statsController();
				gd.setResumed(false);
				lostStage = false;
			}
			if(numLost == 3) // bringing the player to the closing of the game when they lose a stage for a second time in one game
			{
				gd.setNumLost(numLost);
				gd.setFinalScore(runningScore);
				mainCards.show(mainHolder, "quiz holder panel");
			}
		}
// These are instance methods of the KeyListener interface.
// They allow me to use the keyTyped method.
		public void keyReleased(KeyEvent evt){}
		public void keyPressed(KeyEvent evt){}
// This method checks to see if the player played the correct
// note by seeing if the key the player typed is the same as the 
// key that was required to play the correct note (stored in the searchNote array).
// The method only moves on to checking for the next note if the player played the correct current note. 
// This method calls playGame() in order to change the variables
// of lostStage depending on the case. This method calls playGame() 
// after every key the player presses, but it also calls playGame()
// if the player completed playing all five notes (so that the game can
// be updated with whether or not the player won the stage.
		public void keyTyped(KeyEvent evt) 
		{
			userTyped = evt.getKeyChar();
			if (noteNum < 5) // if the player has not finished playing all of the notes in one stage then check if they played the correct note
			{
				if(searchNote[noteNum] == userTyped) // if the note the player played was the correct note then they move onto the next one
				{
					match[noteNum] = true;
					noteNum++;
					if(noteNum == 5) // if the player played their final note of the stage correctly then call the playGame() method, which determines if they won the stage
					{
						playGame();
					}
				}
			}
			playGame();	// the playGame method is called each time the player presses a key in order to let them resume a game after a quiz
		}
// This method draws the image of the staff onto the panel. It also
// draws the notes that were generated. When it draws the notes, it uses
// a for loop to determine what number the note is (first note, second, etc.),
// which determines the x value of the note for drawing (since the first note
// must be 100 pixels away from the left edge of the panel and each successive
// note is 150 pixels away from the last one). The heightNote[] array is 
// also used to determine the y value of the note drawn, determining where on 
// the staff it should be drawn (which further solidifies which note it is).
// This method also draws the timer that counts down on the screen. It also
// makes the note that needs to be played by the player blue in color. This method
// also draws the sign on the screen telling the player what to do if they
// passed their quiz and want to resume their game. This method also checks for
// if the player has returned from looking at the instructions using the help
// item on the menu. In which case the timer resumes and the variables needed
// for checking if the player is at the instructions panel are reset.
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			// drawing the music staff image to the screen
			requestFocusInWindow(); // so that the timer and note selection (which note needs to be played) work
			g.drawImage(staffImage,0,50,this);
			
			// Creating the drawings for showing the level, stage, 
			// and score of the player
			
			g.setColor(lightBlue);
			g.fillRect(0,50,10,350);
			g.fillRect(840,50,10,350);
			
			g.setColor(Color.PINK);
			g.fillRect(850,0,250,450);
			g.setColor(Color.WHITE);
			g.fillRect(900,170,150,150);
			
			Color lightPurple = new Color(208,122,255);
			g.setColor(lightPurple);
			g.fillRect(870,30,210,100);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Monospaced", Font.PLAIN, 40));
			g.setColor(Color.WHITE);
			g.drawString("Lv " + level,870,70);
			g.drawString("Stage " + stageNumber,870,120);
			
			Color lightYellow = new Color(255,243,157);
			g.setColor(lightYellow);
			g.fillRect(870,350,210,100);
			g.setColor(Color.BLACK);
			g.setFont(new Font("SansSerif", Font.PLAIN, 25));
			Color darkBlue = new Color(0,101,255);
			g.setColor(darkBlue);
			g.drawString("Score",875,375);
			g.setFont(new Font("SMonospaced", Font.PLAIN, 50));
			g.drawString("" + runningScore,875,420);

			g.setColor(Color.BLACK);
			
			// drawing the generated notes on screen
			int xValue = 0;
			for(int i = 0; i < 5; i++)
			{
				if (i == 0)
				{
					xValue = 100;
				}
				else 
				{
					xValue += 150;
				}
				
				if (heightNote[i] == 190)
				{
					g.fillRect(xValue - 10, heightNote[i] + 10, 55, 5);
				}
				g.fillOval(xValue,heightNote[i],35,25);
				
			}
			
			// deciding which note to make blue in color (the note that
			// needs to be played next)
			if(numNotePlayed < 5)
			{
				if (numNotePlayed == 0) // deciding what x value to draw the note at 
				{
					xValue = 100;
				}
				else if (numNotePlayed == 1)
				{
					xValue = 250;
				}
				else if (numNotePlayed == 2)
				{
					xValue = 400;
				}
				else if (numNotePlayed == 3)
				{
					xValue = 550;
				}
				else if (numNotePlayed == 4)
				{
					xValue = 700;
				}

				if(match[numNotePlayed] == false) // if the player has not played that next note yet, it is blue in color
				{
					g.setColor(darkBlue);
				}
				g.fillOval(xValue,heightNote[numNotePlayed],35,25); // the blue note is drawn in the correct location
				gd.setCurrentNote(heightNote[numNotePlayed]); // storing the note the player is stuck on
				
			}
			
			// draws the sign on screen if the player passed the quiz and
			// wants to resume the game
			if (lostStage == true && numLost == 1)
			{
				g.setColor(Color.RED);
				g.fillRect(225,25,400,400);
				g.setColor(Color.YELLOW);
				g.setFont(new Font("Monospaced", Font.BOLD, 35));
				g.fillRect(240,40,370,370);
				g.setColor(Color.RED);
				g.drawString("ATTENTION! Your",280,130);
				g.drawString("game is paused!",280,180);
				g.drawString("Press any key",280,270);
				g.drawString("to resume.",280,320);
			}
			
			// starts the timer again when the player returns from reading the instructions
			// panel through the "help" option on the menu
			if(secondsDecimal != 0.0 && gd.getPauseForInst() == 2)
			{
				stageTimer.start();
				gd.setPauseForInst(0);
			}
			
			// Drawing the timer to the screen
			g.setColor(Color.BLACK);
			g.setFont(new Font("Monospaced", Font.BOLD, 25));
			g.drawString("Time Left:",902,200);
			g.setFont(new Font("Monospaced", Font.PLAIN, 40));
			
			secondsDecimal = totalTime - (tenthSecond / 10.0);
			g.drawString(String.format("%.1f", secondsDecimal),930,260);
			g.setFont(new Font("Monospaced", Font.PLAIN, 35));
			g.drawString(" seconds",880,310);
			
		}
// This method reacts to the timer each time it generates an action event
// (which is every tenth of a second) and then increments the variable
// tenthSecond (which indicates how many tenths of a second have passed).
// if the timer is out of time (meaning that secondsDecimal, the variable
// containing how much time is left, is 0.0) the boolean variable outTime 
// is set to true, otherwise it is false. This variable is passed into the
// getOutTime() method of the GameData class in order to let other classes 
// use that value. At the end of this method, repaint() is called in order
// to update the drawn timer on the screen with how much time is actually left.
// This method also checks for if the player has gone to the instructions panel
// using the menu, in which case the timer pauses and is only resumed 
// in paintComponent when the player returns from the instructions again.
		public void actionPerformed(ActionEvent evt) 
		{
			if (secondsDecimal == 0.0 || wonStageEarly == true) 
			{
				stageTimer.stop();
				outTime = true;
				playGame(); // so if the player runs out of time, the playGame() method is called (otherwise would only be called
							// if player pressed a note
				
			}
			else if (secondsDecimal != 0.0 && gd.getPauseForInst() == 1) // pause timer when player goes to instructions through menu
			{
				stageTimer.stop();
			}
			else // otherwise let the timer keep incrementing and change the value of the timer on the screen by incrementing tenthSecond
			{
				tenthSecond++;
				outTime = false;
			}
			this.repaint();
		}
}
// This class contains a drawing of the keyboard for the player to use
// as referance when playing the game. It also contains
// the menu bar that the user can use to quit the game or get help.
	class KeyboardPanel extends JPanel
	{
		public KeyboardPanel()
		{
			setLayout(new FlowLayout(FlowLayout.RIGHT,70,50));
			setBackground(Color.ORANGE);
			JMenuBar moreBar = makeMoreBar();
			add(moreBar);
		}
// This method draws a keyboard on the panel containing all of the keys
// needed to play the game. Each key is also labeled whith the key on the 
// computer keyboard that corresponds with it, and this label is colored pink to 
// indicate that the key is meant to be pressed with the left hand and 
// blue to indicate that it is meant to be pressed with the right hand. 
// knowing which hand plays what is essential to playing piano.
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(Color.PINK);
			g.fillRect(850,0,250,350);
			
			// drawing the keys
			g.setColor(Color.WHITE);
			g.fillRect(50,50,75,250);
			g.fillRect(125,50,75,250);
			g.fillRect(200,50,75,250);
			g.fillRect(275,50,75,250);
			g.fillRect(350,50,75,250);
			g.fillRect(425,50,75,250);
			g.fillRect(500,50,75,250);
			g.fillRect(575,50,75,250);
			g.fillRect(650,50,75,250);
			g.fillRect(725,50,75,250);
			
			// drawing the lines separating the keys
			g.setColor(Color.GRAY);
			g.drawRect(50,50,75,250);
			g.drawRect(125,50,75,250);
			g.drawRect(200,50,75,250);
			g.drawRect(275,50,75,250);
			g.drawRect(350,50,75,250);
			g.drawRect(425,50,75,250);
			g.drawRect(500,50,75,250);
			g.drawRect(575,50,75,250);
			g.drawRect(650,50,75,250);
			g.drawRect(725,50,75,250);
			
			// drawing the sharp keys
			g.setColor(Color.BLACK);
			g.fillRect(50,50,25,150);
			g.fillRect(175,50,50,150);
			g.fillRect(250,50,50,150);
			g.fillRect(325,50,50,150);
			g.fillRect(475,50,50,150);
			g.fillRect(550,50,50,150);
			g.fillRect(775,50,25,150);
			
			Color left = new Color(255,143,185);
			Color right = new Color(98,190,253);
			g.setColor(left);
			Font keyFont = new Font("Monospaced", Font.BOLD, 40);
			g.setFont(keyFont);
			
			
			g.drawString("A",75,260);
			g.drawString("S",150,260);
			g.drawString("D",225,260);
			g.drawString("F",300,260);
			g.drawString("V",375,260);
			g.setColor(right);
			g.drawString("B",450,260);
			g.drawString("H",525,260);
			g.drawString("J",600,260);
			g.drawString("K",675,260);
			g.drawString("L",750,260);
			
		}
// This method makes a JMenu bar as well as menu items, and adds the
// menu items, and then returns the menu bar in order to make a full working
// menu bar with a drop down menu for the player to use. This menu lets the 
// user decide to quit the game or get some help by going to the instructions panel.
		public JMenuBar makeMoreBar()
		{
			JMenuBar bar = new JMenuBar();
			bar.setPreferredSize(new Dimension(100,50));
			
			JMenu more = new JMenu("\tMore");
			more.setPreferredSize(new Dimension(100,50));
			more.setFont(new Font("Monospaced", Font.PLAIN, 20));
			
			JMenuItem aid = new JMenuItem("Help");
			aid.setPreferredSize(new Dimension(100,50));
			aid.setFont(new Font("Monospaced", Font.PLAIN, 20));
			
			JMenuItem quit = new JMenuItem("Quit");
			quit.setPreferredSize(new Dimension(100,50));
			quit.setFont(new Font("Monospaced", Font.PLAIN, 20));
		
			MoreMenuHandler mmh = new MoreMenuHandler();		
			aid.addActionListener(mmh);
			quit.addActionListener(mmh);	
			
			more.add(aid);
			more.add(quit);
			bar.add(more);
			return bar;
		}
// This class handles actions done by the player on the menu. 
// It shows the start panel card to the player if they quit and
// the instructions panel if they click to get help. This method
// also changes the value of pauseForInst to 1 when the player
// presses help and goes to the instructions panel. This causes
// the timer to pause because that variable is passed to, and stored in,
// the GameData class, from where the timer's actionListener
// can get its value and pause the timer when needed.
		class MoreMenuHandler implements ActionListener 
		{
			public void actionPerformed( ActionEvent evt ) 
			{
				String command = evt.getActionCommand();
				if (command.equals("Help" )) // if the player presses help then pause the game and show them the instructions
				{
					pauseForInst = 1;
					gd.setPauseForInst(pauseForInst);
					mainCards.show(mainHolder,"instructions panel");
				}	
				else if ( command.equals("Quit" )) // if the player presses quit bring them back to the start panel
				{
					mainCards.show(mainHolder,"start panel");
				}
			}
		}
	}
}
