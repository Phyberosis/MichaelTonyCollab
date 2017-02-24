package entities;

import java.awt.Image;

public class Bullet extends Entity {
	int damage;
	double speed;
	
	public Bullet(double nX, double nY, double nLength, double nWidth, int nId, int nDamage, Image M) {
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		ID = nId;
		damage = nDamage;
		image = M;
		speed = 0;
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
	public int getDamage(){
		return this.damage;
	}
	public Image getImage(){
		return this.image;
	}
	public double getSpeed(){
		return this.speed;
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
	public void setDamage(int nDamage){
		this.damage = nDamage;
	}
	public void setImage(Image M){
		this.image = M;
	}
	public void setSpeed(double s){
		this.speed = s;
	}
	
}
