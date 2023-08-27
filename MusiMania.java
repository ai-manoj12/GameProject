// Aishwarya Manoj
// 4/20/20 to 5/22/20
// MusiMania.java
// This is my Game Project. This file contains the MusiMania class, which 
// creates a JFrame, and the MainHolderPanel class, which creates a CardLayout
// and creates instances of my other main cards and passes needed cards as
// well as an instance of the MainHolderPanel to each of those cards' classes.
/// Testing:
/// In one run of the program:
/// Play Game, get a non-high score, and lose the quiz. Should get regular
/// closing panel and game should end. Then start a new game, get a non-high
/// score, and pass the quiz. Resume the game, play a few stages, and lose a stage.
/// The game should end and you should get a regular closing panel. Start a 
/// new game, get a high score, play the quiz, lose the quiz, and you should
/// get a high score closing panel (enter in name and see it appear on leaderboard).
/// Start a new game, get a high score, play the quiz, pass the quiz, return 
/// to the game, play a few more stages, and then lose a stage. The game should
/// end and you should get a high scores closing panel (again, put in a name and
/// see it appear on the leaderboard). Start a new game, play the game and get a
/// non-high score, then play the quiz and pass it, when you return to the game
/// play stages until you get a new high score. You should get a high score
/// closing panel and the game should end. 
/// Play a game and access the instructions panel through the "Help" option
/// in the menu before you lose your first stage, after you pass a quiz and 
/// resume a stage, and before you resume a stage. Each time the timer should
/// pause and resume when you return to your game. 
/// When you quit a game make sure that the game restarts when you press start
/// (test this on both start button from start panel and start button in 
/// instructions when accessed through the start panel).
/// Make sure leaderboard has scores in it when you first run the game 
/// (access through start panel) and after games are played (access 
/// through start panel).
/// Make sure the message telling the player what note they got stuck
/// on is accurate. Make sure the note that was played that was correct
/// was actually correct (correct key on the computer keyboard).
/// Make sure you can access the credits through the credits button
/// on the start panel. 
/// Make sure the player is given 10 pts. for every stage they complete
/// and 20 for every level they complete. Make sure they don't get pts
/// for a stage they skipped if they passed the quiz and return to the game.

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Creates a JFrame to hold the MainHolderPanel
public class MusiMania 
{
	public static void main (String []args)
	{
		MusiMania mm = new MusiMania();
		mm.run();
	}
	public void run()
	{
		JFrame frame = new JFrame();
		frame.setSize(1100,800);
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE); // The player can't close the game using the x button (they have to hit my close button instead)
		frame.setLocation(200,0);
		frame.setResizable(false);
		MainHolderPanel mhp = new MainHolderPanel(); 
		frame.getContentPane().add(mhp);
		frame.setVisible(true);
	}
}
// This class creates a CardLayout in its constructor and creates
// instances of each of the classes that are used as card panels
class MainHolderPanel extends JPanel 
{
	public MainHolderPanel()
	{
		CardLayout mainCards = new CardLayout();
		setLayout(mainCards);
		
		// Creating instances of all the cards that are part of this cardlayout
		// in the game and passing these instances into some of
		// the other classes so that they can be accessed 
		// (For example, passing the GameData class into the HighScoreClosingPanel
		// class so that it can access the game data
		
		GameData gData = new GameData(); // Passed into classes so that they can access the game's data
		GamePanel gp = new GamePanel(mainCards,this,gData);
		InstructionsPanel isp = new InstructionsPanel(mainCards,this,gp,gData);
		StartPanel stp = new StartPanel(mainCards,this,gp); 
		LeaderboardPanel lbp = new LeaderboardPanel(mainCards,this,gData);
		ClosingPanel clp = new ClosingPanel(mainCards,this,gData);
		HighScoreClosingPanel hscp = new HighScoreClosingPanel(mainCards, this, gData,lbp);
		QuizHolderPanel qhp = new QuizHolderPanel(mainCards,this,gp,gData,clp,hscp);
		CreditsPanel cp = new CreditsPanel(mainCards,this);
		
		// Adding each of these panels to the cardLayout so that
		// they can be shown when needed
		
		add(stp, "start panel");
		add(isp, "instructions panel");
		add(gp, "game panel");
		add(qhp, "quiz holder panel");
		add(lbp, "leaderboard panel");
		add(cp, "credits panel");
		add(clp,"closing panel");
		add(hscp, "high score closing panel");
		
		mainCards.show(this,"start panel");
		
	}
}
