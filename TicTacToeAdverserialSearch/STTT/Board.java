

public class Board{
	public char[][] board;
	public char turn;
	public char winner;

	public Board(){
		turn = 'x';

		board = new char[3][3];
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				board[i][j] = ' ';
			}
		}
	}

	public void printBoard(){
		System.err.println("-------------");
		for(int i = 0; i<3; i++){
			System.out.print("|");
			for(int j = 0; j<3; j++){
				System.err.print(" " + Character.toUpperCase(board[i][j]) + " |");
			}
			System.err.println("\n-------------");
		}
	}

	public void nextTurn(){
		if(turn == 'x'){
			turn = 'o';
		}
		else{
			turn = 'x';
		}
	}

	public int[] getActions(){
		
		int index = 0;
		int len = 0;

		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(board[i][j] == ' '){
					//actions[index] = 3*i + j + 1;
					len++;
				}
			}
		}
		int[] actions = new int[len];

		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				if(board[i][j] == ' '){
					actions[index] = 3*i + j + 1;
					index++;
				}
			}
		}

		return actions;
	}

	public boolean finished(){
		boolean test = false;

		//check for horizontal winner
		for(int i = 0; i<3; i++){
			if(board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' '&& board[i][0] != 'f'){
				winner = board[i][0];
				return true;
			}
		}

		//check for vertical winner
		for(int i = 0; i<3; i++){
			if(board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' '&& board[0][i] != 'f'){
				winner = board[0][i];
				return true;
			}
		}

		if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' '&& board[0][0] != 'f'){
			//System.out.println("winner");
			winner = board[0][0];
			return true;
		}

		if(board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[2][0] != ' '&& board[2][0] != 'f'){
			winner = board[2][0];
			return true;
		}

		return false;
	}
	public int evaluate(char player){

		int val = 0;

		//check for horizontal
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

		//check for vertical 
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

	public void makeMove(int move){
		//int pos = getPos(move);
		//if(pos == ' '){
		setPos(move);
		nextTurn();
		//}
	}

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

	public int getPos(int pos){
		return board[ (pos-1)/3 ][ (pos-1)%3 ];
	}

	public void setPos(int pos){
		board[ (pos-1)/3 ][ (pos-1)%3 ] = turn;
	}

}