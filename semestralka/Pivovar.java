package semestralka;

public class Pivovar {
	/** Pocet prazdnych sudu */
	public static int prazdnych_sudu = 0;
	/** Indikator plnych sudu - [1 sud = 50l] */
	public static int plnych_sudu = 0;
	/** Pocet litru piva - [1hl = 100l] */
	public static int litru_piva = 700000;
	
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
}
