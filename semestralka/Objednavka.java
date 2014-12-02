package semestralka;

public class Objednavka {
	
	int mnozstvi;
	int indexHospody;
	int vzdalenost;
	double casDoruceni;
	
	Objednavka dalsi;
	Objednavka predchozi;
	
	/** Rychlost zavisla na typu hospody */
	int RYCHLOST; 
	
	public Objednavka(int mnozstvi, int indexHospody, int vzdalenost){
		RYCHLOST = 60;
		if(Hlavni.hospody[indexHospody].typ == 'S'){
			RYCHLOST = 70;
		}
		
		Hlavni.hospody[indexHospody].mnozstvi = mnozstvi;
		
		this.mnozstvi = mnozstvi;
		this.indexHospody = indexHospody;
		this.vzdalenost = vzdalenost;
		this.casDoruceni = this.vzdalenost / (double)RYCHLOST;
		this.dalsi = null;
		this.predchozi = null;
	}
	
}
