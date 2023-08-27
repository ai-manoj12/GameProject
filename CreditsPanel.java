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

// This class creates the credits panel which holds
// a JButton to exit the panel and 
// a JText Area containing credits.
public class CreditsPanel extends JPanel
{
	private CardLayout mainCards; // a cardLayout object that takes in my cardLayout object that is passed in so that different cards can be shown
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class that will be passed in so that different cards can be shown 
	private JTextArea creditWritten; // a JTextArea containing the written credits
	private Scanner readCredits; // a Scanner object used to read the text file containing credits
	private String creditsFileName; // a String object containing the name of the text file containing the credits
	public CreditsPanel(CardLayout cl, MainHolderPanel mhp)
	{
		setLayout(null);
		mainCards = cl;
		mainHolder = mhp;
		readCredits = null;
		creditsFileName = new String("creditsInfo.txt");
		
		setBackground(Color.ORANGE);
		creditWritten = new JTextArea("",7,20);	// Creating a JTextArea to show the player the credits
		creditWritten.setLineWrap(true);  // goes to the next line when printing the text.
		creditWritten.setWrapStyleWord(true);
		creditWritten.setEditable(false); // prevents a word from being split 
		creditWritten.setMargin(new Insets(20,20,0,20));
		creditWritten.setPreferredSize(new Dimension(800,700));
		creditWritten.setFont(new Font("Monospaced", Font.PLAIN, 11));
		creditWritten.setBackground(Color.WHITE);
		creditWritten.setBounds(100,20,900,700);
		add(creditWritten);
		getText();
		
		JButton exitButton2 = new JButton("Exit"); // Creating an exit button
		exitButton2.setFont(new Font("SansSerif", Font.PLAIN, 30));
		ButtonHandlerExit2 bhe2 = new ButtonHandlerExit2("start panel");
		exitButton2.addActionListener(bhe2);
		exitButton2.setBounds(475,730,150,40);
		add(exitButton2);
	}
// This method is called when the program first runs. It uses
// a try-catch block to create a scanner to read the text file
// containing the credits and then sets those credits as the
// text of the text area, thus displaying the credits for
// the player to see.
	public void getText()
	{
		String creditsText = new String("");
		String readingLine = new String("");
		
		File inCredits = new File(creditsFileName);
		// This try catch block creates a scanner object in order
		// to read from the text file containing credits
		try 
		{
			readCredits = new Scanner(inCredits);
		}
		catch(FileNotFoundException e) // catching an exception if it's thrown (indicating that the Scanner couldn't be created) so the program still compiles
		{
			System.err.printf("\n\nERROR: Cannot find/open %s.\n\n", creditsFileName);
			System.exit(8);
		}
		int lineCounter = 0;
		while(readCredits.hasNext() && lineCounter < 21) // reading lines from the text file and storing it in a String object 
		{
			readingLine = readCredits.nextLine();
			creditsText += (readingLine + "\n\n");
			lineCounter++;
		}
		creditWritten.setText(creditsText); // setting the text of the JText Area to the value of the string of credits stored
		readCredits.close();
	}
// This is the buttonHandler class for the exit button
// on this panel that allows the player to return to
// the home screen.
	class ButtonHandlerExit2 implements ActionListener
	{
		private String cardName; // a String object containing the string value corresponding to the card that needs to be shown
								// this value is passed in by the object of the button handler class depending on which 
								// button was pressed
		public ButtonHandlerExit2(String name)
		{
			cardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
			
			mainCards.show(mainHolder,cardName); // showing the correct card depending on the button the player pressed
		}
	}
}
