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

// This class is the start panel that the game shows when you run it.
// It has buttons for navigation that let you play the game, look at 
// instructions, look at the credits, look at the leaderboard, 
// and close the game entirely (close the JFrame).
public class StartPanel extends JPanel
{
	private CardLayout mainCards; // a CardLayout object, this will be initialized to the object passed in by the MainHolderPanel class
	private MainHolderPanel mainHolder; // this is an instance of the holder panel class of the card layout, it is passed in by the MainHolderPanel class
										// so that this panel can use the cardLayout.show method in order to show other cards
	private GamePanel gp; // an instance of the GamePanel class, this is passed in 
						  // by the MainHolderPanel class to ensure that it is the same instance of the GamePanel and not a new one
						  // This instance is used to call a method that starts a new game for the player in the GamePanel class

// this method creates all of the buttons needed on the start panel.
// It also adds buttonHandler classes to each of the buttons.
// Since all of the buttons show a card panel of the card layout, I 
// used one buttonHandler class and made different instances of the class
// for each button. I then passed in a string value as a parameter of each
// of those instances. The string values are the names of the card panels
// that the cards were added to the card layout with in the MainHolderPanel class.
// The buttonHandler then takes in these string values and stores them in 
// a field variable in order to show the correct card panel.
// There is a second buttonHandler class (GameButtonHandler) which is for the
// start button. This class uses the same string perameter technique, but it
// also calls the runGame() method of the GamePanel class, so that a new 
// game begins. This panel has a null layout and buttons are added to it.
	public StartPanel(CardLayout cl, MainHolderPanel mhp, GamePanel gpin)
	{
		mainCards = cl;
		mainHolder = mhp;
		gp = gpin;
		
		Font buttonFont = new Font("Monospaced", Font.BOLD, 20);
		setLayout(null);
		
		// Creating the buttons and adding their instances of 
		// the same actionListener class to them 
		
		JButton instructionsButton = new JButton("Instructions"); // Creating the button that leads to the instructions panel
		instructionsButton.setFont(buttonFont);
		ButtonHandler bhi = new ButtonHandler("instructions panel");
		instructionsButton.addActionListener(bhi);
		
		JButton leaderButton = new JButton("Leaderboard"); // Creating the button that leads to the leaderboard panel
		leaderButton.setFont(buttonFont);
		ButtonHandler bhl = new ButtonHandler("leaderboard panel");
		leaderButton.addActionListener(bhl);
		
		JButton startButton = new JButton("Start"); // Creating the button that leads to the game panel
		startButton.setFont(buttonFont);
		GameButtonHandler gbhs = new GameButtonHandler("game panel");
		startButton.addActionListener(gbhs);
		
		JButton creditsButton = new JButton("Credits"); // Creating the button that leads to the credits panel
		creditsButton.setFont(buttonFont);
		ButtonHandler bhcr = new ButtonHandler("credits panel");
		creditsButton.addActionListener(bhcr);
		
		JButton closeButton = new JButton("Close"); // Creating the button that closes the window
		closeButton.setFont(buttonFont);
		ButtonHandler bhc = new ButtonHandler("close");
		closeButton.addActionListener(bhc);
		
		add(instructionsButton);
		add(leaderButton);
		add(startButton);
		add(creditsButton);
		add(closeButton);
		
		instructionsButton.setBounds(150,320,200,100);
		leaderButton.setBounds(750,320,200,100);
		creditsButton.setBounds(150,620,200,100);
		closeButton.setBounds(750,620,200,100);
		startButton.setBounds(450,470,200,100);
		HeaderPanel hp = new HeaderPanel();
		add(hp);
		hp.setBounds(100,70,900,200);
		
		setBackground(Color.PINK);
		
	}
// This class takes in the string value passed in by the instances
// of this class, and stores that value as a field variable. That value
// is then used to show the correct card panel corresponding to the button
// that was selected. The frame also closes when the close button is pressed
// by using the System.exit() method. 
	class ButtonHandler implements ActionListener
	{
		private String cardName; // This variable stores the String name that corresponds to the card that needs to be shown
								// when a particular button is pressed
		public ButtonHandler(String name)
		{
			cardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
			if(cardName.equals("close"))
			{
				System.exit(1);
			}
			else 
			{
				mainCards.show(mainHolder,cardName);
			}
		}
	}
// This class takes in the string value passed in by the instance of this class.
// This class also calls teh runGame() method of the GamePanel
// class so that a new game begins when you press the start button.
// This class also shows the player the GamePanel.
	class GameButtonHandler implements ActionListener
	{
		private String gameCardName; // This variable stores the String name that corresponds to the card that needs to be shown
									// when a particular button is pressed
		public GameButtonHandler(String name)
		{
			gameCardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
				mainCards.show(mainHolder,gameCardName);
				gp.runGame(); // causes a new game to start each time the start button is pressed
				
		}
	}
}
// This class loads an image and draws it onto a panel that is added
// to the start panel. The image is of the header's background (seen as the 
// picture with the name's title on it on the start panel).
class HeaderPanel extends JPanel
{	
	private Image headerImage; // an Image object that contains the object of the image of the header, it is used to draw the image
	private String headerImageName; // a String object containing the name of the image file so that it can be loaded and the image can be drawn
	public HeaderPanel()
	{
		headerImage = null;
		headerImageName = new String("MusiManiaHeaderImage.jpg");
		setPreferredSize(new Dimension(900,200));
		// Calling the method that loads the image
		getHeaderImage();
	}
	public void getHeaderImage()
	{
		// A try catch block that loads the image
		File imageFile = new File(headerImageName);
		try
		{
			headerImage = ImageIO.read(imageFile);
		}
		catch(IOException e)
		{
			System.err.println("\n\n" + headerImageName + " can't be found.\n\n");
			e.printStackTrace(); 
		}
	}
	public void paintComponent(Graphics g)
	{
		// This method is drawing the image to onto the panel
		// (which is then added to the start panel)
		// Text is then drawn onto the image stating
		// the game's name. I created this image on my own
		// digitally. 
		
		g.drawImage(headerImage,0,0,900,200,this);
		g.setFont(new Font("Monospaced", Font.PLAIN, 60));
		g.drawString("MusiMania",55,114);
	}
}
