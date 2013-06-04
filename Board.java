/** Board class. Controls most of the sequence.
 *@param charArray charArray with values of the specific squares
 *@param charArray1 charArray with one rows values at a time.
 *@param allSquares Square-array which tells us about the values in the squares
 *@param row Array of all the rows
 *@param columns Array of all the cols
 *@param boxes Array of all the boxes
 */
public class Board {
    private int dim, br, hd;
    private char[][] charArray;
    private char[] charArray1;
    private Square [][] allSquares; 
    private Row [] rows;
    private Column[] columns;
    private Box[] boxes;
    
    /** Constructor of board*/
    public Board(int dim, int br, int hd, char[][] charArray) {
	this.dim = dim;
	this.br = br;
	this.hd = hd;
	this.charArray = charArray;
	charArray1 = new char[dim];
	allSquares = new Square[dim][dim];
	rows = new Row[dim];
	columns = new Column[dim];
	boxes = new Box[dim/br * dim/hd];
	setRows();  
	setColumns();
	setBoxes();
	setSquares();
	insertBoxInSquares();
	fillInLegalValues();
	welcome();
	solve();
        showGui();
    }
    /** Set the rows*/
    public void setRows() {
	for(int i = 0; i < rows.length; i++) {	
	    rows[i] = new Row(dim, charArray[i], i+1); 
	}
    }
    /**Set the columns*/
    public void setColumns() {
	int counter = 0;
	
	for(int i = 0; i < dim;i++) {
	    for(int j = 0; j < dim; j++) {
		 charArray1[j] = charArray[j][counter]; 	
	    }
	    counter++;	      
	    columns[i] = new Column(dim, charArray1, i+1);
	}
    }

    /** Set the boxes*/
    public void setBoxes() {
	for(int i = 0; i < boxes.length; i++) {
	    boxes[i] = new Box(i+1, dim);
	} 
    }

    /** Set the squares*/    
    public void setSquares() {
	for(int i = 0; i < dim; i++) {
	    for(int j = 0; j < dim; j++) {
	    	allSquares[i][j] = new Square(rows[i], columns[j]); 
		allSquares[i][j].insert(charArray[i][j]);
	    }
	}
    }

    /** Insert the right box in each square(phew)*/    
    public void insertBoxInSquares() {
	for(int i = 0; i < dim; i++) {
	    for(int j = 0; j < dim; j++) {
		int rowNr = allSquares[i][j].row.getNr();
		int colNr = allSquares[i][j].col.getNr();
		allSquares[i][j].insertBoxObj(boxes[(j/hd) + ((i / br) * br)]);
	    }
	}
    }
    
    /** Set to predefined in chosen squares and set the corresponding values to notLegal in box, col, row(with isLegal-array)*/
    public void fillInLegalValues() {
	int tmpValue = 0;
	for(int i = 0; (tmpValue != -49 && i < dim)  ; i++) {
	    for(int j = 0; (tmpValue != -49 && j < dim); j++) {
		if(allSquares[i][j].getValue() != '\u0020') {
		    if(allSquares[i][j].getValue() == 'A') 
			tmpValue = 9;
		    else if(allSquares[i][j].getValue() == 'B') 
			tmpValue = 10;
		    else if(allSquares[i][j].getValue() == 'C') 
			tmpValue = 11;
		    else if(allSquares[i][j].getValue() == 'D') 
			tmpValue = 12;
		    else if(allSquares[i][j].getValue() == 'E') 
			tmpValue = 13;
		    else 
			tmpValue = (int) allSquares[i][j].getValue() -49;
		    	    	    
		    allSquares[i][j].predefined = true;
		    allSquares[i][j].box.isLegal[tmpValue] = false;
		    allSquares[i][j].col.isLegal[tmpValue] = false;
		    allSquares[i][j].row.isLegal[tmpValue] = false;
		}
	    }
	}
    }

    /** Start the solving-algorithm in the square-object*/
    public void solve() {
	allSquares[0][0].fillInnRemainingOfBoard(allSquares, hd, br);
    }

    /** Show the solutions with a GUI*/    
    public void showGui() {
	if(allSquares[0][0].solutions != null) { /** If there's solutions available*/
	    SudokuGUI gui = new SudokuGUI(dim, br, hd, allSquares[0][0].solutions.solutions, false, 0);
	    if(gui.gui) { /** If solutions should be showed with a graphical interface(!gui = write to file)*/
		System.out.println("Found solutions. Please refer to the graphical interface");
		System.out.println("---------------------------------------------------------------------------\n");
	    }
	}else { /** No solutions available*/
	    System.out.println("This board has no solutions. Program will now exit. Have a nice day");
	    try { 
		Thread.sleep(5000); /** Close the program, but sleep for 5seconds first*/
		System.exit(0);
	    }catch(InterruptedException e) {
		
	    }
	}
    }
    
    /** Print a welcome message while you wait for a sudoku board which takes some time to load*/
    public void welcome() {
	System.out.println("\n---------------------------------------------------------------------------\n");
	System.out.println("Welcome to Henrik's Sudoku Solver, originally written as assignment nr. 5 ");
	System.out.println("in the class 'INF1010' at UiO. The program is currently");
	System.out.println("finding solutions, which can take some time(depending on how large the board is,");
	System.out.println("and how many predefined values there are). ");
	System.out.println("Why don't you enjoy a nice cup of coffee or tea while you wait :) ?");
	System.out.println("If you're using an empty board, this is a good ref. for how many solutions there are");
	System.out.println("http://en.wikipedia.org/wiki/Mathematics_of_Sudoku#Enumeration_results");
	System.out.println("\n---------------------------------------------------------------------------\n");
    }
}

