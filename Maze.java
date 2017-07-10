/**********************************
 * Maze.java
 * compilation: javac Maze.java
 * execution: java Maze
 * 
 * Author: Mike Haines
 * Reason: Thomson Reuters Coding Exercise - Maze
	* Today, we shall be building a 2d array data structure,
	* hard-coded with int values to represent our maze.
    * It will hold the walls (0) and white aisles (1)
    * as specified in the maze sample drawing.
    * Then we will traverse the maze using a depth first algorithm, 
    * (following the right wall), changing the aisles to a 2 to represent
    * our path, and where we are on the path.
    * After reaching the end, the path will be displayed on the maze.

 * Here's how:
 * Once we've created our 2D array data structure,
 * it will be time to iterate through the maze.
 * We will need to know a few things:
 	* Current Location (initialized at row 0, column 2 (Block 3))
 	* Direction we are moving (initialized moving DOWN, because, well, why not?)
 * As we iterate through the aisles, we need to be able
   * to track where we have been.  So, we will need to create 
   * a Path vector, which will hold an int representing the block
   * number the traveler is in.
 * After each move, the block number (initialized at 3) will be calculated 
 * by the row and column it is in.
   * The top left block is block 1.  The top right block is block 16.
   * The leftmost block of the 2nd row begins at 17, and so on.
   * The Path vector will be initialized with Block 3 as the starting point. 
 
 * The algorithm for traversing the maze will be:
   * 1. Check walls - 
     * Check right wall (the ACTUAL WALL will be different depending on direction)
     * If 0, check forward wall
     * If 0, check right wall
     * If 0, reverse direction and move that direction
     * If any are true, adjust row or column, adjust blockNumber, set traveler direction, and move that direction  
   * 2. Check if block number is in Path Vector (in reverse)
   		* If IN array, pop all vectors back to that block number index (reversed or hit a loop)
   		* If not, push block number to path vector, and r and c to row and column vectors
	* 3. Check for Exit Block 253
 	    * If Block 253, print out some data, and we're done
 	    * If not, reprint the maze, and repeat from Step 1 
 *************************************/
package maze;

import java.util.Scanner;
import java.util.Vector;

