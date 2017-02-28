package entities;

import java.awt.Graphics;

public abstract class Entity {
	public double x, y; 			//Position
	public double length, width;	//Dimensions
	public final EntID ID;
	public int imageIndex;
	
	public Entity(EntID id, int resourceIndex)
	{
		ID = id;
		imageIndex = resourceIndex;
	}
	
	public abstract void tick(double dt);
	public abstract void render(Graphics g);
	
	//Getter Methods
	/*public abstract double getX();
	public abstract double getY();
	public abstract double getLength();
	public abstract double getWidth();
	public abstract int getID();
	public abstract Image getImage();
	
	//Setter Methods
	public abstract void setX(double X);
	public abstract void setY(double Y);
	public abstract void setLength(double Length);
	public abstract void setWidth(double width);
	public abstract void setID(int Id);
	public abstract void setImage(Image i);*/

	
}
