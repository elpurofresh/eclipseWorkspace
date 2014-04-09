package backend;


public class SensorBot {

	public final String id;
	private int x, y;
	private String data;
	
	public SensorBot(String id){
		this.id = id;
		this.x = 0;
		this.y = 0;
		this.data = "none";
	}
	
	public void setData(String d){
		this.data = d;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getData(){
		return data;
	}
}
