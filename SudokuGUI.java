import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.util.*;
import java.io.*;

/** 
 * Draws out a sudokuboard.
 * @author Christian Tryti
 * @author Stein Gjessing
 * (modified by Henrik Hansen(henrihan))
 */
public class SudokuGUI extends JFrame {
    private final int SQUARE_SIZE = 50;	/** Size of each square */
    private final int TOP = 50;	/** Place where the top of the board is */
    private JTextField[][] board;   /** to draw all the squares. */
    private int dim;		/** size of the board */
    private int hd;	/** n squares on the board vertically */
    private int br;	/** n squares horizontally */
    private static char [][] charArray;  /**Chararray which knows which values are predefined in what squares*/
    private char [] charArray1; /** Chararray used for assigning the predefined values in the right locations*/
    private static char [][][] solutions; /** Chararray used for displaying the solutions*/
    private String text = ""; /** String-object used for assigning the predefined values in the right location*/
    private Board thisBoard; /** boardObject*/
    private static SudokuGUI first; /** SudokuGUI-object which is the first. Used to grey out "find solutions" after run*/
    private static SudokuGUI solution1; /** A solution*/
    private static int solutionCount; /** Which solution we're now at the moment. Used by nested class ButtonListener2*/
    private static int numberOfSolutions; /** Number of total solutions*/
    private static boolean pressed = false; /** Boolean which defines if a button was pushed. Used by nested class*/
    private static boolean maxed = false; /** Tells if there are more solutions. If false, grey out next solution button*/
    static boolean gui = true; /** Write to file or gui. If gui = true, don't write to file*/
    private static String output = ""; /** Location of the output-file*/
    
    /** Make board with buttons at the top */
    public SudokuGUI(int dim, int hd, int br, Scanner scan, boolean gui, String output) {
	this.gui = gui;
	this.dim = dim;
	this.hd = hd;
	this.br = br;
	this.output = output;
	charArray = new char[dim][dim];
	charArray1 = new char[dim*dim];
	board = new JTextField[dim][dim];
	setPreferredSize(new Dimension(dim * SQUARE_SIZE, 
				       dim * SQUARE_SIZE + TOP));
	setTitle("Sudoku " + dim +" x "+ dim ); /** Title of the sudokuboard*/
	setDefaultCloseOperation(EXIT_ON_CLOSE); /** Close if 'x' are pushed*/
	setLayout(new BorderLayout());
	
	JPanel buttonPanel = makeButtons(true); /** True means that its first time*/
	JPanel boardPanel = makeBoard(scan);
	getContentPane().add(buttonPanel,BorderLayout.NORTH);
	getContentPane().add(boardPanel,BorderLayout.CENTER);
	pack();
	setVisible(false);
	thisBoard = new Board(dim, hd, br, charArray); /** Make new board*/
	
    }
    
