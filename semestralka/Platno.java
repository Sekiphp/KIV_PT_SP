package semestralka;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Platno extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Prekladiste[] prekladiste;
	Hospoda[] hospody;
	int[][] floydWarshall;
	int[][] predchudci;
	
	public Platno(Prekladiste[] prekladiste2, Hospoda[] hospody2, int[][] floydWarshall, int[][] predchudci){
		setPreferredSize(new Dimension(501, 501));
		this.prekladiste = prekladiste2;
		this.hospody = hospody2;
		this.floydWarshall = floydWarshall;
		this.predchudci = predchudci;
	}


	/**
	 * @param pocatek index ve floydWarshall, ktery udava pocatecni vrchol
	 * @param konec index ve floydWarshall, ktery udava koncovy vrchol
	 * @return oznaceni hospod, ktere jsou na dane ceste bez pocatecniho
	 * a koncoveho vrcholu (je to zbytecne)!
	 */
	public int[] zjistiVrcholy(int pocatek, int konec){
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(konec != pocatek){
			konec = predchudci[pocatek][konec];
			list.add(konec);
		}
		if(list.size() >= 2){
			int[] hospodyNaCeste = new int[list.size() - 1];
			// nechci pocatecni hospodu, takze proto list.size() - 2!
			for(int i = list.size() - 2, j = 0; i>=0; i--, j++){
				hospodyNaCeste[j] = vratOznaceni(list.get(i));
			}
			return hospodyNaCeste;
		} else {
			return null;
		}
	}
	
	public int vratOznaceni(int index){
		if(index>8 && index<4009){
			return index-8;
		} else {
			return -1;
		}
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
	
		for(int i = 0; i<prekladiste.length; i++){
			g.fillRect(prekladiste[i].x-2, prekladiste[i].y-2, 4, 4);
		}
		
		g.setColor(Color.BLACK);
		for(int i = 0; i<hospody.length; i++){
			g.fillRect(hospody[i].x, hospody[i].y, 1, 1);
		}
		
		/*		
		// indexy v matice floyd-warshall -> prekladiste_8 na indexu 8 a hospoda_3085 na indexu 3093!
		g.setColor(Color.MAGENTA);
		g.fillRect(prekladiste[7].x - 1, prekladiste[7].y - 1, 2, 2);
		g.fillRect(hospody[3084].x - 1, hospody[3084].y - 1, 2, 2);
		int[] hospodyNaCeste = zjistiVrcholy(8,3093);
		for(int i = 0; i<hospodyNaCeste.length; i++){
			System.out.println(hospodyNaCeste[i]);
			g.setColor(Color.GREEN);
			g.fillRect(hospody[hospodyNaCeste[i]-1].x, hospody[hospodyNaCeste[i]-1].y, 1, 1);
		}
		System.out.println("--------------------");
		*/
		
	}
	
}
