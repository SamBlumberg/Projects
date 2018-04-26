
//class representing an action
public class Action{
	//a1 is large board, a2 is small board
	int a1;
	int a2;

	//constructor
	public Action(int a1, int a2){
		this.a1 = a1;
		this.a2 = a2;
	}
	//copy another actions values
	public void set(Action act){
		this.a1 = act.a1;
		this.a2 = act.a2;
	}
	//print the action
	public void printAction(){
		System.err.print("[" + a1 + ", " + a2 + "] ");
	}

}