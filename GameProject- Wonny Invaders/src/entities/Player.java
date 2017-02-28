package entities;

import java.awt.Graphics;
import java.awt.Image;

public class Player extends Entity {
	int health;
	double speed;
	double accel;
	
	public Player(double nX, double nY, double nLength, double nWidth, int nHealth, Image M) {
		super(EntID.PLAYER, 1);
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		health = nHealth;
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
	
}
