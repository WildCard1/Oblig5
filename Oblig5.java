import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/** Proever oss paa Ingles klassenavn, metoder, variabler etc.
 * This class starts the program.
 * @param inFile if necessary this defines the name of the file with the boards structure(size and predef values). args[0]
 * @param outFile if necessary this defines the name where solutions of the board shall be written to. args[1]
 * @author henrihan <henrik.hansen@student.jus.uio.no> 
 */
class Oblig5 {
    public static void main(String[] args) {
    	String inFile;
	String outFile;
        if(args.length == 1) { /** If one argument were given*/
	    inFile = args[0];
	    new TheSudoku(inFile);
	}
	else if(args.length == 2){ /** If two arguments were given*/
	    inFile = args[0];
	    outFile = args[1];
	    new TheSudoku(inFile,outFile);
	}
	else {	/** If no arguments were given*/
	    new TheSudoku();
	}
    }
}













