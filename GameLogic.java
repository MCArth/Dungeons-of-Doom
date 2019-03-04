import java.util.ArrayList;

/**
* Handles logic for and what happens with commands that could be used by player while ingame
* Also handles moving logic for the bot
*
* @author Arthur Baker
* @version 1.0
* @release 14/12/2018
* 
*/
public class GameLogic {
		
	private int gold = 0; //Stores the amount of gold the player has
	
	private static boolean pGoldSquare = false; //Whether tile player is standing on has gold
	private static boolean pExitSquare = false; //Whether tile player is standing is exit tile
	
	private static boolean bGoldSquare = false; //Whether tile bot is standing on has gold
	private static boolean bExitSquare = false; //Whether tile bot is standing on is exit tile
	
	private Map map = new Map(); //GameLogic's Map object
	
	/**
	 * Default constructor
	 */
	public GameLogic() {
	}


    /**
	 * Prints the gold required to win.
	 * 
     */
    public void hello() {
    	System.out.println("Gold to win: " + map.getGoldRequired());
    }
	
	/**
	 * Prints the gold currently owned by the player.
     */
    public void gold() {
    	System.out.println("Gold owned: " + gold);
    }

    /**
     * Checks if movement is legal and updates actor's location on the map.
     *
     * @param direction : The direction of the movement.
     * @param direction : The actor - either the bot or player character
     */
    public void move(char direction, char player) {
    	
    	ArrayList<ArrayList<Character>> board = map.getMap();
    	int[] coord = {0, 0}; //Stores location of actor
    	boolean goldSquare = false; //Whether actor was standing on gold
    	boolean exitSquare = false; //Whether actor was standing on exit
    	
    	//Sets local variables to the specific variables for either player char or bot.
    	if (player == 'P') {
    		coord = map.getPPos();
    		goldSquare = pGoldSquare;
    		exitSquare = pExitSquare;
    	}
    	else if (player == 'B') {
    		coord = map.getBPos();
    		goldSquare = bGoldSquare;
    		exitSquare = bExitSquare;
    	}
    	
    	//Row and column of actor
        int pRow = coord[0];
        int pCol = coord[1];
        
        char potentialMove = '0'; //Character of board tile player wants to move to.
        int rTo = 0; //Row to move to.
        int cTo = 0; //Column to move to.
        
        //Sets row and column to move to for each specific direction
        if (direction == 'N') {
        	potentialMove = board.get(pRow-1).get(pCol);
        	rTo = pRow-1;
        	cTo = pCol;
        }
        else if (direction == 'E') {
        	potentialMove = board.get(pRow).get(pCol+1);
        	rTo = pRow;
        	cTo = pCol+1;
        }
        else if (direction == 'S') {
        	potentialMove = board.get(pRow+1).get(pCol);
        	rTo = pRow+1;
        	cTo = pCol;
        }
        else if (direction == 'W') {
        	potentialMove = board.get(pRow).get(pCol-1);
        	rTo = pRow;
        	cTo = pCol-1;
        }
       
        //Moves player to new tile while recording the type of the tile the player is moving onto.
        if (potentialMove == '.') {
        	map.movePlayer(rTo, cTo, goldSquare, exitSquare, player);
        	goldSquare = false;
        	exitSquare = false;
        }
        else if (potentialMove == 'G') {
        	map.movePlayer(rTo, cTo, goldSquare, exitSquare, player);
        	goldSquare = true;
        	exitSquare = false;
        }
        else if (potentialMove == 'E') {
        	map.movePlayer(rTo, cTo, goldSquare, exitSquare, player);
        	goldSquare = false;
        	exitSquare = true;
        }
        else if (potentialMove == '#') { //If player tries to move into wall do nothing.
        	if (player == 'P') {
        		System.out.println("FAIL");
        	}
        }
        else if (potentialMove == 'P' || potentialMove == 'B') { //Ends game if the bot/player character move onto the same tile.
        	System.out.println("In the darkness you feel something touch you... \nYou look to your side with wide eyes... \nYou see it with gut-wrenching terror... \nThe boogey monster. \n...\n...");
        	System.out.println("It tickles you to death.");
        	System.out.println("LOSE");
        	System.exit(0);
        }
        
        //Sets the global variables to determine what type of tile it is.
        if (player == 'P') {
        	pGoldSquare = goldSquare;
        	pExitSquare = exitSquare;
        }
        else if (player == 'B') {
        	bGoldSquare = goldSquare;
        	bExitSquare = exitSquare;
        }
    }

    /**
     * Prints the map around the player
     */
    public void look() {
    	
    	ArrayList<ArrayList<Character>> board = map.getMap(); //Imports map
    	
    	//Gets player co-ordinates
    	int pRow = map.getPPos()[0]; 
        int pCol = map.getPPos()[1];
        
        //Prints the map around the player by looking at all tiles which are +- 2 of the player's row and column
        for (int y = -2; y < 3; y++) {
        	for (int x = -2; x < 3; x++) {
        		
        		if (pRow+y < 0 || pCol+x < 0) {
        			System.out.print('#');
        		}
        		else {
        			System.out.print(board.get(pRow+y).get(pCol+x));
        		}
        	}
        	
        	System.out.println();
        }
    	
    }

    /**
     * Processes the player's pickup command, updating the map and the player's gold amount, and prints if success or not.
     */
    public void pickup() {
    	
        if (pGoldSquare) { //If player standing on gold tile.
        	gold++;
        	pGoldSquare = false;
        	System.out.println("SUCCESS. Gold owned: " + gold);
        }
        
        else { //If they were standing on any other tile.
        	System.out.println("FAIL");
        }
    }

    /**
     * Quits the game, shutting down the application, printing the result of the game beforehand.
     */
    public void quitGame() {
    	
    	//Checks win conditions.
    	//Player must have enough gold and be on exit square.
    	if (pExitSquare && (gold >= map.getGoldRequired())) {
    		
    		System.out.println("WIN");
    		System.out.println("You beat the map " + map.getMapName() + ".");
    		System.out.println("Congratulations! Young adventurer, you may now retire to a castle on a hilltop with your hoarde of gold.");
    	}
    	
    	//If win conditions weren't met
    	else {
    		System.out.println("LOSE");
    		
    		if (gold < map.getGoldRequired()) { //If player didn't have enough gold when trying to quit.
    			System.out.println("Try not being so scared and go pick up some gold!");
    		}
    		else { //If player had enough gold but didn't stand on exit tile :(
    			System.out.println("Drats, you were so close! Stand on the exit next time.");
    		}
    		
    	}
    	
    	System.exit(0);
    }
}