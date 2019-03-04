import java.util.ArrayList;
import java.util.Random;

/**
* Contains logic for the bot and works out what the bot's move will be.
* 
* DIFFICULTY 1/'WEAKLING' - There is no bot
* 
* DIFFICULTY 2/'EXPLORER' - Bot moves randomly without attempting to find the player
* 
* DIFFICULTY 3/'WARRIOR' - Bot uses the LOOK command every few moves and if it sees the player it will chase them.
* 						   If bot doesn't see player then will move randomly without moving into walls.
*
* @author Arthur Baker
* @version 1.0
* @release 14/12/2018
*/
public class Bot {

	private GameLogic logic = new GameLogic(); //Creates GameLogic object for bot to use to move actors
	private Map map = new Map(); //New map object for Bot to use
	int diff; //Difficulty of the bot
	
	/**
	 * Constructor for the class.
	 * Sets the difficulty of the bot to be used based on input by the player when starting the game.
	 * Spawns in the bot if not on the easiest difficulty where there is no bot.
	 * 
	 * @param difficulty
	 * 			Difficulty setting.
	 */
	public Bot(int difficulty) {
		diff = difficulty;
		if (diff > 0) {
			map.randPersonPos('B');
		}
	}
	
	/**
	 * Called at end of player turn. Calls for the bot to take an action based on difficulty.
	 */
	public void callBot() {
		
		switch(diff) {
		case(1):
			badBot();
			break;
		case(2):
			goodBot();
			break;
		}
	}
	
	/**
	 * Called when the bot processes for the specific difficulty have decided on a way for the bot to move.
	 * 
	 * @param direction
	 * 			Direction for bot to move.
	 */
	private void moveBot(char direction) {
		
		switch(direction) {
		case('N'):
			logic.move('N', 'B');
			break;
		case('E'):
			logic.move('E', 'B');
			break;
		case('S'):
			logic.move('S', 'B');
			break;
		case('W'):
			logic.move('W', 'B');
			break;
		
		}
	}
	
	/**
	 * Easiest difficulty where there still is a bot.
	 * Chooses a random direction to move and doesn't attempt to chase player.
	 */
	private void badBot() {
		int direction = rand.nextInt(4);
		
		switch(direction) {
		case(0):
			moveBot('N');
			break;
		case(1):
			moveBot('E');
			break;
		case(2):
			moveBot('S');
			break;
		case(3):
			moveBot('W');
			break;
		}
	}
	
	int lookCounter = 0; //Turns till okBot uses look command
	char okBotDirection; //Direction bot is currently moving in
	Random rand = new Random();
	
	/**
	 * Hardest difficulty of bot.
	 * Bot uses look command every few turns depending on certain factors.
	 * Method checks to see if bot is due to use look command and executes it if so.
	 * Then checks to see if it can see player. If it can will move towards player.
	 * If not will move randomly without faceplanting into walls.
	 * 
	 * If not using look, will move in previously specified direction.
	 */
	private void goodBot() {
		
		if (lookCounter == 0) { //If bot is going to 'look' this turn.
			
			//Gets both the player and bots' co-ordinates.
			int[] bPos = map.getBPos();
			int[] pPos = map.getPPos();
			
			//Gets difference between x and y co-ordinates of player and bot.
			int yDiff = bPos[0]-pPos[0];
			int xDiff = bPos[1]-pPos[1];
			
			//Checks if bot can see player when 'using' look by using the difference in x and y.
			if (yDiff > -3 && yDiff < 3 && xDiff > -3 && xDiff < -3) {
				moveTowardsPlayer(yDiff, xDiff);
			}
			
			//If doesn't see player will move in a 'smart' random direction.
			else {
				smartRandomDirection(bPos[0], bPos[1]);
			}
		}
		
		//If not looking, move in the direction previously determined.
		else {
			moveBot(okBotDirection);
			lookCounter--;
		}
	}
	
