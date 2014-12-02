package semestralka;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Nastaveni extends JFrame {
	private static final long serialVersionUID = 1L;
	private JComboBox<?> seznamDnu;

	/**
	 * Nastavi zankladni veci ohledne vykresleni okna
	 */
	private void setGUI() {
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(4, 1));
		this.setTitle("KIV/PT - Simulace pivovaru Chmelokvas - Nastavení");
	}
	
	/**
	 * 
	 */
	private JPanel content(){
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		
		JLabel lbl = new JLabel("Zvolte dobu bìhu celé simulace: ");
		g.fill = GridBagConstraints.HORIZONTAL;		
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 3;
		p.add(lbl, g);

		String[] dny = {"1 den", "2 dny", "3 dny", "4 dny", "5 dní", "6 dní", "7 dní"};
		seznamDnu = new JComboBox<>(dny);
		seznamDnu.setSelectedIndex(0);
		g.fill = GridBagConstraints.HORIZONTAL;	
		g.gridx = 3;
		g.gridy = 0;
		g.gridwidth = 1;
		p.add(seznamDnu, g);
		
		
		JCheckBox check = new JCheckBox();
		g.fill = GridBagConstraints.HORIZONTAL;	
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 1;
		p.add(check, g);		
		
		JLabel lbl2 = new JLabel("Rychlá simulace - probìhne celá najednou");
		g.fill = GridBagConstraints.HORIZONTAL;	
		g.gridx = 1;
		g.gridy = 1;
		g.gridwidth = 3;
		p.add(lbl2, g);
		

		JLabel lbl3 = new JLabel("Rychlost simulace [v sekundách]");
		g.fill = GridBagConstraints.HORIZONTAL;	
		g.gridx = 0;
		g.gridy = 3;
		g.gridwidth = 3;
		p.add(lbl3, g);
		
		JSlider rychlost = new JSlider(JSlider.HORIZONTAL, 0, 48, 24);
		rychlost.setMajorTickSpacing(5);
		rychlost.setMinorTickSpacing(1);
		rychlost.setPaintTicks(true);
		rychlost.setPaintLabels(true);
		//rychlost.disable();
		g.fill = GridBagConstraints.HORIZONTAL;	
		g.gridx = 0;
		g.gridy = 4;
		g.gridwidth = 5;
		g.gridheight = 3;
		p.add(rychlost, g);
		
		return p;
	}
	
	public Nastaveni(){
		setGUI();
		JPanel p1 = content();
		this.add(p1);
		this.setVisible(true);
		
	}
}
