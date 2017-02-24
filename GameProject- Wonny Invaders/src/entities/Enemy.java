package entities;

public class Enemy extends Entity {

	public Enemy(double nX, double nY, double nLength, double nWidth, int nId) {
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		ID = nId;
	}
	
	//Getter Methods
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public double getLength() {
		return this.length;
	}
	public double getWidth() {
		return this.width;
	}
	public int getID(){
		return this.ID;
	}
	
	//Setter Methods
	public void setX(double nX){
		this.x = nX;
	}
	public void setY(double nY){
		this.y = nY;
	}
	public void setLength(double nLength){
		this.length = nLength;
	}
	public void setWidth(double nWidth){
		this.width = nWidth;
	}
	public void setID(int nID){
		this.ID = nID;
	}
	
}
