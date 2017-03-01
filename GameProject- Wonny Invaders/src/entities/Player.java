package entities;

import java.awt.Graphics;
import java.awt.Point;

import mechanics.Resources;

public class Player extends Entity {
	int health;
	double dx, dy;
	double ax, ay;
	
	private final double MAXSPEED;
	private final double ARATE;
	
	private int animationIndex;
	private final int animationTime;
	private double animationCounter;

	private final int XBOUND, YBOUND; //takes dimensions into account
	
	//debug
	private String debugSpeed;
	
	public Player(double nX, double nY, int nHealth,int xb,int yb) {
		super(EntID.PLAYER, 1, 0); // use res 1, 0th player
		x = nX;
		y = nY;
		setWidth(130);
		setLength(160);
		health = nHealth;

		animationTime = 650; 	// in ms
		animationIndex = 1;		// MUST SYNC WITH RES # IN SUPER
		animationCounter = 0;	// Increments with dt in tick until next animation frame
		
		MAXSPEED = 4;
		XBOUND = xb - (width/2);
		YBOUND = yb - (length/2);
		
		ARATE = 0.015;
	}

	public double getARate()
	{
		return ARATE;
	}
	
	/*
	//see keyDown and keyUp
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
	}*/

	private void boundGuard()
	{
		if(x < (width/2))
		{
			x = (width/2);
			dx = 0;
		}else if(x > XBOUND)
		{
			x = XBOUND;
			dx = 0;
		}
		
		if(y < (length/2))
		{
			y = (length/2);
			dy = 0;
		}else if(y > YBOUND)
		{
			y = YBOUND;
			dy = 0;
		}
	}
	
	private void speedGuard()
	{
		if(dx > MAXSPEED)
		{
			dx = MAXSPEED;
		}else if(dx < -MAXSPEED)
		{
			dx = -MAXSPEED;
		}
		if(dy > MAXSPEED)
		{
			dy = MAXSPEED;
		}else if(dy < -MAXSPEED)
		{
			dy = -MAXSPEED;
		}
	}
	
	private void passiveSlow(double dt)
	{
		double factor = 2.0;
		
		if(ax == 0 && dx != 0)
		{
			if(dx > 0)
			{
				dx -= ARATE*dt/factor;
			}else{
				dx += ARATE*dt/factor;
			}
		}
		if(ay == 0 && dy != 0)
		{
			if(dy > 0)
			{
				dy -= ARATE*dt/factor;
			}else{
				dy += ARATE*dt/factor;
			}
		}
		
		if (dx < ARATE*dt/factor && dx > -ARATE*dt/factor)
		{
			dx = 0.0;
		}
		if (dy < ARATE*dt/factor && dy > -ARATE*dt/factor)
		{
			dy = 0.0;
		}
	}
	
	@Override
	public void tick(double dt)
	{

		//update vectors
		/*
		Point spd = getSpeed();
		Point accel = getAccel();
		Point loc = getLoc();

		setSpeed(spd.x + (accel.x * dt/1000), spd.y + (accel.y * dt /1000));
		setX(loc.x + (spd.x * dt/1000));
		setY(loc.y + (spd.y * dt/1000));
		*/
		
		// Increment speed
		dx = dx + (ax * dt);
		dy = dy + (ay * dt);
		
		//auto slow down
		passiveSlow(dt);
		
		//speed guard
		speedGuard();
		
		//move player
		x += dx * dt;
		y += dy * dt;

		//out of bounds guard
		boundGuard();
		
		//update animation
		animationCounter += dt;
		if(animationCounter > animationTime)
		{
			animationCounter = 0;
			if(animationIndex == 1)
			{
				animationIndex = 2;
			}else{
				animationIndex = 1;
			}
		}
	}

	//should handle elements needing access to res
	public void render(Graphics g, Resources r)
	{

		g.drawImage(r.getImg(animationIndex),(int) (x - (getWidth()/2)), (int)(y - (getLength()/2)), null);

		render(g);
	}

	//should handle efx elements
	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub

	}
	
	public String setDebugInfo(String speed)
	{
		return setGetDebugInfo(true, speed);
	}
	public String getDebugInfo()
	{
		return setGetDebugInfo(false, null);
	}
	// crossthread sync mechanism of debug info
	private synchronized String setGetDebugInfo(boolean set, String speed)
	{
		if(set)
		{
			debugSpeed = speed;
			return null;
		}else{
			return 	"   accel:  " + getXAccel()*1000 + ", " + getYAccel()*1000  + "\n" +
					"   speed:  " + Math.floor(dx * 1000) / 1000 + ", " + Math.floor(dy * 1000) / 1000 + "\n" +
				   	"   loc:  " + getLoc().x + ", " + getLoc().y;
		}
	}

	@Override
	public void setID(int Id)
	{
		// TODO Auto-generated method stub

	}

	//Getter Methods
	public Point getLoc() {
		Point p = new Point();
		p.setLocation(x,  y);
		return p;
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
	public int getHealth(){
		return this.health;
	}
	public Point getSpeed(){
		Point p = new Point();
		p.setLocation(dx,  dy);
		return p;
	}
	public Point getAccel(){
		Point p = new Point();
		p.setLocation(ax,  ay);
		return p;
	}
	public double getXAccel(){
		return ax;
	}
	public double getYAccel(){
		return ay;
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
	public void setHealth(int nHealth){
		this.health = nHealth;
	}
	public void setSpeed(double x, double y)
	{
		if(x > MAXSPEED)
		{
			x = MAXSPEED;
		}
		if(y > MAXSPEED)
		{
			y = MAXSPEED;
		}
		dx = x;
		dy = y;
	}
	public void setAccel(double x, double y)
	{
		ax = x;
		ay = y;
	}
	public void setXAccel(double x)
	{
		ax = x;
	}
	public void setYAccel(double y)
	{
		ay = y;
	}
}
