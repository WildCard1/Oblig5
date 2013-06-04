import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/** This class starts the program with different methods, depending on if you include args or not
 *@param input The name of the file which contains the structure of the board
 *@param output The name of the file which the solution(s) should be written to(if specificed).
 *@param chooser JFileChooser-object necessary if the user wants to find the structure-file with a GUI.
 */
public class TheSudoku extends JFrame {
    private String input, output;
    private JFileChooser chooser;

    /**Constructor which assigns chooser. Used if args.length == 0*/
    public TheSudoku() {
	chooser = new JFileChooser();
	int returnVal = chooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) { /** If this file is chosen*/
	    System.out.println("You chose to open this file: "
			       + chooser.getSelectedFile().getPath());
	    readFile(chooser.getSelectedFile());
	}
    }
    /** Constructor used if args.length == 1. */
    public TheSudoku(String input) {
	this.input = input;
	readFile(new File(input));
    }
    /** Constructor used if args.length == 2)*/
    public TheSudoku(String input, String output) {
	this.input = input;
	this.output = output;
	readFile(new File(input));
    }
    
    public void readFile(File file) {
	try {
	    if(chooser != null)
		chooser.setVisible(false); /** Make the chooser-object invisible*/
	    Scanner scan = new Scanner(file); /**New scanner object*/
	    int dim = scan.nextInt();
	    int ver = scan.nextInt();
	    int hor = scan.nextInt();
	    /** If solutions should be written to file*/
	    if(output != null) {
		SudokuGUI test = new SudokuGUI(dim, ver, hor, scan, false, output); /** False is for no gui*/
	    }else {
		SudokuGUI test = new SudokuGUI(dim, ver, hor, scan, true, null); /** True is for show gui*/
	    }
	    
	}
	catch(FileNotFoundException e) {
	    System.out.println("Please choose the right file");	    
	    System.exit(1);
	}
	catch(InputMismatchException e) {
	    System.out.println("Choose a file with the right format, please!");
	    System.exit(1);
	}
    }
}
