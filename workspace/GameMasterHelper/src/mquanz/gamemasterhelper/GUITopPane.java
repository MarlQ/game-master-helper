package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUITopPane extends JPanel{
	private static final long serialVersionUID = 1L;

	private GUIMain parentFrame;
	
	public GUITopPane(GUIMain parentFrame){

		this.parentFrame = parentFrame;

		int mapNameCount = parentFrame.generalInformation.maps.size();

		String[] mapNameList = new String[mapNameCount];

		for(int i = 0; i < mapNameCount; i++){
			mapNameList[i] = parentFrame.generalInformation.maps.get(i).name;
		}


		JComboBox comboBoxMaps = new JComboBox(mapNameList);
		comboBoxMaps.setEditable(true);
		comboBoxMaps.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				GUITopPane.this.parentFrame.drawingSurface.changeMap(GUITopPane.this.parentFrame.generalInformation.maps.get(cb.getSelectedIndex()));
			}
		});
		add(comboBoxMaps);
		
	}

}
