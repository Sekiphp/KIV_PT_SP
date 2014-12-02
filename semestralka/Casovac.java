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
	/** Instance casovace */
	public static Timer casovac;
	/** Aktualni den */
	public static int den = 1;
	/** Aktualni hodina */
	public static int hodina = 0;
	/** Aktualni minuta */
	public static int minuta = 0;
	/** ArrayList s cisly 0...3999 - kazdy den se obnovuje; pro generovani objednavek */
	private static List<Integer> arrlist;
	/** Pivovar je unikatni - jedinacek */
	private static final Pivovar pivovar = new Pivovar();
	/** Fronta dovozu - jedinacek */
	private static final Fronta fronta = new Fronta();
	
	/** Kolik hospod uz si dnes objednalo */
	public static int objednalo_si = 0;	
    
	/**
	 * Inicializuje casovac s nastavenim rychlosti - cim vyssi je vislo v Timeru, tim pomaleji se simulace provede
	 */
	public Casovac(){
		casovac = new Timer(1, new PosluchacCasovace()); // 50	
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
    	den = hodina = minuta = 0;
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
        	try {
				citaciBez();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }
    
    /**
	 * Zajisti beh citace (rotace minut, dni, hodin) 
	 * Probihaji zde vsechna volani - poptavky atp. 
     * @throws IOException 
	 */
	public static void citaciBez() throws IOException {
		String output = "";
		
		// formatovani retezcu
		String hod = (hodina < 10) ? "0" + String.valueOf(hodina) : String.valueOf(hodina);
		String min = (minuta < 10) ? "0" + String.valueOf(minuta) : String.valueOf(minuta);

		
		output = "Dnes je " + String.valueOf(den) + " srpna " + hod + ":" + min + " hodin";
		Hlavni.cas.setText(output);
		//System.out.println(output);
		
	
		if(minuta == 0){
			Hlavni.jtf.addTextLn("+++ Prave je " + hod + ":00 hodin +++");
			output = pivovar.varPivo();
			Hlavni.jtf.addTextLn(output);
			
			// kazdou hodinu inicializuji novy spojovy seznam
			// na nultem indexu objednavky pro tankove, v ostatnich pro sudové v danych regionech 1 - 8
			SpojovySeznam[] poleSeznamu = new SpojovySeznam[9];
			for(int j = 0; j < poleSeznamu.length; j++){
				poleSeznamu[j] = new SpojovySeznam();
			}
			
			// tady se vytvori poradnik pro cely den
			if(hodina == 8){
				poradiObjednavek();
				Hlavni.jtf.addTextLn("  Prijimam objednavky");	
			}
			
			// prijem objednavek
			if(hodina >= 8 && hodina <= 16){
				int pocet_objednavek = pocetObjednavek(hodina);
				objednalo_si += pocet_objednavek;
				
				
				// odbavime kazdou objednavku co prave prisla
				String out1 = "";
				for(int i = 0; i < pocet_objednavek; i++){
					int mnozstvi_piva = pocetSudu();
					//int vzalenost_k_prekladisti = 100;
					int id_hospoda = arrlist.remove(0);
					
					// Vime: index hospody; kolik chteji sudu/hl; cas objednavky; vzdalenost k
					Hlavni.hospody[id_hospoda].setCasObjednavky(den, hodina);
					Hlavni.hospody[id_hospoda].setMnozstvi(mnozstvi_piva);
					

					// pridame objednavku do poleSeznamu na dany index podle oznaceni nebo podle regionu
					int region = 0;
					if(Hlavni.hospody[id_hospoda].typ == 'S'){
						region = Hlavni.hospody[id_hospoda].region; 
					}
					
					// pivovar nebo dane prekladiste - komu uctovat odber?
					if(region != 0){
						Hlavni.prekladiste[region-1].plnych_sudu -= mnozstvi_piva;
					}
					else{
						// jedna se o pivovar		
						Pivovar.litru_piva -= mnozstvi_piva*100;
					}							
					
					poleSeznamu[region].pridej(new Objednavka(mnozstvi_piva, id_hospoda, Hlavni.floydWarshall[region][id_hospoda+9]));	
					//System.out.println("Hosp" + id_hospoda + ": " + mnozstvi_piva + " piva (" + Hlavni.hospody[id_hospoda].typ + ")");
					out1 += "  Hospoda" + (id_hospoda+1) + "(" + Hlavni.hospody[id_hospoda].typ + ") si objednala: " + mnozstvi_piva + " piva\n";
					
					
					
					
					// nyni budeme brat jednotlive objednavky - pocitat cas doruceni a ukladat rovnou do fronty			
					for(int j = 0; j<poleSeznamu.length; j++){						
						while(poleSeznamu[j].prvni != null){
							double casSpozdeni = 0.0; // vykladani nakladu
							Objednavka posledni = poleSeznamu[j].odeber(poleSeznamu[j].prvni.indexHospody); // nejvzdalenejsi hospoda ze vsech, co si objednaly
							int[] indexyHospodNaDaneCeste = zjistiVrcholy(j, posledni.indexHospody); // indexy hospod na nejkratsi ceste
							if(indexyHospodNaDaneCeste != null){ // pokud jsou nejake jine hospody na ceste:
								for(int k = 0; k<indexyHospodNaDaneCeste.length; k++){ // pro vsechny vypocitame cas doruceni objednavky
									Objednavka zjisteni = poleSeznamu[j].odeber(indexyHospodNaDaneCeste[k]); // zjistim, jestli si dana hospoda objednala a v pripade, ze ano, rovnou ji odeberu ze seznamu
									if(zjisteni != null){ // pokud se nic neodstranilo => hospoda si neobjednala
										Objednavka hospodaNaCeste = zjisteni;
										hospodaNaCeste.casDoruceni += casSpozdeni; // cas doruceni vypocteny pro kazdou objednavku na zaklade vzdalenosti v km a v rychlosti v km/h musime zvetsit o dane spozdeni na ceste
										if(j == 0){
											casSpozdeni += hospodaNaCeste.mnozstvi * (1/30.0); // 2 minut = 1/30 hodiny, jde o cisterny
										} else {
											casSpozdeni += hospodaNaCeste.mnozstvi * (1/12.0); // 5 minut = 1/12 hodiny, jde o sudy
										}
										fronta.pridej(hospodaNaCeste); // pridej upravenou objednavku do finalni fronty
									}
								}
							}
							posledni.casDoruceni += casSpozdeni;
							fronta.pridej(posledni); // nakonec pridam tu nejvzdalenejsi na dane ceste
						}						
					}
				}
				
				Hlavni.jtf.addText(out1);
			}
			
			if(hodina == 16){
				Hlavni.jtf.addTextLn("  Prijem objednavek zastaven!!!");
				//System.out.println(fronta.vypis2());
			}
		}
		
		minuta++;

		// ukonceni simulace
		if(den == 1 && hodina == 23 && minuta == 60){
			//System.out.println("konec simulace!!!");
			Hlavni.jtf.addTextLn("SIMULACE DOBEHLA NA KONEC");
			casovac.stop();
		
			VystupSoubor vystup = new VystupSoubor();
			vystup.createHTMLHead();
			vystup.createTableHospod();
			
			for(int i = 0; i <= 3999; i++)
				vystup.vlozHospodu(i);

			vystup.closeTableHospod();
			vystup.createHTMLFoot();
			vystup.neplechaUkoncena();
			
			
			return;
		}
		
		// rotace hodin
		if(minuta == 60){			
			minuta = 0;
			hodina++;
		}	

		// rotace dni
		if(hodina == 24){
			minuta = 0;
			hodina = 0;
			den++;
			
			// kontrola prekladist
			String out_p = "";
			for(int t = 0; t < Hlavni.prekladiste.length; t++){
				out_p += "Prekladiste" + (t+1) + "- zasoby: " + Hlavni.prekladiste[t].plnych_sudu + " sudu\n";
			}
			Hlavni.jtf.addTextLn(out_p);
					
			Hlavni.jtf.addTextLn("  Dnes si objednalo pivo celkem " + objednalo_si + " hospod");
			objednalo_si = 0;
			
			Hlavni.jtf.addTextLn("Zacina " + String.valueOf(den) + ".srpen");
		}
		
		// kazdou hodinu zkontrolujeme jestli lze neco obslouzit - ale rano pockame do desiti na vic objednavek
		// !!! FUNGUJE JEN PRO PRVNI DEN !!!
		double cas_doruceni = 0;
		if((hodina >= 10 && hodina <= 15) && minuta == 30){
			Objednavka pom = fronta.prvni;
			while(pom != null){
				System.out.println("_"+pom.indexHospody + "_" +pom.casDoruceni);
				cas_doruceni = pom.casDoruceni;
				if(cas_doruceni > zbyvaDnesCasuvHodinach()){
					break;
				}
				// stihnu to, tak jdeme na to!
				double kdy_bude = Double.valueOf(hodina) + cas_doruceni;
				Hlavni.hospody[pom.indexHospody].obslouzeno_kdy = String.valueOf(kdy_bude);
	
				pom = pom.dalsi;
			}
		}
    }
	
	private static double zbyvaDnesCasuvHodinach(){
		double cas = 16;
		
		return (cas-hodina);
	}
	
	/**
	 * Vygeneruje pocet objednavek, tak aby to bylo za den 4000
	 * @param hodina Hodina c simulaci mezi 8-16 (vcetne)
	 * @return Kolik hospod si objedna pivo v danou hodinu
	 */
	private static int pocetObjednavek(int hodina){
		int o = 0;
		switch(hodina){
			case 8: o = 476; break;
			case 9: o = 494; break;
			case 10: o = 500; break;
			case 11: o = 494; break;
			case 12: o = 476; break;
			case 13: o = 449; break;
			case 14: o = 413; break;
			case 15: o = 372; break;
			case 16: o = 326; break;
		}
			
		/*
		if(hodina != 15){
			switch(hodina){
				case 8:  o = randInt(100, 200);	break;
				case 9:  o = randInt(400, 500); break;
				case 10: o = randInt(900, 1000); break;
				case 11: o = randInt(600, 700);  break;
				case 12: o = randInt(500, 600);	break;
				case 13: o = randInt(400, 500);	break;
				case 14: o = randInt(300, 400);	break;//zb. 100
			}
			objednalo_si += 0;
		}
		else{
			// je treba dorovnat skore, aby si objednali vsechny hospody
			o = 4000 - objednalo_si;
		}
		*/
		Hlavni.jtf.addTextLn("  Prave si objednalo " + o + " hospod");
		
		return o;
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
	      
	    System.out.println("Dnesni poradnik: " + arrlist);		
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
