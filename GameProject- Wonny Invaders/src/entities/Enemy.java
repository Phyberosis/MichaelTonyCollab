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
	
}
