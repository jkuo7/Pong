import java.awt.Graphics2D;
import java.awt.Rectangle;

public class PongBall{
	private static final int DIAMETER = 16;
	private static final int PIXELSIZE = 16;
	private int x;
	private int y;
	private int dx = 1;
	private int dy = 1;
	private Pong game;

	public PongBall(Pong game){
		this.game = game;
		center();
		if (Math.random() < 0.5){
			dx *= -1;
		}
	}

	private void center(){
		//puts ball in center of screen
		x = game.width()/2 - DIAMETER/2;
		y = game.height()/2 - DIAMETER/2;
		dy = (int)(Math.random()*3 + 1);
		if (Math.random() < 0.5){
			dy *= -1;
		}
	}

	public void move(){
		if(x + DIAMETER + 2 < 0){
			game.rightScores();
			center();
		}
		if(x - 2 > game.getWidth()){
			game.leftScores();
			center();
		}
		if(y + dy < PIXELSIZE || y + dy > game.getHeight() - PIXELSIZE - DIAMETER){
			dy *= -1;
		}
		if(collision(game.racquet1)){
			dx *= -1;
			if(dx>0){
				x = game.racquet1.getRightX();
			}
			if(dx<0){
				x = game.racquet1.getLeftX() - DIAMETER;
			}
		}
		if(collision(game.racquet2)){
			dx *= -1;
			if(dx<0){
				x = game.racquet2.getLeftX() - DIAMETER;
			}
			if(dx>0){
				x = game.racquet2.getRightX();
			}
		}
		x += dx;
		y += dy;
	}

	public void paint(Graphics2D g){
		g.fillRect(x, y, DIAMETER, DIAMETER);
	}

	public Rectangle getBounds(){
		return new Rectangle(x, y, DIAMETER, DIAMETER);
	}
	
	private boolean collision(PongRacquet racquet){
		//tests if ball has collided with given racquet
		return getBounds().intersects(racquet.getBounds());	
	}

}