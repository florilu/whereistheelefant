import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Sprite extends Rectangle2D.Double implements Drawable, Movable{

	private static final long serialVersionUID = 1L;
	long delay;
	long animation = 0;
	Main parent;
	BufferedImage[] pics;
	int currentpic = 0;
	
	protected double dx;
	protected double dy;
	
	int loop_from;
	int loop_to;
	
	boolean remove;
	
	public Sprite(BufferedImage[] i, double x, double y, long delay, Main p){
		pics = i;
		this.x = x;
		this.y = y;
		this.delay = delay;
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		parent = p;
		loop_from = 0;
		loop_to = pics.length - 1;
	}
	
	@Override
	public void doLogic(long delta) {
		animation += (delta/1000000);
		if(animation > delay){
			animation = 0;
			computeAnimation();
		}
	}

	@Override
	public void move(long delta) {
		if(dx!=0){
			x += dx * (delta / 1e9);
		}
		
		if(dy!=0){
			y += dy * (delta / 1e9);
		}
	}

	@Override
	public void drawObjects(Graphics g) {
		g.drawImage(pics[currentpic], (int)x, (int)y, null);
	}
	
	private void computeAnimation(){
		currentpic++;
		if(currentpic > loop_to){
			currentpic = loop_from;
		}
	}
	
	public void setLoop(int from, int to){
		loop_from = from;
		loop_to = to;
		currentpic = from;
	}
	
	public double getHorizontalSpeed(){
		return dx;
	}
	
	public void setHorizontalSpeed(double dx){
		this.dx = dx;
	}
	
	public double getVerticalSpeed(){
		return dy;
	}
	
	public void setVerticalSpeed(double dy){
		this.dy = dy;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getY(){
		return y;
	}
	
	public double getX(){
		return x;
	}
	
	public abstract boolean collidedWith(Sprite s);
	
	
}