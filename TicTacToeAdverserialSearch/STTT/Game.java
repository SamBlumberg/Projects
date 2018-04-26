
import java.util.*;
import java.io.*;

public class Game{

	static char ai;  //this can change depending on what human player chooses
	static char humanPlayer;

	public Game(char aiPlayer, char humanPlayer){
		this.ai = aiPlayer;
		this.humanPlayer = humanPlayer;
	}

	public static void playGame(){
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

	public static void humanPlayer(AdvBoard gameBoard){

		ArrayList<Action> actions= gameBoard.getActions();
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

	//AI player
	public static void aiPlayer(AdvBoard gameBoard){

		ArrayList<Action> actions= gameBoard.getActions();
		Action compTurn = ABSearch(gameBoard);
		gameBoard.makeMove(compTurn);
		System.out.println(compTurn.a1 + " " + compTurn.a2);
		gameBoard.printBoard();
		//compTurn.printAction();
		

		//System.err.println();

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

	public static int utility(AdvBoard state, int depth){
		int value = Integer.MIN_VALUE;
		if(state.finished()){
			if(state.winner == ai){
				value = 1000;
			}
			else{
				value = -1000;
			}
		}
		if(state.full()){
			value = 0;
		}
		if(cutoffTest(depth)){
			value = state.evaluate(ai);
		}

		return value;
	}

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
	public static boolean cutoffTest(int depth){
		int cut = 6;
		return depth >= cut;
	}
}
