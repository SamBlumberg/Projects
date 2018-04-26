import java.util.ArrayList;
import java.util.List;

//class representing a simple 3x3 tic tac toe board
public class Board{
	public char[][] board;
	public char turn;
	public char winner;

	//constructor
	public Board(){
		turn = 'x';

		board = new char[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				board[i][j] = ' ';
			}
		}
	}

	//print board
	public void printBoard(){
		System.err.println("-------------");
		for(int i = 0; i<3; i++){
			System.err.print("|");
			for(int j = 0; j<3; j++){
				System.err.print(" " + Character.toUpperCase(board[i][j]) + " |");
			}
			System.err.println("\n-------------");
		}
	}

	//change whos turn it is
	public void nextTurn(){
		if(turn == 'x'){
			turn = 'o';
		}
		else{
			turn = 'x';
		}
	}


    //Returns valid actions for this state
    public List<Integer> getAction(){
        List<Integer> action = new ArrayList<>();
        for(int i = 0; i <board.length; i++){
            for(int j = 0; j<board.length; j++){
                if(board[i][j] == ' '){
                    int ac = 3*i + j + 1;
                    action.add(ac);
                }
            }
        }
        return action;
    }

    //check if somebody has won the game
	public boolean finished(){
		boolean test = false;

		//check for horizontal winner
		for(int i = 0; i<3; i++){
			if(board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' '){
				winner = board[i][0];
				return true;
			}
		}

		//check for vertical winner
		for(int i = 0; i<3; i++){
			if(board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' '){
				winner = board[0][i];
				return true;
			}
		}
		//check for diagonal winner
		if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' '){
			winner = board[0][0];
			return true;
		}
		//check for diagonal winner
		if(board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[2][0] != ' '){
			winner = board[2][0];
			return true;
		}

		return false;
	}

	//check if board is full
	public boolean full(){
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(board[i][j] == ' '){
					return false;
				}
			}
		}
		return true;
	}

	//apply a move to the board
	public void makeMove(int move){
			setPos(move);
			nextTurn();
	}
	//create a copy of the board
	public Board copy(){
		Board copy = new Board();
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				copy.board[i][j] = this.board[i][j];
			}
		}
		copy.turn = this.turn;
		return copy;
	}
	//return value at a position in the board
	public int getPos(int pos){
		return board[ (pos-1)/3 ][ (pos-1)%3 ];
	}
	//set value at a position in the board
	public void setPos(int pos){
		board[ (pos-1)/3 ][ (pos-1)%3 ] = turn;
	}

}
