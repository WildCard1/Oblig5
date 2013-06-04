import java.lang.Error.*;
import java.util.*;

/** The square-class
 *@param solutions Sudokucontainer-object which holds the solutions
 *@param value Value of this square
 *@param valueInt the same as value only displayed in int
 *@param box This square's box-object
 *@param col This square's col-object
 *@param row This square's row-object
 *@param 2D square array used solving the sudokuboard
 *@param count counter used in FillInnRemainingOfBoard-method i.e.
 *@param predefined Tells us if the value is a predefined value. Shall not be tempered with
 *@param lastSquare Tells us if this is the last square
 *@param Points to the next square. If end of row; first in next col. Else next in row. Next == null means this is last square
 */
public class Square {
    
    protected static Sudokucontainer solutions;
    protected char value;
    protected int valueInt;
    protected Box box;
    protected Column col;
    protected Row row;
    protected static Square allSquares[][];
    protected int count = 0;
    protected boolean predefined = false;
    protected boolean lastSquare = false;
    protected Square next = null;
  
    /** Constructor the Square-class*/
    public Square(Row row, Column col) {
	this.col = col;
	this.row = row;
    }

    /** Constructor for Square-class. Creates the solutions-object*/
    public Square() {
	solutions = new Sudokucontainer();
    }
    
    /** Sets the next-pointers as explained above*/
    public void setNext() {
	if(col.getNr() < col.dim) {
	    next = allSquares[row.getNr()-1][col.getNr()];
	}else if(col.getNr() == col.dim) {
	    if(row.getNr() < row.dim) {
		next = allSquares[row.getNr()][0];
	    }else {
		next = null;
		lastSquare = true;
	    }
	}
	if(next != null) {
	    if(next.col.getNr() == col.dim && next.row.getNr() == col.dim && next.predefined == true)
		lastSquare = true;
	}
    }

    /** This is where to magic happens. This method uses backtracing and recursive method calling*/
    public void fillInnRemainingOfBoard(Square[][] allSquares, int hd, int br) { 
	this.allSquares = allSquares; /** Sets this allSquares to point to the receives allSquares*/
	int highestNumber = col.dim; /** Sets the highest number to try to the dim of the board*/
	setNext(); /** Set next pointer of this square*/
	
	if(value == '\u0020') { /** If square is empty(\u0020 is whitespace in unicode(ASCII))*/
	    for(int i = 1; i <= highestNumber; i++) { /** Start to try with 1 if not increment by one til theres a legal value*/
		if(row.isLegal[i-1] && col.isLegal[i-1] && box.isLegal[i-1]) { /** If row, col and box says this value is legal*/
		    String s1 = Integer.toString(i); /** Used for converting to char*/
		    value = s1.charAt(0);
		    
		    if(i > 9) { /** Necessary because converting a,b,c etc is a bit trickier*/
			i = i +55; /** Needed because thats how to convert it in unicode(ASCII)*/
			value = (char)i; /** Cast intvalue to char*/
			i = i -55; /** Set i back to the original value*/
		    }
		    
		    insert(value); /** Assign this value to this square temporarly*/
		    insertInt(i); /** Same as insert, only for int*/
		    box.isLegal[i-1] = false; /** Set that this value is no longer legal for box, col, and row*/
		    col.isLegal[i-1] = false;
		    row.isLegal[i-1] = false;
		    
		    if(!lastSquare) {
			next.fillInnRemainingOfBoard(this.allSquares, hd, br); /** Recursive call to next's method*/
		    }else{   
			solutions.insert(allSquares, count, col.dim); /** Insert this as solution, as this is the last square*/
			++count; /** Increment number of solutions*/
		    } 
		    
		    box.isLegal[valueInt-1] = true; /** Set this value as legal in box, row, col*/
		    col.isLegal[valueInt-1] = true;
		    row.isLegal[valueInt-1] = true;
		    value = '\u0020'; /** Set the value to empty*/
		} 		
	    }  
	}else {	/** If value is not empty*/	
	    if(!lastSquare) {
		next.fillInnRemainingOfBoard(this.allSquares, hd, br);
	    } else {
		solutions.insert(allSquares, count, col.dim);  
		count++;
	    }
	} 
    }

    /** Inserts value in this square*/
    public void insert(char value) {
	if(value != '.') {
	    this.value = value;
	}else{
	    this.value = '\u0020'; // In other words; space(in unicode)
	}
    }

    /** Insert this value as int*/
    public void insertInt(int value) {
	valueInt = value;
    }
    
    /** Insert box-obj in this square*/
    public void insertBoxObj(Box box) {
	this.box = box;
    }
    /** Get this square's value*/
    public char getValue() {
	return value;
    }
    
    /** If this square's object is called its box, row, col and value will be printed out to terminal*/
    public String toString() {
	String s1 = "Row: " + row + " | Column: " + col + " | Box: " + box + " | Value: " + value;
	return s1;
    }
}

