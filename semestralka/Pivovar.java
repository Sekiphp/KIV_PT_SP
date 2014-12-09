package semestralka;

import java.util.ArrayList;

public class Pivovar {
	
	/** Pocet prazdnych sudu */
	public static int prazdnych_sudu = 0;
	/** Indikator plnych sudu - [1 sud = 50l] */
	public static int plnych_sudu = 0;
	/** Pocet litru piva - [1hl = 100l] */
	public static int litru_piva = 700000;
	
	SpojovySeznam spojak = new SpojovySeznam();
	ArrayList<Cisterna> auta = new ArrayList<Cisterna>();
	int pocetAut = 0;

	/**
	 * Kazdou hodinu uvari priblizne 1/24 denni varky
	 * 
	 * @return Retezec oznamujici udalost
	 */
	public String varPivo(){
		int hodinovaVarka = 29166;
		
		litru_piva += hodinovaVarka;
		return "  Pivovar: bylo uvareno " + hodinovaVarka + "litru piva (celkem: " + litru_piva + "l)";
	}
	
	/**
	 * Pivovarsti ucni naplni sudy pivem -> pripraveno k rozvozu
	 * 
	 * @param pocet Pocet sudu k naplneni
	 * @return Retezec oznamujici udalost
	 */
	public static String naplnSudy(int pocet){
		String ret = "";
		
		// je malo sudu
		if(prazdnych_sudu < pocet){
			int kolik = pocet - prazdnych_sudu;
			nakupSudy(kolik);
			ret += "  Pivovar: Právì jsme dokoupili " + kolik + " prázdných sudù.\n";
		}	
		
		// samotne plneni
		int sud_litru = 50;
		litru_piva -= pocet * sud_litru;
		plnych_sudu += pocet;
		prazdnych_sudu -= pocet;
		
		System.out.println(plnych_sudu);
		System.out.println(prazdnych_sudu);
		
		ret += "Pivovar: právì jsme naplnili " + pocet + " sudù zlatavým mokem.";
		return ret;
	}
	
	/**
	 * V pivovaru se objevi zadany pocet sudu
	 * 
	 * @param pocet Pocet novych sudu
	 */
	private static void nakupSudy(int pocet){
		prazdnych_sudu += pocet;
	}
	
	public void inicializujSpojak(){
		spojak = new SpojovySeznam();
	}
	
	public void pridejDoSpojaku(Objednavka nova){
		spojak.pridej(nova);
	}
	
	public void urciObjednavkyKVyrizeni(){
		while(spojak.prvni != null){
			Cisterna cisterna = najdiVolneAuto();
			double casZpozdeni = 0.0; // vykladani objednavky a nakladani prazdnych sudu
			Objednavka posledni = spojak.odeber(spojak.prvni.indexHospody); // nejvzdalenejsi hospoda ze vsech, co si objednaly
			int[] indexyHospodNaDaneCeste = Casovac.zjistiVrcholy(0, posledni.indexHospody + 9); // indexy hospod na nejkratsi ceste
			ArrayList<Objednavka> objednavkyNaCeste = spojak.odeber(indexyHospodNaDaneCeste);
			int mnozstviOdberu = zjistiMnozstviNaCeste(objednavkyNaCeste) + posledni.mnozstvi;
			casZpozdeni += mnozstviOdberu * 2;
			litru_piva -= mnozstviOdberu * 100;
			int pocetOpakovani = objednavkyNaCeste.size();
			for(int i = 0; i<pocetOpakovani; i++){
				Objednavka hospodaNaCeste = objednavkyNaCeste.remove(0);
				hospodaNaCeste.casDoruceni += (casZpozdeni + Casovac.minut); // dobu jizdy musime zvetsit o dane spozdeni na ceste
				hospodaNaCeste.obsluhujiciAuto = cisterna.oznaceni;
				cisterna.pole[Casovac.getDen()].pridej(hospodaNaCeste.indexHospody);
				casZpozdeni += hospodaNaCeste.mnozstvi * 2; // 2 minuty, jde o cisterny
				Casovac.pridejDoFronty(hospodaNaCeste); // pridej upravenou objednavku do finalni fronty
			}
			posledni.casDoruceni += (casZpozdeni + Casovac.minut);
			posledni.obsluhujiciAuto = cisterna.oznaceni;
			cisterna.casNavratu += (posledni.casDoruceni + posledni.mnozstvi*2 + posledni.vzdalenost);
			cisterna.pole[Casovac.getDen()].pridej(posledni.indexHospody);
			Casovac.pridejDoFronty(posledni); // nakonec pridam tu nejvzdalenejsi na dane ceste
		}
	}
	
	private int zjistiMnozstviNaCeste(ArrayList<Objednavka> objednavkyNaCeste) {
		int sum = 0;
		for(int i = 0; i<objednavkyNaCeste.size(); i++){
			sum += objednavkyNaCeste.get(i).mnozstvi;
		}
		return sum;
	}
	
	public Cisterna najdiVolneAuto(){
		for(int i = 0; i<auta.size(); i++){
			if(auta.get(i).casNavratu <= Casovac.minut){
				auta.get(i).casNavratu = 0;
				return auta.get(i);
			}
		}
		pocetAut++;
		return new Cisterna(pocetAut);
	}
		
}
