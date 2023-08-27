import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Image;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.PrintWriter;


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

// This class creates the leader board panel for the high 
// scores to be displayed.
public class LeaderboardPanel extends JPanel
{
	private CardLayout mainCards; // a cardLayout object that takes in my cardLayout object that is passed in so that different cards can be shown
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class that will be passed in so that different cards can be shown 
	private GameData gd; // an object of the GameData class that lets this class have access to data such as the player's score
	private JLabel [] scores; // an array of JLabels used in other methods in order to hold high scores
	private Scanner highScoresFile; // a scanner object to read the text file containing the high scores
	private String highScoresFileName; // a string object containing the name of the text file that contains the high scores
	private PrintWriter writingHighScores; // a printwriter object in order to write to the file containing the high scores
	private String writeHighScoresFileName; // a string object containing the name of the text file for the print writer to write to
	private Scanner leaderReader; // a scanner object to read the text file containing the high scores 
	private String leaderFileName; // a String object containing the name of the text file containing the high scores to read from
	public LeaderboardPanel(CardLayout cl, MainHolderPanel mhp, GameData gdIn)
	{
		mainCards = cl;
		mainHolder = mhp;
		gd = gdIn;
		highScoresFile = null;
		highScoresFileName = new String("highScores.txt");
		leaderReader = null;
		leaderFileName = new String("highScores.txt");
		
		writingHighScores = null;
		writeHighScoresFileName = new String("highScores.txt");
	
		Color lightBlue = new Color(170,238,250);
		setBackground(lightBlue);
		setLayout(new FlowLayout(FlowLayout.CENTER,550,55));
		
		Font userFont = new Font("Monospaced", Font.BOLD, 40);
		JLabel header = new JLabel("\tLeaderBoard\t");
		header.setFont(new Font("Monospaced", Font.BOLD, 50));
		header.setOpaque(true);
		header.setBackground(Color.WHITE);
		add(header);
		
		// initializing the Jlabel array and making each
		// JLabel look the way I want it to
		scores = new JLabel[5];
		for(int i = 0; i < scores.length; i++)
		{
			scores[i] = new JLabel("\tUser" + (i+1) + " & score\t");
			scores[i].setFont(userFont);
			scores[i].setOpaque(true);
			scores[i].setBackground(Color.WHITE);
			add(scores[i]);
		}
		
		JButton exitButton = new JButton("Exit"); // Creating an exit button
		exitButton.setPreferredSize(new Dimension(150,60));
		exitButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
		ButtonHandlerExit bhe = new ButtonHandlerExit("start panel");
		exitButton.addActionListener(bhe);
		add(exitButton);
		
		setUpLeaderBoard();
	}
// This method is called when the player enters their name
// into the high scores closing panel. First, the text file
// containing high scores is read by a scanner and the scores 
// as well as the player names are stored in arrays. The cells
// at index 4 of both arrays are then set to the player's values
// because the person whose data was in those cells before will
// be kicked off the leaderboard now that a new person is on it
// (since they were in fifth place). Then, the array of scores is 
// bubble sorted in order to get it in order from least to greatest.
// While bubble sorting this the string array of names is also
// sorted to correspond with the order of the scores. So that 
// one index of both of the arrays contains data for the same player.
// A print writer object is then created and the file is cleared
// and then written to, and the new names with high scores are 
// written in the high scores text file. The high scores and names
// are also then set as the text for the JLabels on the panel
// in order to display the high scores for players to see.
	public void updateLeaderboard()
	{
		String playerName = new String("");
		int playerScore = 0;
		playerName = gd.getPlayerName();
		playerScore = gd.getFinalScore();
		
		// This try catch block is used to create a Scanner
		// to read from the text file containing high scores
		File inFile = new File(highScoresFileName);
		try
		{
			highScoresFile = new Scanner(inFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("ERROR: Cannot open %s\n", highScoresFileName);
			System.exit(5);
		}
		
		// Initializing the String values in the String array
		String filePhrase = new String("");
		String [] scoreNames = new String[5];
		for (int i = 0; i < 5; i++)
		{
			scoreNames[i] = new String("");
		}
		
		int [] highScoresValues = new int [5];
		while(highScoresFile.hasNext())
		{
			for(int i = 0; i < 5; i++) // getting the names and scores of players from the text file and storing them in separate arrays with corresponding indices
			{
				filePhrase = highScoresFile.nextLine();
				scoreNames[i] = filePhrase.substring((filePhrase.indexOf(".") + 1), filePhrase.indexOf(":"));
				highScoresValues[i] = Integer.parseInt(filePhrase.substring(filePhrase.indexOf(":") + 1));
			}
		}
		
		highScoresValues[4] = playerScore; // dropping the lowest score, thus adding the player to the leaderboard 
		scoreNames[4] = playerName;
		
		String tempName = new String("");
		int tempScore = 0;
		
		// Using bubble sorting in order to make the array of scores
		// be in order from least to greatest. The array of player names
		// is also sorted at the same time to make sure that the indices
		// of the player's name and score in separate arrays still correspond
		
		for(int i = 0; i < 6; i++) // repeating five times in order to place each score at its correct index so that
								   // the scores are in the array in ascending order
		{
			for(int z = 0; z < 4; z++) // moving one score to its position in the ascending order
			{
				if(highScoresValues[z] > highScoresValues[z + 1]) // if the current score is greater than the next one then switch their orders in the array
				{
					tempScore = highScoresValues[z]; // putting scores in order
					highScoresValues[z] = highScoresValues[z + 1];
					highScoresValues[z + 1] = tempScore;
					
					tempName = scoreNames[z]; // putting the player names in order of who has the lowest score to the highest
					scoreNames[z] = scoreNames[z + 1];
					scoreNames[z + 1] = tempName;
				}
			}
		}
		
		// This try catch block creates a PrintWriter object so
		// that the text file  of player scores and names can be written to
		File outLeaderboard = new File(writeHighScoresFileName);
		try
		{
			writingHighScores = new PrintWriter(outLeaderboard);
		}
		catch(IOException e)
		{
			System.out.println("\n\nERROR: Cannot create " 
				+ writeHighScoresFileName + " file.\n\n");
			System.exit(6);
		}
		
		// Writing to the text file with the correct format
		int counter = 0;
		for(int k = 4; k >= 0; k--)
		{
			counter++;
			writingHighScores.println("" + counter + "." + scoreNames[k] + ":" + highScoresValues[k]);
		}
		
		writingHighScores.close();
		highScoresFile.close();
		
		// Setting the high scores and their corresponding names as text
		// for the JLabels that appear on the leaderboard panel
		int index = 4;
		for(int i = 0; i < scores.length; i++)
		{
			scores[i].setText(" " + scoreNames[index] + " " + highScoresValues[index] + " Points ");
			index--;
		}	
	}
// This method is called when the program first runs
// and it displays the current high scores by reading them
// from a text file and then setting them as the text for
// the JLabels on the panel with the correct format. A different
// scanner is used compared to the one used when updating the leader
// board while games are in session because I don't want to cause errors
// of the scanner being closed and not being able to reopen it again.
	public void setUpLeaderBoard()
	{
		// This try catch block creates a Scanner object
		// in order to read from the high scores text file.
		File inFile = new File(leaderFileName);
		try
		{
			leaderReader = new Scanner(inFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("ERROR: Cannot open %s\n", highScoresFileName);
			System.exit(9);
		}
		
		String fileLine = new String("");
		String scoreName = new String("");
		int score = 0;
		int counter = 0;
		
		while(leaderReader.hasNext()) // Taking each line from the text file, storing it a string, and then formatting it so it can be 
									  // added as text to the Jlabels on the leaderboard panel
		{
			fileLine = leaderReader.nextLine();
			scoreName = fileLine.substring((fileLine.indexOf(".") + 1), fileLine.indexOf(":"));
			score = Integer.parseInt(fileLine.substring(fileLine.indexOf(":") + 1));
			scores[counter].setText(" " + scoreName + " " + score + " Points");
			counter++;
		}
	}
// This class is a buttonHandler class for the exit button
// on this panel that allows the player to return to the start panel
	class ButtonHandlerExit implements ActionListener
	{
		private String cardName;// This variable stores the String name that corresponds to the card that needs to be shown
								// when a particular button is pressed
		public ButtonHandlerExit(String name)
		{
			cardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
			mainCards.show(mainHolder,cardName);
		}
	}

}
