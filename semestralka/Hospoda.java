package semestralka;

public class Hospoda {
	
	public int oznaceni;
	public int x;
	public int y;
	public int region;
	public char typ;
	/** Ktery den bylo objednano */
	public int den = 0;
	/** V jakou hodinu bylo objednano */
	public int hodina = 0;
	/** Mnozstvi v sudech/hl - dle atributu typ */
	public int mnozstvi = 0;
	
	public String obslouzeno_kdy = "ne dnes";
	
	public Hospoda(int oznaceni, int x, int y, char typ, int region){
		this.oznaceni = oznaceni;
		this.x = x;
		this.y = y;
		this.typ = typ;
		this.region = region;
	}
	
	/**
	 * Nastavi cas objednavky
	 * @param den Den simulace
	 * @param hodina Hodina simulace
	 * @return 
	 */
	public void setCasObjednavky(int den, int hodina){
		this.den = den;
		this.hodina = hodina;
	}
	
	/**
	 * Nastavi mnozstvi piva kolik je objednano
	 * @param mnozstvi Mnozstvi piva kolik je objednano
	 */
	public void setMnozstvi(int mnozstvi){
		this.mnozstvi = mnozstvi;
	}

	
}