    /** This constructor is used when thisBoard makes a new SudokuGUI object
     * This is necessary because now SudokuGUI knows the structure of the board
     */
    public SudokuGUI(int dim, int hd, int br, char[][][] solutions, boolean showSolutions, int count) {
	this.dim = dim;
	this.hd = hd;
	this.br = br;
	this.solutions = solutions;
	if(!gui) {
	    setVisible(false);
	    int counter = 1;
	    File outFile = new File(output);
	    try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		while(counter <= getSolutionCount()) {
		    writer.write(counter + ": ");
		    for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
			    writer.write(solutions[counter-1][i][j]);
			}
			writer.write("// ");
			
		    } 
		    writer.write("\n");
		    counter++;
		}
		writer.close();
	    }catch(IOException e) {
		e.printStackTrace();
		System.out.println("Error. Program will now exit.");
		
	    }
	    
	    System.out.println("All solutions wrote to file " + output);
	    System.out.println("Program will now exit. Have a nice day!");
	    System.exit(0);
	}
	/** If first time*/
	if(count == 0)
	    System.out.println("This board has " + getSolutionCount() + " solutions");
	board = new JTextField[dim][dim];
	
	setPreferredSize(new Dimension(dim * SQUARE_SIZE, 
				       dim * SQUARE_SIZE + TOP));
	setTitle("Sudoku " + dim +" x "+ dim );
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLayout(new BorderLayout());
	
	JPanel buttonPanel = makeButtons(false); /** False means that its not the first time*/
	JPanel boardPanel = makeBoard1(showSolutions, count);
	
	getContentPane().add(buttonPanel,BorderLayout.NORTH);
	getContentPane().add(boardPanel,BorderLayout.CENTER);
	pack();
	setVisible(true);
    }
    
    
    /** Make a panel with all the squares.*/
    private JPanel makeBoard(Scanner scan) {
	int top, left;
	JPanel boardPanel = new JPanel();
	boardPanel.setLayout(new GridLayout(dim,dim));
	boardPanel.setAlignmentX(CENTER_ALIGNMENT);
	boardPanel.setAlignmentY(CENTER_ALIGNMENT);
	setPreferredSize(new Dimension(new Dimension(dim * SQUARE_SIZE, 
						     dim * SQUARE_SIZE)));		
	int counter = 0;
	
	while(scan.hasNext()) {
	    text = scan.nextLine(); /** Puts the next line of the file in String-variable "text"*/
	    for(int i = 0; i < text.length(); i++) {
		charArray1[i] = text.charAt(i); /** Puts the contents of text in charArray1*/
	    }
	    for(int i = 0; i < text.length(); i++) {
		charArray[counter-1][i] = charArray1[i]; /** Puts the contents of charArray in charArray1*/
	    }
	    counter++; /** Increments counter*/
	}
	
	
	for(int i = 0; i < dim; i++) {
	    /* find out if this row need a thicker line top: */
	    top = (i % hd == 0 && i != 0) ? 4 : 1;
	    for(int j = 0; j < dim; j++) {
		/* find out if this square is a part of a column which
		   should have a thicker line to the left */
		left = (j % br == 0 && j != 0) ? 4 : 1;
		
	        JTextField square = new JTextField();
		square.setBorder(BorderFactory.createMatteBorder
				 (top,left,1,1, Color.black));
		square.setHorizontalAlignment(SwingConstants.CENTER);
		square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
		String s1 = Character.toString(charArray[i][j]);
		if(s1.equals(".")) /** If the square is not predefined*/
		    square.setText("");
		else
		    square.setText(s1);
		board[i][j] = square;
		boardPanel.add(square);
	    }
	    
	}
	return boardPanel;
    }
    
    /** Used when the program has a solution*/

    private JPanel makeBoard1(boolean showSolutions, int count) {
     	int top, left;
	JPanel boardPanel = new JPanel();
	
	boardPanel.setLayout(new GridLayout(dim,dim));
	boardPanel.setAlignmentX(CENTER_ALIGNMENT);
	boardPanel.setAlignmentY(CENTER_ALIGNMENT);
	setPreferredSize(new Dimension(new Dimension(dim * SQUARE_SIZE, 
						     dim * SQUARE_SIZE)));		
	for(int i = 0; i < dim; i++) {
	    /* find out if this row need a thicker line top: */
	    top = (i % hd == 0 && i != 0) ? 4 : 1;
	    for(int j = 0; j < dim; j++) {
		/* find out if this square is a part of a column which
		   should have a thicker line to the left */
		left = (j % br == 0 && j != 0) ? 4 : 1;
		
		JTextField square = new JTextField();
		square.setBorder(BorderFactory.createMatteBorder
				 (top,left,1,1, Color.black));
		square.setHorizontalAlignment(SwingConstants.CENTER);
		square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
	        String s1 = "";
		/** If there's the solutions to show*/
		if(showSolutions)
		    s1 = Character.toString(solutions[count][i][j]);
		else
		    s1 = Character.toString(charArray[i][j]);
		
		if(s1.equals("."))
		    square.setText("");
		else
		    square.setText(s1);
		board[i][j] = square;
		boardPanel.add(square);
	    }
	}
	return boardPanel;
    }
    
    /** *Makes a panel with some buttons. 
     * @return a pointer to the panel which is made.
     */
    private JPanel makeButtons(boolean firstTime) {
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	JButton findSolutionButton= new JButton("Find solution(s)");
	JButton nextButton = new JButton("Next solution");
	findSolutionButton.addActionListener(new ButtonListener());
	nextButton.addActionListener(new ButtonListener2());
	
	if(pressed) /** If find solution button was pushed earlier, grey out the button*/
	    findSolutionButton.setEnabled(false); 
	if(maxed || numberOfSolutions == 1) /** If all solutions are shown or there are only one solution*/
	    nextButton.setEnabled(false);
	buttonPanel.add(findSolutionButton);
	buttonPanel.add(nextButton);
	return buttonPanel;
    }    
    
    /** Private nested class which are listening for when findSolutionButton are pushed
     */
    private class ButtonListener implements ActionListener {
	int counter;
	
	public void actionPerformed(ActionEvent e) {
	    pressed = true;
	    counter++;
	    setVisible(false); 
	    System.out.println("Showing solution number: 1");
	    if(counter < getSolutionCount() || counter == 1 && getSolutionCount() == 1) {
		first = new SudokuGUI(dim, hd, br, solutions, true, 0);
	    }
	}
    }
    
    /** Private nested class which are listening for when nextButton are pushed
     */
    private class ButtonListener2 implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
	    solutionCount++; /** Increment when button are pushed, so we know which solution we're on*/
	    if(solutionCount == getSolutionCount()-1) /** If last solution*/
		maxed = true;
	    System.out.println("Showing solution number: " + (solutionCount+1));
	    
	    first.setVisible(false); 
	    
	    if(solution1 != null) /** To avoid NPE*/
		solution1.setVisible(false);
	    solution1 = new SudokuGUI(dim, hd, br, solutions, true, solutionCount);
	    
	}
	
    }

    /** Method which returns the total number of solutions*/
    public int getSolutionCount() {
	int count = 0;
	char tmp = solutions[count][0][0];
        	
	do{
	    try {
		tmp = solutions[count][0][0];
		count++;
	    }
	    catch(ArrayIndexOutOfBoundsException e) {
		numberOfSolutions = count-1;
		return numberOfSolutions;
	    }
      	}while(tmp == '1' || tmp == '2' || tmp == '3' || tmp == '4' || tmp == '5' || tmp == '6' || tmp == '7'
	       || tmp == '8' || tmp == '9' || tmp == 'A' || tmp == 'B' || tmp == 'C'); {
	}
	numberOfSolutions = count-1;
	return numberOfSolutions;
    }
}





