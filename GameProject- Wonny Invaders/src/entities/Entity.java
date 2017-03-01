package entities;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Entity {
	public double x, y; 			//Position
	public int length, width;		//Dimensions
	public final EntID ID;
	public int number;
	public int imageIndex;
	
	public Entity(EntID id, int resourceIndex, int number)
	{
		ID = id;
		imageIndex = resourceIndex;
		this.number = number;
	}
	
	public abstract void tick(double dt);
	public abstract void render(Graphics g);
	
	//Getter Methods
	public abstract Point getLoc();
	public abstract int getLength();
	public abstract int getWidth();
	public abstract EntID getID();
	public int getNumber()
	{
		return number;
	}
	
	//Setter Methods
	public abstract void setX(double X);
	public abstract void setY(double Y);
	public abstract void setLength(int Length);
	public abstract void setWidth(int width);
	public abstract void setID(int Id);

}
