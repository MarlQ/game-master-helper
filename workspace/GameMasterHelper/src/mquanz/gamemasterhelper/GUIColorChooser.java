package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIColorChooser extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;

	private Color colorChooserStandardColors[] = {Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.ORANGE, Color.YELLOW, Color.PINK, Color.DARK_GRAY};
	
	private static final int COLOR_CHOOSER_COLOR_COUNT = 10;
	private static final int COLORS_PER_ROW = 5;
	
	private JButton buttonsColorChooser[] = new JButton[COLOR_CHOOSER_COLOR_COUNT];
	private JButton buttonColorChooserPrim;
	private JButton buttonColorChooserSeco;
	
	private GUISidePane parentFrame;
	
	private boolean isPrimSelected = true;
	
	 GUIColorChooser(GUISidePane parentFrame){
		super();
		this.parentFrame = parentFrame;
		
		setOpaque(true);

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gridbagLayout = new GridBagLayout();

		setLayout(gridbagLayout);
		
		JPanel primSecColorPanel = new JPanel();
		primSecColorPanel.setLayout(gridbagLayout);
	
		buttonColorChooserPrim = new JButton();
		buttonColorChooserPrim.addActionListener(this);
		buttonColorChooserPrim.setBackground(Color.BLACK);
		buttonColorChooserPrim.setOpaque(true);
		buttonColorChooserPrim.setPreferredSize(new Dimension(30,30));
		
		buttonColorChooserSeco = new JButton();
		buttonColorChooserSeco.addActionListener(this);
		buttonColorChooserSeco.setBackground(Color.WHITE);
		buttonColorChooserSeco.setOpaque(true);
		buttonColorChooserSeco.setPreferredSize(new Dimension(30,30));
		
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 0;
		c.gridx = 0;
		
		primSecColorPanel.add(buttonColorChooserPrim, c);
		c.gridx++;
		primSecColorPanel.add(buttonColorChooserSeco, c);

		c.gridwidth = COLORS_PER_ROW;
		c.gridx = 0;
		add(primSecColorPanel, c);

		c.gridwidth = 1;
		c.gridy = 1;
		
		for(int i = 0; i < COLOR_CHOOSER_COLOR_COUNT; i++){
			buttonsColorChooser[i] = new JButton();
			buttonsColorChooser[i].addActionListener(this);
			buttonsColorChooser[i].setBackground(colorChooserStandardColors[i]);
			buttonsColorChooser[i].setOpaque(true);
			buttonsColorChooser[i].setPreferredSize(new Dimension(20,20));
			add(buttonsColorChooser[i], c);
			c.gridx++;
			if(Math.floorMod(i+1, COLORS_PER_ROW) == 0){
				c.gridx = 0;
				c.gridy++;
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton source = (JButton) arg0.getSource();
		
		if(source == buttonColorChooserPrim){
			isPrimSelected = true;
		}
		else if(source == buttonColorChooserSeco){
			isPrimSelected = false;
		}else{
			Color chosenColor = source.getBackground();
			if(isPrimSelected){
				this.parentFrame.parentFrame.drawingSurface.setDrawingColorPrim(chosenColor);
				buttonColorChooserPrim.setBackground(chosenColor);
			}
			else{
				this.parentFrame.parentFrame.drawingSurface.setDrawingColorSeco(chosenColor);
				buttonColorChooserSeco.setBackground(chosenColor);
			}
		}
			
		
		// TODO Auto-generated method stub
		
	}
}
