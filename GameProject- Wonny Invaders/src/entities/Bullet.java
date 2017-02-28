package entities;

import java.awt.Graphics;
import java.awt.Image;

public class Bullet extends Entity {
	int damage;
	double speed;
	
	public Bullet(double nX, double nY, double nLength, double nWidth, int nId, int nDamage) {
		super(EntID.BULLET, -1); /**ADD BULLET RESOURCE*/
		x = nX;
		y = nY;
		length = nLength;
		width = nWidth;
		damage = nDamage;
		speed = 0;
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
