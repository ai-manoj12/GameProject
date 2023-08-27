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

// This class has a cardLayout that 
// holds the card panels that are needed
// for each part of the quiz the player plays
// when they lose one stage of the game. This includes
// the intro panel, the play panel that actually has the quiz,
// and the closing panel that appears and tells the player
// their score and whether or not they can resume their game.
public class QuizHolderPanel extends JPanel
{
	private CardLayout mainCards; // a cardLayout object that takes in my cardLayout object that is passed in so that different cards can be shown
	private MainHolderPanel mainHolder; // an object of the MainHolderPanel class that will be passed in so that different cards can be shown 
	private GamePanel gPan; // an object of the GamePanel class that lets this method use data from the player's game (such as the if this is their first stage lost)
	private int quizScore; // a variable tracking the players score in the quiz
	private int numQuizQuestions; // a variable tracking the number of questions the player has received in the quiz.
	private QuizPlayPanel qppan; // an object of the QuizPlayPanel class that lets the class' methods be called from methods in this class
	private QuizData qData; // an object of the QuizData class that lets the class' methods be called from methods in this class
	private QuizClosingPanel qcpan; // an object of the QuizClosingPanel class that lets the class' methods be called from methods in this class
	private GameData gd; // an object of the GameData class that lets this class use data from the player's game (such as their final score)
	private QuizIntroPanel qipan; // an object of the QuizIntroPanel class that lets the class' methods be called from methods in this class
	private ClosingPanel cp; // an object of the ClosingPanel class that lets the class' methods be called from methods in this class
	private HighScoreClosingPanel hcp; // an object of the HighScoreClosingPanel class that lets the class' methods be called from methods in this class
// This is the constructor, it is creating the card layout for the quiz and adding the cards to it
	public QuizHolderPanel(CardLayout cl, MainHolderPanel mhp, GamePanel gpIn, GameData gdIn, ClosingPanel cpIn, HighScoreClosingPanel hcpIn)
	{
		// Storing passed in values in field variables for this class so that they can be used in other methods of this class
		mainCards = cl;
		mainHolder = mhp;
		gPan = gpIn;
		quizScore = 0;
		numQuizQuestions = 1;
		gd = gdIn;
		cp = cpIn;
		hcp = hcpIn;
		
		CardLayout quizCards = new CardLayout();
		setLayout(quizCards);
		qData = new QuizData();
		qData.readQandAFromFile();
		qipan = new QuizIntroPanel(this,quizCards);
		qppan = new QuizPlayPanel(this,quizCards,qData);
		qcpan = new QuizClosingPanel(this,quizCards,gPan);
		
		add(qipan, "intro");
		add(qppan, "question");
		add(qcpan, "closing");
		
		quizCards.show(this,"intro");
	}
// This method is called when the player finishes one quiz so that if the
// game is played again without rerunning the program a new quiz will appear
// since all of its values were reset
	public void initialQuizValues()
	{
		quizScore = 0;
		numQuizQuestions = 0;
	}
// This class contains the QuizIntroPanel which tells the player
// what note they got wrong regardless of if they can move onto
// the quiz or not (depending on if this is their first or second
// time losing a stage in one game).
	class QuizIntroPanel extends JPanel
	{
		private QuizHolderPanel quizHolder; // an object of the QuizHolderPanel class
		private CardLayout quizCards; // an object of the card layout for the quiz
		JTextArea whatNote; // the JTextArea that tells the player what note they were stuck on
		public QuizIntroPanel(QuizHolderPanel qhpIn, CardLayout qc)
		{
			quizHolder = qhpIn;
			quizCards = qc;
			setBackground(Color.PINK);
			setLayout(new BorderLayout());
			
			JPanel textAreaHolderPanel = new JPanel();
			textAreaHolderPanel.setPreferredSize(new Dimension(1100,640));
			textAreaHolderPanel.setBackground(new Color(189,118,189));
			
			whatNote = new JTextArea("This is the note you got wrong",7,20); // Creating a JTextArea to tell the player what note they were stuck on
			whatNote.setLineWrap(true);  // goes to the next line when printing the text.
			whatNote.setWrapStyleWord(true);
			whatNote.setEditable(false); // prevents a word from being split 
			whatNote.setMargin(new Insets(90,60,0,60));
			whatNote.setFont(new Font("Monospaced", Font.BOLD, 60));
			whatNote.setBackground(new Color(194,246,255));
			
			JPanel buttonHolderPanel = new JPanel(); // creating the panel to hold the buttons
			buttonHolderPanel.setPreferredSize(new Dimension(1100,100));
			buttonHolderPanel.setBackground(Color.PINK);
			JPanel spacingPanel = new JPanel();
			spacingPanel.setBackground(Color.PINK);
			spacingPanel.setPreferredSize(new Dimension(820,60));
			buttonHolderPanel.setLayout(new FlowLayout(20,20,FlowLayout.CENTER));
			
			JButton nextButton = new JButton("Next"); // Creating a next button
			nextButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			nextButton.setPreferredSize(new Dimension(100,60));
			ButtonHandler nbhs = new ButtonHandler("question");
			nextButton.addActionListener(nbhs);
			
			JButton quitButton = new JButton("Quit"); // Creating a quit button
			quitButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			quitButton.setPreferredSize(new Dimension(100,60));
			ButtonHandler nbhq = new ButtonHandler("start panel");
			quitButton.addActionListener(nbhq);
			
			
			buttonHolderPanel.add(quitButton);
			buttonHolderPanel.add(spacingPanel);
			buttonHolderPanel.add(nextButton);
			textAreaHolderPanel.add(whatNote);
			add(textAreaHolderPanel, BorderLayout.NORTH);
			add(buttonHolderPanel, BorderLayout.SOUTH);
			
		}
// This is the paintComponent method. I used it because
// Swing calls it as many times as needed, which includes
// if values were updated. This method checks what note
// the player was stuck on by getting the height value 
// of the note from the GameData class. It then
// prints which note the player missed (by getting 
// the name of the note from the QuizData class) as 
// well as what key on the computer keyboard corresponds 
// to that note (by once again getting that
// value from the QuizData class).
		public void paintComponent (Graphics g)
		{
			super.paintComponent(g);
			whatNote.setText("The note you were stuck on was ");
			whatNote.append(qData.getNoteName(gd.getCurrentNote())); // taking the value of the note the player was stuck on from the GameData class and 
																	 // passing it into a method in the QuizData class that returns a string containing the 
																	 // name of the note so that it can be added to the text field to show the player
																	 // what note they missed
			whatNote.append(", remember this is " );
			whatNote.append(qData.getKeyName(gd.getCurrentNote()));
			whatNote.append(" on your computer keyboard.");
			
		}
// This class is the ButtonHandler that checks if the player
// presses quit or next on the quiz intro panel. If they press quit
// they are brought back to the start panel. If they press next and
// this was the second time they lost a stage, they are shown the closing screen.
// Their closing screen depends on if they got a highscore, which is
// determined by the QuizData class. If they got a highscore they
// are shown the HighScoreClosingPanel which gets the player's
// name by having them enter it into a text field. If they did not get
// a high score they are shown the regular closing panel. If this 
// is the first time the player has lost a stage, they are brought to the
// quiz, which is the QuizPlayPanel class. 
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
				String message = evt.getActionCommand();
				if(message.equals("Quit")) // if the player presses quit show them the start panel
				{
					mainCards.show(mainHolder, cardName); 
				}
				else if(message.equals("Next") && gd.getNumLost() == 3) // if the player presses next and this is their second stage lost then they are shown a closing panel
				{
					int scorePlayer = gd.getFinalScore();
					qData.compareScores(scorePlayer);
					if(qData.isHighScore() == true) // if the player got a highscore they are shown the closing panel for high scores
					{
						hcp.displayHighScore();
						mainCards.show(mainHolder, "high score closing panel");
					}
					else
					{
						cp.displayClosingScore();
						mainCards.show(mainHolder, "closing panel"); // if the player didn't get a high score then they are shown a regular closing panel
					}
				}
				else if(message.equals("Next") && gd.getNumLost() != 3) // if the player presses next and this is the first stage they lost then they are shown the quiz
				{
					quizCards.show(quizHolder,cardName);
				}
							
			}
		}
	}
