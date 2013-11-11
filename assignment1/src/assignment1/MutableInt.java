package assignment1;
/**
 *  A mutable integer for AggregationParallel and AggregationRunnable.
 *  @author Jens Fredskov, Henrik Bendt
 *  
 */

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
