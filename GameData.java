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

// This class contains a lot of getter and setter methods to store and share
// important values to the other classes of this game. These classes include
// the ones in this file, as well as the QuizHolderPanel, LeaderboardPanel, ClosingPanel,
// and HighScoreClosingPanel classes
public class GameData
{
	private int generatedNote; // the integer value of the note that was generated (the int value corresponds to a specific note)
	private int currentNoteHeight; // the y value of the current note the player is on (indicates which note it is and at what height to draw it at)
	private boolean resumed; // indicates whether or not the game is being resumed after passing a quiz
	private int numLost; // contains the number of times the player has a stage (lets the QuizHolderPanel class know if they should let the player play a quiz)
	private int pauseForInst; // keeps track of if the player is pausing the game to go to instructions
	private int finalScore; // stores the score of the player at the end of the game so that it can be shown to them in the closing panels
	private String nameOfPlayer;
	public GameData()
	{
		generatedNote = 0;
		currentNoteHeight = 0;
		resumed = false;
		numLost = 0;
		pauseForInst = 0;
		finalScore = 0;
		nameOfPlayer = new String("");
	}
// This method returns the y value that each note should be drawn at
// depending on which random number was generated (each random number
// corresponds to a different note). This is important because on 
// sheet music, the position of the note vertically on the staff 
// indicates what note it is. 
	public int getDimensions(int whichNote)
	{
		int yValue = 0;
		generatedNote = whichNote;
		
		if(generatedNote == 1)
		{
			yValue = 190; // treble C
		}
		else if(generatedNote == 2)
		{
			yValue = 173; // treble D
		}
		else if(generatedNote == 3)
		{
			yValue = 159; // treble E
		}
		else if(generatedNote == 4)
		{
			yValue = 138; // treble G
		}
		else if (generatedNote == 5)
		{
			yValue = 148; // treble F
		}
		else if (generatedNote == 6)
		{
			yValue = 312; // Bass E
		}
		else if (generatedNote == 7)
		{
			yValue = 302; // Bass F
		}
		else if (generatedNote == 8)
		{
			yValue = 290; // Bass G
		}
		else if (generatedNote == 9)
		{
			yValue = 280; // Bass A
		}
		else if (generatedNote == 10)
		{
			yValue = 267; // Bass B
		}
		return yValue;
	}
// This class returns a char value that indicates what key the 
// user should type in order to play the note in question. 
// the note in question is passed in as the value noteHeight
// and then the method returns the char typed by the key on the 
// computer keyboard that corresponds with the key, and thus the note, 
// on the piano keyboard.
	public char setSearchNote(int noteHeight)
	{ 
		int heightNote = 0;
		heightNote = noteHeight;
		char searchNote = '-'; 
		if (heightNote == 190) // treble C
		{
			searchNote = 'b';
		}
		else if (heightNote == 173) // treble D
		{
			searchNote = 'h';
		}
		else if (heightNote == 159) // treble E
		{
			searchNote = 'j';
		}
		else if (heightNote == 148) // treble F
		{
			searchNote = 'k';
		}
		else if (heightNote == 138) // treble G
		{
			searchNote = 'l';
		}
		else if (heightNote == 312) // bass E
		{
			searchNote = 'a';
		}
		else if (heightNote == 302) // bass F
		{
			searchNote = 's';
		}
		else if (heightNote == 290) // bass G
		{
			searchNote = 'd';
		}
		else if (heightNote == 280) // bass A
		{
			searchNote = 'f';
		}
		else if (heightNote == 267) // bass B
		{
			searchNote = 'v';
		}
		return searchNote;
	}
	// This method stores the height that the note
	// the player needs to currently play is drawn at
	public void setCurrentNote(int noteHeight)
	{
		currentNoteHeight = noteHeight;
	}
	// This method returns the height that
	// the player's current note that they need
	// to play is drawn at. This method is called
	// in the QuizIntroPanel class in order
	// to tell the player which note they missed so
	// that they can improve
	public int getCurrentNote()
	{
		return currentNoteHeight;
	}
	// This method stores the boolean value
	// passed in by the QuizPlayPanel class
	// when the player finishes their quiz
	public void setResumed(boolean value)
	{
		resumed = value;
	}
	// This method is called to get the
	// boolean value that indicates whether or not
	// to resume the game. If the game should 
	// resume, the boolean value will be true, 
	// and it will be so because the player has
	// passed the quiz.
	public boolean getResumed()
	{
		return resumed;
	}
	// This method stores the integer value of
	// how many times the player has lost a stage
	public void setNumLost(int numberOfTimesLost)
	{
		numLost = numberOfTimesLost;
	}
	// This method returns the integer value of how
	// many times the player has lost a stage. This value
	// is used to determine whether to end the player's game
	// or let them take a quiz.
	public int getNumLost()
	{
		return numLost;
	}
	// This method stores the integer value corresponding 
	// to the values indicating different steps of pausing the 
	// game to go look at the instructions using the menu. The 
	// value 0 indicates that the game is occuring as usual, 
	// the value 1 indicates that the player went to instructions
	// so the game must be paused. And the value 2 indicates that
	// the player returned from instructions and the game 
	// must resume. The value is then reset to 0 to continue
	// regular gameplay.
	public void setPauseForInst(int pauseNumber)
	{
		pauseForInst = pauseNumber;
	}
	// This method returns the integer value indicating
	// whether or not the game should be paused, resumed, 
	// or just continued like normal depending on 
	// whether or not the player is taking the steps
	// to read instructions using the menu.
	public int getPauseForInst()
	{
		return pauseForInst;
	}
	// This method stores the players score when they lose a stage
	// as this could end up being their final score for the game if they
	// lose the game or this is the second stage they lost in one game.
	public void setFinalScore(int endScore)
	{
		finalScore = endScore;
	}
	// This method returns the players final score. 
	// This value is displayed for the player in the closing panel
	// and high score closing panel of the game (depending on 
	// which the player sees).
	public int getFinalScore()
	{
		return finalScore;
	}
	// This method stores the player's name that is entered
	// if they got a high score. It is passed in by the HighScoreClosingPanel
	// class.
	public void setPlayerName(String name)
	{
		nameOfPlayer = name;
	}
	// This method returns the player's name that was entered 
	// if they got a high score. This is used by the LeaderboardPanel class
	// in order to get the player's name on the leaderboard.
	public String getPlayerName()
	{
		return nameOfPlayer;
	}
}
