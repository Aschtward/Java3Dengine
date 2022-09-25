package engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

import math.MatrizUtil;
import math.Vec3d;
import objects.Cube;

public class Game extends Canvas implements Runnable, KeyListener,MouseListener,MouseMotionListener{
	
	
	private static final long serialVersionUID = 1227254042505466843L;
	
	///Definindo parametros para janela
	public static JFrame frame;
	public static int WIDTH = 600;
	public static int HEIGHT = 600;
	public static int SCALE = 1;
	///Fim parametros para janela
	
	public static BufferedImage image;
	public static Random rand;
	public String level_now = "/map.png";
	private Thread thread;
	private boolean isRunning = true;
	private Cube cubeMesh;
	private float time, elapsedTime;
	public static Camera cam;
	public static float yMouse = 0;
	public static float xMouse = 0;
	
	public Game() {
		
		///Cria��o da Janela
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		inicia_frame();
		///
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		rand = new Random();
		cubeMesh = new Cube();	
		cam = new Camera();
	}
	
	public void inicia_frame() {///Inicializa janela
		frame = new JFrame("Kill the red marshmallow");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
		
	public void tick() {
		this.cubeMesh.tick();
		Game.cam.tick();
		time += 1.0f;
		elapsedTime = time/60;
	}
	
	public void  render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		cubeMesh.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		bs.show();
	}
	
	public void run() {
		
		//Implementa��o game looping
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		int frames = 0;
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta+=(now - lastTime)/ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer += 1000;
			}
		}//Fim implementa��o game looping
		
		stop();
}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_W) {
			cam.goFoward = true;
			System.out.println("entrou");
		}
		if(e.getKeyChar() == KeyEvent.VK_S) {
			cam.goBackwards = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			cam.turnLeft = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			cam.turnRight = true;
		}
		/*if(e.getKeyCode() == KeyEvent.VK_UP){
			cam.vCamera.y += 1.0f;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			cam.vCamera.y -= 1.0f;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			cam.vCamera.x += 1.0f;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			cam.vCamera.x -= 1.0f;
		}*/
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A){
			cam.turnLeft = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			cam.turnRight = false;
		}
		if(e.getKeyChar() == KeyEvent.VK_W) {
			cam.goFoward = false;
		}
		if(e.getKeyChar() == KeyEvent.VK_S) {
			cam.goBackwards = false;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//selected component
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(xMouse < e.getYOnScreen()) {
			cam.fXaw +=0.02f;
			xMouse = e.getYOnScreen();
		}
		if(xMouse > e.getYOnScreen()) {
			cam.fXaw -=0.02f;
			xMouse = e.getYOnScreen();
		}
		if(yMouse < e.getXOnScreen()) {
			cam.fYaw +=0.02f;
			yMouse = e.getXOnScreen();
		}
		if(yMouse > e.getXOnScreen()) {
			cam.fYaw -=0.02f;
			yMouse = e.getXOnScreen();
		}
	}


}
