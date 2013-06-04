/** Sudokucontainer-class. Holds the solutions in a 3D-chararray
 * @param dim Dimension of board
 * @param count Tells us which solution we're on.
 * @param solutions 3D chararray which holds the solutions. 1st dimension = solutionnumber, 2nd row-value, 3rd col-value
 */
public class Sudokucontainer {
    private int dim = 0;
    private int count;
    public char[][][] solutions;
    
    /** Inserts solutions in the 3D array*/
    public void insert(Square[][] allSquares, int count, int dim) {
	
	if(count == 0)
	    solutions = new char[500][dim][dim]; /** Make the chararray if this is the first time*/
	this.count = count;
	this.dim = dim;
	if(count <= 499) {
	    for(int i = 0; i < dim; i++) {
		for(int j = 0; j < dim; j++) {
		    solutions[count][i][j] = allSquares[i][j].getValue();
       		}
	    }
	}
    }
    
    /** Returns the solution of a given solution number*/
    public char[][] get(int nr) {
	try {
	    return solutions[nr];
	}catch(IndexOutOfBoundsException e) {
	    System.out.println("Error. There aren't that many solutions");
	    System.exit(1);
	}
	return solutions[nr];
    }

    /** Returns n number of solutions*/   
    public int getSolutionCount() {
	return solutions.length;
    }
}
