package connect;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class Bot {
	
	private byte color;
	private int depth;
	
	public Bot(byte color, int depth) {
		this.color = color;
		this.depth = depth;
	}
	
	public byte getColor() {
		return color;
	}
	
	public int move(Board board) {
		ArrayList<Double> values = update(board);
		int highest = -1;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) != null) {
				if (highest < 0) {
					highest = i;
				}
				if (values.get(i) >= values.get(highest)) {
					highest = i;
				}
			}
		}
		return highest;
	}
	
	private ArrayList<Double> update(Board board) {	
		ArrayList<Double> values = new ArrayList<Double>(); 
		Node tree = new Node(board);
		tree.generateChildren();
		values.clear();
		for (int i = 0; i < tree.childCount(); i++) {
			values.add(0.1);
		}
		ForkJoinPool pool = new ForkJoinPool();	
		int count = 0;
		int index = (tree.childCount() / 2);
		while (count < tree.childCount()) {
			Node child = tree.getChild(index);
			
			if (child != null) {
				values.set(index, pool.invoke(
						new Minimax(child, color, 1, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false)));
			}
			else {
				values.set(index, null);
			}
			count++;
			if (count % 2 == 0) {
				index += count;
			} 
			else {
				index -= count;
			}
		}
		pool.shutdown();
		return values;
	}
	
}
