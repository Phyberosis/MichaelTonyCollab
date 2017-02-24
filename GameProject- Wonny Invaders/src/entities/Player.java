package entities;

import java.awt.Image;

public class Player extends Entity {
	int health;
	double speed;
	double accel;
	
	public Player(double nX, double nY, double nLength, double nWidth, int nId, int nHealth, Image M) {
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		ID = nId;
		health = nHealth;
		image = M;
		speed = 0;
		accel = 5;
	}
	
	public void moveUp(double s){
		y+=s;
	}
	public void moveDown(double s){
		y-=s;
	}
	public void moveLeft(double s){
		x -= s;
	}
	public void moveRight(double s){
		x += s;
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
	public int getHealth(){
		return this.health;
	}
	public Image getImage(){
		return this.image;
	}
	public double getSpeed(){
		return this.speed;
	}
	public double getAccel(){
		return this.accel;
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
	public void setHealth(int nHealth){
		this.health = nHealth;
	}
	public void setImage(Image M){
		this.image = M;
	}
	public void setSpeed(double s){
		this.speed = accel * s;
	}
	public void setAccel(double a){
		this.accel = a;
	}
	
}