// This class instantiates all of the buttons and values needed for the quiz. This class
// is the one that basically creates the playable quiz. The instantiated values also
// serve to create the first question and answer set in the game by calling the 
// QuizData class and getting those values. However, this is only for the 
// very first time a quiz is played in one run of the java file, when the game
// is replayed multiple times in one run, the first question's values are 
// set in the nextQuestion method, just like all of the other questions. 
	class QuizPlayPanel extends JPanel 
	{
		private QuizHolderPanel quizHolder; // an object of the QuizHolderPanel class that lets this class show different cards of the quiz's card layout
		private QuizData qData; // an object of the GameData class that lets the class' methods be called from methods in this class in order to get data
		private CardLayout quizCards; // an object of the card layout for the quiz that lets this method show different cards of the quiz's card layout
		private ButtonGroup choicesGroup; // the button group for the radio buttons that are answer choices for quiz questions
		private JTextArea questionTA; // the JTextArea that holds the quiz question
		private JRadioButton [] answerChoices; // the array of radio buttons that are the answer choices for the quiz questions
		private JButton submit; // the button used to submit an answer for a question on the quiz
		private JButton next; // the button used to go to the next question on the quiz or the quiz's closing panel
		private JButton quit; // the button to quit the quiz/game and go back to the start panel
		private boolean finishQuiz; // the boolean value for whether or not the player has finished their quiz
		public QuizPlayPanel(QuizHolderPanel qhpIn, CardLayout qc, QuizData qdIn) 
		{
			quizHolder = qhpIn;
			quizCards = qc;
			qData = qdIn;
			finishQuiz = false;
			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			
			JPanel questionPanel = new JPanel();
			questionPanel.setPreferredSize(new Dimension(1100,280));
			questionPanel.setBackground(Color.ORANGE);
			
			Font questionFont = new Font("Monospaced", Font.BOLD, 50); // Creating a JTextArea that holds the question for the quiz
			questionTA = new JTextArea(qData.getQuestion(numQuizQuestions), 3, 30);
			questionTA.setFont(questionFont);
			questionTA.setLineWrap(true);
			questionTA.setWrapStyleWord(true);
			questionTA.setOpaque(false);
			questionTA.setEditable(false);
			questionPanel.add(questionTA);
			
			JPanel quizPlaySpacingPanel1 = new JPanel(); // Creating a panel to help with spacing for the flow layout
			quizPlaySpacingPanel1.setBackground(Color.PINK);
			quizPlaySpacingPanel1.setPreferredSize(new Dimension(200,50));
			
			JPanel quizPlaySpacingPanel2 = new JPanel();
			quizPlaySpacingPanel2.setBackground(Color.PINK);
			quizPlaySpacingPanel2.setPreferredSize(new Dimension(200,50));
			
			
			JPanel answersPanel = new JPanel(); // Creating the panel to hold the answer choices
			answersPanel.setPreferredSize(new Dimension(1100,320));
			answersPanel.setBackground(Color.WHITE);
			QuizButtonHandler qbHandler = new QuizButtonHandler();
			answersPanel.setLayout(new GridLayout(4,1));
			answerChoices = new JRadioButton[4];
			Font answerFont = new Font("Monospaced", Font.BOLD, 20);
			
			// getting answer choices to show up for a question
			choicesGroup = new ButtonGroup();
			for(int i = 0; i < answerChoices.length; i++)
			{
				answerChoices[i] = new JRadioButton("" + qData.getAnswer(i)); // adding the answer choices to the JRadioButtons 
				choicesGroup.add(answerChoices[i]);
				answerChoices[i].setBackground(Color.WHITE); 
				answerChoices[i].setFont(answerFont);
				answerChoices[i].setOpaque(true); // add in order to get background color of radiobuttons to show up
				answerChoices[i].addActionListener(qbHandler);
				answersPanel.add(answerChoices[i]);	
			}
			
			JPanel quizButtonHolder = new JPanel();
			quizButtonHolder.setPreferredSize(new Dimension(1100,100));
			quizButtonHolder.setBackground(Color.PINK);
			Font buttonFont = new Font("Monospaced", Font.BOLD, 30);
			
				
			submit = new JButton("Submit"); // Creating a submit button
			submit.setFont(buttonFont);
			submit.addActionListener(qbHandler);
			submit.setEnabled(false);
			quizButtonHolder.add(submit);
				
			next = new JButton("Next"); // Creating a next button
			next.setFont(buttonFont);
			next.addActionListener(qbHandler);
			next.setEnabled(false);
			quizButtonHolder.add(next);
			
			
			add(questionPanel, BorderLayout.NORTH);
			add(quizButtonHolder, BorderLayout.SOUTH);
			add(quizPlaySpacingPanel1, BorderLayout.EAST);
			add(quizPlaySpacingPanel2, BorderLayout.WEST);
			add(answersPanel, BorderLayout.CENTER);
			
		}
// This method generates the next question and answer set
// for the quiz. It is called when the player submits their
// answer for one quiz question and clicks next to move onto
// the next one. This method calls the readQAndAFromFile() method
// of the QuizData class that uses File IO to read a text file
// containing questions and answers and then stores those values
// for the player to use here. This method is mostly taken from
// Mr. DeRuiter's GameModuleFiles.java program (cited in 
// bibliography)
		public void nextQuestion()
		{
			choicesGroup.clearSelection();
			numQuizQuestions++;
			qData.readQandAFromFile();
			questionTA.setText(qData.getQuestion(numQuizQuestions));
			for (int z = 0; z < 4; z++) // getting answer choices for a question and setting them as the text for the radio buttons
			{
					answerChoices[z].setText("" + qData.getAnswer(z));
			}
			for(int i = 0; i < 4; i++)
			{
				answerChoices[i].setEnabled(true);
				answerChoices[i].setBackground(Color.WHITE);
			}		
		}
// This class is the button handler for buttons on the quiz, it
// lets the player move onto more questions or the quiz closing panel
// depending on if they have submitted an answer yet and depending
// on how many questions they have left for the quiz (since each
// quiz should only have five questions).
		class QuizButtonHandler implements ActionListener
		{
			public QuizButtonHandler()
			{
				
			}
			public void actionPerformed(ActionEvent evt)
			{
				// This part colors the correct answer green and, if the player selected an incorrect one, colors that one red.
				// The code was taken from DeRuiter's GameModuleFiles.java
				String pressed = evt.getActionCommand();	
				if(choicesGroup.getSelection() != null) // if the player selects a radio button then they can submit their answer
				{
					submit.setEnabled(true);
				}
				if(pressed.equals("Submit")) // if the player presses submit then they cannot select another answer and the background colors
											 // of the choices indicate if the player's choice was right or wrong
				{	
					answerChoices[qData.getCorrectAnswer()].setOpaque(true);
					answerChoices[qData.getCorrectAnswer()].setBackground(Color.GREEN);
					for(int i = 0; i < 4; i++)
					{
						if(answerChoices[i].isSelected()) // checking if the answer the player selected was correct or not and changing it's background color accordingly
						{
							if(i != qData.getCorrectAnswer())
							{
								answerChoices[i].setOpaque(true);
								answerChoices[i].setBackground(Color.RED);
							}
							else
							{
								quizScore++; // the player's score is incremented if they selected the right answer
							}
						}
					}
					choicesGroup.clearSelection(); // clear radiobutton selection
					for(int i = 0; i < 4; i++)
					{
						answerChoices[i].setEnabled(false); // can't select radiobuttons after you hit submit
					}
					submit.setEnabled(false); // can't hit submit again after you already did once
					if(numQuizQuestions == 5) // ends the quiz if 5 questions have been asked
					{
						finishQuiz = true;
					}
					next.setEnabled(true); // if there are more questions to answer
				}
				else if(pressed.equals("Next") && finishQuiz == false) // lets the player move onto the next question if the quiz isn't over
																	   // and they submit an answer before pressing the next button
				{
						next.setEnabled(false);
						nextQuestion();
				}
				else if(pressed.equals("Next") && finishQuiz == true) // lets the player go to the closing panel of the game if they finish the quiz
				{
					quizCards.show(quizHolder, "closing");
					finishQuiz = false;
					next.setEnabled(false);
					qcpan.alterValues(); 
				}
			}
		}
	}
