package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Main;

public class GraphicalRep extends JPanel{

	private static final long serialVersionUID = 1709208790748660218L;

	Main main = null;
	private BufferedImage imgbuf = null;
	Graphics2D g2D = null;
	
	
	double centerPanelX = 0;
	double centerPanelY = 0; 
	private String masterState = "";
	
	
	public GraphicalRep(Main main){
		super();
		this.main = main;
		
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
		
		//g2D = (Graphics2D) g;
		
		//drawCircleText(g2D, "Mn", "Sending", centerPanelX-350, centerPanelY, 60, 60);
		//drawCircleText(g2D, "C1", "T:25.2^C", centerPanelX, centerPanelY, 50, 50);
		//drawCircleText(g2D, "C2", "T:24.8^C", centerPanelX+100, centerPanelY, 50, 50);
		g2D.setColor(Color.BLACK);
		//drawMasterNode(g2D, "Idle", centerPanelX-400, centerPanelY);
		drawMasterNode(g2D, masterState, centerPanelX-400, centerPanelY);
		drawSensorbot(g2D, "C1", "T:25.2^C", centerPanelX, centerPanelY/*, 50, 50*/);

		g.drawImage(imgbuf,0,0,null);
	}
	
	private Ellipse2D.Double drawCircleCenterAt(double cx, double cy, double w, double h){
		double x = cx - (w/2);
		double y = cy - (h/2);
		return new Ellipse2D.Double(x, y, w, h);		
	}
	
	private Rectangle2D.Double drawRectangleCenterAt(double cx, double cy, double w, double h){
		double x = cx - (w/2);
		double y = cy - (h/2);
		return new Rectangle2D.Double(x, y, w, h);		
	}
	
	@SuppressWarnings("unused")
	private void drawCircleText(Graphics2D g, String name, String data, double cx, double cy, double w, double h){
		g.draw(drawCircleCenterAt(cx, cy, w, h));
		g.drawString(name, (int) (cx-7), (int) (cy+5));
		g.drawString(data, (int) (cx-w/2), (int) (cy + (h/2 + 16)));
		g.draw(drawRectangleCenterAt(cx, cy+(3*h/4), w*1.5, h/2));
	}
	
	private void drawSensorbot(Graphics2D g, String name, String data, double cx, double cy/*, double w, double h*/){
		Image sensorBot = new ImageIcon(this.getClass().getResource("/images/sensorBot.jpg")).getImage();
		double w = sensorBot.getWidth(null);
		double h = sensorBot.getHeight(null);
		double x = cx - w/2;
		double y = cy - h/2;
		g.drawImage(sensorBot, (int) x, (int) y, null);
		g.drawString(name, (int) (cx+15), (int) (cy-15));
		g.drawString(data, (int) (cx-w/2), (int) (cy + h*0.90));
		g.draw(drawRectangleCenterAt(cx, cy+3*h/4, w*1.5, h/2));
	}
	
	private void drawMasterNode(Graphics2D g, String state, double cx, double cy){
		Image masterNode = new ImageIcon(this.getClass().getResource("/images/masterNode.jpg")).getImage();
		double w = masterNode.getWidth(null);
		double h = masterNode.getHeight(null);
		double x = cx;// - (masterNode.getWidth(null)/2);
		double y = cy - (h/2);
		g.drawImage(masterNode, (int)x, (int)y, null);
		g.drawString(state, (int) (x + w/2), (int) (cy + h));
	}
	
	public void updateMasterNodePic(String state){
		//drawMasterNode(g2D, state, centerPanelX, centerPanelY);
		masterState = state;
		repaint();
		//new MasterNodeUpdater();
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
	
	
	


}
