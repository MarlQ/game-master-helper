package mquanz.gamemasterhelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class GUIPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	GUIDrawingSurface drawingSurface;
	
	int posX = 0;
	int posY = 0;
	
	
	public JMenuItem makeItemEntry(final ObjectType objectType){
		JMenuItem menuEntry = new JMenuItem(objectType.typeName);
		menuEntry.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUIPopupMenu.this.drawingSurface.addNewIcon(GUIPopupMenu.this.posX, GUIPopupMenu.this.posY, objectType);
			}
		});
		return menuEntry;
	}
	
	
	
	public GUIPopupMenu(GUIDrawingSurface drawingSurface){
		super();
		GeneralInformation generalInformation = drawingSurface.generalInformation;
		this.drawingSurface = drawingSurface;
		
		JMenu newItem = new JMenu("New Item");
		
		for(ItemType itemType : generalInformation.itemTypes){
			newItem.add(makeItemEntry(itemType));
		}
		JMenu newNpc = new JMenu("New Npc");
		for(NpcType npcType : generalInformation.npcTypes){
			newNpc.add(makeItemEntry(npcType));
		}
		JMenu newMapLink = new JMenu("New Map Link");
		for(MapLinkType mapLinkType : generalInformation.mapLinkTypes){
			newMapLink.add(makeItemEntry(mapLinkType));
		}
		
		add(newItem);
		add(newNpc);
		add(newMapLink);
		
	}
	
	
	
	
	
	/**
	
	public GUIPopupMenu(GUIDrawingSurface drawingSurface){
		super();
		this.drawingSurface = drawingSurface;
		
		
		
		JMenuItem newFirearm = new JMenuItem("Firearm");
		JMenu newNpc = new JMenu("New NPC");
		JMenu newMapLink = new JMenu("New Map-Link");
		
		JMenuItem newNpcGeneric = new JMenuItem("Generic");
		JMenuItem newNpcImportant = new JMenuItem("Important");
		JMenuItem newNpcVampire = new JMenuItem("Vampire");
		
		JMenuItem newMapLinkSmall = new JMenuItem("Small");
		
		newText.addActionListener(this);
		newText.setActionCommand(newItemTextCommand);
		newFirearm.addActionListener(this);
		newFirearm.setActionCommand(newItemFirearmCommand);
		
		newNpcGeneric.addActionListener(this);
		newNpcImportant.addActionListener(this);
		newNpcVampire.addActionListener(this);
		newNpcGeneric.setActionCommand(newNPCGenericCommand);
		newNpcImportant.setActionCommand(newNPCImportantCommand);
		newNpcVampire.setActionCommand(newNPCVampireCommand);
		
		newMapLinkSmall.addActionListener(this);
		newMapLinkSmall.setActionCommand(newMapLinkSmallCommand);
		
		newItem.add(newText);
		newItem.add(newFirearm);
		
		newNpc.add(newNpcGeneric);
		newNpc.add(newNpcImportant);
		newNpc.add(newNpcVampire);
	
		newMapLink.add(newMapLinkSmall);
		
		add(newItem);
		add(newNpc);
		add(newMapLink);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		
		if (command.equals(newItemTextCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi", "hi",GeneralInformation.itemTypeText);
		}
		else if (command.equals(newItemFirearmCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi", "hi",GeneralInformation.itemTypeFirearm);
		}
		else if (command.equals(newNPCGenericCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi",GeneralInformation.npcTypeGeneric);
		}
		else if (command.equals(newNPCImportantCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi",GeneralInformation.npcTypeImportant);
		}
		else if (command.equals(newNPCVampireCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi",GeneralInformation.npcTypeVampire);
		}else if (command.equals(newMapLinkSmallCommand)) {
			//TODO:
			drawingSurface.addNewIcon(this.posX, this.posY, "Hi",  2000, 2500, GeneralInformation.testMAPLINK, GeneralInformation.mapLinkType_small);
		}
	}
**/
	
	
	
	
	

}