// This class is for the closing panel of the quiz that
// tells the player what they scored (on the quiz) and
// whether or not they can return to their game
	class QuizClosingPanel extends JPanel
	{
		private QuizHolderPanel quizHolder; // an object of the QuizHolderPanel class used to show a card of the quiz cardlayout in this class
		private CardLayout quizCards; // an object of the card layout for the quiz used so that this class can show a card of the quiz cardLayout
		private JTextArea resultsMessage; // the JTextArea that tells the player their score and if they can return to their game
		private GamePanel gPan; // an object of the GameData class that lets this class use data from that class and get info (such as the player's score)
		public QuizClosingPanel(QuizHolderPanel qhpIn, CardLayout qc, GamePanel gpIn)
		{
			quizHolder = qhpIn;
			quizCards = qc;
			gPan = gpIn;
			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			
			
			setBackground(Color.PINK);
			setLayout(new BorderLayout());
			JPanel resultsPanel = new JPanel();
			resultsPanel.setPreferredSize(new Dimension(1100,640));
			resultsPanel.setBackground(new Color(189,118,189));
			
			resultsMessage = new JTextArea("",7,20); // Creating a JTextArea that tells the player what they scored on the quiz
			resultsMessage.setLineWrap(true);  
			resultsMessage.setWrapStyleWord(true);
			resultsMessage.setEditable(false); 
			resultsMessage.setMargin(new Insets(90,60,0,60));
			resultsMessage.setFont(new Font("Monospaced", Font.BOLD, 60));
			resultsMessage.setBackground(new Color(194,246,255));

			JPanel resultsButtonHolderPanel = new JPanel();
			resultsButtonHolderPanel.setPreferredSize(new Dimension(1100,100));
			resultsButtonHolderPanel.setBackground(Color.PINK);
			
			JPanel resultsSpacingPanel = new JPanel();
			resultsSpacingPanel.setBackground(Color.PINK);
			resultsSpacingPanel.setPreferredSize(new Dimension(820,60));
			resultsSpacingPanel.setLayout(new FlowLayout(20,20,FlowLayout.CENTER));
			
			JButton nextButton = new JButton("Next"); // Creating a Next Button
			nextButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			nextButton.setPreferredSize(new Dimension(100,60));
			ResultsButtonHandler rnbhs = new ResultsButtonHandler();
			nextButton.addActionListener(rnbhs);
			
			JButton quitButton = new JButton("Quit"); // Creating a Quit Button
			quitButton.setFont(new Font("Monospaced", Font.BOLD, 30));
			quitButton.setPreferredSize(new Dimension(100,60));
			ResultsButtonHandler rqbhs = new ResultsButtonHandler();
			quitButton.addActionListener(rqbhs);
			
			
			resultsButtonHolderPanel.add(quitButton);
			resultsButtonHolderPanel.add(resultsSpacingPanel);
			resultsButtonHolderPanel.add(nextButton);
			resultsPanel.add(resultsMessage);
			add(resultsPanel, BorderLayout.NORTH);
			add(resultsButtonHolderPanel, BorderLayout.SOUTH);
		}
// This method updates the text area with what the player scored on the quiz
// and whether or not they can return to their game
		public void alterValues()
		{
			resultsMessage.setText("You got " + quizScore + " out of 5 questions right. ");
			if (quizScore >= 3) // if the player passed the score they get a message telling them they can resume the game
			{
				resultsMessage.append("Nice job! You may resume your game!");
			}
			else // if the player did not pass the quiz then they are told that their game is now over
			{
				resultsMessage.append("Nice try! Your game is now over!");
			}
		}
// This is the button handler class for the buttons on the quiz closing
// panel. This allows the game to respond differently to button clicks
// depending on the scenario. 
		class ResultsButtonHandler implements ActionListener
		{
			public ResultsButtonHandler()
			{

			}
// If the player presses the next button but they lost the quiz,
// they are shown their respective closing panel (either high 
// score or the regular closing panel depending on the
// boolean value returned by a method in the QuizData class that
// checks for whether or not the player's score is a high score).
// If the player passed the quiz and presses next then they can
// return to their game and a boolean value true is passed
// to the method setResumed of the GameData class which lets
// the game know it needs to let the player resume the game
// when the player sees the game panel by pressing the next button.
			public void actionPerformed(ActionEvent evt)
			{
				String pressed = evt.getActionCommand();
				if(pressed.equals("Next") && quizScore >= 3) // if the player presses the next button and they passed the quiz then they are brought back to their game
				{
					mainCards.show(mainHolder, "game panel");
					gd.setResumed(true); // sets the variable resumed to true
					initialQuizValues(); 
					qData.quizDataInitialValues();
					quizCards.show(quizHolder, "intro");
					qppan.nextQuestion(); 
				}
				else if(pressed.equals("Next") && quizScore < 3) // if the player presses the next button and did not pass the quiz then they are brought to a closing panel
				{
					int scorePlayer = gd.getFinalScore();
					qData.compareScores(scorePlayer);
					if(qData.isHighScore() == true) // if the player got a high score then they are shown the high score closing panel
					{
						hcp.displayHighScore();
						mainCards.show(mainHolder, "high score closing panel");
					}
					else // if the player did not get a high score then they are shown the regular closing panel
					{
						cp.displayClosingScore();
						mainCards.show(mainHolder, "closing panel"); 
					}
					initialQuizValues(); 
					qData.quizDataInitialValues();
					quizCards.show(quizHolder, "intro");
					qppan.nextQuestion(); 
				}
				else if(pressed.equals("Quit")) // if the player presses quit then they are brought back to the start panel
				{
					mainCards.show(mainHolder, "start panel");
					initialQuizValues(); 
					qData.quizDataInitialValues();
					quizCards.show(quizHolder, "intro");
					qppan.nextQuestion(); 
					
				}
			}
		}
	}
}
// This class reads from files and contains the data needed
// for the quiz in terms of questions, answer choices, and which one
// is the right answer.
class QuizData
{
	private Scanner inFile; // A Scanner object that lets the class read from the text file of questions and answers for the quiz
	private String fileName; // A String object containing the name of the text file containing questions and answers to read
	private String question; // contains the question that is being asked
	private String [] answerSet; // an array containing the set of possible answers for each question
	private int correctAnswer; // the number of the correct answer (index of the correct answer in the answerSet array)
	private boolean [] chosenQuestions; //questions that where asked (so won't be repeated in one game)
	private boolean highScore; // whether or not the player's score is a high score
	private Scanner scoreFile; // a Scanner object that lets the class read from the text file containing high scores
	private String scoreFileName; // a String object containing the name of the text file to read the high scores from
	public QuizData()
	{
		inFile = null;
		fileName = new String("musicTriviaQuestions.txt");
		question = new String(""); ;
		answerSet = new String[4]; //the set of possible answers for each question
		correctAnswer = 0; // index of the correct answer in the array of answer choices
		chosenQuestions = new boolean[29]; //number of questions in the file
		scoreFile = null;
		scoreFileName = new String("highScores.txt");
		highScore = false;
	}
// A method that stores the initial values of this class,
// such as the question and answer set, so that no data from 
// an old quiz is saved and interferes with a new quiz.
	public void quizDataInitialValues()
	{
		question = new String(""); ;
		answerSet = new String[4]; //the set of possible answers for each question
		correctAnswer = 0; //number of the correct answer
		chosenQuestions = new boolean[29]; //number of questions in the file
		readQandAFromFile();
	}
// This method uses a try catch block to create a Scanner
// to read a text file. This method then reads through a specifically
// formatted text file in which the question is written, answer choices 
// are listed, and then the index of the correct answer choice in an 
// array of answer choices is listed. This text file's format was taken
// Mr. DeRuiter's text file format in GameModuleFiles.java so that the
// method of reading questions and answer choices works. I used the code
// for reading and storing question and answer choices from Mr. DeRuiter's
// program as well. This method stores the question, the answer choices in an array, 
// and the index of the correct answer in the array of answer choices. 
	public void readQandAFromFile ( )
	{
		File inputFile = new File(fileName);
		// This try catch block creates a scanner object in order to read from the text file of questions and answers
		try
		{
			inFile = new Scanner(inputFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("ERROR: Cannot open %s\n", fileName);
			System.exit(3);
		}
		
		int questionNumber = (int)(Math.random() * 29); // generates numbers from 0 to 28
		while(chosenQuestions[questionNumber] == true) // Prevents same question from being chosen twice
		{
			questionNumber = (int)(Math.random() * 29);
		}
		chosenQuestions[questionNumber] = true;
		int counter = 0;
		while(inFile.hasNext() && counter < 6 * questionNumber) //read lines until the question which the questionNumber indicates is there
		{
			String line = inFile.nextLine(); //read lines until you get to your question (after every 6 lines)
			counter++; //gets line number that was read
		}
		question = inFile.nextLine(); //saving the question
		counter = 0;
		while(inFile.hasNext() && counter < 4)
		{
			answerSet[counter] = inFile.nextLine();
			counter++;
		}
		correctAnswer = inFile.nextInt(); // the number at the end of the choices contains which answer of the answer set 
										 // is correct, that's the index of the correct answer in the answer set array
										// the number at the end of the choices contains which answer of the answer set is 
										// correct, that's the index of the correct answer in the answer set array
		inFile.close();
	}
// This method returns the question formatted with the question number
// in order to display the question for the player.
	public String getQuestion (int questionNumber)
	{
		int numQuizQuestions = questionNumber;
		return "" + numQuizQuestions + ". " + question;
	}
// This method returns the answer choice stored 
// in one cell of the answer set array depending
// on which index was passed in
	public String getAnswer(int index)
	{
		return answerSet[index];
	}
// This method returns the correct answer for the question
// using the index of the correct answer in the answer set 
// array that was found using the readQAndAFromFile method	
	public int getCorrectAnswer ( )
	{
		return correctAnswer;
	}
// This method returns the name of the note and 
// the the clef it is in depending 
// on the height of the note (and thus which note)
// the player was stuck on.
	public String getNoteName(int height)
	{
		int heightOfNote = 0;
		heightOfNote = height;
		String noteName = new String("");
		
		if(heightOfNote == 190)
		{
			noteName = "C in treble clef";
		}
		else if(heightOfNote == 173)
		{
			noteName = "D in treble clef";
		}
		else if(heightOfNote == 159)
		{
			noteName = "E in treble clef";
		}
		else if(heightOfNote == 138)
		{
			noteName = "G in treble clef";
		}
		else if (heightOfNote == 148)
		{
			noteName = "F in treble clef";
		}
		else if (heightOfNote == 312)
		{
			noteName = "E in bass clef";
		}
		else if (heightOfNote == 302)
		{
			noteName = "F in bass clef";
		}
		else if (heightOfNote == 290)
		{
			noteName = "G in bass clef";
		}
		else if (heightOfNote == 280)
		{
			noteName = "A in bass clef";
		}
		else if (heightOfNote == 267)
		{
			noteName = "B in bass clef";
		}
		return noteName;
	}
// This method returns the name of the computer
// key corresponding to the note that the player 
// was stuck on by taking in the height of
// the note (and thus which note) the player
// was stuck on
	public String getKeyName(int height)
	{
		int noteHeight = 0;
		noteHeight = height;
		String keyName = ""; 
		if (noteHeight == 190) // treble C
		{
			keyName = "b";
		}
		else if (noteHeight == 173) // treble D
		{
			keyName = "h";
		}
		else if (noteHeight == 159) // treble E
		{
			keyName = "j";
		}
		else if (noteHeight == 148) // treble F
		{
			keyName = "k";
		}
		else if (noteHeight == 138) // treble G
		{
			keyName = "l";
		}
		else if (noteHeight == 312) // bass E
		{
			keyName = "a";
		}
		else if (noteHeight == 302) // bass F
		{
			keyName = "s";
		}
		else if (noteHeight == 290) // bass G
		{
			keyName = "d";
		}
		else if (noteHeight == 280) // bass A
		{
			keyName = "f";
		}
		else if (noteHeight == 267) // bass B
		{
			keyName = "v";
		}
		return keyName;
	}
// This method uses a try catch block and file io
// to create a scanner to read a text file containing
// the high scores. If the player's score is greater
// than the high score in fifth place, then the player
// has a high score.
	public void compareScores(int score)
	{
		int playerScore = score;
		
		// This try catch block is creating a scanner object in
		// order to read from the text file containing high scores
		
		File readInFile = new File(scoreFileName);
		try
		{
			scoreFile = new Scanner(readInFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("ERROR: Cannot open %s\n", fileName);
			System.exit(4);
		}
		String scorePhrase = new String("");
		while(scoreFile.hasNext())
		{
			scorePhrase = scoreFile.nextLine();
			while(scorePhrase.indexOf("5.") == -1)
			{
				scorePhrase = scoreFile.nextLine();
			}
		}
		String scoreValue = new String("");
		scoreValue = scorePhrase.substring(scorePhrase.indexOf(":") + 1);
		int fifthPlaceScore = 0;
		fifthPlaceScore = Integer.parseInt(scoreValue);
		if(playerScore > fifthPlaceScore) // if the player got a score higher than the score of the person in fifth place then they got a high score
		{
			highScore = true;
		}
		else // otherwise the player did not get a high score
		{
			highScore = false;
		}
		
	}
// This method returns a boolean value indicating whether or 
// not the player's score was a high score. Depending on this
// value, the player will be shown either the regular closing panel
// or the high score closing panel.
	public boolean isHighScore()
	{
		return highScore;
	}
	
}

