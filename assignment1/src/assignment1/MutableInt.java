package assignment1;

public class MutableInt {
	private int state;
	
	public MutableInt(int i) {
		this.state = i;
	}
	
	public int getInt(){
		return this.state;
	}
	
	public void setInt(int i){
		this.state = i;
	}
}
