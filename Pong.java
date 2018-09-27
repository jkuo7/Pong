import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Pong extends JPanel{

	private static final int GAMEWIDTH = 800;
	private static final int GAMEHEIGHT = 600;
	private static final int PIXELSIZE = 16;
	private boolean gameNotStart = true;
	private boolean gameOver = false;
	private boolean flicker = false;

	private static int maxScore = 11;
	private int score1 = 0;
	private int score2 = 0;

	PongBall ball = new PongBall(this);
	PongRacquet racquet1 = new PongRacquet(this, 1);
	PongRacquet racquet2 = new PongRacquet(this, 2);
	private Timer timer;

	public Pong(){
		this.setBackground(Color.BLACK);
		bindKeyStrokes();

		timer = new Timer(750, new ActionListener(){
			@Override
  			public void actionPerformed(ActionEvent e) {
	 			repaint();
  			}
		});
		timer.start();
	}

	private void bindKeyStrokes(){
		bindKeyStrokeTo("2.up.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), pressAc(racquet2, true));
        bindKeyStrokeTo("2.up.released", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), releaseAc(racquet2));
        bindKeyStrokeTo("2.down.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), pressAc(racquet2, false));
        bindKeyStrokeTo("2.down.released", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), releaseAc(racquet2));

        bindKeyStrokeTo("1.up.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), pressAc(racquet1, true));
        bindKeyStrokeTo("1.up.released", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), releaseAc(racquet1));
        bindKeyStrokeTo("1.down.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), pressAc(racquet1, false));
        bindKeyStrokeTo("1.down.released", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), releaseAc(racquet1));

        bindKeyStrokeTo("enter.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), startGame());
	}

    private void bindKeyStrokeTo(String name, KeyStroke keyStroke, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(keyStroke, name);
        am.put(name, action);
    }

    private Action releaseAc(PongRacquet racquet){
    	return new AbstractAction(){
        	@Override
        	public void actionPerformed(ActionEvent ae){
        		racquet.keyReleased();
        	}
        };
    }
   
    private Action pressAc(PongRacquet racquet, boolean up){
    	return new AbstractAction(){
        	@Override
        	public void actionPerformed(ActionEvent ae){
        		racquet.keyPressed(up);
        	}
        };
    }

    private Action startGame(){
    	return new AbstractAction(){
        	@Override
        	public void actionPerformed(ActionEvent ae){
				if(gameNotStart){
					gameNotStart = false;

					timer = new Timer(10, new ActionListener(){
						@Override
  						public void actionPerformed(ActionEvent e) {
							move();
	 						repaint();
	 						if(gameOver){
	 							gameOver();
	 						}
  						}
					});
					timer.start();
				}
        	}
        };
	}

    public Dimension getPreferredSize() {
        return new Dimension(GAMEWIDTH, GAMEHEIGHT);
    }

    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g2d.setColor(Color.WHITE);
    	ball.paint(g2d);
		racquet1.paint(g2d);
		racquet2.paint(g2d);

		for (int i=0; i<GAMEHEIGHT/PIXELSIZE; i+=2){
			g2d.fillRect(GAMEWIDTH/2 - PIXELSIZE/2, PIXELSIZE*i, PIXELSIZE, PIXELSIZE);
		}

		g2d.fillRect(0, 0, GAMEWIDTH, PIXELSIZE);
		g2d.fillRect(0, GAMEHEIGHT - PIXELSIZE, GAMEWIDTH, PIXELSIZE);

		g2d.setFont(new Font("Consolas", Font.BOLD, 60));
		FontMetrics fm = g2d.getFontMetrics();
		g2d.drawString(String.valueOf(score1), GAMEWIDTH/2 - fm.stringWidth(Integer.toString(score1)) - 70, 60);
		g2d.drawString(String.valueOf(score2), GAMEWIDTH/2 + 70, 60);
		//72 font about 96 pixels, 60 font about 80

		if(gameNotStart){
			if(!flicker){
				g2d.setFont(new Font("Consolas", Font.BOLD, 30));
				fm = g2d.getFontMetrics();
				g2d.drawString("P1: W/S to move up/down", (GAMEWIDTH - fm.stringWidth("P1: W/S to move up/down"))/2, (GAMEHEIGHT - fm.getHeight()) / 2 + fm.getAscent() - 50);
				g2d.drawString("P2: UP/DOWN to move up/down", (GAMEWIDTH - fm.stringWidth("P2: UP/DOWN to move up/down"))/2, (GAMEHEIGHT - fm.getHeight()) / 2 + fm.getAscent());
				g2d.drawString("-Press Enter to Start-", (GAMEWIDTH - fm.stringWidth("-Press Enter to Start-"))/2, (GAMEHEIGHT - fm.getHeight()) / 2 + fm.getAscent() + 50);
			}
			flicker = !flicker;
		}		
    }

	private void move(){
		//moves the ball and racquets
		ball.move();
		racquet1.move();
		racquet2.move();
	}

	public void leftScores(){
		//called when ball exits on right side
		score1++;
		gameOver = score1 >= maxScore;
	}

	public void rightScores(){
		//called when ball exits on left side
		score2++;
		gameOver = score2 >= maxScore;
	}

	public int height(){
		return GAMEHEIGHT;
	}

	public int width(){
		return GAMEWIDTH;
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI(){
		JFrame frame = new JFrame("Pong");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Pong pong = new Pong();
		frame.getContentPane().add(pong);
		frame.pack();
		frame.setVisible(true);
	}

	private void gameOver(){
		//Displays winner
		String winner = "";
		if (score1 >= maxScore){
			winner = "Player 1";
		}
		else{
			winner = "Player 2";
		}
		JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}


}