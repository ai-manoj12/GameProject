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

// This class creates the closing panel
// to be displayed when the player loses the game
// and doesn't have a high score. This panel uses a borderlayout.
public class ClosingPanel extends JPanel
{
	private CardLayout mainCards; // a cardlayout object of the main cardlayout for the game
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class
	private GameData gd; // an object of the GameData class
	private JTextArea scoreResultsTA; // the JTextArea that displays the player's score
	public ClosingPanel(CardLayout cl, MainHolderPanel mhp, GameData gdIn)
	{
		mainCards = cl;
		mainHolder = mhp;
		gd = gdIn;
		setBackground(new Color(194,246,255));
		setLayout(new BorderLayout());
			
			
		JPanel scoreResultsPanel = new JPanel();
		scoreResultsPanel.setPreferredSize(new Dimension(1100,250));
		scoreResultsPanel.setBackground(Color.ORANGE);
			
		scoreResultsTA = new JTextArea("You scored _____ points",7,20);	 // Creating a JTextArea that tells the player what they scored in the game
		scoreResultsTA.setLineWrap(true);  // goes to the next line when printing the text.
		scoreResultsTA.setWrapStyleWord(true);
		scoreResultsTA.setEditable(false); 
		scoreResultsTA.setMargin(new Insets(60,60,0,60));
		scoreResultsTA.setFont(new Font("Monospaced", Font.BOLD, 70));
		scoreResultsTA.setBackground(Color.ORANGE);
			
		JTextArea closingMessage = new JTextArea("Great job! Now You're even closer to being an amazing musician!",7,20);	// Creating a JTextArea with a closing message
		closingMessage.setLineWrap(true);  // goes to the next line when printing the text.
		closingMessage.setWrapStyleWord(true);
		closingMessage.setEditable(false); // prevents a word from being split 
		closingMessage.setMargin(new Insets(60,60,0,60));
		closingMessage.setFont(new Font("Monospaced", Font.BOLD, 50));
		closingMessage.setBackground(new Color(194,246,255));
			
		JPanel exitButtonHolderPanel = new JPanel();
		exitButtonHolderPanel.setPreferredSize(new Dimension(1100,100));
		exitButtonHolderPanel.setBackground(Color.PINK);
		exitButtonHolderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,20,20));
			
		JPanel scoreResultsSpacing1 = new JPanel(); // Creating panels to help with spacing in flow layout
		JPanel scoreResultsSpacing2 = new JPanel();
		scoreResultsSpacing1.setBackground(Color.ORANGE);
		scoreResultsSpacing1.setPreferredSize(new Dimension(200,60));
		scoreResultsSpacing2.setBackground(Color.ORANGE);
		scoreResultsSpacing2.setPreferredSize(new Dimension(200,60));
			
			
		JButton exitButton = new JButton("Exit"); // Creating an exit button
		exitButton.setFont(new Font("Monospaced", Font.BOLD, 30));
		exitButton.setPreferredSize(new Dimension(100,60));
		ExitButtonHandler ebh = new ExitButtonHandler("start panel");
		exitButton.addActionListener(ebh);
			
		scoreResultsPanel.add(scoreResultsTA);
		exitButtonHolderPanel.add(exitButton);
			
		add(scoreResultsPanel, BorderLayout.NORTH);
		add(exitButtonHolderPanel, BorderLayout.SOUTH);
		add(scoreResultsSpacing1, BorderLayout.EAST);
		add(scoreResultsSpacing2, BorderLayout.WEST);
		add(closingMessage, BorderLayout.CENTER);
			
	}
// This method gets the player's score and displays
// it for them by adding it to the text area at the top of the panel.
	public void displayClosingScore()
	{
		int gameScore = gd.getFinalScore();
		scoreResultsTA.setText("You scored " + gameScore + " points");
	}
// This classreacts to the player pressing the exit button, which
// brings them back to the start panel. This class is like most
// of the other button handler classes in this program
// in that it takes in the name of the card the button 
// should show as a parameter and uses it to show the card in question.
	class ExitButtonHandler implements ActionListener
	{
		String cardName; // This variable stores the String name that corresponds to the card that needs to be shown
						 // when a particular button is pressed
		public ExitButtonHandler(String nameCard)
		{
			cardName = nameCard;
		}
		public void actionPerformed(ActionEvent evt)
		{
			mainCards.show(mainHolder, cardName);
		}
	}
}
