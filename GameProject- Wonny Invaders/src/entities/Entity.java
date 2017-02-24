package entities;

public abstract class Entity {
	double x, y; 			//Position
	double length, width;	//Dimensions
	int ID;
	
	//Getter Methods
	public abstract double getX();
	public abstract double getY();
	public abstract double getLength();
	public abstract double getWidth();
	public abstract int getID();
	
	//Setter Methods
	public abstract void setX(double X);
	public abstract void setY(double Y);
	public abstract void setLength(double Length);
	public abstract void setWidth(double width);
	public abstract void setID(int Id);
	
	
}
