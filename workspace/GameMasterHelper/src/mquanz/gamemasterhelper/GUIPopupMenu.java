package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private GUIDrawingSurface drawingSurface;
	
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
		CampaignInformation campaignInformation = drawingSurface.campaignInformation;
		this.drawingSurface = drawingSurface;
		
		JMenu newItem = new JMenu("New Item");
		for(ItemType itemType : campaignInformation.itemTypes){
			newItem.add(makeItemEntry(itemType));
		}

		JMenu newNpc = new JMenu("New Npc");
		for(NpcType npcType : campaignInformation.npcTypes){
			newNpc.add(makeItemEntry(npcType));
		}

		JMenu newMapLink = new JMenu("New Map Link");
		for(MapLinkType mapLinkType : campaignInformation.mapLinkTypes){
			newMapLink.add(makeItemEntry(mapLinkType));
		}
		
		add(newItem);
		add(newNpc);
		add(newMapLink);
		
	}
}
