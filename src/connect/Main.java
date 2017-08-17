package connect;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		try {
			int width = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the Width of the Rows:", null, JOptionPane.PLAIN_MESSAGE));
			int height = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the Height of the Columns:", null,
					JOptionPane.PLAIN_MESSAGE));
			int connect = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the Win Condition (4 for Connect4):",
					null, JOptionPane.PLAIN_MESSAGE));
			
			Board board = new Board(height, width, connect);
			Bot bot = new Bot(Board.BLACK, 8);
			Game game = new Game(board, bot);
			new GUI(game);
			game.start();
		}
		catch (NumberFormatException e) {
			System.exit(0);
		}
	}
	
}
