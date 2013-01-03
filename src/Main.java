import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JPanel implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	JFrame frame;
	
	long delta = 0;
	long last = 0;
	long fps = 0;
	
	int count = 0;
	
	boolean remove;
	
	Elefant elefantPlayer;
	Cactus cactusPlayer;
	Vector<Sprite> actors;
	Vector<Sprite> painter;
	
	SoundLib soundLib;
	
	public static void main(String[] args){
		new Main(800, 600);
	}
	
	public Main(int width, int height){
		this.setPreferredSize(new Dimension(width, height));
		frame = new JFrame("Where is the Elefant");
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		frame.addKeyListener(this);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		
		doInitializations();
		
		Thread th = new Thread(this);
		th.start();
	}
	
	private void doInitializations(){
		last = System.nanoTime();
		
		BufferedImage[] elefant = loadPics("pics/elefant.gif", 1);
		BufferedImage[] cactus = loadPics("pics/cactus.gif", 1);
		
		actors = new Vector<Sprite>();
		painter = new Vector<Sprite>();
		elefantPlayer = new Elefant(elefant, 400, 300, 100, this);
		cactusPlayer = new Cactus(cactus, 400, 300, 100, this);
		actors.add(elefantPlayer);
		actors.add(cactusPlayer);
		
		soundLib = new SoundLib();
		soundLib.loadSound("music", "sound/music.wav");
		soundLib.loopSound("music");
	}

	private void computeDelta(){
		delta = System.nanoTime() - last;
		last = System.nanoTime();
		fps = ((long) 1e9) / delta;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(Color.red);
		g.drawString("Count: " + Long.toString(count), 750, 595);
		
		if(painter != null){
			for(ListIterator<Sprite> it = painter.listIterator(); it.hasNext();){
				Sprite r = it.next();
				r.drawObjects(g);
			}
		}
	}
	
	@Override
	public void run() {
		while(frame.isVisible()){
			
			computeDelta();
			
			checkKeys();
			doLogic();
			moveObjects();
			cloneVectors();
			
			repaint();
			
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	private void cloneVectors() {
		painter = (Vector<Sprite>) actors.clone();
	}

	private BufferedImage[] loadPics (String path, int pics){
		BufferedImage[] anim = new BufferedImage[pics];
		BufferedImage source = null;

		URL pic_url = getClass().getClassLoader().getResource(path);

		try{
			source = ImageIO.read(pic_url);
		}catch(IOException e){}

		for(int x = 0; x < pics; x++){
			anim[x] = source.getSubimage(x * source.getWidth() / pics, 0, source.getWidth() / pics, source.getHeight());
		}

		return anim;
	}

	private void moveObjects() {
		for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();){
			Sprite r = it.next();
			r.move(delta);
		}
	}

	private void doLogic() {
		for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();){
			Sprite r = it.next();
			r.doLogic(delta);
			
			if(r.remove){
				it.remove();
			}
		}
	}

	private void checkKeys() {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){

			count += 1;
			
			elefantPlayer.remove = true;
			cactusPlayer.remove = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			double x = (int)(Math.random()*700) + 1;
			double y = (int)(Math.random()*500) + 1;
			
			double x2 = (int)(Math.random()*700) + 1;
			double y2 = (int)(Math.random()*500) + 1;
			
			cactusPlayer.remove = false;
			elefantPlayer.remove = false;
						
			BufferedImage[] elefant = loadPics("pics/elefant.gif", 1);
			BufferedImage[] cactus = loadPics("pics/cactus.gif", 1);
			elefantPlayer = new Elefant(elefant, x, y, 100, this);
			cactusPlayer = new Cactus(cactus, x2, y2, 100, this);
			actors.add(elefantPlayer);
			actors.add(cactusPlayer);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
