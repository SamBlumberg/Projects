

public class Action{
	int a1;
	int a2;

	public Action(int a1, int a2){
		this.a1 = a1;
		this.a2 = a2;
	}

	public void set(Action act){
		this.a1 = act.a1;
		this.a2 = act.a2;
	}

	public void printAction(){
		System.err.print("[" + a1 + ", " + a2 + "] ");
	}

}