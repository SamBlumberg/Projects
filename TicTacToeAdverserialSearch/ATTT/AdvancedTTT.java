import java.util.Scanner;

//class with main method
public class AdvancedTTT{

  public static void main(String[] args){

    Scanner s = new Scanner(System.in);
    System.err.println("Do you want to play as 'X' or 'O'?");
    String humanPlayer = s.nextLine().toLowerCase();

    while(!humanPlayer.equals("o") && !humanPlayer.equals("x")){
        System.err.println("========= Please enter 'X' or 'O' =========");
        humanPlayer = s.nextLine().toLowerCase();
    }

    if(humanPlayer.equals("x")){
        Game game = new Game('o', 'x');
        game.playGame();
    }
    else{
        Game game = new Game('x', 'o');
        game.playGame();
    }

}

}
