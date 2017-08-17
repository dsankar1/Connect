package connect;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class Minimax extends RecursiveTask<Double> {
	private static final long serialVersionUID = 1L;
	private final Node node;
	private final byte color;
	private final int depth;
	private final boolean max;
	private final int maxDepth;
	private double alpha, beta;
	
	public Minimax(Node node, byte color, int depth, int maxDepth, double alpha, double beta, boolean max) {
		this.node = node;
		this.color = color;
		this.depth = depth;
		this.maxDepth = maxDepth;
		this.alpha = alpha;
		this.beta = beta;
		this.max = max;
	}

	@Override
	protected Double compute() {
		if (depth < maxDepth) {
			node.generateChildren();
		}
		if (depth == maxDepth || node.isTerminal()) {
			return heuristic(node, color);
		}
		else {		
			if (max) {
				return maximize();
			}
			else {
				return minimize();
			}
		}
	}
	
	private Double maximize() {
		double heuristic = Integer.MIN_VALUE;
		int index = (node.childCount()/2);
		int count = 0;
		while (count < node.childCount()) {
			Node child = node.getChild(index);
			
			if (child != null) {
				Minimax subtask = new Minimax(child, color, depth + 1, maxDepth, alpha, beta, false);
				double temp = subtask.invoke();
				
				heuristic = Math.max(temp,  heuristic);
				alpha = Math.max(temp, alpha);
				
				if (alpha >= beta) {
					break;
				}
			}
			count++;
			if (count % 2 == 0) {
				index += count;
			}
			else {
				index -= count;
			}
		}
		return heuristic;
	}
	
	private Double minimize() {
		double heuristic = Integer.MAX_VALUE;
		int index = (node.childCount()/2);
		int count = 0;
		while (count < node.childCount()) {
			Node child = node.getChild(index);

			if (child != null) {
				Minimax subtask = new Minimax(child, color, depth + 1, maxDepth, alpha, beta, true);
				double temp = subtask.invoke();

				heuristic = Math.min(temp, heuristic);
				beta = Math.min(temp, beta);
				
				if (alpha >= beta) {
					break;
				}
			}
			count++;
			if (count % 2 == 0) {
				index += count;
			}
			else {
				index -= count;
			}
		}
		return heuristic;
	}
	
	public static double heuristic(Node node, byte color) {
		Board board = node.board;
		byte opponent = Board.RED;
		if (color == Board.RED) {
			opponent = Board.BLACK;
		}
		ArrayList<Coordinate> myEvens = new ArrayList<Coordinate>(),
				myOdds = new ArrayList<Coordinate>();
		ArrayList<Coordinate> oppEvens = new ArrayList<Coordinate>(),
				oppOdds = new ArrayList<Coordinate>();
		ArrayList<Coordinate> mpThreats = board.threats(color, true),
				mnpThreats = board.threats(color, false);
		ArrayList<Coordinate> opThreats = board.threats(opponent, true),
				onpThreats = board.threats(opponent, false);
		
		// HEURISTIC
		if (board.isWon()) {
			if (board.winnerColor() == opponent) {
				return Integer.MIN_VALUE;
			}
			else {
				return Integer.MAX_VALUE;
			}
		}
		if (board.isDraw()) {
			return 0;
		}
		if (board.currentPlayer == opponent && opThreats.size() > 0) {
			return Integer.MIN_VALUE;
		}
		if (board.currentPlayer == color && mpThreats.size() > 0) {
			return Integer.MAX_VALUE;
		}
		
		for (Coordinate threat : mnpThreats) {
			if (threat.isOdd()) {
				myOdds.add(threat);
			}
			else {
				myEvens.add(threat);
			}
		}
		for (Coordinate threat : onpThreats) {
			if (threat.isOdd()) {
				oppOdds.add(threat);
			}
			else {
				oppEvens.add(threat);
			}
		}
		
		double heuristic = 0;
		if (color == Board.RED) {
			if (myOdds.size() > 0 && oppOdds.size() == 0) {
				boolean sameColumn = false;
				int yOdd = -1, yEven = -1;
				for (int i = 0; i < myOdds.size(); i++) {
					for (int j = 0; j < oppEvens.size(); j++) {
						Coordinate even = oppEvens.get(j);
						Coordinate odd = myOdds.get(i);
						if (odd.x == even.x) {
							yOdd = odd.y;
							yEven = even.y;
							sameColumn = true;
						}
						
					}
				}
				if (sameColumn) {
					if (yOdd > yEven) {
						heuristic += 100;
					}
					else {
						heuristic -= 100;
					}
				}
				else {
					heuristic += 100;
				}
			}
			if (myOdds.size() > oppOdds.size() && oppEvens.size() == 0) {
				heuristic += 100;
			}
			if (oppEvens.size() > 0 && myOdds.size() == 0) {
				heuristic -= 100;
			}
		}
		else {
			if (myEvens.size() > 0 && oppOdds.size() > 0) {
				boolean sameColumn = false;
				int yOdd = -1, yEven = -1;
				for (int i = 0; i < oppOdds.size(); i++) {
					for (int j = 0; j < myEvens.size(); j++) {
						Coordinate even = myEvens.get(j);
						Coordinate odd = oppOdds.get(i);
						if (odd.x == even.x) {
							yOdd = odd.y;
							yEven = even.y;
							sameColumn = true;
						}
						
					}
				}
				if (sameColumn) {
					if (yOdd < yEven) {
						heuristic += 100;
					}
					else {
						heuristic -= 100;
					}
				}
				else {
					heuristic += 100;
				}
			}
			if (oppOdds.size() > myOdds.size() && myEvens.size() == 0) {
				heuristic -= 100;
			}
			if (myEvens.size() > 0 && oppOdds.size() == 0) {
				heuristic += 100;
			}
		}
		int[] myPotential = board.potential(color);
		int[] oppPotential = board.potential(opponent);
		
		heuristic += (((double)myPotential[Board.P4]/(double)myPotential[Board.T4]) * 10);
		heuristic -= (((double)oppPotential[Board.P4]/(double)oppPotential[Board.T4]) * 10);
		heuristic += (((double)myPotential[Board.P3]/(double)myPotential[Board.T3]));
		heuristic -= (((double)oppPotential[Board.P3]/(double)oppPotential[Board.T3]));
		
		return heuristic;
	}
	
}

