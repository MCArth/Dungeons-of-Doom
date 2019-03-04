import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
* Stores the map and player + bot locations and performs all operations to do with modifying the map.
*
* @author Arthur Baker
* @version 1.0
* @release 14/12/2018
*/
public class Map {
	
	Random rand = new Random();

	//Representation of the map.
	private static ArrayList<ArrayList<Character>> map = new ArrayList<>();
	
	//Map name.
	private static String mapName;
	
	//Gold required for the human player to win.
	private static int goldRequired;
	
	//Position of the player and bot.
	static int[] pPos = {0, 0};
	static int[] bPos = {0, 0};
	
	//Whether a map has been successfully loaded.
	boolean mapLoaded = false;
	
	/**
	 * Constructor for use when not starting the game for the first time and making the map
	 * Called in GameLogic so as to have a map object that it can use to call methods on.
	 * 
	 */
	public Map() {
	}
	
	/**
	 * Constructor that accepts a map to read in from and sets map to the map it has read in from the file.
	 * Performs modifications to the map that it reads in so that the map is a rectangle 
	 * and has sufficient walls around the edge of the map.
	 *
	 * @param : The filename of the map file.
	 */
	public Map(String filename) {
		String line = null;
		int maxLength = 0;
		
		try {
			//BufferedReader reads map file from the folder that the java files are compiled in.
			//Hence map files must be stored in the same folder as the rest of the game or reading the map will fail.
			BufferedReader bufferedMapReader = new BufferedReader(new FileReader(filename));
			
			int i = 0; //Line of the file you are on.
			
			//While there are still lines to read.
			while((line = bufferedMapReader.readLine()) != null) {
				
				if (i == 0) { //First line is name of the map.
					mapName = line.replace("name ", "");
				}
				
				else if (i == 1) { //Second line is the number of gold needed to win when exiting map.
					goldRequired = Integer.parseInt(line.replace("win ", ""));
				}
			
				else { //Adds rows to the ArrayList which is the map.
					
					ArrayList<Character> mapLine = new ArrayList<>();
					for (int x = 0; x<line.length();x++) {
						mapLine.add(line.charAt(x));
					}
					
					if (line.length()>maxLength) {
						maxLength = line.length();
					}
					
					//Adds a wall at the end of every line so that even if player at edge of map no IndexError is received when using LOOK.
					mapLine.add('#');
					map.add(mapLine);

					
				}
				i++; //Increments line counter once line completely read.
			}
				
			bufferedMapReader.close();
			
			//Adds a line of wall at the bottom of the map to prevent IndexError if player uses 'LOOK' command while at bottom of map.
			ArrayList<Character> mapLine = new ArrayList<>();
			for (int x = 0; x < maxLength+2; x++) {
				mapLine.add('#');
			}
			map.add(mapLine);
			
			randPersonPos('P'); //Generates player character on the map
			
			mapLoaded = true; //Once map has been completely loaded sets this flag so as to tell HumanPlayer to load the game.
		}
		
		//If something goes wrong the player will be prompted to enter map name again by HumanPlayer due to mapLoaded flag.
		catch(Exception e) {
			System.out.print("Something went wrong. If using a default map, please ensure you entered the map name correctly, including the '.txt'.");
			System.out.println(" If using player created map, please check README and ensure map file follows all guidelines.");
			
			mapLoaded = false;
		}
	}
	
	/**
	 * Generates the human character or bot on a random spot on the map.
	 * @param player
	 * 			Which actor - bot or human character.
	 */
	public void randPersonPos(char player) {
		//Gets length and width of map.
		int y = rand.nextInt(map.size());
		int x = rand.nextInt(map.get(0).size());
		
		//Ensures actor doesn't start on wall, exit, gold or the player.
		while (map.get(y).get(x) == '#' || map.get(y).get(x) == 'E' || map.get(y).get(x) == 'G' || map.get(y).get(x) == 'P') {
			y = rand.nextInt(map.size());
			x = rand.nextInt(map.get(0).size());
		}
		
		map.get(y).set(x, player); //Places actor on map.
		
		//Sets actor position to the co-ordinates generated.
		if (player == 'P') {
			pPos[0] = y;
			pPos[1] = x;
		}
		else if (player == 'B') {
			bPos[0] = y;
			bPos[1] = x;
		}
	}
	
    /**
     * @return : Gold required to exit the current map.
     */
    public int getGoldRequired() {
        return goldRequired;
    }

    /**
     * @return : The map as stored in memory.
     */
    public ArrayList<ArrayList<Character>> getMap() {
        return map;
    }
    
    /**
     * Takes an actor and the position it wants to move to on the map, setting the new position as the actor
     * and changing the position it moved from to what was underneath the actor.
     * 
     * @param pRowTo
     * 			Row actor wishes to move to.
     * @param pColTo
     * 			Column actor wishes to move to.
     * @param wasGold
     * 			Whether the tile the actor was previously on had gold on it.
     * @param wasExit
     * 			Whether the tile the actor was previously on was an exit tile.
     * @param player
     * 			The actor - player character or bot.
     */
    public void movePlayer(int pRowTo, int pColTo, boolean wasGold, boolean wasExit, char player) {
    	
    	map.get(pRowTo).set(pColTo, player); //Places the actor on the tile it wants to move to.
    	
    	//Position actor was previously before move.
    	int oldY = 0;
    	int oldX = 0;
    	if (player == 'P') {
        	oldY = pPos[0];
        	oldX = pPos[1];
    	}
    	else if (player == 'B') {
        	oldY = bPos[0];
        	oldX = bPos[1];
    	}
    	
    	//Sets the position the actor moved from to what was underneath the actor.
    	if (wasGold) {
    		map.get(oldY).set(oldX, 'G');
    	}
    	else if (wasExit) {
    		map.get(oldY).set(oldX, 'E');
    	}
    	else {
    		map.get(oldY).set(oldX, '.');
    	}
    	
    	//Sets actor positions to new location.
    	if (player == 'P') {
        	pPos[0] = pRowTo;
        	pPos[1] = pColTo;
    	}
    	else if (player == 'B') {
        	bPos[0] = pRowTo;
        	bPos[1] = pColTo;
    	}
    }

    /**
     * @return : The name of the current map.
     */
    public String getMapName() {
        return mapName;
    }
    
    /**
     * @return : The size 2 array of player y and x co-ordinates.
     */
    public int[] getPPos() {
    	return pPos;
    }
    
    /**
     * @return : The size 2 array of bot y and x co-ordinates.
     */
    public int[] getBPos() {
    	return bPos;
    }

    /**
     * @return : Returns whether the map was successfully loaded when constructor was called or if an Exception was caught.
     */
    public boolean getMapLoaded() {
    	return mapLoaded;
    }
}
