package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUITopPane extends JPanel{
	private static final long serialVersionUID = 1L;

	private GUIMain parentFrame;

	JComboBox comboBoxMaps;
	GUIDialogMapList dialogMapList;
	
	public GUITopPane(GUIMain parentFrame){

		this.parentFrame = parentFrame;

		comboBoxMaps = new JComboBox();

		for(MapInformation map : parentFrame.generalInformation.maps){
			comboBoxMaps.addItem(map);
		}
		comboBoxMaps.setEditable(false);
		comboBoxMaps.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				parentFrame.mapController.changeMap((MapInformation)cb.getSelectedItem());
			}
		});
		add(comboBoxMaps);

		JButton buttonOpenMapList = new JButton("+");
		buttonOpenMapList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(GUITopPane.this.dialogMapList != null){
					GUITopPane.this.dialogMapList.dispose();
				}
				GUITopPane.this.dialogMapList = new GUIDialogMapList(parentFrame);
			}
		});

		add(buttonOpenMapList);
		
	}


}
