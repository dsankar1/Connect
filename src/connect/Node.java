package connect;

import java.util.ArrayList;

public class Node {
	
	public Board board;
	public ArrayList<Node> children;

	public Node(Board board) {
		this.board = board;
		children = new ArrayList<Node>();
	}

	public boolean isTerminal() {
		return (children.size() == 0);
	}

	public Node getChild(int index) {
		return children.get(index);
	}

	public void addChild(Node child) {
		children.add(child);
	}

	public int childCount() {
		return children.size();
	}
	
	public void generateChildren() {
		if (!board.isOver()) {
			children.clear();
			for (int i = 0; i < board.columns; i++) {
				if (board.isValidMove(i)) {
					Board temp = board.getCopy();
					temp.dropPiece(i);
					addChild(new Node(temp));
				}
				else {
					addChild(null);
				}
			}
		}	
	}

	public int size() {
		int size = 0;
		for (int i = 0; i < childCount(); i++) {
			Node child = getChild(i);
			if (child != null) {
				size += child.size();
			}
		}
		return size + 1;
	}

}

