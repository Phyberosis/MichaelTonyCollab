package entities;

import java.awt.Image;

public abstract class Entity {
	double x, y; 			//Position
	double length, width;	//Dimensions
	int ID;
	Image image;
	
	//Getter Methods
	public abstract double getX();
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
	public abstract void setImage(Image i);

	
}
