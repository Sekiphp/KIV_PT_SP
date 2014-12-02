package semestralka;


public class SpojovySeznam {
	
	Objednavka prvni;
	
	public SpojovySeznam(){
		prvni = null;
	}
	
	public void pridej(Objednavka nova){
		if(prvni == null){
			prvni = nova;
		} else {
			Objednavka pom = prvni;
			while(nova.vzdalenost <= pom.vzdalenost && pom.dalsi != null){
				pom = pom.dalsi;
			}
			if (pom.dalsi == null && nova.vzdalenost <= pom.vzdalenost) {
				pom.dalsi = nova;
				nova.predchozi = pom;
			} else {
				if(pom.predchozi == null){
					nova.dalsi = pom;
					pom.predchozi = nova;
					prvni = nova;
				} else {
					pom.predchozi.dalsi = nova;
					nova.predchozi = pom.predchozi;
					nova.dalsi = pom;
					pom.predchozi = nova;
				}
			}
		}
	}
	
	public Objednavka odeber(int indexHospody){
		Objednavka pom = null;
		if(prvni != null){
			pom = prvni;
			while (pom != null) {
				if(pom.indexHospody == indexHospody){
					break;
				}
				pom = pom.dalsi;
			}
			if(pom != null){
				if (pom.predchozi == null) {
					if(pom.dalsi == null){
						prvni = null;
					} else {
						prvni = pom.dalsi;
						pom.dalsi.predchozi = null;
						pom.dalsi = null;
					}
				} else if(pom.dalsi == null){
					pom.predchozi.dalsi = null;
					pom.predchozi = null;
				} else {
					pom.predchozi.dalsi = pom.dalsi;
					pom.dalsi.predchozi = pom.predchozi;
					pom.dalsi = null;
					pom.predchozi = null;
				}
			}
		}
		return pom;
	}
	
	public void vypis(){
		Objednavka pom = prvni;
		if(pom == null){
			System.out.println("Ve frontì už nic neni.");
		} else {
			while(true){
				System.out.println(pom.mnozstvi + " " + pom.indexHospody + " " + pom.vzdalenost + " " + pom.casDoruceni);
				if(pom.dalsi == null){
					break;
				} else {
					pom = pom.dalsi;
				}
			}
		}
		System.out.println("KONEC VYPSANI");
	}
	
}
