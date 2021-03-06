package entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Bullet extends Entity {
	int damage;
	double speed;

	public Bullet(double nX, double nY, int nLength, int nWidth, int nId, int nDamage, int number) {
		super(EntID.BULLET, -1, number); /**ADD BULLET RESOURCE*/
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		damage = nDamage;
		speed = 0;
	}

	//Getter Methods
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public int getLength() {
		return this.length;
	}
	public int getWidth() {
		return this.width;
	}
	public EntID getID(){
		return this.ID;
	}
	public int getDamage(){
		return this.damage;
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
	public void setLength(int nLength){
		this.length = nLength;
	}
	public void setWidth(int nWidth){
		this.width = nWidth;
	}
	public void setID(int nID){
		//this.ID = nID;
	}
	public void setDamage(int nDamage){
		this.damage = nDamage;
	}
	public void setSpeed(double s){
		this.speed = s;
	}

	@Override
	public void tick(double dt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getLoc()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
