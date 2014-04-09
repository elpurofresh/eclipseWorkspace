package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.Main;
import backend.SensorBot;

public class GraphicalRep3 extends JPanel{

	private static final long serialVersionUID = 1709208790748660218L;

	Main main = null;
	private BufferedImage imgbuf = null;
	Graphics2D g2D = null;


	public double centerPanelX = 0;
	public double centerPanelY = 0; 
	private String masterState = "";

	private final BufferedImage sensorBotImage = loadAsBufferedImage("images/sensorbot.jpg");
	private final BufferedImage masterNodeImage = loadAsBufferedImage("images/masterNode.jpg");

	private Object sensorBotsLock;
	private HashMap<String, SensorBot> sensorBots;


	public GraphicalRep3(Main main){
		super();
		this.main = main;
		this.sensorBots = new HashMap<String, SensorBot>();
		this.sensorBotsLock = new Object();

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				imgbuf = null;
			}
		});
	}

	private final void initDoubleBuffer() {
		if (imgbuf == null) {
			imgbuf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			imgbuf.setAccelerationPriority(0);
		}
		g2D = imgbuf.createGraphics();
	}


	//Main method
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		if (imgbuf == null || g2D == null) {
			initDoubleBuffer();
		}

		g2D.setBackground(Color.WHITE);
		g2D.clearRect(0, 0, getWidth(), getHeight());

		//super.paintComponent(g); //clear

		centerPanelX = getWidth()/2;
		centerPanelY = getHeight()/2;
		//System.out.println(centerPanelX + " , " + centerPanelY);


		g2D.setColor(Color.BLACK);
		drawMasterNode(g2D, masterState, centerPanelX-400, centerPanelY);
		//drawSensorbot(g2D, "C1", "T:25.2^C", centerPanelX, centerPanelY/*, 50, 50*/);

		HashMap<String, SensorBot> tmp = getSensorBots();
		for(String s : tmp.keySet()){
			SensorBot sb = tmp.get(s);
			drawSensorbot(g2D, s, sb.getData(), sb.getX(), sb.getY());
		}

		g.drawImage(imgbuf,0,0,null);
	}


	private Rectangle2D.Double drawRectangleCenterAt(double cx, double cy, double w, double h){
		double x = cx - (w/2);
		double y = cy - (h/2);
		return new Rectangle2D.Double(x, y, w, h);		
	}



	private void drawSensorbot(Graphics2D g, String name, String data, double cx, double cy/*, double w, double h*/){
		//Image sensorBot = new ImageIcon(this.getClass().getResource("/images/sensorBot.jpg")).getImage();
		double w = sensorBotImage.getWidth(null);
		double h = sensorBotImage.getHeight(null);
		double x = cx - w/2;
		double y = cy - h/2;
		g.drawImage(sensorBotImage, (int) x, (int) y, null);
		g.drawString(name, (int) (cx+15), (int) (cy-15));
		g.drawString(data, (int) (cx-w/2), (int) (cy + h*0.90));
		g.draw(drawRectangleCenterAt(cx, cy+3*h/4, w*1.5, h/2));
	}

	private void drawMasterNode(Graphics2D g, String state, double cx, double cy){
		double w = masterNodeImage.getWidth(null);
		double h = masterNodeImage.getHeight(null);
		double x = cx;// - (masterNode.getWidth(null)/2);
		double y = cy - (h/2);
		g.drawImage(masterNodeImage, (int)x, (int)y, null);
		g.drawString(state, (int) (x + w/2), (int) (cy + h));
	}

	public void updateMasterNodePic(String state){
		//drawMasterNode(g2D, state, centerPanelX, centerPanelY);
		masterState = state;
		repaint();
		//new MasterNodeUpdater();
	}

	//public void updateSensorbotPic(String botId, String data){
	//	addSensorBot(botId, data);
	//drawSensorbot(g2D, botId, data, centerPanelX, centerPanelY);
	//	repaint();
	//}

	public void addSensorBot(String id, String data, int x, int y){
		synchronized (sensorBotsLock) {
			SensorBot sb = new SensorBot(id);
			sb.setData(data);
			sb.setLocation(x, y);
			sensorBots.put(id, sb);
		}
		repaint();
	}

	public void updateSensorBot(String id, String data){
		synchronized (sensorBotsLock) {
			if(sensorBots.containsKey(id)){
				sensorBots.get(id).setData(data);
			}
		}
		repaint();
	}

	public void removeSensorBot(String id){
		synchronized (sensorBotsLock) {
			sensorBots.remove(id);
		}
		repaint();
	}

	private HashMap<String, SensorBot> getSensorBots(){
		HashMap<String, SensorBot> tmp = new HashMap<String, SensorBot>();
		synchronized (sensorBotsLock) {
			tmp.putAll(sensorBots);
		}
		return tmp;
	}


	/*class MasterNodeUpdater implements Runnable{

		private Thread runner;

		public MasterNodeUpdater(){
			runner = new Thread(this);
			runner.start();
		}


		@Override
		public void run() {
			repaint();
			drawMasterNode(g2D, masterState, centerPanelX, centerPanelY);
		}
	}*/


	/*private void drawCircleText(Graphics2D g, String name, String data, double cx, double cy, double w, double h){
		g.draw(drawCircleCenterAt(cx, cy, w, h));
		g.drawString(name, (int) (cx-7), (int) (cy+5));
		g.drawString(data, (int) (cx-w/2), (int) (cy + (h/2 + 16)));
		g.draw(drawRectangleCenterAt(cx, cy+(3*h/4), w*1.5, h/2));
	}*/	

	/*private Ellipse2D.Double drawCircleCenterAt(double cx, double cy, double w, double h){
	double x = cx - (w/2);
	double y = cy - (h/2);
	return new Ellipse2D.Double(x, y, w, h);		
	}*/

	private static final BufferedImage loadAsBufferedImage(String filename){
		BufferedImage bi = null;

		try {
			File f = new File(filename);
			if(!f.exists()) 
				throw new IOException();
			else
				bi = ImageIO.read(f);
		} catch (IOException e) {
			try {
				URL url = ClassLoader.getSystemResource(filename);
				if(url != null){
					bi = ImageIO.read(url);					
				}else{
					throw new IOException();
				}
			} catch (IOException e1) {
				System.err.println("Could not load file: "+filename);
			}
		}
		return bi;
	}



}
