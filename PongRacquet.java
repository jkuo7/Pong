import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class PongRacquet{
	private static final int WIDTH = 8;
	private static final int HEIGHT = 60;
	private static final int PIXELSIZE = 16;
	private int y;
	private int dy = 0;
	private int x = 40;
	private Pong game;
	
	public PongRacquet(Pong game, int player){
		this.game = game;
		y = game.height()/2 - HEIGHT/2;
		if (player == 2){
			x = game.width() - x - WIDTH;
		}
	}
	
	public void move(){
		if (y + dy >= PIXELSIZE && y + dy <= game.getHeight() - HEIGHT - PIXELSIZE){
			y += dy;
		}
	}

	public void paint(Graphics2D g){
		g.fillRect(x, y, WIDTH, HEIGHT);
	}

	public void keyReleased(){
		dy = 0;
	}

	public void keyPressed(boolean up){
		if(up){
			dy = -2;
		}else{
			dy = 2;
		}

	}

	public Rectangle getBounds(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public int getRightX(){
		return x + WIDTH;
	}

	public int getLeftX(){
		return x;
	}

}