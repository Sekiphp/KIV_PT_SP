package semestralka;

import java.util.ArrayList;

/**
 * Trida pro simulaci prekladiste
 * 
 * @author Lubos Hubacek, Michal Horky
 */
public class Prekladiste extends Stavba{
	/** Aktualni pocet prazdnych sudu v prekladisti */
	public static int prazdnych_sudu = 0;
	/** Aktualni pocet plnych sudu v prekladisti */
	public int plnych_sudu = 2000;
	
	SpojovySeznam spojak;
	ArrayList<NakladniAutomobil> auta = new ArrayList<NakladniAutomobil>();
	int pocetAut = 0;

	
	/**
	 * Vtvori instanci prekladiste
	 * 
	 * @param cislo ID skladu
	 * @param x X na mape (v grafu)
	 * @param y Y na mape (v grafu)
	 */
	public Prekladiste(int cislo, int x, int y){
		super(cislo, x, y);
		inicializujSpojak();
	}
	
	public void inicializujSpojak(){
		spojak = new SpojovySeznam();
	}
	
	public void pridejDoSpojaku(Objednavka nova){
		spojak.pridej(nova);
	}
	
	public void urciObjednavkyKVyrizeni(){
		while(spojak.prvni != null){
			NakladniAutomobil auto = najdiVolneAuto();
			double casZpozdeni = 0.0; // vykladani objednavky a nakladani prazdnych sudu
			Objednavka posledni = spojak.odeber(spojak.prvni.indexHospody); // nejvzdalenejsi hospoda ze vsech, co si objednaly
			int[] indexyHospodNaDaneCeste = Casovac.zjistiVrcholy(oznaceni, posledni.indexHospody + 9); // indexy hospod na nejkratsi ceste
			ArrayList<Objednavka> objednavkyNaCeste = spojak.odeber(indexyHospodNaDaneCeste);
			int mnozstviOdberu = zjistiMnozstviNaCeste(objednavkyNaCeste) + posledni.mnozstvi;
			casZpozdeni += mnozstviOdberu * 5;
			plnych_sudu -= mnozstviOdberu;
			int pocetOpakovani = objednavkyNaCeste.size();
			for(int i = 0; i<pocetOpakovani; i++){
				Objednavka hospodaNaCeste = objednavkyNaCeste.remove(0);
				hospodaNaCeste.casDoruceni += (casZpozdeni + Casovac.minut); // dobu jizdy musime zvetsit o dane spozdeni na ceste
				hospodaNaCeste.obsluhujiciAuto = auto.oznaceni;
				auto.pole[Casovac.getDen()].pridej(hospodaNaCeste.indexHospody);
				int nalozeni = mnozstviKNalozeni(posledni);
				casZpozdeni += hospodaNaCeste.mnozstvi * 5; // 5 minut, jde o sudy
				casZpozdeni += nalozeni * 5;
				auto.prazdneSudy += nalozeni;
				Casovac.pridejDoFronty(hospodaNaCeste); // pridej upravenou objednavku do finalni fronty
			}
			posledni.casDoruceni += (casZpozdeni + Casovac.minut);
			posledni.obsluhujiciAuto = auto.oznaceni;
			auto.pole[Casovac.getDen()].pridej(posledni.indexHospody);
			int nalozeni = mnozstviKNalozeni(posledni);
			auto.prazdneSudy += nalozeni;
			auto.casNavratu += (posledni.casDoruceni + posledni.mnozstvi*5 + nalozeni*5 + posledni.vzdalenost + auto.prazdneSudy * 5);
			Casovac.pridejDoFronty(posledni); // nakonec pridam tu nejvzdalenejsi na dane ceste
		}
	}
	
	private int mnozstviKNalozeni(Objednavka pom){
		int nalozeni = 0;
		if(Casovac.getDen() != 0){
			nalozeni = Hlavni.hospody[pom.indexHospody].poctySudu[Casovac.getDen()-1];
		}
		return nalozeni;
	}
	
	private int zjistiMnozstviNaCeste(ArrayList<Objednavka> objednavkyNaCeste) {
		int sum = 0;
		for(int i = 0; i<objednavkyNaCeste.size(); i++){
			sum += objednavkyNaCeste.get(i).mnozstvi;
		}
		return sum;
	}

	public NakladniAutomobil najdiVolneAuto(){
		for(int i = 0; i<auta.size(); i++){
			if(auta.get(i).casNavratu <= Casovac.minut){
				prazdnych_sudu += auta.get(i).prazdneSudy;
				auta.get(i).prazdneSudy = 0;
				auta.get(i).casNavratu = 0;
				return auta.get(i);
			}
		}
		pocetAut++;
		return new NakladniAutomobil(oznaceni, pocetAut);
	}
	
}