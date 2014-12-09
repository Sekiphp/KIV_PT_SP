package semestralka;

public class Objednavka {
	
	final double RYCHLOST;
	
	int mnozstvi;
	int indexHospody;
	int vzdalenost;
	int casDoruceni; // doba cesty v minutach, ta bude pozdeji upravena na cas doruceni
	int casPrijetiObjednavky;
	
	String obsluhujiciAuto;
	
	Objednavka dalsi;
	Objednavka predchozi;
	
	public Objednavka(int mnozstvi, int indexHospody){
		this.mnozstvi = mnozstvi;
		this.indexHospody = indexHospody;
		int region = 0;
		if(Hlavni.hospody[indexHospody].typ == 'S'){
			region = Hlavni.hospody[indexHospody].region;
			RYCHLOST = 70.0;
		}else{
			RYCHLOST = 60.0;
		}
		vzdalenost = Hlavni.floydWarshall[region][indexHospody+9];
		casDoruceni = (int)((vzdalenost/RYCHLOST) * 60);
		casPrijetiObjednavky = 0;
		obsluhujiciAuto = "";
		dalsi = null;
		predchozi = null;
	}
	
}
