package mquanz.gamemasterhelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class GUIMapPopupMenu extends JPopupMenu implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	GUISidePane parentFrame;
	ArrayList<String> changeMapCommands = new ArrayList<String>();
	ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();
	int mapCount = 0;
	
	public GUIMapPopupMenu(GUISidePane parentFrame){
		super();
		this.parentFrame = parentFrame;
		
		for(MapInformation mapInformation : this.parentFrame.parentFrame.generalInformation.maps){
					
			addMap(mapInformation);
		}
	}
	public void addMap(MapInformation mapInformation){
		JMenuItem menuItem = new JMenuItem(mapInformation.name);
		String actionCommand = "Map" + mapCount;
		menuItem.addActionListener(this);
		menuItem.setActionCommand(actionCommand);
		this.menuItems.add(menuItem);
		this.changeMapCommands.add(actionCommand);	
		
		add(menuItem);
		this.mapCount++;
	}
	public void deleteMap(int map){
		//TODO:
		
		remove(menuItems.get(map));
		menuItems.remove(map);
		changeMapCommands.remove(map);
		mapCount--;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		int i = 0;
		for(String changeMapCommand : changeMapCommands){
			if(command.equals(changeMapCommand)){
				this.parentFrame.buttonChangeMap.setText(this.parentFrame.parentFrame.generalInformation.maps.get(i).name);
				this.parentFrame.parentFrame.drawingSurface.changeMap(this.parentFrame.parentFrame.generalInformation.maps.get(i));
			}
			i++;
		}		
	}

}
