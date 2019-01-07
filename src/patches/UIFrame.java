package patches;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;

import javax.media.j3d.Canvas3D;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UIFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label yearLabel = new Label("Rok: ");
	private Label dayLabel = new Label("Dzien: ");
	
	JPanel canva;
	JPanel data;
	
	
	UIFrame(int width, int height, String title){
		super();
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,width,height);
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.LINE_AXIS));
		canva = new JPanel();
		canva.setPreferredSize(new Dimension(width, height));
		//data = new JPanel();
		//data.setPreferredSize(new Dimension(width, height));
		
		getContentPane().add(canva);
		//getContentPane().add(data);
		
		
	}

	public void prepareUIFrame(Canvas3D canvas3d) {
		canva.setLayout(new BorderLayout());
		canva.add("North", yearLabel);
		canva.add("Center", canvas3d);
		canva.add("South", dayLabel);
		pack();
		setVisible(true);
	}

	private JPanel visualizeModule(Model h){
		JPanel thisPanel = new JPanel();
		//SpringLayout sl = new SpringLayout();
		//thisPanel.setLayout(sl);
		
		if(h!= null){
			JButton elem = new JButton(h.toString());
			thisPanel.add(new Label("bum"));
			thisPanel.add(elem);
			//sl.putConstraint(SpringLayout.WEST, elem, 50, SpringLayout.WEST, thisPanel);
			//sl.putConstraint(SpringLayout.NORTH, elem, 50, SpringLayout.NORTH, thisPanel);
			
		}
		thisPanel.setVisible(true);
		return thisPanel;
	}
	
	
	
}
