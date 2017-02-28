package entities;

import java.awt.Graphics;

public class Enemy extends Entity {
	int health;
	double speed;
	double accel;

	public Enemy(double nX, double nY, double nLength, double nWidth, int nHealth) {
		super(EntID.ENEMY, -1);/**ADD ENEMIES*/
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		health = nHealth;
		speed = 0;
		accel = 0;
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
		public EntID getID(){
			return this.ID;
		}
		public int getHealth(){
			return this.health;
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
			//this.ID = nID;
		}
		public void setHealth(int nHealth){
			this.health = nHealth;
		}
		public void setSpeed(double s){
			this.speed = s;
		}
		public void setAccel(double a){
			this.accel = a;
		}
		}
