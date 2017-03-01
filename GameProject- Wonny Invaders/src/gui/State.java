package gui;

import java.awt.Graphics;
import java.util.LinkedList;

import entities.Entity;

public abstract class State
{
	public LinkedList<Entity> entities;
	public final StateID id;
	
	public State(StateID id)
	{
		entities = new LinkedList<>();
		this.id = id;
	}

	public abstract void tick(double dt);
	public abstract void render(Graphics g);

	// crossthread sync mechanism of debug info -> add " synchronized " to resulting methods
	public abstract void setDebugInfo(String str);
	public abstract String getDebugInfo();
	protected abstract String setGetDebugInfo(boolean set, String str);
}

