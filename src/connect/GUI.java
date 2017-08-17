package connect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class GUI extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	private Game game;
	public static final String[] OPTIONS = {"Yes", "No", "Cancel"};
	public static final String[] OK = {"OK"};
	private JFrame frame;
	private int columns, rows, connect;
	private String exit_command, new_game_command;
	private ButtonGroup modeGroup, depthGroup;
	private static JRadioButtonMenuItem HvH, HvR, RvH;
	private ArrayList<JRadioButtonMenuItem> depths;

	public GUI(Game game) {
		this.game = game;
		columns = game.getBoard().columns;
		rows = game.getBoard().rows;
		connect = game.getBoard().connect;
		setBackground(Color.getHSBColor(.15f, 1f, .9f));
		setLayout(new BorderLayout());
		createMenuStuff();
		createWindow();
	}
	
	private void createWindow() {
		ImageIcon img = new ImageIcon("four.png");
		frame = new JFrame();
		frame.setIconImage(img.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(100 * columns + 7, 100 * rows + 70);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.setVisible(true);
	}
	
	private void createMenuStuff() {
		JMenuBar menubar = new JMenuBar();
		JMenu file, options, mode, depth;
		JMenuItem newGame, exit;
		
		file = new JMenu("File");
		newGame = new JMenuItem("New Game");
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newGame.addActionListener(this);
		new_game_command = newGame.getActionCommand();
		
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		exit.addActionListener(this);
		exit_command = exit.getActionCommand();
		
		file.add(newGame);
		file.addSeparator();
		file.add(exit);
		file.setMnemonic(KeyEvent.VK_F);
		
		options = new JMenu("Options");		
		mode = new JMenu("Select Mode");
		depth = new JMenu("Select Depth");
		
		modeGroup = new ButtonGroup();
		HvH = new JRadioButtonMenuItem("Human vs. Human");
		HvR = new JRadioButtonMenuItem("Human vs. Bot");
		RvH = new JRadioButtonMenuItem("Bot vs. Human");
		HvR.setSelected(true);
		modeGroup.add(HvH);
		modeGroup.add(HvR);
		modeGroup.add(RvH);
		
		mode.add(HvH);
		mode.add(HvR);
		mode.add(RvH);
		mode.setMnemonic(KeyEvent.VK_S);
		
		depthGroup = new ButtonGroup();
		depths = new ArrayList<JRadioButtonMenuItem>();
		depths.add(new JRadioButtonMenuItem("4"));
		depths.add(new JRadioButtonMenuItem("6"));
		depths.add(new JRadioButtonMenuItem("8"));
		for (int i = 0; i < depths.size(); i++) {
			depthGroup.add(depths.get(i));
			depth.add(depths.get(i));
		}
		depth.setMnemonic(KeyEvent.VK_S);
		depths.get(depths.size() - 1).setSelected(true);
		
		options.add(mode);
		options.addSeparator();
		options.add(depth);
		options.setMnemonic(KeyEvent.VK_O);
		
		menubar.add(file);
		menubar.add(options);
		add(menubar, BorderLayout.NORTH);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		game.render(g2d);
		repaint();
	}

	private void exit() {
		int playerOption = JOptionPane.showOptionDialog(this, "Would you like to Quit?", null, JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, GUI.OPTIONS, null);
		if (playerOption == 0) {
			System.exit(0);
		}
	}
	
	private void newGame() {
		int depth = 0;
		for (JRadioButtonMenuItem temp : depths) {
			if (temp.isSelected()) {
				depth = Integer.parseInt(temp.getText());
			}
		}
		Bot bot = null;
		if (HvR.isSelected()) {
			bot = new Bot(Board.BLACK, depth);
		}
		else if (RvH.isSelected()) {
			bot = new Bot(Board.RED, depth);
		}
		Board board = new Board(rows, columns, connect);
		game = new Game(board, bot);
		game.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(exit_command)) {
			exit();
		}
		if (e.getActionCommand().equals(new_game_command)) {
			newGame();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		game.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		game.mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		game.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.mousePressed(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		game.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		game.mouseExited(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//stub
	}
	
}
