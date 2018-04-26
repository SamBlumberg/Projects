
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

	//check if a player has won the board
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
	//evalutation function for small board
	//checks all 8 possible ways to win
	//adds 0 if nobody can win 
	//adds 1 or -1 if there is a x/o and 2 spaces
	//adds 5 or -5 if there is 2 x/o and 1 space
	public int evaluate(char player){

		int val = 0;

		//check horizontals
		for(int i = 0; i<3; i++){
			int playerCount = 0;
			int oppCount = 0;
			int blankCount = 0;
			for(int j = 0; j<3; j++){
				if(board[i][j] == player){
					playerCount++;
				}
				else if (board[i][j] == ' ') {
					blankCount++;
				}
				else {
					oppCount++;
				}
			}
			if(blankCount == 1){
				if(playerCount == 2){
					val += 5;
				}
				if(oppCount == 2){
					val += -5;
				}
			}
			else if(blankCount == 2){
				if(playerCount == 1){
					val += 1;
				}
				else{
					val += -1;
				}
			}
		}

		//check verticals
		for(int i = 0; i<3; i++){
			int playerCount = 0;
			int oppCount = 0;
			int blankCount = 0;
			for(int j = 0; j<3; j++){
				if(board[j][i] == player){
					playerCount++;
				}
				else if (board[j][i] == ' ') {
					blankCount++;
				}
				else {
					oppCount++;
				}
			}
			if(blankCount == 1){
				if(playerCount == 2){
					val += 3;
				}
				if(oppCount == 2){
					val += -3;
				}
			}
			else if(blankCount == 2){
				if(playerCount == 1){
					val += 1;
				}
				else{
					val += -1;
				}
			}
		}
		//check for diagonal down 
		for(int i = 0; i<3; i++){
			int playerCount = 0;
			int oppCount = 0;
			int blankCount = 0;
			for(int j = 0; j<3; j++){
				if(board[j][i] == player && i == j){
					playerCount++;
				}
				else if (board[j][i] == ' ' && i == j) {
					blankCount++;
				}
				else {
					oppCount++;
				}
			}
			if(blankCount == 1){
				if(playerCount == 2){
					val += 3;
				}
				if(oppCount == 2){
					val -= 3;
				}
			}
			else if(blankCount == 2){
				if(playerCount == 1){
					val += 1;
				}
				else{
					val -= 1;
				}
			}
		}
		//check up diagonal
		int playerCount = 0;
		int oppCount = 0;
		int blankCount = 0;

		if(board[2][0] == ' '){
			blankCount++;
		}
		else if(board[2][0] == player){
			playerCount++;
		}
		else{
			oppCount++;
		}
		if(board[0][0] == ' '){
			blankCount++;
		}
		else if(board[0][0] == player){
			playerCount++;
		}
		else{
			oppCount++;
		}
		if(board[0][2] == ' '){
			blankCount++;
		}
		else if(board[0][2] == player){
			playerCount++;
		}
		else{
			oppCount++;
		}
		if(blankCount == 1){
			if(playerCount == 2){
				val += 3;
			}
			if(oppCount == 2){
				val += -3;
			}
		}
		else if(blankCount == 2){
			if(playerCount == 1){
				val += 1;
			}
			else{
				val += -1;
			}
		}
		

		return val;
	}
	//check if the board is full
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

	//make a move on the board and advance the turn
	public void makeMove(int move){
		//int pos = getPos(move);
		//if(pos == ' '){
		setPos(move);
		nextTurn();
		//}
	}

	//create a new copy of this board
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