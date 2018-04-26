import java.util.*;

public class AdvBoard{
	public Board[][] advBoard;
	public char turn;
	public char winner;
	public Action lastAction;
	public boolean freeTurn;
	public Board repBoard;

	public AdvBoard(){
		turn = 'x';
		lastAction = new Action(0, 0);
		freeTurn = true;
		repBoard = new Board();

		advBoard = new Board[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				advBoard[i][j] = new Board();
			}
		}
	}

	public ArrayList<Action> getActions(){
		
		ArrayList<Action> actions = new ArrayList<Action>();
		if(freeTurn){
			for(int i = 0; i<3; i++){
				for(int j = 0; j<3; j++){

					if(repBoard.board[i][j] == ' '){
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
		}
		else{
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

	public void makeMove(Action act){
		setPos(act);
		lastAction = act;
		nextTurn();

		if(advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].finished()){
			repBoard.board[ (act.a1-1)/3 ][ (act.a1-1)%3 ] = advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].winner;
		}

		else if(advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].full()){
			repBoard.board[ (act.a1-1)/3 ][ (act.a1-1)%3 ] = 'f';

		}

		if(repBoard.board[(act.a2-1)/3][(act.a2-1)%3] != ' '){
			freeTurn = true;
		}
		else{
			freeTurn = false;
		}
	}

	public void undoMove(Action act){
		repBoard.board[ (act.a1-1)/3 ][ (act.a1-1)%3 ] = ' ';

		winner = ' ';
		unSetPos(act);
		nextTurn();
	}

	public void nextTurn(){
		if(turn == 'x'){
			turn = 'o';
		}
		else{
			turn = 'x';
		}
	}
	public void setPos(Action act){	
		advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].board[(act.a2-1)/3 ][ (act.a2-1)%3 ] = turn;
	}
	public void unSetPos(Action act){
		advBoard[ (act.a1-1)/3 ][ (act.a1-1)%3 ].board[(act.a2-1)/3 ][ (act.a2-1)%3 ] = ' ';
	}



	public boolean finished(){
		if(repBoard.finished()){
			winner = repBoard.winner;
			return true;
		}
		return false;
	}

	public boolean full(){
		return repBoard.full();
	}

	public int evaluate(char player){
		int val = 0;

		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				val += advBoard[i][j].evaluate(player);
			}
		}
		return val;
	}

	
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