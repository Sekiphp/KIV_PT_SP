package semestralka;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class Platno extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Platno(){
		setPreferredSize(new Dimension(501, 501));
	}
		
	@Override
	public void paint(Graphics g){
		
		g.setColor(Color.GRAY);
		
		g.drawLine(0, 0, 500, 500);
		g.drawLine(0, 500, 500, 0);
		g.drawLine(0, 250, 500, 250);
		g.drawLine(250, 0, 250, 500);
		
		g.drawLine(0, 0, 500, 0);
		g.drawLine(0, 500, 0, 0);
		g.drawLine(0, 500, 500, 500);
		g.drawLine(500, 0, 500, 500);
		
		g.setColor(Color.RED);
		g.fillRect(248, 248, 5, 5);
		
		g.setColor(Color.BLUE);
	
		for(int i = 0; i<Hlavni.prekladiste.length; i++){
			g.fillRect(Hlavni.prekladiste[i].x-2, Hlavni.prekladiste[i].y-2, 4, 4);
		}
		
		g.setColor(Color.BLACK);
		for(int i = 0; i<Hlavni.hospody.length; i++){
			g.fillRect(Hlavni.hospody[i].x, Hlavni.hospody[i].y, 1, 1);
		}
				
	}
	
}
