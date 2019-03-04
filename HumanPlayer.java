import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
* Contains main method, which contains bufferedreader and reads player commands.
* Contains the processes for the player starting the game
* For ingame commands discerns what it is then passes to GameLogic
*
* @author Arthur Baker
* @version 1.0
* @release 14/12/2018
* 
*/
public class HumanPlayer {

	private static GameLogic logic = new GameLogic(); //Makes logic object for HumanPlayer to use.
	
	private static boolean inGame = false; //If the player is in game or reading introduction/selecting options.
	private static int difficulty; //Difficulty of bot.
	
	//Will be the object of the bot class for HumanPlayer to call at end of each player turn.
	//Constructor will be called when difficulty is known.
	private static Bot monster;
	
    /**
     * Main function.
     * Reads player's input from the console and passes to processor method.
     * 
     */
    public static void main (String[] args) {
    	
    	System.out.println("Welcome to the Dungeons of Doom, young adventurer!");
		System.out.print("Press Enter");
    	
		BufferedReader readCommands = new BufferedReader(new InputStreamReader(System.in)); //Buffered reader for reading input from console.

        //Continue taking input while program is running.
        while(true) {
        	String command; //String player enters
        	
        	//Catches any I/O errors
			try {
				command = readCommands.readLine();
				
				//If player uses ctrl-D on terminal, forces exit.
				if(command == null)
				{
					System.exit(0);
				}
					
					//If player is in the game processes as a game command
					if (inGame) {
						processGameCommand(command);
					}
				 	
					//If player isn't in game processes as start up command
					else {
						processStartCommand(command);
					}
			}
			//Exits on IO error
			catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			} 
        }
    }
    
    static int count = 1; //What stage of the game startup/introduction player is on
    
    /**
     * Processes commands entered by player before game has started.
     * Contains intro, map and difficulty selection code.
     * 
     * @param command
     * 			The string the player entered into console
     */
    private static void processStartCommand(String command) {
    
    	//What stage of intro player is on.
    	switch(count) {
    	case(1):
    		System.out.print("Prepare to find your way through murky caves in your quest for gold.");
    		break;
    		
    	case(2):
    		System.out.println("You have heard rumours of an evil boogey monster who lives deep in these dungeons.");
    		System.out.println("Be ready to deal with the evils that lurk in the dark corners of these dungeons or you may find yourself meeting your own Doom...");
    		break;
    		
    	case(3):
    		System.out.println("Please read the README file if you require any help.");
    		System.out.println("Default maps are: 'smalldungeon.txt', 'mediumdungeon.txt'.");
    		System.out.println("If you have made your own map enter the name of your map file.");
    		System.out.println("Enter a map filename: ");
    		break;
    		
    	case(5):
    		System.out.println("Select a difficulty: 'WEAKLING', 'EXPLORER', 'WARRIOR'.");
    		count++;
    		break;
    	}
    	
    	
    	//If player is supposed to be entering map name.
    	if(count == 4) {
    		Map map = new Map(command); //Attempts to construct map from given map file.
    		if (!(map.getMapLoaded())) { //If the map loading failed for any reason
    			System.out.println("Please enter a valid map name:");
    		}
    		else { //If loaded successfully
    			count = 5;
    			System.out.println("Map loaded successfully! \n Press enter to continue.");
    		}
    	}
    	
    	//Increment count each time on intro and player presses enter.
    	else if(count < 4) {
    		count++;
    	}
    	
    	//If player selecting difficulty.
    	else if(count == 6 || count ==7) {
    		
    		//If command entered was valid difficulty sets difficulty to that difficulty level.
    		if (command.equals("WEAKLING") || command.equals("EXPLORER") || command.equals("WARRIOR")) {
    			
    			switch(command) {
    			case("WEAKLING"):
    				difficulty = 0;
    				break;
    				
    			case("EXPLORER"):
    				difficulty = 1;
    				break;
    				
    			case("WARRIOR"):
    				difficulty = 2;
    				break;
    			}
    			
    			monster = new Bot(difficulty); //Constructs Bot object with the difficulty of the bot.
    			
    			inGame = true;
    			System.out.println("You descend into the depths of the dungeon... Good luck adventurer...\n");
    		}
    		
    		else if (count == 7) { //Called if previous command was incorrect difficulty
    			System.out.println("That was an invalid difficulty. Please enter either 'WEAKLING', 'EXPLORER', 'WARRIOR'");
    		}
    		else {
    			count++;
    		}
    		
    	}
    }
    
    /**
     * Processes the game command by the player. 
     * If valid it calls the relevant method in GameLogic to execute.
     * If not valid the player is told player loses their turn.
     *
     * @param command
     * 			The string the player entered into the console.	
     */
    private static void processGameCommand(String command) {
    	
    	char direction = '0'; //Direction of move command if entered.
    	
    	//Checks for if command is a move command and has a valid direction.
    	if (command.length() > 4) {
    		if (command.substring(0,5).equals("MOVE ")) {
    		
    			//Gets the attempted direction and checks if valid direction.
    			String woMove = command.replace("MOVE ", ""); 
    			if (woMove.equals("N") || woMove.equals("E") || woMove.equals("S") || woMove.equals("W") ) {
    				direction = woMove.charAt(0);
    				command = "MOVE";
    			}
    		
    			else {
    				System.out.println("Invalid direction. Please enter 'MOVE <direction>' where direction is N, E, S or W.");
    			}
    		}
    	}
    	//Checks for if only MOVE was entered.
    	else if(command.equals("MOVE")) {
    		System.out.println("Please enter 'MOVE <direction>' where direction is N, E, S or W.");
    		command = "1";
    	}
    	
    	//Checks for other commands.
    	switch (command) {
    	case ("HELLO"):
    		logic.hello();
    		break;
    	case ("GOLD"):
    		logic.gold();
    		break;
    	case ("MOVE"):
    		logic.move(direction, 'P');
    		break;
    	case ("PICKUP"):
    		logic.pickup();
    		break;
    	case ("LOOK"):
    		logic.look();
    		break;
    	case("QUIT"):
    		logic.quitGame();
    		break;
    		
    	default:
    		System.out.println("Invalid command, please refer to the README for all valid commands.");
    		
    	}
    	
    	//Bot takes their turn.
    	monster.callBot();
    }
}