	/**
	 * Called when bot uses look and player is within the bots look radius.
	 * Will determine in which direction the bot should move to get to the player's position.
	 * @param yDiff
	 * 			Difference in bot's and player's y co-ordinates.
	 * @param xDiff
	 * 			Difference in bot's and player's x co-ordinates.
	 */
	private void moveTowardsPlayer(int yDiff, int xDiff) {
		
		//If player is further away vertically than horizontally, move vertically towards player
		if (yDiff > xDiff) {
			
			//If player is vertically above bot
			if (yDiff > 0) {
					okBotDirection = 'N';
			}
			else { //Vertically below
					okBotDirection = 'S';
			}
				//Sets number of moves to move in this direction as the difference in y, so if player stays in same row the bot will
				//in one or two turns move into the same row as player and then look again
				lookCounter = yDiff;
			}
		else { //If further away horizontally than vertically
				
			//If player west from bot
			if (xDiff > 0) {
				okBotDirection = 'W';
			}
			else { //East from bot
				okBotDirection = 'E';
			}
			
			lookCounter = xDiff; //So moves into same column as player
		}
	}
		
	
	
	/**
	 * Called to determine new direction if bot has used look and can't see player.
	 * Pseudo-random but will try and move bot in direction where bot isn't walking into a wall.
	 * 
	 * @param botY
	 * 			Current y co-ordinate of the bot.
	 * @param botX
	 * 			Current x co-ordinate of the bot.
	 */
	private void smartRandomDirection(int botY, int botX) {
	
		ArrayList<ArrayList<Character>> board = map.getMap();
				
		boolean dirChose = false; //If a direction has been chosen - for use in while loop.
		int ranDir = rand.nextInt(4); //Chooses random direction.
		
		//How many times while loop has been repeated
		//Once while loop has been repeated a certain amount of times it relaxes the conditions on a random direction to be chosen
		//(Useful if there is no direction in which there is no wall for 2 spaces ahead)
		int count = 0;
		
		//Repeats while loop until a direction has been chosen
		//First looks for direction in which both tiles in that direction are not walls. If it cannot find one within 8 random generations of direction
		//it then looks for direction with only 1 wall in that direction.
		while (!dirChose) {
			ranDir = rand.nextInt(4);
			if (ranDir == 0) {
				if ((board.get(botY).get(botX+1) != '#' &&  board.get(botY).get(botX+2) != '#' || ((count > 7) && (board.get(botY).get(botX+1) != '#')))) {
					okBotDirection = 'E';
					dirChose = true;
				}
				count++;
			}
			else if (ranDir == 1) {
				if ((board.get(botY).get(botX-1) != '#' &&  board.get(botY).get(botX-2) != '#' || ((count > 7) && (board.get(botY).get(botX-1) != '#')))) {
					okBotDirection = 'W';
					dirChose = true;
				}
				count++;
			}
			else if (ranDir == 2) {
				if ((board.get(botY-1).get(botX) != '#' &&  board.get(botY-2).get(botX) != '#' || ((count > 7) && (board.get(botY-1).get(botX) != '#')))) {
					okBotDirection = 'N';
					dirChose = true;
				}
				count++;
			}
			else if (ranDir == 3) {
				if (board.get(botY+1).get(botX) != '#' &&  board.get(botY+2).get(botX) != '#' || (count > 7 && board.get(botY+1).get(botX) != '#')) {
					okBotDirection = 'S';
					dirChose = true;
				}
				count++;
			}
			
			//To prevent infinite loop if player made map has a 1*1 box surrounded by walls which the bot spawned in.
			if (count>100) {
				dirChose = true;
			}
		}
		
		if (count > 7) { //If moving in direction (likely) with a wall after one tile of movement.
			lookCounter = 1;
		}
		else { //If can move two tiles in that direction freely.
			lookCounter = 2;
		}
	}
}
