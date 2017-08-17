package connect;

public class Coordinate {
	
	public int x, y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isOdd() {
		if (y % 2 != 0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
