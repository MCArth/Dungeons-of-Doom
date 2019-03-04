
Installation:

Keep all files in the same folder.
Then compile using 'javac *.java' on the command line.
To play game use command 'java HumanPlayer' on the command line.




How to play:

The aim of the game is to collect enough gold to escape the dungeon.

The map is represented by various characters.

'P' - YOU!
'.' - Boring old floor tile
'G' - Floor tile containing one piece of gold
'E' - Exit tile
'#' - Wall

You have also heard rumours of a boogey monster living deep inside this dungeon.
If you happen to stumble across it, it will likely look like a 'B' standing for Boogey monster.
Be careful, as it may attempt to harm you if it moves onto the same tile as it, or you its tile.


There are a few certain commands you can use in your quest.
Be careful to enter these carefully as any misspelling will result in you losing your turn!

'HELLO' - You will check your quest specification for how many gold pieces you must find before you can leave.
'GOLD' - You will check how many gold pieces you currently have.
'MOVE <direction>' - You will move in the specified direction. <direction> must be either 'N', 'S', 'W' or 'E'.
'PICKUP' - You will pickup the gold piece on the tile you are on, if there is one.
'LOOK' - You will look around and observe what's near you.




Difficulty Selection:

There are 3 difficulty settings in this game.
'WEAKLING' - Perhaps the legend of the boogey monster is just that, a legend.
'EXPLORER' - The legends say that the boogey monster is not much smarter than a twig. 
				He will likely harm you if you come into contact with him but will not chase you.
'WARRIOR' - The legends say the boogey monster is a fearsome creature who attempts to sniff out and chase down all who enter his dungeon.




Map creation:

All new maps must be placed in the same folder as the rest of the game.

The first line of the map is the name of the map and must be in the format 'name <Name of Map>'.
The second line of the map is the gold required to beat the map and exit successfully and must be in the format 'win <Gold Required>'.
All lines beneath that must be the map in text form with no spaces inbetween.

For an example please see one of the included default maps 'mediumdungeon.txt' or 'smalldungeon.txt'.

Always ensure that there is enough gold on the map to be able to meet the requirement to win otherwise you will not be able to beat your map!

Every tile on the edge of the map must be a wall tile - '#'.
The map must also be rectangularly shaped.
However if you wish to have a map that would be irregularly shaped, we recommend that you simply fill in the gaps with walls so as to make the map a rectangle.

Please ensure that when selecting maps in-game you include the '.txt' in the filename.




Implementation Details:
For those adventurers of you who may be a bit more technically minded and wish to delve into the inner workings of Dungeon of Doom!

The game is implemented in java using 4 different classes.

HumanPlayer - This class contains the main class and reads input from the command line.
It also handles processing commands to do with starting the game and passes commands while in game to GameLogic.

GameLogic - Contains the logic for all commands the human player can enter while in game.

Map - Stores the map and contains methods that deal with modifying the map.

Bot - Contains the logic and decision making for the bot or the 'Boogey Monster'.

