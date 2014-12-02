package semestralka;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * @author Lubos Hubacek
 * @author Michal Horky
 * @version 0.2 alfa
 */
public class Hlavni extends JFrame {
	/** UID tridy */
	private static final long serialVersionUID = 7875882076450107150L;
	
	/** Vypis aktualniho dne a casu */
	public static JLabel cas;
	/** Trida pro zobrazeni vystupu v GUI */
	public static TextFieldDemo jtf;
	/** Jedina instance casovace pro rizeni programu */
	private static final Casovac casovac = new Casovac();

	public static Hospoda[] hospody;
	public static Prekladiste[] prekladiste;		
	public static int[][] floydWarshall;
	public static int[][] predchudci;

	/**
	 * Nastavi zankladni veci ohledne vykresleni okna
	 */
	private void setGUI() {
		this.setSize(1040, 572);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout());
		this.setTitle("KIV/PT - Simulace pivovaru Chmelokvas");
		this.setMinimumSize(new Dimension(1000, 572));
		this.setResizable(false);
	}
	
	/**
	 * Zajisti ze pred jakymkoliv zpusobem vypnuti programu se provede dotaz jeslti jsme si jisti
	 */
	@Override
	public void dispose(){
		int n = JOptionPane.showConfirmDialog(null,
				"Opravdu chcete zavøít aplikaci?",
				"Chmelokvas - Ukonèit?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(n == 0){
			System.exit(0);
		}
	}

	/**
	 * Sestavi menu programu vcetne nastaveni klavesovych zkratek a ikonek
	 * tlacitek; pripravi posluchace pro kliknuti v menu
	 */
	private void addMenu() {
		// Menu - "panel" pro pridavani menu tlacitek
		JMenuBar jMenuBar1 = new JMenuBar();

		// Menu > Program
		JMenu jMenu1 = new JMenu("Program");
		JMenuItem jMenuItem2 = new JMenuItem("Nastavení");
		JMenuItem jMenuItem3 = new JMenuItem("Konec");
		jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
		jMenuItem2.setIcon(new ImageIcon(getClass().getResource("Gear.png")));
		jMenuItem3.setIcon(new ImageIcon(getClass().getResource("shutdown.png")));

		jMenu1.add(jMenuItem2);
		jMenu1.addSeparator();
		jMenu1.add(jMenuItem3);
		
		// Menu > Simulace
		JMenu jMenu2 = new JMenu("Simulace");
		JMenuItem jMenuItem1 = new JMenuItem("Pøidat objednávku");
		JMenuItem jMenuItem6 = new JMenuItem("Další krok simulace");
		JMenuItem jMenuItem7 = new JMenuItem("Start/Stop simulace");
		JMenuItem jMenuItem8 = new JMenuItem("Restart simulace");
		jMenuItem1.setIcon(new ImageIcon(getClass().getResource("Add.png")));
		jMenuItem6.setIcon(new ImageIcon(getClass().getResource("next.png")));
		jMenuItem7.setIcon(new ImageIcon(getClass().getResource("play.png")));
		jMenuItem8.setIcon(new ImageIcon(getClass().getResource("play.png")));

		jMenu2.add(jMenuItem1);
		jMenu2.add(jMenuItem6);
		jMenu2.add(jMenuItem7);
		jMenu2.add(jMenuItem8);
		

		// Menu > Napoveda
		JMenu jMenu3 = new JMenu("Nápovìda");
		JMenuItem jMenuItem4 = new JMenuItem("O programu");
		JMenuItem jMenuItem5 = new JMenuItem("Dokumentace");
		jMenuItem4.setIcon(new ImageIcon(getClass().getResource("About.png")));
		jMenuItem5.setIcon(new ImageIcon(getClass().getResource("pdf.png")));

		jMenu3.add(jMenuItem5);
		jMenu3.add(jMenuItem4);

		// sestavi cele menu a prida jej do stranky
		jMenuBar1.add(jMenu1);
		jMenuBar1.add(jMenu2);
		jMenuBar1.add(jMenu3);
		this.setJMenuBar(jMenuBar1);

		// Pridani listeneru pro obsluhu menu
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// pridejObjednavku(evt);
			}
		});
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new Nastaveni();
			}
		});
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dispose();
			}
		});
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				napoveda(evt);
			}
		});
		jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				otevriDokumentaci();
			}
		});
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					krokujSimulaci();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				simulaceStartStop();
			}
		});
		jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				restartSimulace();
			}
		});
	}

	/**
	 * Zobrazi se pri kliknuti na: Napoveda > O programu
	 * 
	 * @param evt
	 */
	private void napoveda(java.awt.event.ActionEvent evt) {
		String title = "KIV/PT Chmelokvas - O programu";
		String message = ""
				+ "Program vznikl v rámci øešení semestrální práce na pøedmìt \n"
				+ "Programovací techinky na Katedøe Informatiky a Výpoèetní \n"
				+ "techniky na Fakultì Aplikovaných Vìd ZÈU v Plzni.\n\n"
				+ "Autoøi (2014): Luboš Hubáèek, Michal Horký";

		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Otevre prilozenou dokumentaci pokud je v .jar souboru k dispozici nebo skonci errorem
	 */
	private void otevriDokumentaci(){
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File("test.pdf");
		        Desktop.getDesktop().open(myFile);
		    } catch (IllegalArgumentException | IOException ex) {
				String title = "KIV/PT - Simulace pivovaru Chmelokvas - Chyba";
				String message = "Dokumentace ve formátu PDF nebyla nalazena!";

				JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		    }
		}		
		
	}
	
	/**
	 * Provede vynulovani Timeru, vymaze TextField a smaze vystupni soubory
	 */
	private void restartSimulace(){
		int n = JOptionPane.showConfirmDialog(null,
				"Opravdu chcete restartovat aplikaci?",
				"KIV/PT Chmelokvas - Restartovat?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(n == 0){
			casovac.vynuluj();
			
			cas.setText("Dnes je 1 srpna 00:00 hodin");
			jtf.clear();
			jtf.addTextLn("Proveden RESTART simulace");
		}		
	}
	
    
    /**
     * Zastavi nebo znovuzapne sumulaci
     */
    private void simulaceStartStop(){
    	if(Casovac.den == 7 && Casovac.hodina == 23 && Casovac.minuta == 59){
    		String title = "KIV/PT Chmelokvas - Chyba";
    		String message = ""
    				+ "Chyba - nelze pokraèovat v simulaci, je nutno provést restart!";

    		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);    		
    	}
    	else{  
	    	if(casovac.isRunning()){
	    		casovac.stop();	
	    	}
	    	else{
	    		casovac.start();
	    	}
    	}
    }
    
    private void krokujSimulaci() throws IOException{
    	if(casovac.isRunning()){
    		String title = "KIV/PT Chmelokvas - Chyba";
    		String message = ""
    				+ "Pro krokování je nejprve nutné zastavit simulaci!!!";

    		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);    		
    	}
    	else{
    		if(Casovac.den == 7 && Casovac.hodina == 23 && Casovac.minuta == 60){
        		String title = "KIV/PT Chmelokvas - Chyba";
        		String message = ""
        				+ "Simulace už dobìhla na konec!!!";

        		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);    			
    		}
    		else{
    			casovac.udelejKroky(60);	
    		}
    	}    	
    }
 
	/**
	 * Prida a nastavi vlastnosti JPanelu s orientaèním plánkem mapy do okna
	 * 
	 * @return Container obsahujici kreslici platno
	 */
    private Container createLeftPanel(){
    	Container c = new Container();
    	c.setLayout(new FlowLayout());
    	
    	c.add(new Platno(prekladiste, hospody, floydWarshall, floydWarshall));
    	
    	return c;
    }
    
    
    /**
     * Panel s aktualnim casem simulace 
     * 
     * @return Panel s dnem a hodinou - prava cast hlavniho okna GUI
     */
    private JPanel aktualniCas(){
    	JPanel p = new JPanel();
    	p.setLayout(new FlowLayout());
    	p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    	
		cas = new JLabel("Dnes je 1 srpna 00:00 hodin");
		cas.setFont(cas.getFont().deriveFont(32.0f));
		p.add(cas);
		
		return p;
    }
    
    /**
     * Panel, ve kterem je vse, co se nachazi v prave oblasti GUI
     * 
     * @return Panel s dalsim GUI
     */
    private JPanel createRightPanel(){
    	JPanel c = new JPanel();   	
    	c.setPreferredSize(new Dimension(500, 500));
    	c.setLayout(new BorderLayout());
    	
		c.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		c.add(aktualniCas(), BorderLayout.NORTH);
		jtf = new TextFieldDemo();
		
		c.add(jtf.initComponents(), BorderLayout.CENTER);
		jtf.setStart();
		
		JTextArea output = new JTextArea();
		output.setPreferredSize(new Dimension(300, 300));
    	
    	return c;    	
    }
    
    /**
     * Sesumiruje levou a pravou cast JFramu (jeho hlavni obsah!)
     */
    private void createLayoutPanel() {
    	this.setLayout(new FlowLayout());
        this.add(createLeftPanel());
        this.add(createRightPanel());
    }
    
    /**
     * 
     */
	public void nactiData(){
		hospody = new Hospoda[4000];
		prekladiste = new Prekladiste[8];
		floydWarshall = new int[9][4009];
		predchudci = new int[9][4009];
		try {
			BufferedReader br = new BufferedReader(new FileReader("data_hospody.txt"));
			for(int i = 0; i<4000; i++){
				String radka = br.readLine();
				String[] hospoda = radka.split(" ");
				int oznaceni = Integer.valueOf(hospoda[0].substring(8));
				int x = Integer.valueOf(hospoda[1]);
				int y = Integer.valueOf(hospoda[2]);
				char typ = hospoda[3].charAt(0);
				int region = Integer.valueOf(hospoda[4]);
				hospody[i] = new Hospoda(oznaceni, x, y, typ, region);
			}
			br.close();
			br = new BufferedReader(new FileReader("data_prekladiste.txt"));
			for(int i = 0; i<8; i++){
				String radka = br.readLine();
				String[] nase_prekladiste = radka.split(" ");
				int cislo = Integer.valueOf(nase_prekladiste[0].substring(12));
				int x = Integer.valueOf(nase_prekladiste[1]);
				int y = Integer.valueOf(nase_prekladiste[2]);
				prekladiste[cislo - 1] = new Prekladiste(cislo, x, y);
			}
			br.close();
			br = new BufferedReader(new FileReader("FW_matice_cest.txt"));
			for(int i = 0; i<9; i++){
				String radka = br.readLine();
				int delkaRadky = radka.length();
				radka = radka.substring(1, delkaRadky - 1);
				String[] pom = radka.split(", ");
				for(int j = 0; j<pom.length; j++){
					floydWarshall[i][j] = Integer.valueOf(pom[j]);
				}
			}
			br.close();
			br = new BufferedReader(new FileReader("FW_matice_predchudcu.txt"));
			for(int i = 0; i<9; i++){
				String radka = br.readLine();
				int delkaRadky = radka.length();
				radka = radka.substring(1, delkaRadky - 1);
				String[] pom = radka.split(", ");
				for(int j = 0; j<pom.length; j++){
					predchudci[i][j] = Integer.valueOf(pom[j]);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bezparametricky konstruktor, ktery zavola vsechny potrebne metody pro beh
	 * programu
	 * @throws IOException Chybi vstupni soubor(y) 
	 */
	public Hlavni() throws IOException {
		nactiData();

		setGUI();
		addMenu();
		createLayoutPanel();
	}

	/**
	 * Hlavni trida aplikace
	 * 
	 * @param args Argumenty prikazove radky nejsou pri spusteny programu vyzadovany
	 * @throws IOException Chybi vstupni soubor(y)
	 */
	public static void main(String[] args) throws IOException {
		new Hlavni().setVisible(true);
	}
}
