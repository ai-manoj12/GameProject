import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
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

// This class creates the high scores closing
// panel that is shown if the player got a high
// score. This panel uses a borderLayout.
public class HighScoreClosingPanel extends JPanel
{
	private CardLayout mainCards; // an object of the main card layout for the game that lets this class show different cards of the cardLayout
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class that lets this class show different cards of the cardLayout
	private GameData gd; // an object of the GameData class that lets this class have access to getter and setter methods and take data from the game
	private LeaderboardPanel leaderPan; // an object of the LeaderboardPanel class that lets this class call methods of that class
	private JButton leaderButton; // the button leading to the leaderboard
	private JTextArea highScoreResultsTA; // the text area showing the player their score
	private JTextField playerNameTF; // the text field asking for the player's name
	public HighScoreClosingPanel(CardLayout cl, MainHolderPanel mhp, GameData gdIn, LeaderboardPanel lbpIn)
	{
		setBackground(Color.BLUE);
		mainCards = cl;
		mainHolder = mhp;
		gd = gdIn;
		leaderPan = lbpIn;
		setBackground(new Color(194,246,255));
		setLayout(new BorderLayout());
			
			
		JPanel highScoreResultsPanel = new JPanel();
		highScoreResultsPanel.setPreferredSize(new Dimension(1100,350));
		highScoreResultsPanel.setBackground(Color.ORANGE);
		
		// Creating a JTextArea to say the player's game score
		highScoreResultsTA = new JTextArea("Congratulations! You scored _____ points. That's a new high score!",7,20); 
		highScoreResultsTA.setLineWrap(true);  // goes to the next line when printing the text.
		highScoreResultsTA.setWrapStyleWord(true);
		highScoreResultsTA.setEditable(false); 
		highScoreResultsTA.setMargin(new Insets(10,30,0,30));
		highScoreResultsTA.setFont(new Font("Monospaced", Font.BOLD, 70));
		highScoreResultsTA.setBackground(Color.ORANGE);
			
			
		JPanel leaderButtonPanel = new JPanel();
		leaderButtonPanel.setPreferredSize(new Dimension(1100,100));
		leaderButtonPanel.setBackground(Color.PINK);
		leaderButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,20,20));
			
		JPanel highScoreResultsSpacing1 = new JPanel(); // Creating panels to help with flow layout spacing
		JPanel highScoreResultsSpacing2 = new JPanel();
		highScoreResultsSpacing1.setBackground(Color.ORANGE);
		highScoreResultsSpacing1.setPreferredSize(new Dimension(200,60));
		highScoreResultsSpacing2.setBackground(Color.ORANGE);
		highScoreResultsSpacing2.setPreferredSize(new Dimension(200,60));
			
		JPanel nameTextFieldHolderPanel = new JPanel();
		nameTextFieldHolderPanel.setBackground(new Color(194,246,255));
		nameTextFieldHolderPanel.setPreferredSize(new Dimension(700,430));
			
		// Creating a JTextArea to prompt for the player's name
		JTextArea enterName = new JTextArea("Put your name into the text box below in order to display your score on the leaderboard! (Names may not contain periods or colons)");
		enterName.setFont(new Font("Monospaced", Font.BOLD, 40));
		enterName.setForeground(Color.PINK);
		enterName.setLineWrap(true);  // goes to the next line when printing the text.
		enterName.setWrapStyleWord(true);
		enterName.setEditable(false); // prevents a word from being split 
		enterName.setMargin(new Insets(5,10,0,10));
		enterName.setPreferredSize(new Dimension(700,250));
		enterName.setBackground(new Color(194,246,255));
		
			
		playerNameTF = new JTextField("Click here to enter your name"); // Creating a JTextField to get the player's name
		playerNameTF.setFont(new Font("Monospaced", Font.PLAIN, 20));
		playerNameTF.setPreferredSize(new Dimension(400,40));
		LeaderButtonHandler tfh = new LeaderButtonHandler("name entered");
		playerNameTF.setEditable(true);
		playerNameTF.addActionListener(tfh);
			
		leaderButton = new JButton("To the LeaderBoard!"); // creating a button to go to the leaderboard panel
		leaderButton.setFont(new Font("Monospaced", Font.BOLD, 30));
		leaderButton.setPreferredSize(new Dimension(500,60));
		LeaderButtonHandler lbh = new LeaderButtonHandler("leaderboard panel");
		leaderButton.addActionListener(lbh);
		leaderButton.setEnabled(false);
			
		highScoreResultsPanel.add(highScoreResultsTA); // adding components to their holder panels
		leaderButtonPanel.add(leaderButton);
		nameTextFieldHolderPanel.add(enterName);
		nameTextFieldHolderPanel.add(playerNameTF);
			
		add(highScoreResultsPanel, BorderLayout.NORTH); // putting panels in their locations in the border layout
		add(leaderButtonPanel, BorderLayout.SOUTH);
		add(highScoreResultsSpacing1, BorderLayout.EAST);
		add(highScoreResultsSpacing2, BorderLayout.WEST);
		add(nameTextFieldHolderPanel, BorderLayout.CENTER);
			
	}
// This method gets the player's score and displays it in 
// the text area for the player to see by adding it to the 
// text in the text area.
	public void displayHighScore()
	{
		int gameScore = gd.getFinalScore();
		highScoreResultsTA.setText("Congratulations! You scored " + gameScore + " points. That's a new high score!");
	}
// This is the button handler class for the button that leads
// to the leaderboard and the textfield that asks for 
// the player's name to be entered. 
	class LeaderButtonHandler implements ActionListener
	{
		String cardName; // This variable stores the String name that corresponds to the card that needs to be shown
						 // when a particular button is pressed
		public LeaderButtonHandler(String nameCard)
		{
			cardName = nameCard;
		}
// The player's name can not contain a period or colon, 
// and the player will be prompted to enter a different name
// if they try to use those characters in their name. If the player enters
// their name the button to the leaderboard is enabled, otherwise it is not, 
// so the player MUST enter their name. 
		public void actionPerformed(ActionEvent evt)
		{
			if(cardName.equals("name entered")) // if the player entered their name 
			{
				String playerName = evt.getActionCommand(); // get the name the player entered
				if(playerName.indexOf(".") != -1 || playerName.indexOf(":") != -1) // if the player's name had a . or : then make them enter another name
				{
					playerNameTF.setText("Please enter a valid name");
				}
				else // if the player's name was a valid name then store their name in the setPlayerName() method of the GameData class, 
					 // enable the button going to the leaderboard, make the text field uneditable, and call the updateLeaderboard
					 // method of the LeaderboardPanel class in order to alter the leaderbaord accordingly
				{
					leaderButton.setEnabled(true);
					gd.setPlayerName(playerName);
					playerNameTF.setEditable(false);
					leaderPan.updateLeaderboard();
				}
			}
			else // if the player presses the button going to the leaderboard than reset the orinigal values of this panel in terms of 
				 // making that button be disabled and making the text field editable
			{
				mainCards.show(mainHolder, cardName);
				gd.setPlayerName("_");
				playerNameTF.setText("Click here to enter your name");
				playerNameTF.setEditable(true);
				leaderButton.setEnabled(false);
			}
		}
	}
}


	

