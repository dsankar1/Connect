package connect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class Game extends Thread {

	private Rectangle[] columns;
	private Rectangle mouse;
	private Board board;
	private Bot bot;
	
	public Game(Board board, Bot bot) {
		columns = new Rectangle[board.columns];
		mouse = new Rectangle(1, 1);
		this.board = board;
		this.bot = bot;
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new Rectangle((i * 100), 20, 
					100, (board.rows * 100) + 20);
		}
	}
	
	@Override
	public void run() {
		while (!board.isOver()) {
			if (bot != null && board.currentPlayer == bot.getColor()) {
				int column = bot.move(board);
				board.dropPiece(column);
			}
		}
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		renderBoard(g2d);
		if (!board.isOver()) {
			highlightMove(g2d);
		} else {
			gameOver(g2d);
		}
	}
	
	private void gameOver(Graphics2D g2d) {
		if (board.isDraw()) {
			g2d.setColor(new Color(1, 1, 0, .2f));
		}
		else {
			if (board.currentPlayer == Board.RED) {
				g2d.setColor(new Color(0, 0, 0, .2f));
			}
			else {
				g2d.setColor(new Color(1, 0, 0, .2f));
			}
		}
		g2d.fillRect(-1, 0, board.columns * 100, board.rows * 100 + 30);
	}

	private void highlightMove(Graphics2D g2d) {
		if (bot == null || board.currentPlayer != bot.getColor()) {
			for (int i = 0; i < columns.length; i++) {
				Rectangle temp = columns[i];
				if (mouse.intersects(temp)) {
					if (board.currentPlayer == Board.RED) {
						g2d.setColor(new Color(1, 0, 0, .3f));
					} else {
						g2d.setColor(new Color(0, 0, 0, .3f));
					}
					int y = board.columnSpace[i];
					g2d.fillOval(temp.x + 10, y * 100 + 40, temp.width - 20, 80);
				}
			}
		}
	}
	
	private void renderBoard(Graphics2D g2d) {
		int x = 10, y = 40;
		for (int i = 0; i < board.rows; i++) {
			for (int j = 0; j < board.columns; j++) {
				if (board.get(i, j) == Board.RED) {
					g2d.setColor(Color.RED);
				} else if (board.get(i, j) == Board.BLACK) {
					g2d.setColor(Color.BLACK);
				} else {
					g2d.setColor(Color.getHSBColor(.15f, .2f, 1f));
				}
				g2d.fillOval(x, y, 80, 80);
				x += 100;
			}
			x = 10;
			y += 100;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (bot == null || board.currentPlayer != bot.getColor()) {
			for (int i = 0; i < columns.length; i++) {
				Rectangle temp = columns[i];
				if (mouse.intersects(temp)) {
					board.dropPiece(i);
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		mouse.x = e.getX() - 2;
		mouse.y = e.getY() - 38;
	}

	public void mouseExited(MouseEvent e) {
		mouse.x = -1;
		mouse.y = -1;
	}

	public void mouseEntered(MouseEvent e) {
		mouse.x = e.getX() - 2;
		mouse.y = e.getY() - 38;
	}

	public void mouseDragged(MouseEvent e) {
		mouse.x = e.getX() - 2;
		mouse.y = e.getY() - 38;
	}

	public void mousePressed(MouseEvent e) {
		mouse.x = e.getX() - 2;
		mouse.y = e.getY() - 38;
	}

}
