
import java.util.*;
import java.io.*;

public class Game{

	static char ai;  //this can change depending on what human player chooses
	char humanPlayer;

	//constructor
	Game(char aiPlayer, char humanPlayer){
		this.ai = aiPlayer;
		this.humanPlayer = humanPlayer;
	}
	//start the game
	public void playGame(){
		Board gameBoard = new Board();
		if(humanPlayer == 'x'){
			gameBoard.printBoard();
			System.out.println();
			humanPlayer(gameBoard);
		}
		else{
			aiPlayer(gameBoard);
		}
	}

	//read and evaluate human (or opponents)'s move
	public static void humanPlayer(Board gameBoard){
		Scanner scan = new Scanner(System.in);
		List actions = gameBoard.getAction();
		System.err.println("Make a move: " + actions);
		String input;
		int action;
		//make sure input is formatted correct
		while(true){
			input = scan.nextLine();
			if("123456789".contains(input) && input.length() == 1){
				action = Integer.parseInt(input);
				break;
			}
			else{
				System.err.println("Please enter valid input:");
			}
		}

		//Loops until player makes a valid move
		while(!actions.contains(action)){
			System.err.println("-------Move NOT allowed. Please make a valid move----------");
			System.err.println("[Hint: Valid moves: "+actions+ "]");
			while(true){
				input = scan.nextLine();
				if("123456789".contains(input) && input.length() == 1){
					action = Integer.parseInt(input);
					break;
				}
				else{
					System.err.println("Please enter valid input:");
				}
			}

		}
		gameBoard.makeMove(action);
		gameBoard.printBoard();

		if(gameBoard.finished()){
			if(gameBoard.winner == 'x'){
				System.err.println("X wins");
			}
			else{
				System.err.println("O wins");
			}
		}
		else if(gameBoard.full()){
			System.err.println("Tie");
		}
		else{
			//AI's turn
			aiPlayer(gameBoard);
		}

	}
	//program's move
	public static void aiPlayer(Board gameBoard){
		int compTurn = 	minimax(gameBoard);
		gameBoard.makeMove(compTurn);
		gameBoard.printBoard();
		System.out.println(compTurn);

		if(gameBoard.finished()){
			if(gameBoard.winner == 'x'){
				System.err.println("X wins");
			}
			else{
				System.err.println("O wins");
			}
		}
		else if(gameBoard.full()){
			System.err.println("Tie");
		}
		else{
			//human player's turn
			humanPlayer(gameBoard);
		}
	}

	//start minimax algorithm to decide move
	public static int minimax(Board b){
		List actions = b.getAction();
		System.err.println(actions);

		int max = Integer.MIN_VALUE;
		int ret = 0;
		for(int i = 0; i<actions.size(); i++){
			Board c = b.copy();
			c.makeMove((int)actions.get(i));
			int val = minValue(c);

			Board e = b.copy();
			e.makeMove((int)actions.get(i));

			if (val > max) {
				max = val;
				ret = (int) actions.get(i);
			}

			//prevents prolonged win
			if(val >= max && e.finished()) {
				max = val;
				ret = (int) actions.get(i);
			}
		}
		return ret;
	}
	//max value part of minimax
	public static int maxValue(Board b){
		int util = utility(b);
		if(util != Integer.MIN_VALUE)
			return util;

		int max = Integer.MIN_VALUE;
		List actions = b.getAction();

		for(int i = 0; i<actions.size(); i++){
			Board c = b.copy();
			c.makeMove((int) actions.get(i));
			int val = minValue(c);

			if (val > max) {
				max = val;
			}
		}

		return max;
	}

	//min value part of minimax
	public static int minValue(Board b){
		int util = utility(b);
		if(util != Integer.MIN_VALUE)
			return util;

		int min = Integer.MAX_VALUE;
		List actions = b.getAction();

		for(int i = 0; i<actions.size(); i++){
			Board c = b.copy();
			c.makeMove((int) actions.get(i));
			int val = maxValue(c);

			if (val < min) {
				min = val;
			}
		}

		return min;
	}

	//Returns utility value of a terminal state
	public static int utility(Board b){
		int value = Integer.MIN_VALUE;
		if(b.finished()){
			if(b.winner == ai){
				value =  1;
			}
			else{
				value = -1;
			}
		}
		else if(b.full()){
			value = 0;
		}
		return value;
	}
}
