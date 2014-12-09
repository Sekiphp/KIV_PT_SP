package semestralka;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.Timer;


/**
 * trida pro reprezentaci casu a volani obsluhy udalosti
 * 
 * @author Lubos Hubacek, Michal Horky
 */
public class Casovac {
	
	final int HODINA = 60;
	
	/** Instance casovace */
	public static Timer casovac;
	/** Aktualni minuta */
	public static int minut = 0;
	/** ArrayList s cisly 0...3999 - kazdy den se obnovuje; pro generovani objednavek */
	private static List<Integer> arrlist;
	/** Pivovar je unikatni - jedinacek */
	private static final Pivovar pivovar = new Pivovar();
	/** Fronta dovozu - jedinacek */
	private static final Fronta fronta = new Fronta();
	
	/** Kolik hospod uz si dnes objednalo */
	public static int objednalo_si = 0;	
	
	/**
	 * Podle normálního rozložení pravdìpodobnosti se každý den o pùlnoci
	 * vygeneruje poèet objednávek pro každou hodinu od 8 - 16.
	 */
	int[] pocetObjednavekNaHodinu;
	    
	/**
	 * Inicializuje casovac s nastavenim rychlosti - cim vyssi je vislo v Timeru, tim pomaleji se simulace provede
	 */
	public Casovac(){
		casovac = new Timer(1, new PosluchacCasovace()); // 50	
		pocetObjednavekNaHodinu = new int[9];
	}
	
    /**
     * Zapne simulaci casu
     */
    public void start(){		
    	casovac.start();   	
    }
    
    /** 
     * Pozastavi simulaci casu 
     */
    public void stop(){
    	casovac.stop();
    }
    
    /**
     * Zacne simulaci od znova - nemenit hodnoty!!!
     */
    public void vynuluj(){
    	casovac.stop();
    	//den = hodina = minuta = 0;
    	casovac.restart();
    	casovac.stop();
    }
    
    
    /**
     * Otestuje jeslti casovac bezi, ci nikoliv
     * @return pravda nebo nepravda
     */
    public boolean isRunning(){
    	return casovac.isRunning();
    }
    
    /**
     * Provede zadany pocet kroku v Casovaci
     * 
     * @param pocet pocet minut
     * @throws IOException 
     */
    public void udelejKroky(int pocet) throws IOException{
    	for(int i = 0; i < pocet; i++){
    		citaciBez();
    	}
    }
	
	/**
	 * Novy posluchac pro ridici tridu Timer
	 * 
	 * @author Lubos Hubacek
	 */
    public class PosluchacCasovace implements ActionListener {
    	/**
    	 * Obsluha udalosti casovace
    	 */
        @Override
        public void actionPerformed(ActionEvent e) {
        	citaciBez();
        }
    }
    
    public static int getDen(){
    	return (minut / 1440);
    }
    
    /**
	 * Zajisti beh citace (rotace minut, dni, hodin) 
	 * Probihaji zde vsechna volani - poptavky atp. 
     * @throws IOException 
	 */
	public void citaciBez() {
		
		//System.out.println(minut);
		
		if(minut == 10080){
			stop();
			vystupDoSouboru();	
			Hlavni.jtf.addTextLn("KONEC");
			return;
		}
		
		zkontrolujFrontu();
		
		if(minut % 1440 == 0){
			Hlavni.jtf.addTextLn("DEN " + (getDen() + 1));
			vygenerujPoleProPocetObjednavek(); // o pùlnoci vygeneruj pocet objednavek pro cele hodiny
			poradiObjednavek();	// poradi hospod pro dnesni den
		}
		
		if(minut % 60 == 0){
			int den = getDen()*1440; // prijem objednavek 8:00 - 16:00
			if(minut >= (den + 480) && minut <= (den + 960)){
				
				inicializujPoleSpojSezn();
				
				int pomPocet = 0;
				for(int i = 0; i<Hlavni.manualniObjednavky.size(); i++){
					Objednavka pridana = Hlavni.manualniObjednavky.remove(0);
					pomPocet += odstranPrvekArrlist(pridana.indexHospody);
					pridejDoSpojakuPodleTypu(pridana);
				}
				int kolik = pocetObjednavek((minut / 60) - getDen()*24) - pomPocet;
				for(int i = 0; i < kolik; i++){
					int indexHospody = arrlist.remove(0);
					Objednavka nova = new Objednavka(pocetSudu(), indexHospody);
					nova.casPrijetiObjednavky = minut;
					pridejDoSpojakuPodleTypu(nova);
				}
					
				pivovar.urciObjednavkyKVyrizeni();
				for(int i = 0; i<8; i++){
					Hlavni.prekladiste[i].urciObjednavkyKVyrizeni();
				}
						
			}
		}
		
		minut++;

    }
	
	public static void pridejDoFronty(Objednavka x){
		fronta.pridej(x);
	}
			
