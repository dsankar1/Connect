package connect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Board {

	public static final byte RED = 1, BLACK = 2, EMPTY = 0;
	public static final int P2 = 0, T2 = 1, P3 = 2, T3 = 3, P4 = 4, T4 = 5, ARRAY_SIZE = 6;
	public int rows, columns, connect;
	public byte[][] board;
	public int[] columnSpace;
	public boolean won = false;
	public byte winner = -2;
	public byte currentPlayer = RED;
	public int piecesPlaced = 0;
	
	public Board(int rows, int columns, int connect) {
		this.rows = rows;
		this.columns = columns;
		this.connect = connect;
		
		board = new byte[rows][columns];
		columnSpace = new int[columns];
		Arrays.fill(columnSpace, (rows-1));
	}
	
	public void setBoard(byte[][] board) {
		this.board = board;
	}
	
	public Board getCopy() {
		Board copy = new Board(rows, columns, connect);
		copy.won = won;
		copy.winner = winner;
		copy.piecesPlaced = piecesPlaced;
		copy.currentPlayer = currentPlayer;
		for (int i = 0; i < rows; i++) {
			copy.board[i] = board[i].clone();
		}
		copy.columnSpace = columnSpace.clone();
		return copy;
	}
	
	public void display() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public synchronized void dropPiece(int column) {
		if (!won) {
			if (isValidMove(column)) {
				int row = columnSpace[column];
				board[row][column] = currentPlayer;
				columnSpace[column]--;
				piecesPlaced++;
				
				if (won = checkForWin(row, column, currentPlayer)) {
					winner = currentPlayer;
				}
				nextPlayer();
			}
		}
	}
	
	public void nextPlayer() {
		if (currentPlayer == Board.RED) {
			currentPlayer = Board.BLACK;
		}
		else {
			currentPlayer = Board.RED;
		}
	}
	
	public boolean isValidMove(int column) {
		int row = columnSpace[column];
		if (row >= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public byte winnerColor() {
		return winner;
	}
	
	public byte get(int row, int column) {
		return board[row][column];
	}
	
	public synchronized boolean isOver() {
		return (isWon() || isDraw());
	}
	
	public boolean isWon() {
		return won;
	}
	
	public boolean isDraw() {
		if ((piecesPlaced == (rows * columns)) && !won) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean playable(int x, int y) {
		if (board[y][x] == EMPTY) {
			if (y == (rows - 1)) {
				return true;
			}
			else if (board[y + 1][x] != EMPTY) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public byte[][] getBoard() {
		return board;
	}
	
	public boolean checkForWin(int row, int column, byte color) {
		return (checkRow(row, column, color) || checkColumn(row, column, color)
				|| checkUpDiagonal(row, column, color) || checkDownDiagonal(row, column, color));
	}
	
	private boolean checkRow(int row, int column, byte color) {
		int count = 0;
		for (int i = 0; i < columns; i++) {
			if (board[row][i] == color) {
				count++;
			}
			else {
				count = 0;
			}
			
			if (count == connect) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkColumn(int row, int column, byte color) {
		int count = 0;
		for (int i = 0; i < rows; i++) {
			if (board[i][column] == color) {
				count++;
			}
			else {
				count = 0;
			}
			
			if (count == connect) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkUpDiagonal(int row, int column, byte color) {
		int x = column, y = row;
		while (x > 0 && y < rows - 1) {
			x--;
			y++;
		}
		
		int count = 0;
		while (x < columns && y >= 0) {
			if (board[y][x] == color) {
				count++;
			}
			else {
				count = 0;
			}
			if (count == connect) {
				return true;
			}
			x++;
			y--;
		}
		return false;
	}
	
	private boolean checkDownDiagonal(int row, int column, byte color) {
		int x = column, y = row;
		while (x < columns - 1 && y < rows - 1) {
			x++;
			y++;
		}
		
		int count = 0;
		while (x >= 0 && y >= 0) {
			if (board[y][x] == color) {
				count++;
			}
			else {
				count = 0;
			}
			if (count == connect) {
				return true;
			}
			x--;
			y--;
		}
		return false;
	}
	
	private byte[] getRow(int rowIndex) {
		return board[rowIndex];
	}
	
	
	public byte[] getColumn(int columnIndex) {
		byte[] column = new byte[rows];
		for (int i = (rows - 1); i >= 0; i--) {
			column[(rows - 1) - i] = board[i][columnIndex];
		}
		return column;
	}
	
	private byte[] getAscendingDiagonal(int x, int y) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		while (x < columns && y >= 0) {
			list.add(board[y][x]);
			x++;
			y--;
		}
		byte[] diagonal = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			diagonal[i] = list.get(i);
		}
		return diagonal;
	}
	
	private byte[] getDescendingDiagonal(int x, int y) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		while (x < columns && y < rows) {
			list.add(board[y][x]);
			x++;
			y++;
		}
		byte[] diagonal = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			diagonal[i] = list.get(i);
		}
		return diagonal;
	}
	
	public ArrayList<Coordinate> threats(byte color, boolean playable) {
		ArrayList<Coordinate> row = rowThreatsLocal(color);
		ArrayList<Coordinate> column = columnThreatsLocal(color);
		ArrayList<Coordinate> diagonalUp = diagonalUpThreatsLocal(color);
		ArrayList<Coordinate> diagonalDown = diagonalDownThreatsLocal(color);
		
		ArrayList<Coordinate> temp = new ArrayList<Coordinate>();
		temp.addAll(row);
		temp.addAll(column);
		temp.addAll(diagonalUp);
		temp.addAll(diagonalDown);
		
		ArrayList<Coordinate> total = new ArrayList<Coordinate>();
		for (int i = 0; i < temp.size(); i++) {
			Coordinate threat = temp.get(i);
			if (playable) {
				if (playable(threat.x, threat.y)) {
					total.add(threat);
				}
			}
			else {
				if (!playable(threat.x, threat.y)) {
					total.add(threat);
				}
			}
		}
		return total;
	}
	
	private ArrayList<Coordinate> rowThreatsLocal(byte color) {
		ArrayList<Coordinate> threats = new ArrayList<Coordinate>();
		for (int i = 0; i < rows; i++) {
			byte[] row = getRow(i);
			HashSet<Integer> indices = scanForThreats(row, color);
			for (Integer index : indices) {
				int x = index, y = i;
				Coordinate threat = new Coordinate(x, y);
				threats.add(threat);
			}
		}
		return threats;
	}
	
	private ArrayList<Coordinate> columnThreatsLocal(byte color) {
		ArrayList<Coordinate> threats = new ArrayList<Coordinate>();
		for (int i = 0; i < columns; i++) {
			byte[] column = getColumn(i);
			HashSet<Integer> indices = scanForThreats(column, color);
			for (Integer index : indices) {
				int x = i, y = index;
				Coordinate threat = new Coordinate(x, y);
				threats.add(threat);
			}
		}
		return threats;
	}
	
	private ArrayList<Coordinate> diagonalUpThreatsLocal(byte color) {
		ArrayList<Coordinate> threats = new ArrayList<Coordinate>();
		int x = 0, y = connect - 1;
		while (x <= columns - connect) {
			byte[] diagonal = getAscendingDiagonal(x, y);
			HashSet<Integer> indices = scanForThreats(diagonal, color);
			for (Integer index : indices) {
				int x2 = (x + index), y2 = (y - index);
				Coordinate threat = new Coordinate(x2, y2);
				threats.add(threat);
			}
			
			if (y < rows - 1) {
				y++;
			}
			else {
				x++;
			}
		}
		return threats;
	}
	
	private ArrayList<Coordinate> diagonalDownThreatsLocal(byte color) {
		ArrayList<Coordinate> threats = new ArrayList<Coordinate>();
		int x = (columns - connect), y = 0;
		while (y <= rows - connect) {
			byte[] diagonal = getDescendingDiagonal(x, y);
			HashSet<Integer> indices = scanForThreats(diagonal, color);
			for (Integer index : indices) {
				int x2 = (x + index), y2 = (y + index);
				Coordinate threat = new Coordinate(x2, y2);
				threats.add(threat);
			}
			if (x > 0) {
				x--;
			}
			else {
				y++;
			}
		}
		return threats;
	}
	
	private HashSet<Integer> scanForThreats(byte[] line, byte color) {
		HashSet<Integer> indices = new HashSet<Integer>();
		int count = 0, streak = 0, savedIndex = 0;
		boolean emptyUsed = false;
		for (int i = 0; i < line.length; i++) {
			if (line[i] == color) {
				count++;
				streak++;
			}
			else if (line[i] == EMPTY) {
				streak = 0;
				if (!emptyUsed && count > 0) {
					count++;
					savedIndex = i;
					emptyUsed = true;
				}
				else {
					count = 0;
					emptyUsed = false;
				}
			}
			else {
				count = 0;
				streak = 0;
				emptyUsed = false;
			}
			
			if (count == connect) {
				count = streak;
				emptyUsed = false;
				indices.add(savedIndex);
			}
		}
		
		count = 0; 
		streak = 0;
		savedIndex = 0;
		emptyUsed = false;
		for (int i = line.length - 1; i >= 0; i--) {
			if (line[i] == color) {
				count++;
				streak++;
			}
			else if (line[i] == EMPTY) {
				streak = 0;
				if (!emptyUsed && count > 0) {
					count++;
					savedIndex = i;
					emptyUsed = true;
				}
				else {
					count = 0;
					emptyUsed = false;
				}
			}
			else {
				count = 0;
				streak = 0;
				emptyUsed = false;
			}
			
			if (count == connect) {
				count = streak;
				emptyUsed = false;
				indices.add(savedIndex);
			}
		}
		return indices;
	}
	
	
	public int[] potential(byte color) {
		int[] rows = potentialRowConnects(color);
		int[] columns = potentialColumnConnects(color);
		int[] diagonalUps = potentialDiagonalUpConnects(color);
		int[] diagonalDowns = potentialDiagonalDownConnects(color);
		
		int[] grandTotal = new int[ARRAY_SIZE];
		grandTotal[P2] = (rows[P2] + columns[P2] + diagonalUps[P2] + diagonalDowns[P2]);
		grandTotal[T2] = (rows[T2] + columns[T2] + diagonalUps[T2] + diagonalDowns[T2]);
		grandTotal[P3] = (rows[P3] + columns[P3] + diagonalUps[P3] + diagonalDowns[P3]);
		grandTotal[T3] = (rows[T3] + columns[T3] + diagonalUps[T3] + diagonalDowns[T3]);
		grandTotal[P4] = (rows[P4] + columns[P4] + diagonalUps[P4] + diagonalDowns[P4]);
		grandTotal[T4] = (rows[T4] + columns[T4] + diagonalUps[T4] + diagonalDowns[T4]);
		
		return grandTotal;
	}
	
	private int[] potentialRowConnects(byte color) {
		int[] total = new int[ARRAY_SIZE];
		for (int i = 0; i < rows; i++) {
			byte[] row = getRow(i);
			scanPotential(total, row, color);
		}
		return total;
	}
	
	private int[] potentialColumnConnects(byte color) {
		int[] total = new int[ARRAY_SIZE];
		for (int i = 0; i < columns; i++) {
			byte[] column = getColumn(i);
			scanPotential(total, column, color);
		}
		return total;
	}
	
	private int[] potentialDiagonalUpConnects(byte color) {
		int[] total = new int[ARRAY_SIZE];
		int x = 0, y = connect - 1;
		while (x <= columns - connect) {
			byte[] diagonal = getAscendingDiagonal(x, y);
			scanPotential(total, diagonal, color);
			if (y < rows - 1) {
				y++;
			}
			else {
				x++;
			}
		}
		return total;
	}
	
	private int[] potentialDiagonalDownConnects(byte color) {
		int[] total = new int[ARRAY_SIZE];
		int x = (columns - connect), y = 0;
		while (y <= rows - connect) {
			byte[] diagonal = getDescendingDiagonal(x, y);
			scanPotential(total, diagonal, color);
			if (x > 0) {
				x--;
			}
			else {
				y++;
			}
		}
		return total;
	}
	
	public void scanPotential(int[] total, byte[] line, byte color) {
		int count = 0;
		int countAll = 0;
		for (int i = 0; i < line.length; i++) {
			countAll++;
			if (line[i] == color || line[i] == EMPTY) {
				count++;
			}
			else {
				count = 0;
			}
			if (count >= connect) {
				total[P4]++;
			}
			if (count >= connect - 1) {
				total[P3]++;
			}
			if (count >= connect - 2) {
				total[P2]++;
			}
			if (countAll >= connect) {
				total[T4]++;
			}
			if (countAll >= connect - 1) {
				total[T3]++;
			}
			if (countAll >= connect - 2) {
				total[T2]++;
			}
		}
	}
	
}

