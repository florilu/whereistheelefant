import java.awt.image.BufferedImage;

public class Elefant extends Sprite{
	private static final long serialVersionUID = 1L;
	
	public Elefant(BufferedImage[] i, double x, double y, long delay, Main p){
		super(i, x, y, delay, p);
	}

	@Override
	public boolean collidedWith(Sprite s) {
		// TODO Auto-generated method stub
		return false;
	}

	
}