public class Maze {
    public static void main(String[] args) {
    // Greeting
        System.out.print("Maze Initializing");
       
        //INITIALIZE a 16 x 16 2D ARRAY - 0 is default 
        int[][] maze;
        maze = new int[16][16];
      
        //Set up initialized variables for our "traveler"
        int r = 0; //row
        int c = 2; //column
        String direction = "down";  // up down left right are the options...
        
        //Path Vectors
        Vector<Integer> Path = new Vector<>();  // Holds the block number
        int blockNumber = 3;        //this will get calculated as we go through the path
        Path.add(blockNumber);		//initializes the path vector with starting point

        Vector<Integer> PathRow = new Vector<>();	//   these vectors are needed for the UI to rebuild
        PathRow.add(r);								//   in case we back up or hit a loop
        
        Vector<Integer> PathColumn = new Vector<>();
        PathColumn.add(c);
        
        int count = 0;				//this is also a failsafe variable in case we don't find the exit
        int leftCount = 0;			//so we can compare with rightcount
        int rightPathCount = 0;

        
        
        displayDots();  //just for fun...
        
       
        //test (comment out later)
        /*
        System.out.println("Maze Testing - - all should be zero");
        System.out.println("maze at 0, 0 = " + maze[0][0]);
        System.out.println("maze at 0, 2 = " + maze[0][2]);
        System.out.println("maze at 4, 15 = " + maze[4][15]);
        System.out.println("maze at 15, 12 = " + maze[15][12] + "\n");
		*/
        //test passed - 0 values 
        
        //Create the Aisles
        aisleCreator(maze);  //creates aisles and sets traveler at 0, 2
        
        //test (comment out later)
        /*
        System.out.println("after resetting 0, 2 to 2 - 0, 2 = " + maze[0][2] + " <- should be 2\n");
        */
        //testing passed...continue with aisle (1) elements

/***********************************
 * For Fun, let's create a visual representation of the maze....... 
 ************************************/
        /*System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
        for (int j = 0; j<maze[0].length; j++){
            for (int i = 0; i<maze.length; i++){
               	if (maze[j][i]) {
        		System.out.print("|   ");
	        	}
	        	else {
	        		System.out.print("| X ");
	        	}
               	
            }
            System.out.print("\n");
            System.out.print("|");
            System.out.println(" --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ");
            
        } */
     
        //Okay - that is HUGE.  Let's try this:

        for (int j = 0; j<maze[0].length; j++){
            for (int i = 0; i<maze.length; i++){
               	if (maze[j][i] == 1) {
               		System.out.print("  ");  // these are the aisles
	        	}
               	else if (maze[j][i] == 2) {  // "2" added for the traveler
               		System.out.print("^^");  // this is what the traveler will look like
               	}
	        	else {
	        		System.out.print("##");
	        	}
            }
            System.out.print("\n");
        }
        //okay, wow. that turned out cooler than expected!
           
        // TRAVERSE the maze...
        System.out.println("Let's traverse the maze...");
        waitForEnter();

        while (blockNumber != 253 || count > 200) {  //because we have found the exit or NOT
        	//Step 1: MOVE based on direction
        	if (direction == "down") {
  				//check left block
        		if(maze[r][c-1] != 0) {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        		//check bottom block
        		else if (maze[r+1][c] != 0) {
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			// no direction change
        		}
        		//check right block
        		else if (maze[r][c+1] != 0) {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}
        		else {  // go up a block
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		} 
        	}
        	
        	else if (direction == "up") {
        		//check right block
        		if(maze[r][c+1] != 0) {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}
        		//check top block
        		else if (maze[r-1][c] != 0) {
        			r = r - 1;
        			// direction stays the same
        			blockNumber = blockNumber - 16;
        		}
        		//check left block
        		else if (maze[r][c-1] != 0) {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        		else {  // go back down a block
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			direction = "down";
        		}
        	}
        	else if (direction == "right") {
        		//check bottom block
        		if (maze[r+1][c] != 0) {  
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			direction = "down";
        		}
        		//check right block
        		else if(maze[r][c+1] != 0) {
        			c = c + 1;
        			// direction stays the same
  					blockNumber = blockNumber + 1;
        		}
        		//check top block
        		else if (maze[r-1][c] != 0) {
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		}
        		//go back a block
        		else {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        	}
        	else if (direction == "left") {
        		
    			//check top block
    			if (maze[r-1][c] != 0) {
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		}
    			//check left block
        		else if (maze[r][c-1] != 0){
        			c = c - 1;
        			//direction = "left";
        			blockNumber = blockNumber - 1;
        		}	
        		
        		//check bottom block
        		else if (maze[r+1][c] != 0) {  
	    			r = r + 1;
	    			direction = "down";
	    			blockNumber = blockNumber + 16;

	    		}
    			//go back a block
    			else {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}    		
          	}  //END of the MOVE FUNCTIONS
        	
     	
        	// Adjust Path Vectors
        	// check if blockNumber is in the path vector already
        	for(int i = (Path.size() - 1); i >= 0; i--) {  //checking backwards
        		if (Path.elementAt(i) == blockNumber) {
        			//pop Path vectors up to this index (removing unused path numbers)
        			for (int j = (Path.size() - 1); j >= i; j--) { 			// j >= i removes current blockNumber 
        				Path.remove(j);										// (which is added back below)
        				//reset Maze before popping
        				maze[PathRow.elementAt(j)][PathColumn.elementAt(j)] = 1;
        				PathRow.remove(j);
        				PathColumn.remove(j);
        			}
        		}
        	}
        	//add blockNumber to Path, row and column to PathRow/PathColumn
			Path.addElement(blockNumber);
			PathRow.addElement(r);
			PathColumn.addElement(c);
			maze[r][c] = 2;
			
			showMaze(maze);
			count++;
        	System.out.println("Steps: " + count + "   Path length: " + (Path.size() - 1));
      	
        	
        }
        if (blockNumber == 253) {
            System.out.println("\nTake note of the above Steps and Path statistics...\n");
            rightPathCount = (Path.size() - 1);  //for safekeeping
            System.out.println("That algorithm followed the right wall.\n"
            		+ "Now, let's try the left wall to see if it works any better...");
            waitForEnter();
        }
        else if (count > 200) {
        	System.out.println("Aborted: The Algorithm did not work very efficiently...");
        }
        else {
        	System.out.println("Something went seriously wrong...");
        }
        
//BEGIN LEFT WALL ALGORITHM
   //RESET STUFF
      //RESET The Maze
        aisleCreator(maze);  //resets aisles and sets traveler at 0, 2
        
    //RESET variables
        r = 0; //row
        c = 2; //column
        direction = "down";  // up down left right are the options...
        
    //RESET Path Vectors
        Path.clear();
        blockNumber = 3;        
        Path.add(blockNumber);		//initializes the path vector with starting point

        PathRow.clear();	
        PathRow.add(r);		
        
        PathColumn.clear();
        PathColumn.add(c);
        
    //Traverse the maze on the left wall...    
        while (blockNumber != 253 || leftCount > 200) {  //because we have found the exit or NOT
        	//Step 1: MOVE based on direction
        	if (direction == "down") {
        		//check right block
        		if (maze[r][c+1] != 0) {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}
        		//check bottom block
        		else if (maze[r+1][c] != 0) {
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			// no direction change
        		}
  				//check left block
        		else if(maze[r][c-1] != 0) {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        		else {  // go up a block
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		}
        	}
        	else if (direction == "up") {
        		//check left block
        		if (maze[r][c-1] != 0) {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        		//check top block
        		else if (maze[r-1][c] != 0) {
        			r = r - 1;
        			// direction stays the same
        			blockNumber = blockNumber - 16;
        		}
          		//check right block
        		else if(maze[r][c+1] != 0) {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}
        		else {  // go back down a block
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			direction = "down";
        		}
        	}
        	else if (direction == "right") {
        		//check top block
        		if (maze[r-1][c] != 0) {
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		}
        		//check right block
        		else if(maze[r][c+1] != 0) {
        			c = c + 1;
        			// direction stays the same
  					blockNumber = blockNumber + 1;
        		}
        		//check bottom block
        		else if (maze[r+1][c] != 0) {  
        			r = r + 1;
        			blockNumber = blockNumber + 16;
        			direction = "down";
        		}
        		//go back a block
        		else {
        			c = c - 1;
        			direction = "left";
        			blockNumber = blockNumber - 1;
        		}
        	}
        	else if (direction == "left") {
        		//check bottom block
        		if (maze[r+1][c] != 0) {  
	    			r = r + 1;
	    			direction = "down";
	    			blockNumber = blockNumber + 16;

	    		}
    			//check left block
        		else if (maze[r][c-1] != 0){
        			c = c - 1;
        			//direction = "left";
        			blockNumber = blockNumber - 1;
        		}	
        		//check top block
        		else if (maze[r-1][c] != 0) {
        			r = r - 1;
        			direction = "up";
        			blockNumber = blockNumber - 16;
        		}
    			//go back a block
    			else {
        			c = c + 1;
        			direction = "right";
  					blockNumber = blockNumber + 1;
        		}    		
          	}  //END of the MOVE FUNCTIONS
        	
     	
        	// Adjust Path Vectors
        	// check if blockNumber is in the path vector already
        	for(int i = (Path.size() - 1); i >= 0; i--) {  //checking backwards
        		if (Path.elementAt(i) == blockNumber) {
        			//pop Path vectors up to this index (removing unused path numbers)
        			for (int j = (Path.size() - 1); j >= i; j--) { 			// j >= i removes current blockNumber 
        				Path.remove(j);										// (which is added back below)
        				//reset Maze before popping
        				maze[PathRow.elementAt(j)][PathColumn.elementAt(j)] = 1;
        				PathRow.remove(j);
        				PathColumn.remove(j);
        			}
        		}
        	}
        	//add blockNumber to Path, row and column to PathRow/PathColumn
			Path.addElement(blockNumber);
			PathRow.addElement(r);
			PathColumn.addElement(c);
			maze[r][c] = 2;
			
			showMaze(maze);
			leftCount++;
        	System.out.println("Steps: " + leftCount + "   Path length: " + (Path.size() - 1));
        	
        }
        if (blockNumber == 253) {
            //Do some comparison calculations...first, steps, then path length
            if (count < leftCount) {
            	System.out.println("Following the left wall took " + (leftCount - count) + " more steps than taking the right wall.");
            }
            else if (count > leftCount) {
            	System.out.println("Following the left wall was " + (count - leftCount) + " faster than taking the right wall!");
            }
            else {
                System.out.println("Whoa!  These algorithms found their way through the maze in the\n"
                		+ "same number of steps!");
            }
            System.out.println("\nThe left wall path is " + (Path.size() - 1) + " blocks long.");
            //compare total path size
            if ((Path.size() - 1) > rightPathCount) {
            	System.out.println("The left wall path is " + ((Path.size() - 1) - rightPathCount) + " blocks longer than the right wall path.");
            }
            else if (count > leftCount) {
            	System.out.println("The left wall path is " + (rightPathCount - (Path.size() - 1)) + " blocks shorter than the right wall path!");
            }
            else {
                System.out.println("Whoa!  The paths these algorithms found are of equal number!");
            }
            
            waitForEnter();
        }
        else if (count > 200) {
        	System.out.println("Aborted: The Algorithm did not work very efficiently...");
        }
        else {
        	System.out.println("Something went seriously wrong...");
        }
        	

        System.out.println("Thanks for checking out my maze program...  -Mike");
        
        
        		
    }
    
    
    /********** FUNCTIONS ********************/
    public static void waitForEnter(){
	   System.out.println("Please press \"ENTER\" to continue...");
	   Scanner wait = new Scanner(System.in);
	   wait.nextLine();
	}
    
    public static void slomo() {
    	try {
    		Thread.sleep(150);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}	
    }
    
    public static void displayDots() {
    	slomo();
        System.out.print(".");
        slomo();
        System.out.print(".");
        slomo();
        System.out.print(".");
        slomo();
        System.out.print(".");
        slomo();
        System.out.println(".");
        slomo();
        System.out.println("Done.");
        waitForEnter();
    }
    

    
    
    public static void aisleCreator(int maze[][]) {
    	maze[0][2] = 2;  // this is the entry point, so a 2 is necessary for the traveler
        maze[1][1] = 1;  // 1 represents the aisles
        maze[1][2] = 1;
        maze[1][4] = 1;
        maze[1][6] = 1;
        maze[1][8] = 1;
        maze[1][9] = 1;
        maze[1][10] = 1;
        maze[1][11] = 1;
        maze[1][12] = 1;
        maze[1][13] = 1;
        maze[1][14] = 1;
        maze[2][1] = 1;
        maze[2][2] = 1;
        maze[2][4] = 1;
        maze[2][6] = 1;
        maze[2][7] = 1;
        maze[2][8] = 1;
        maze[2][10] = 1;
        maze[2][11] = 1;
        maze[2][12] = 1;
        maze[2][14] = 1;
        maze[3][1] = 1;
        maze[3][4] = 1;
        maze[3][6] = 1;
        maze[3][12] = 1;
        maze[3][14] = 1;
        maze[4][1] = 1;
        maze[4][2] = 1;
        maze[4][3] = 1;
        maze[4][4] = 1;
        maze[4][6] = 1;
        maze[4][7] = 1;
        maze[4][8] = 1;
        maze[4][10] = 1;
        maze[4][12] = 1;
        maze[4][14] = 1;
        maze[5][1] = 1;
        maze[5][7] = 1;
        maze[5][10] = 1;
        maze[5][14] = 1;
        maze[6][1] = 1;
        maze[6][2] = 1;
        maze[6][3] = 1;
        maze[6][4] = 1;
        maze[6][5] = 1;
        maze[6][6] = 1;
        maze[6][7] = 1;
        maze[6][9] = 1;
        maze[6][10] = 1;
        maze[6][12] = 1;
        maze[6][13] = 1;
        maze[6][14] = 1;
        maze[7][3] = 1;
        maze[7][9] = 1;
        maze[8][1] = 1;
        maze[8][2] = 1;
        maze[8][3] = 1;
        maze[8][4] = 1;
        maze[8][6] = 1;
        maze[8][7] = 1;
        maze[8][8] = 1;
        maze[8][9] = 1;
        maze[8][11] = 1;
        maze[8][13] = 1;
        maze[8][14] = 1;
        maze[9][1] = 1;
        maze[9][6] = 1;
        maze[9][8] = 1;
        maze[9][9] = 1;
        maze[9][11] = 1;
        maze[9][12] = 1;
        maze[9][13] = 1;
        maze[9][14] = 1;
        maze[10][1] = 1;
        maze[10][2] = 1;
        maze[10][3] = 1;
        maze[10][4] = 1;
        maze[10][6] = 1;
        maze[10][8] = 1;
        maze[10][11] = 1;
        maze[11][1] = 1;
        maze[11][3] = 1;
        maze[11][4] = 1;
        maze[11][5] = 1;
        maze[11][6] = 1;
        maze[11][8] = 1;
        maze[11][9] = 1;
        maze[11][10] = 1;
        maze[11][11] = 1;
        maze[11][12] = 1;
        maze[11][13] = 1;
        maze[11][14] = 1;
        maze[12][1] = 1;
        maze[12][8] = 1;
        maze[12][9] = 1;
        maze[12][14] = 1;
        maze[13][1] = 1;
        maze[13][3] = 1;
        maze[13][4] = 1;
        maze[13][5] = 1;
        maze[13][6] = 1;
        maze[13][7] = 1;
        maze[13][8] = 1;
        maze[13][11] = 1;
        maze[13][12] = 1;
        maze[13][14] = 1;
        maze[14][1] = 1;
        maze[14][3] = 1;
        maze[14][7] = 1;
        maze[14][8] = 1;
        maze[14][9] = 1;
        maze[14][10] = 1;
        maze[14][11] = 1;
        maze[14][12] = 1;
        maze[14][14] = 1;
        maze[15][12] = 1;
    }
    
    public static void showMaze(int maze[][]) {
    	slomo();

    	//clear the screen (the cheap and cheesy way...)
        for (int i = 0; i < 50; i++) {
        	System.out.print("\n");
        }
        for (int j = 0; j<maze[0].length; j++){
            for (int i = 0; i<maze.length; i++){
               	if (maze[j][i] == 1) {
               		System.out.print("  ");
	        	}
               	else if (maze[j][i] == 2) {
               		System.out.print("^^");
               	}
	        	else {
	        		System.out.print("##");
	        	}
            }
            System.out.print("\n");
        }
    }
    
    
    
}
