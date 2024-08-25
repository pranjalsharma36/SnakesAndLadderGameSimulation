package app;

import java.io.*;

import java.util.*;

public class GameSimulation {
	public static void main (String[] args) {
		
		System.out.println("Executing Simulation");
		Player p1 = new Player("Pranjal");
		
		Player p2 = new Player("Sharma");
		
		
		
		Queue<Player> playerQueue = new LinkedList<>();
		
		playerQueue.add(p1);
		playerQueue.add(p2);
		
		SnakesAndLaddersGame game = new SnakesAndLaddersGame(playerQueue, BoardType.MEDIUM);
		
		game.startGame();
	}
}

class Player{
    
    String name;
    int cellId;
    
    // constructor
    
    Player(String name){
        this.name = name;
        cellId = 0;
    }
    
}

// Observation : Cell is not doing anything, cellId will help us to play our game
class SingletonDice{
    
    private static SingletonDice dice;
    
    public static SingletonDice getInstance(){
        
        synchronized(SingletonDice.class){
            if(dice == null){
                dice = new SingletonDice();
            }
        }
        return dice;
    }
    
    public int roll(){
        
        int dice_number = (int)(Math.random()*6 +1);
        return dice_number;
    }
}


class SnakesAndLaddersGame{
    
    Queue<Player> playerQueue;
    
    BoardFactory boardFactory;
    
    SingletonDice dice;
    
    Board board;
    
    // constructor
    SnakesAndLaddersGame(Queue<Player> playerQueue, BoardType type){
        
        dice = SingletonDice.getInstance();
        
        boardFactory = new BoardFactory();
        // can we have a factory for creating board based on the type provided to us => makes sense
        board = boardFactory.getInstance(type);
        
        this.playerQueue = playerQueue;
    }
    
    public void startGame(){
        
        System.out.println("GAME STARTED");
        
        while(!playerQueue.isEmpty()){
            
            Player player = playerQueue.remove();
            
//            System.out.println("Player Id = " + );
            // throws dice
            int dice_number = dice.roll();
            
           
            int len = board.length;
            
            // how to get final cell?
            int currentCellId = player.cellId;
            
            currentCellId += dice_number;
            
            if(currentCellId == len * len){
                System.out.println("Player completed " + player.name);
                continue;
            }
            
            
            if(currentCellId < len * len){
                player.cellId = currentCellId; 
                player.cellId = board.getDestinationCell(player.cellId); // here board will apply logic for snakes and ladders also
                
                System.out.println(player.cellId);
            }
            
            playerQueue.add(player);
            
        }
        
        System.out.println("GAME OVER");
    }
}

class BoardCellCatalogMapping{
    
    HashMap<BoardType, HashMap<Integer, Passage>> boardCellCatalogMapping;
    
    // constructor 
    BoardCellCatalogMapping(){
        // todo : add snakes and ladders here
        boardCellCatalogMapping = new HashMap<>();
        
        boardCellCatalogMapping.put(BoardType.SMALL, new HashMap<>());
        boardCellCatalogMapping.put(BoardType.MEDIUM, new HashMap<>());
        boardCellCatalogMapping.put(BoardType.LARGE, new HashMap<>());
        
        // we can have predefined Mapping for every boardType
        Snake snake = new Snake(12);
        boardCellCatalogMapping.get(BoardType.MEDIUM).put(99, snake);
    }
    
}

enum BoardType{
    
    SMALL,
    MEDIUM,
    LARGE
}

class BoardLengthMapping{
    
    Map<BoardType, Integer> boardLengthMapping;
    
    // constructor
    BoardLengthMapping(){
        boardLengthMapping = new HashMap<>();
        boardLengthMapping.put(BoardType.SMALL, 5);
        boardLengthMapping.put(BoardType.MEDIUM, 10);
        boardLengthMapping.put(BoardType.LARGE, 20);
    }
    
}

class Board {
    
    BoardType type;
    int length;
    
    HashMap<Integer, Passage> cellPassageCatalog; // each type of board will have its own type of cellPassage catalog
                                                // cellId vs Passage => cellId = Integer => row*col
                                                // row = cellId/ length;
                                                // col = cellId % length;
    
    // constructor
    
    Board(BoardType type) {
        this.type = type;
        // using type, we can create the below objects
        this.length = new BoardLengthMapping().boardLengthMapping.get(type);
        this.cellPassageCatalog = new BoardCellCatalogMapping().boardCellCatalogMapping.get(type);
    }
    
    public void initialize(){
        // we can print the information of the initial board to the user based on the information we have
    }
    
    public int getDestinationCell(int cellId){
        
//        int cellId = row*col;
        
        if(cellPassageCatalog.containsKey(cellId)){
            cellId = cellPassageCatalog.get(cellId).destinationCellId;
        }
        
        return cellId;
        
    }
}

class MediumBoard extends Board{
    
    MediumBoard(){
        super(BoardType.MEDIUM);   
    }
}

class SmallBoard extends Board{
    
    SmallBoard(){
        super(BoardType.SMALL);   
    }
}


class LargeBoard extends Board{
    
    LargeBoard(){
        super(BoardType.LARGE);   
    }
}

class BoardFactory{
    
    public Board getInstance(BoardType type){
        
        if(type == BoardType.SMALL){
            return new SmallBoard();
        }
        else if(type == BoardType.MEDIUM){
            return new MediumBoard();
        }
        else{
            return new LargeBoard();
        }
    }
}


enum PassageType{
    SNAKE,
    LADDER
}

class Passage{
    
    PassageType type;
    Integer destinationCellId; // getter can be created
    // consrtrutor
    Passage(PassageType type, Integer destinationCellId){
        this.type = type;
        this.destinationCellId = destinationCellId;
    }
}

class Snake extends Passage{
    
    // constructor
    Snake(int destinationCellId){
        super(PassageType.SNAKE, destinationCellId);
    }
}

class Ladder extends Passage{
    
    Ladder(int destinationCellId){
        super(PassageType.LADDER, destinationCellId);
    }
}

/*
Objects :

SnakesAndLaddersGame

Board : Small Board, MediumBoard, LargeBoard

Cell

Player

PlayerQueue

Dice 

Passage

Passage type

ladder 

Snake

how board initialization will work
1. cells and their numbering
2. Passge creation, note for a given cell I should be able to identify passage

CellPassageCatalog :- cellId vs Passage

we can iterate on this catalog to initialize the board. The board should be immuable and the info of the boxes can 
be with the Player

Approach : I'll start with core logic and create objets and their functionalities as and when needed.
*/