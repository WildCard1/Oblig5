/** Superclass for Row, Column and Box
 * @param isLegal boolean-array which knows which values are legal
 * @param squares The Squares in this row, col or box
 * @param values Values in the row, col or box
 * @param nr Tells which row, col or box we're on
 * @param dim Dimension of the board
 * @param counter
 */

class Superclass {
    public boolean[] isLegal;
    protected Square [] squares;
    protected char[] values;
    protected int nr; 
    protected static int dim;
    
    /** Constructor which sets number, dimenson and creates the isLegal array.*/
    public Superclass(int nr, int dim) {
	this.nr = nr;
	this.dim = dim;
	isLegal = new boolean[dim];
	
	for(int i = 0; i < isLegal.length; i++) {
	    isLegal[i] = true;
	}
    }
    
    /** Constructor which sets number, dimension, and creates the isLegal array.*/
    public Superclass(int dim, char[] charArray, int nr) { 
	this.nr = nr;
	this.dim = dim;
	isLegal = new boolean[dim];
	values = charArray;
	squares = new Square[values.length];
	
	for(int i = 0; i < values.length; i++) {
	    squares[i] = new Square();
	    squares[i].insert(values[i]);
	    isLegal[i] = true;
	}
    }
    
    /** If a row, col or box object is called, the number of the row, col or box is printed*/
    public String toString() {
	return Integer.toString(nr);
    }

    /**Returns number of row, col or box*/
    public int getNr() {
	return nr;
    }
} // End of Superclass.