	private void pridejDoSpojakuPodleTypu(Objednavka x) {
		int region = zjistiRegionHospody(x.indexHospody);
		if(region == 0){
			pivovar.pridejDoSpojaku(x);
		}else{
			Hlavni.prekladiste[region - 1].pridejDoSpojaku(x);
		}
	}

	private int zjistiRegionHospody(int indexHospody){
		int region = 0;
		if(Hlavni.hospody[indexHospody].typ == 'S'){
			region = Hlavni.hospody[indexHospody].region; 
		}
		return region;
	}

	private void inicializujPoleSpojSezn() {
		pivovar.inicializujSpojak();
		for(int i = 0; i<8; i++){
			Hlavni.prekladiste[i].inicializujSpojak();
		}
	}

	private int odstranPrvekArrlist(int indexHospody) {
		for(int i = 0; i<arrlist.size(); i++){
			if(arrlist.get(i) == indexHospody){
				arrlist.remove(i);
				return 1;
			}
		}
		return 0;
	}
	
	private int pocetObjednavek(int hodina){
		return pocetObjednavekNaHodinu[hodina - 8];
	}
	
	private void vygenerujPoleProPocetObjednavek(){
		Random rd = new Random();
		int zacatek = 8 * HODINA;
		int konec = 16 * HODINA;
		int stredniHodnota = 10 * HODINA;
		int odchylka = 2 * HODINA;
		vynulujPoleProPocetObjednavek();
		int cas;
		for(int i = 0; i<4000; i++){
			cas = 0;
			while (cas < zacatek || cas > konec) {
				cas = (int)(rd.nextGaussian() * odchylka + stredniHodnota);
			}
			pocetObjednavekNaHodinu[cas/60 - 8]++;
		}
		
	}
	
	private void vynulujPoleProPocetObjednavek(){
		for(int i = 0; i<pocetObjednavekNaHodinu.length; i++){
			pocetObjednavekNaHodinu[i] = 0;
		}
	}
		
	private void zkontrolujFrontu() {
		if(fronta.prvni != null){
			if(minut == fronta.prvni.casDoruceni){
				Objednavka doruceni = fronta.prvni;
				while(doruceni.casDoruceni == minut){
					Hlavni.hospody[doruceni.indexHospody].setDenniData(getDen(), doruceni);
					fronta.odeber();
					doruceni = fronta.prvni;
					if(doruceni == null){
						 break;
					}
				}
			}
		}
	}

	private void vystupDoSouboru() {
		VystupSoubor vystup;
		try {
			vystup = new VystupSoubor();
			vystup.createHTMLHead();
			vystup.createTableHospod();
			vystup.vlozHospody();
			vystup.closeTableHospod();
			vystup.createHTMLFoot();
			vystup.neplechaUkoncena();
		} catch (IOException e) {
			System.out.println("Chyba pøi vytváøení výstupního souboru!");
		}
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max
	 */
	public static int randInt(int min, int max) {
	    Random rd = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rd.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	
	/**
	 * Vytvori rozhazeny seznam cisel 0 - 3999 a to ak pouzijeme jako poradi
	 * objednavek pro hospody; kdyz pak vezmeme ze seznamu 100 tak nebudou 
	 * indexy serazene; -> unikatni objednavani v casech
	 */
	private static void poradiObjednavek(){       
		arrlist = new ArrayList<>();
		
		// pro kazdou hospodu pridame do listu
		for(int i = 0; i <= 3999; i++){
			arrlist.add(i);
		}

		// zamichame bramboracku
		Collections.shuffle(arrlist);	
	}
	
	/**
	 * Urceni denni spotreby poctu sudu/hl
	 * @return Cislo v rozmezi 1-6
	 */
	private static int pocetSudu(){
		Random rd = new Random();
		double kostka = rd.nextDouble();
		
		int sudu = 6;
		if(kostka < 0.25){
			sudu = 1; 
		}
		else if(kostka < 0.5){
			sudu = 2;
		}
		else if(kostka < 0.7){
			sudu = 3;
		}
		else if(kostka < 0.85){
			sudu = 4;
		}
		else if(kostka < 0.95){
			sudu = 5;
		}
		
		return sudu;
	}
	
	/**
	 * @param pocatek index ve floydWarshall, ktery udava pocatecni vrchol
	 * @param konec index ve floydWarshall, ktery udava koncovy vrchol
	 * @return oznaceni hospod, ktere jsou na dane ceste bez pocatecniho
	 * a koncoveho vrcholu (je to zbytecne)!
	 */
	public static int[] zjistiVrcholy(int pocatek, int konec){
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(konec != pocatek){
			konec = Hlavni.predchudci[pocatek][konec];
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
	
	public static int vratOznaceni(int index){
		if(index>8 && index<4009){
			return index-8;
		} else {
			return -1;
		}
	}
}
