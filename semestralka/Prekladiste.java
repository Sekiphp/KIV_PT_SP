package semestralka;

/**
 * Trida pro simulaci prekladiste
 * 
 * @author Lubos Hubacek, Michal Horky
 */
public class Prekladiste {
	/** Aktualni pocet prazdnych sudu v prekladisti */
	public static int prazdnych_sudu = 0;
	/** Aktualni pocet plnych sudu v prekladisti */
	public int plnych_sudu = 2000;	
	/** Cislo prekladiste */
	int cislo;
	/** X souradnice prekladiste */
	int x;
	/** Y souradnice prekladiste */
	int y;
	
	/**
	 * Vtvori instanci prekladiste
	 * 
	 * @param cislo ID skladu
	 * @param x X na mape (v grafu)
	 * @param y Y na mape (v grafu)
	 */
	public Prekladiste(int cislo, int x, int y){
		this.cislo = cislo;
		this.x = x;
		this.y = y;
	}
	
}