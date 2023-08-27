import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
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

public class InstructionsPanel extends JPanel
{
	private CardLayout mainCards; // a cardLayout object that takes in my cardLayout object that is passed in so that different cards can be shown
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class that will be passed in so that different cards can be shown
	private GamePanel gp; // an object of my GamePanel class that is passed in so that the method runGame() can be called to start a new game
						  // when a start button is pressed from the instructions panel
	private GameData gd; // an object of the GameData class that is passed so that this method can pass in a numerical value
						// indicating that the player is returning to the game after going to the instructions by pressing help in the menu
	public InstructionsPanel(CardLayout cl, MainHolderPanel mhp, GamePanel gpin, GameData gdin)
	{
		setLayout(null);
		mainCards = cl;
		mainHolder = mhp;
		gp = gpin;
		gd = gdin;
		setBackground(Color.PINK);
		InstructionsHolder ih = new InstructionsHolder();
		ih.setBounds(100,30,900,700);
		add(ih);
		
	}
// This class is a Jpanel which contains a scroll bar (that is part of a JScrollPane) that scrolls through another panel
	class InstructionsHolder extends JPanel
	{
		public InstructionsHolder()
		{
			InstructionsWritten pan = new InstructionsWritten();
			add(pan);
			// adding the scrollpane to this panel but it is scrolling through the panel InstructionsWritten
			JScrollPane scroller = new JScrollPane(pan,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
			scroller.setPreferredSize(new Dimension(900,700));
			add(scroller);
		}
	}
// This class is a JPanel which contains buttons to start a new game or go
// back to the start panel. This will eventually contain written instructions.
	class InstructionsWritten extends JPanel
	{
		private JButton startButton; // the button that lets the player start a new game
		private JButton backButton; // the button that lets the player go back to the start panel
		private JTextArea paragraph1; // a text area containing instructions on the purpose of the game
		private JTextArea paragraph2; // a text area containing instructions on how to read notes and keys
		private JTextArea paragraphToPlay; // a text area containing instructions on how to play the game
		private JTextArea paragraphGameControls; // a text area containing instructions on how to control the game
		private JTextArea paragraphQuizControls; // a text area containing instructions on how to control the quiz
		private JTextArea paragraphClosing; // a text area containing the closing words for the instructions
		private Scanner instructionsReader; // a Scanner object used to read through a text file containing instructions
		private String instructionsFileName; // a String object containing the name of the text file containing instructions
		public InstructionsWritten()
		{
			setBackground(new Color(246,116,121));
			instructionsReader = null;
			instructionsFileName = new String("instructionsInfo.txt");
			
			// Creating JTextAreas to appear on the InstructionsWritten panel
			// so that the player can scroll through the panel and see all of 
			// the instructions
			
			paragraph1 = new JTextArea("",7,20); // Creating a JTextArea to show the intro to the player
			paragraph1.setLineWrap(true);  
			paragraph1.setWrapStyleWord(true);
			paragraph1.setEditable(false); 
			paragraph1.setMargin(new Insets(30,30,0,30));
			paragraph1.setPreferredSize(new Dimension(900,400));
			paragraph1.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraph1.setBackground(new Color(199,239,245));
			
			paragraph2 = new JTextArea("",7,20); // Creating a JTextArea to tell the player how to read notes
			paragraph2.setLineWrap(true);  
			paragraph2.setWrapStyleWord(true);
			paragraph2.setEditable(false);
			paragraph2.setMargin(new Insets(30,30,0,30));
			paragraph2.setPreferredSize(new Dimension(900,820));
			paragraph2.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraph2.setBackground(new Color(199,239,245));
			
			paragraphToPlay = new JTextArea("",7,20); // Creating a JTextArea to tell the player how to play the game
			paragraphToPlay.setLineWrap(true);  
			paragraphToPlay.setWrapStyleWord(true);
			paragraphToPlay.setEditable(false); 
			paragraphToPlay.setMargin(new Insets(30,30,0,30));
			paragraphToPlay.setPreferredSize(new Dimension(900,1750));
			paragraphToPlay.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraphToPlay.setBackground(new Color(250,250,187));
			
			paragraphGameControls = new JTextArea("",7,20);	// Creating a JTextArea to tell the player how to control the game
			paragraphGameControls.setLineWrap(true);  
			paragraphGameControls.setWrapStyleWord(true);
			paragraphGameControls.setEditable(false); 
			paragraphGameControls.setMargin(new Insets(30,30,0,30));
			paragraphGameControls.setPreferredSize(new Dimension(900,990));
			paragraphGameControls.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraphGameControls.setBackground(new Color(239,211,255));
			
			paragraphQuizControls = new JTextArea("",7,20);	// Creating a JTextArea to tell the player how to control the quiz
			paragraphQuizControls.setLineWrap(true);  
			paragraphQuizControls.setWrapStyleWord(true);
			paragraphQuizControls.setEditable(false); 
			paragraphQuizControls.setMargin(new Insets(30,30,0,30));
			paragraphQuizControls.setPreferredSize(new Dimension(900,300));
			paragraphQuizControls.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraphQuizControls.setBackground(new Color(255,220,241));
			
			paragraphClosing = new JTextArea("",4,20);	// Creating a JTextArea with a closing message for the player
			paragraphClosing.setLineWrap(true);  
			paragraphClosing.setWrapStyleWord(true);
			paragraphClosing.setEditable(false); 
			paragraphClosing.setMargin(new Insets(30,30,0,0));
			paragraphClosing.setPreferredSize(new Dimension(900,100));
			paragraphClosing.setFont(new Font("Monospaced", Font.PLAIN, 30));
			paragraphClosing.setBackground(new Color(255,220,241));
			
			// Creating objects of classes that will load and draw images
			// onto the panels. These panels are then added to the InstructionsWritten
			// panel so that the player can see them while reading instructions
			
			GamePlayImagePanel gpin = new GamePlayImagePanel();
			QuizQuestionImagePanel qqip = new QuizQuestionImagePanel();
			ReadingNotesImagePanel rnip = new ReadingNotesImagePanel();
			MenuImagePanel mip = new MenuImagePanel();
			
			// Calling the getText() method sets the text of all of the
			// text areas to the desired instructions that are read
			// from a text file
			
			getText();
			add(paragraph1);
			add(paragraph2);
			add(rnip);
			add(paragraphToPlay);
			add(gpin);
			add(paragraphGameControls);
			add(mip);
			add(paragraphQuizControls);
			add(qqip);
			add(paragraphClosing);
			
			// These are the dimensions for the InstructionsWritten panel.
			// This panel holds all of the instructions text areas and 
			// panels with images drawn on them. The height of this panel
			// is far greater than the height of it's holder panel and that
			// is because you scroll through this panel, so it can be 
			// larger than the panel it is held in (since only a portion
			// of this panel is shown at a time)
			
			setPreferredSize(new Dimension(350,6580));
			startButton = new JButton("Start");
			backButton = new JButton("Back");
			startButton.setPreferredSize(new Dimension(150,80));
			backButton.setPreferredSize(new Dimension(150,80));
			startButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			backButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			
			ButtonHandler2 bhs = new ButtonHandler2("game panel");
			startButton.addActionListener(bhs);
			
			ButtonHandler3 bhb = new ButtonHandler3("start panel");
			backButton.addActionListener(bhb);
			
			add(backButton);
			add(startButton);
		}
// This method is called when the program first runs. It 
// creates a scanner object using a try catch block and then
// sets the text that it reads as the text for Text Areas
// that are added to the instructions written panel for
// the player to scroll through. The file that is being
// read contains the instructions.
		public void getText()
		{
			// Initializing the values for variables used for 
			// getting the text that will be added to the 
			// text areas
			String paragraph1Text = new String("");
			String paragraph2Text = new String("");
			String paragraphToPlayText = new String("");
			String paragraphGameControlText = new String("");
			String paragraphQuizControlText = new String("");
			String paragraphClosingText = new String("");
			String textLine = new String("");
			
			// This is a try catch block used to load create a 
			// Scanner object to read the text file with
			
			File inInstructions = new File(instructionsFileName);
			try 
			{
				instructionsReader = new Scanner(inInstructions);
			}
			catch(FileNotFoundException e)
			{
				System.err.printf("\n\nERROR: Cannot find/open %s.\n\n",
					instructionsFileName);
				System.exit(7);
			}
			// The following code is used to read specific portions
			// and lines of the text file and set them as the text
			// for specific text areas.
			
			int lineCounter = 0;
			while(instructionsReader.hasNext() && lineCounter < 8) // Storing the text from a portion of the text file into a string
			{
				textLine = instructionsReader.nextLine();
				paragraph1Text += (textLine + " ");
				lineCounter++;
			}
			paragraph1.setText(paragraph1Text); // Setting the text of a Text Area to the value inside that String
			textLine = new String("");
			lineCounter = 9;
			instructionsReader.nextLine();
			while(instructionsReader.hasNext() && lineCounter < 22)
			{
				textLine = instructionsReader.nextLine();
				paragraph2Text += (textLine + " ");
				lineCounter++;
			}
			paragraph2.setText(paragraph2Text);
			textLine = new String("");
			instructionsReader.nextLine();
			instructionsReader.nextLine();
			lineCounter = 24;
			while(instructionsReader.hasNext() && lineCounter < 61)
			{
				textLine = instructionsReader.nextLine();
				paragraphToPlayText += (textLine + " ");
				lineCounter++;
			}
			paragraphToPlay.setText(paragraphToPlayText);
			textLine = new String("");
			instructionsReader.nextLine();
			lineCounter = 62;
			while(instructionsReader.hasNext() && lineCounter < 80)
			{
				textLine = instructionsReader.nextLine();
				paragraphGameControlText += (textLine + " ");
				lineCounter++;
			}
			paragraphGameControls.setText(paragraphGameControlText);
			textLine = new String("");
			lineCounter = 86;
			instructionsReader.nextLine();
			while(instructionsReader.hasNext() && lineCounter < 91)
			{
				textLine = instructionsReader.nextLine();
				paragraphQuizControlText += (textLine + " ");
				lineCounter++;
			}
			paragraphQuizControls.setText(paragraphQuizControlText);
			textLine = new String("");
			lineCounter = 91;
			instructionsReader.nextLine();
			while(instructionsReader.hasNext() && lineCounter < 93)
			{
				textLine = instructionsReader.nextLine();
				paragraphClosingText += (textLine + " ");
				lineCounter++;
			}
			paragraphClosing.setText(paragraphClosingText);
			
			// Closing the Scanner so that no data is lost
			instructionsReader.close();
		}
// paintComponent is constantly called by Swing 
// when needed so I used it to check the updated
// variables. If the getpauseForInst() method of
// the GameData class returns the number 1 then the 
// player is reading instructions by clicking
// the help button on the menu mid game. This means
// that the text on the start button is changed to 
// "Return to Game" and the back button's text is
// changed to "Quit" for clarity. If the method
// returns anything else that means the instructions
// aren't being read mid game and thus the buttons'
// texts are set back to what they originally are
// (start and back).
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if(gd.getPauseForInst() == 1)
			{
				startButton.setPreferredSize(new Dimension(300,80));
				startButton.setText("Return to Game");
				backButton.setText("Quit");
			}
			else 
			{
				startButton.setPreferredSize(new Dimension(150,80));
				startButton.setText("Start");
				backButton.setText("Back");
			}
		}
	}
// This class is an actionListener which allows different cards to be
// shown depending on which button is pressed. As always it takes in 
// a string value passed in by the objects of this class, which 
// then allows the button's corresponding card to be shown. This class
// makes the player return to their current game if they press the
// return to game button while reading instructions through pressing
// help in the menu of the game. This class makes the player
// start a new game if they press the start button when going
// to instructions without being mid game.
	class ButtonHandler2 implements ActionListener
	{
		private String cardName; // This variable stores the String name that corresponds to the card that needs to be shown
								 // when a particular button is pressed
		public ButtonHandler2(String name)
		{
			cardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
			String message = evt.getActionCommand();
			if (message.equals("Start")) // If the player presses start then a new game will begin
			{
				mainCards.show(mainHolder,cardName);
				gp.runGame();
			}
			else if (message.equals("Return to Game")) // If the player presses Return to Game then their game simply resumes
			{
				gd.setPauseForInst(2); // let's game know it needs to be resumed
				mainCards.show(mainHolder,cardName);
			}
		}
	
	}
// This class let's the player end their game and go back 
// to the start panel or just go back to the start panel in general.
	class ButtonHandler3 implements ActionListener
	{
		private String cardName;// This variable stores the String name that corresponds to the card that needs to be shown
								// when a particular button is pressed
		public ButtonHandler3(String name)
		{
			cardName = name;
		}
		public void actionPerformed(ActionEvent evt)
		{
			gd.setPauseForInst(0); // resets game's initial values
			mainCards.show(mainHolder,cardName);
		}
	
	}
}
// This class is a panel that loads an Image and then uses the paintComponent()
// method to draw it onto the panel. This panel is later added to the InstructionsWritten
// panel
class GamePlayImagePanel extends JPanel 
{
	private Image gamePlay; // This is an Image object that contains the Image of what the game looks like, it is used to draw the image
	private String gamePlayImageName; // This is a String containing the name of the Image file, it is used to load the image so that it can be drawn
	public GamePlayImagePanel()
	{
		gamePlay = null;
		gamePlayImageName = new String("gamePlayImage.PNG");
		setPreferredSize(new Dimension(800,580));
		// Calling the method that loads the image
		getGameImage();
	}
	public void getGameImage()
	{
		// This try catch block loads the image so that it can be
		// drawn on the panel
		File imageFile = new File(gamePlayImageName);
		try
		{
			gamePlay = ImageIO.read(imageFile);
		}
		catch(IOException e)
		{
			System.err.println("\n\n" + gamePlayImageName + " can't be found.\n\n");
			e.printStackTrace(); 
		}
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(gamePlay,0,0,800,567,this); // Drawing the image on the panel and resizing it
	}
}
class QuizQuestionImagePanel extends JPanel
{
	private Image quizQuestion; // This is an Image object that contains the Image of what the game looks like, it is used to draw the image
	private String quizQuestionImageName; // This is a String containing the name of the Image file, it is used to load the image so that it can be drawn
	public QuizQuestionImagePanel()
	{
		quizQuestion = null; // initializing all of the field variables for this class
		quizQuestionImageName = new String("quizQuestionImage.PNG");
		setPreferredSize(new Dimension(850,610));
		getQuizImage(); // calling the method that loads the image
	}
	public void getQuizImage()
	{
		// This try catch block loads the image so that it
		// can be drawn
		File imageFile = new File(quizQuestionImageName);
		try
		{
			quizQuestion = ImageIO.read(imageFile);
		}
		catch(IOException e) // Catches an exception that could be thrown if the Image is unable to load (so that the program can still compile)
		{
			System.err.println("\n\n" + quizQuestionImageName + " can't be found.\n\n");
			e.printStackTrace(); 
		}
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(quizQuestion,0,0,850,602,0,0,1099,778,this); // drawing the image to the screen by picking a section of it
																 // to draw and then resizing it and then drawing it to the screen
	}
	
}
class ReadingNotesImagePanel extends JPanel
{
	private Image readNotes; // This is an Image object that contains the Image of how to read notes and read the piano keyboard, it is used to draw the image
	private String readNotesImageName; // This is a String containing the name of the Image file, it is used to load the image so that it can be drawn
	public ReadingNotesImagePanel()
	{
		readNotes = null;
		readNotesImageName = new String("readingNotesImage.PNG");
		setPreferredSize(new Dimension(700,550));
		// calling the method that loads the image
		getNotesImage();
	}
	public void getNotesImage()
	{
		// This try catch block loads the image so that it can be drawn on 
		// the panel
		File imageFile = new File(readNotesImageName);
		try
		{
			readNotes = ImageIO.read(imageFile);
		}
		catch(IOException e)
		{
			System.err.println("\n\n" + readNotesImageName + " can't be found.\n\n");
			e.printStackTrace(); 
		}
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(readNotes,0,0,700,530,this); // Drawing the image on the panel and resizing it
	}
	
}
class MenuImagePanel extends JPanel
{
	private Image menuImage; // This is an Image object that contains the Image of what the menu of the game (and the piano keyboard at the bottom with the computer
							 // keys corresponding to it) looks like, it is used to draw the image
	private String menuImageName; // This is a String containing the name of the Image file, it is used to load the image so that it can be drawn
	public MenuImagePanel()
	{
		menuImage = null;
		menuImageName = new String("menuImage.PNG");
		setPreferredSize(new Dimension(850,250));
		// Calling the method that loads the image
		getMenuImage();
	}
	public void getMenuImage()
	{
		// Loading the image so that it can be 
		// drawn onto the panel
		File imageFile = new File(menuImageName);
		try
		{
			menuImage = ImageIO.read(imageFile);
		}
		catch(IOException e)
		{
			System.err.println("\n\n" + menuImageName + " can't be found.\n\n");
			e.printStackTrace(); 
		}
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(menuImage,0,0,850,233,this); // Drawing the image to the panel and resizing it
	}
}
