package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;

public class RoundButton extends Entity
{
	public int x, y, r;
	
	public String text;
	private int textOffsetX, textOffsetY;
	
	private final Font font;
	
	public Point windowLoc;
	
	public final int MOUSEON = 213;
	private final int MOUSEOFF = 214;
	public final int MOUSECLICKED = 215;
	
	private final Color ON = new Color(65, 244, 128);
	private final Color OFF = new Color(66, 137, 244);
	private final Color CLICKED = new Color(255, 157, 38);
	
	public RoundButton(String text, int x, int y, int radius)
	{
		super(EntID.ROUNDBUTTON, 214); //graphics generated -> MOUSEOFF case
		this.x = x;
		this.y = y;
		r = radius;
		windowLoc = new Point(0,0);
		font = new Font("TimesRoman", Font.BOLD, (int) (r/2.5));
		setText(text);
	}

	private boolean onMe(Point p)
	{
		//System.out.println(Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2)));
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2)) < r;
	}
	
	public void setText(String t)
	{
		text = t;
		textOffsetX = t.length()*font.getSize() / 5;
		textOffsetY = (int) (font.getSize() / 2);
	}
	
	public void updateWindowPosition(Point pos)
	{
		windowLoc.x = pos.x;
		windowLoc.y = pos.y;
		
	}
	
	@Override
	public void tick(double dt)
	{
			if(imageIndex == MOUSECLICKED)
			{
				return;
			}
			
			Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
			mouseLoc.translate(-windowLoc.x, -windowLoc.y);
			
			if(onMe(mouseLoc))
			{
				imageIndex = MOUSEON;
			}else{
				imageIndex = MOUSEOFF;
			}	
	}

	@Override
	public void render(Graphics g)
	{
			//draw body
			if (imageIndex != MOUSECLICKED)
			{
				switch(imageIndex)
				{
				case MOUSEOFF:
					g.setColor(OFF);
					break;
				case MOUSEON:
					g.setColor(ON);
					break;
				}
			}else{
				g.setColor(CLICKED); //set externally -> we don't want this to set it back
			}

			g.fillRoundRect(x - r, y - r, 2*r, 2*r, 2*r, 2*r);
			
			//draw text
			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString(text, x - textOffsetX, y + textOffsetY);
	}
}
