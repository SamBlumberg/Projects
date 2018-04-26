import java.util.*;

//class for a 3x3 board of 3x3 boards
public class AdvBoard{

	//variables
	public Board[][] advBoard;
	public char turn;
	public char winner;
	public Action lastAction;
	public boolean freeTurn;
	public int moveCount;

	//constructor
	public AdvBoard(){
		turn = 'x';
		lastAction = new Action(0, 0);
		freeTurn = true;
		moveCount = 0;

		advBoard = new Board[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				advBoard[i][j] = new Board();
			}
		}
	}
	//get and return a list of possible actions from the current state
	public ArrayList<Action> getActions(){
		ArrayList<Action> actions = new ArrayList<Action>();

		if(freeTurn){ //if move can be made anywhere
			for(int i = 0; i<3; i++){
				for(int j = 0; j<3; j++){

					for(int l = 0; l<3; l++){
						for(int m = 0; m<3; m++){
							if(advBoard[i][j].board[l][m] == ' '){
								Action act = new Action(3*i + j+1, 3*l + m+1);
								actions.add(act);
							}
						}
					}

				}
			}
		}
		else{ // if move is restricted to a certain small board
			int pos = lastAction.a2;
			for(int i = 0; i<3; i++){
				for(int j = 0; j<3; j++){
					if(advBoard[ (pos-1)/3 ][ (pos-1)%3 ].board[i][j] == ' '){
						Action act = new Action(pos, 3*i + j+1);
						actions.add(act);
					}
				}
			}
		}
		return actions;
	}
	//make move on board and set freeTurn for next move
	public void makeMove(Action act){
		moveCount++;
		setPos(act);
		lastAction = act;
		nextTurn();

		if(advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].full()){
			freeTurn = true;
		}
		else{
			freeTurn = false;
		}
	}
	//uncomplete a move that has already been made
	public void undoMove(Action act){
		moveCount--;
		winner = ' ';
		unSetPos(act);
		nextTurn();
	}
	//advance to next players turn
	public void nextTurn(){
		if(turn == 'x'){
			turn = 'o';
		}
		else{
			turn = 'x';
		}
	}
	//return value at a position in the board
	public void setPos(Action act){	
		advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].board[(act.a2-1)/3 ][ (act.a2-1)%3 ] = turn;
	}
	//set value at a position in the board
	public void unSetPos(Action act){
		advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].board[(act.a2-1)/3 ][ (act.a2-1)%3 ] = ' ';
	}

	//check if a player has won the board and set winner to that player if so
	public boolean finished(){

		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(advBoard[i][j].finished()){
					winner = advBoard[i][j].winner;
					return true;
				}
			}
		}
		return false;
	}
	//check if the board is full
	public boolean full(){
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(!advBoard[i][j].full()){
					return false;
				}
			}
		}
		return true;
	}
	//evaluation function for h-minimax, returns sum of evaluation for each smaller board
	public int evaluate(char player){
		int val = 0;
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(i == (lastAction.a2-1)/3 && j == (lastAction.a2-1)%3){
					val += 5*advBoard[i][j].evaluate(player);
				}
				else{
					val += advBoard[i][j].evaluate(player);
				}
			}
		}
		return val;
	}

	//print board
	public void printBoard(){
		System.err.println("=============================================");

		for(int i = 0; i<3; i++){

			for(int j = 0; j<3; j++){
				System.err.println("-------------   -------------   -------------");

				for(int l = 0; l<3; l++){
					System.err.print("|");
					for(int m = 0; m<3; m++){
						System.err.print(" " + Character.toUpperCase(advBoard[i][l].board[j][m]) + " |");
					}
					System.err.print("   ");
				}
				System.err.println("");

			}
			if(i<2){
				System.err.println("-------------   -------------   -------------\n");
			}
			else{
				System.err.println("-------------   -------------   -------------");
			}
		}
		System.err.println("=============================================");
	}

}