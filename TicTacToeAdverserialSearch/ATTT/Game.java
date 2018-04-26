
import java.util.*;
import java.io.*;

//class for minimax algortihm and running the games
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
		AdvBoard gameBoard = new AdvBoard();
		if(humanPlayer == 'x'){
			gameBoard.printBoard();
			System.err.println();
			humanPlayer(gameBoard);
		}
		else{
			aiPlayer(gameBoard);
		}
	}
	//read and evaluate human (or opponents)'s move
	public static void humanPlayer(AdvBoard gameBoard){
		ArrayList<Action> actions= gameBoard.getActions();

		//read move 
		System.err.println("================ Your turn ================");
		Scanner scan = new Scanner(System.in);
		int largeBoardMove = scan.nextInt();
		int smallBoardMove = scan.nextInt();

		//Loops until player makes a valid move
		while(!validMove(actions, largeBoardMove, smallBoardMove)){
			System.err.println("-------Move NOT allowed. Please make a valid move----------");
			System.err.print("(Hint: Valid moves: ");
			for(int i = 0; i<actions.size();i++){
				actions.get(i).printAction();
			}
			System.err.println(" )");
			largeBoardMove = scan.nextInt();
			smallBoardMove = scan.nextInt();
		}

		//process the move
		gameBoard.makeMove(new Action(largeBoardMove, smallBoardMove));
		gameBoard.printBoard();
		System.err.println();

		//check if game has been won
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

	//Program's move
	public static void aiPlayer(AdvBoard gameBoard){
		//chose and make move for the computer
		ArrayList<Action> actions= gameBoard.getActions();
		Action compTurn = ABSearch(gameBoard);
		gameBoard.makeMove(compTurn);
		gameBoard.printBoard();
		//compTurn.printAction();
		System.out.println(compTurn.a1 + " " + compTurn.a2);
		System.err.println();

		//check if someone has one the game
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
			//prepare for opponent/human's turn
			actions= gameBoard.getActions();
			for(int i = 0; i<actions.size();i++){
				actions.get(i).printAction();
			}
			System.err.println();
			//human player's turn
			humanPlayer(gameBoard);
		}

	}

	//Check whether a player is making a valid move
	public static boolean validMove(ArrayList<Action> actions, int smallBoardMove, int largeBoardMove){
		for(int i = 0; i<actions.size(); i++){
			if(actions.get(i).a1 == smallBoardMove && actions.get(i).a2 == largeBoardMove)
				return true;
		}
		return false;
	}

	//beginning of minimax with alpha beta pruning
	public static Action ABSearch(AdvBoard ab){
		Action ret = new Action(0, 0);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		boolean isFree;
		Action last = new Action(0, 0);
		ArrayList<Action> actions = ab.getActions();
		int max = Integer.MIN_VALUE;

		for(int i = 0; i< actions.size(); i++){
			Action act = actions.get(i);

			//retain values for unwinding recursive calls
			last.set(ab.lastAction);
			isFree = ab.freeTurn;
			ab.makeMove(act);
			int val = minValue(ab, alpha, beta, 0);
			if(val > max){
				max = val;
				ret.set(act);
			}
			ab.undoMove(act);
			ab.freeTurn = isFree;
			ab.lastAction.set(last);


			if(max >= beta){
				ret.set(act);
				return ret;
			}
			alpha = Math.max(alpha, max);
		}
		return ret;

	}
	//utility/evaluation part of minimax
	public static int utility(AdvBoard state, int depth){
		int value = Integer.MIN_VALUE;
		if(state.finished()){
			if(state.winner == ai){
				value = 100;
			}
			else{
				value = -100;
			}
		}
		if(state.full()){
			value = 0;
		}
		if(cutoffTest(depth, state.moveCount)){
			value = state.evaluate(ai);
		}

		return value;
	}
	//max value part of minimax
	public static int maxValue(AdvBoard ab, int alpha, int beta, int depth){
		int util = utility(ab, depth);
		if(util != Integer.MIN_VALUE)
			return util;

		boolean isFree;
		Action last = new Action(0, 0);
		int max = Integer.MIN_VALUE;
		ArrayList<Action> actions = ab.getActions();

		for(int i = 0; i< actions.size(); i++){
			Action act = actions.get(i);

			//retain values for unwinding recursive calls
			last.set(ab.lastAction);
			isFree = ab.freeTurn;
			ab.makeMove(act);
			max = Math.max(max, minValue(ab, alpha, beta, depth+1));
			ab.undoMove(act);
			ab.freeTurn = isFree;
			ab.lastAction.set(last);

			if(max >= beta){
				return max;
			}
			alpha = Math.max(alpha, max);
		}
		return max;
	}
	//min value part of minimax
	public static int minValue(AdvBoard ab, int alpha, int beta, int depth){
		int util = utility(ab, depth);
		if(util != Integer.MIN_VALUE)
			return util;

		boolean isFree;
		Action last = new Action(0, 0);
		int min = Integer.MAX_VALUE;
		ArrayList<Action> actions = ab.getActions();

		for(int i = 0; i< actions.size(); i++){
			Action act = actions.get(i);

			//retain values for unwinding recursive calls
			last.set(ab.lastAction);
			isFree = ab.freeTurn;
			ab.makeMove(act);
			min = Math.min(min, maxValue(ab, alpha, beta, depth+1));
			ab.undoMove(act);
			ab.freeTurn = isFree;
			ab.lastAction.set(last);

			if(min <= alpha){
				return min;
			}
			beta = Math.min(beta, min);
		}
		return min;
	}
	//dynamically cut off search depending on number of moves on the board
	public static boolean cutoffTest(int depth, int moveCount){
		if(moveCount > 60){
			return depth>=20;
		}
		else if(moveCount > 40){
			return depth>=10;
		}
		else if(moveCount > 20){
			return depth >= 8;
		}
		else{
			return depth >= 5;
		}
		/*
		if(moveCount > 60){
			return depth>=20;
		}
		else if(moveCount > 40){
			return depth>=13;
		}
		else if(moveCount > 30){
			return depth >= 10;
		}
		else if(moveCount > 20){
			return depth >= 8;
		}
		else{
			return depth >= 5;
		}
		*/
	}
}
