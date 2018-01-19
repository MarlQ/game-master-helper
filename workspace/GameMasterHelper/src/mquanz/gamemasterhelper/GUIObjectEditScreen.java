package mquanz.gamemasterhelper;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


class GUIObjectEditScreen extends JDialog {
	private static final long serialVersionUID = 1L;
	
	
	private JButton changeIconButton;
	MapObjectIcon mapObjectIcon;
	MapInformation currentMap;
	private GUIMain mainFrame;
	private ArrayList<GUIObjectEditScreen> editScreenList;

	GUIObjectEditScreen(GUIMain parentFrame,MapObjectIcon mapObjectIcon, ArrayList<GUIObjectEditScreen> editScreenList, MapInformation currentMap){
		super(parentFrame,"Edit");
		this.editScreenList = editScreenList;
		this.mapObjectIcon = mapObjectIcon;
		this.mainFrame = parentFrame;
		this.currentMap = currentMap;

		MapObject mapObject = mapObjectIcon.mapObject;

		if(mapObject.getClass() == Item.class){
			//Item Edit Screen
			createItemEditScreen();

		} else if(mapObject.getClass() == Npc.class){
			//NPC Edit Screen
			createNPCEditScreen();
		} else if(mapObject.getClass() == MapLink.class){
			//Map LInk Edit Screen
			createMapLinkEditScreen();
		}
		setVisible(true);
		pack();
	}



	private void createBasicGUI(GridBagLayout gbl, GridBagConstraints c){
		MapObject mapObject = mapObjectIcon.mapObject;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				GUIObjectEditScreen.this.editScreenList.remove(GUIObjectEditScreen.this);
				dispose();
			}
		});

		JTextField textFieldName = new JTextField(mapObject.name);
		textFieldName.setColumns(20);

		JTextField changeTextColorDesc = new JTextField("Text color:");
		changeTextColorDesc.setEditable(false);
		JButton buttonChangeTextColor = new JButton();
		buttonChangeTextColor.setBackground(mapObject.textColor);
		buttonChangeTextColor.setOpaque(true);
		buttonChangeTextColor.setPreferredSize(new Dimension(30,30));

		JTextField iconDesc = new JTextField("Custom Icon");
		iconDesc.setEditable(false);
		this.changeIconButton = new JButton();
		changeIconButton.setPreferredSize(new Dimension(64,64));
		if (mapObject.icon != null){
			ImageIcon imageIcon = new ImageIcon(mapObject.icon.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
			changeIconButton.setIcon(imageIcon);
		}

		changeIconButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "png", "jpg", "jpeg", "gif", "bmp");
				fileChooser.setFileFilter(filter);

				if (fileChooser.showOpenDialog(GUIObjectEditScreen.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					ImageIcon imageIcon = null;
					try {
						imageIcon = CampaignInformation.createImageIcon(file.toURI().toURL());
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(imageIcon != null){
						ImageIcon resizedIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
						GUIObjectEditScreen.this.changeIconButton.setIcon(resizedIcon);
						GUIObjectEditScreen.this.mapObjectIcon.mapObject.icon = imageIcon;
						GUIObjectEditScreen.this.mapObjectIcon.updateIcon();
					}
					else{
						//TODO:
					}

				}

			}

		});

		LH.place(0,0,4,1,1,1,"n","c",null,this,c,textFieldName);
		LH.place(1,1,1,1,0,0,"n","c",null,this,c,changeTextColorDesc);
		LH.place(2,1,1,1,0,0,"n","c",null,this,c,buttonChangeTextColor);
		LH.place(3,1,1,1,1,1,"n","e",null,this,c,iconDesc);
		LH.place(3,2,1,1,1,1,"n","e",null,this,c,changeIconButton);
	}

	MapInformation tempMap;
	int tempMapX;
	int tempMapY;
	MapLink tempTwoWayLink;

	private void createMapLinkEditScreen(){
		MapLink mapLink = (MapLink)mapObjectIcon.mapObject;
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		tempMap = mapLink.map;
		tempMapX = mapLink.linkPosX;
		tempMapY = mapLink.linkPosY;
		tempTwoWayLink = mapLink.twoWayLink;

		createBasicGUI(gbl,c);

		JTextField textFieldLinkDescr = new JTextField("Links to");
		textFieldLinkDescr.setEditable(false);
		JRadioButton radioButtonOneWay = new JRadioButton("1-way");
		JRadioButton radioButtonTwoWay = new JRadioButton("2-way");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radioButtonOneWay);
		buttonGroup.add(radioButtonTwoWay);

		JTextField textFieldMapDescr = new JTextField("Map");
		JComboBox comboBoxMaps = new JComboBox();
		for(MapInformation map : mainFrame.campaignInformation.maps){
			comboBoxMaps.addItem(map);
		}
		comboBoxMaps.setEditable(false);
		comboBoxMaps.setSelectedItem(mapLink.map);
		comboBoxMaps.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				tempMap = (MapInformation)cb.getSelectedItem();
			}
		});

		textFieldMapDescr.setEditable(false);
		JTextField textFieldXDescr = new JTextField("X");
		JFormattedTextField textFieldX = new JFormattedTextField(mapLink.linkPosX);
		textFieldX.setColumns(8);
		textFieldX.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				int value = (int) textFieldX.getValue();
				if(mapLink.map == null || value < 0){
					textFieldX.setValue(0);
					tempMapX = 0;
				}
				else if(value > tempMap.mapSize.width){
					textFieldX.setValue(tempMap.mapSize.width);
					tempMapX = tempMap.mapSize.width;
				}
				else{
					tempMapX = value;
				}
			}
		});
		textFieldXDescr.setEditable(false);
		JTextField textFieldYDescr = new JTextField("Y");
		textFieldYDescr.setEditable(false);
		JFormattedTextField textFieldY = new JFormattedTextField(mapLink.linkPosY);
		textFieldY.setColumns(8);
		textFieldY.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				int value = (int) textFieldY.getValue();
				if(mapLink.map == null || value < 0){
					textFieldY.setValue(0);
					tempMapY = 0;
				}
				else if(value > tempMap.mapSize.height){
					textFieldY.setValue(tempMap.mapSize.height);
					tempMapY = tempMap.mapSize.height;
				}
				else{
					tempMapY = value;
				}
			}
		});

		JTextField textFieldTwoWayLinkDescr = new JTextField("Link");
		textFieldTwoWayLinkDescr.setEditable(false);
		JComboBox comboBoxMapLinks = new JComboBox();
		for(MapInformation map : mainFrame.campaignInformation.maps){
			for(MapObjectIcon mapObjectIcon : map.itemIcons){
				if(mapObjectIcon.mapObject.getClass() == MapLink.class){
					MapLink ml = (MapLink) mapObjectIcon.mapObject;
					if(ml != mapLink){
						comboBoxMapLinks.addItem(ml);
					}
				}
			}
		}
		if(mapLink.twoWayLink != null){
			comboBoxMaps.setSelectedItem(mapLink.twoWayLink);
		}
		comboBoxMapLinks.setEditable(false);
		comboBoxMapLinks.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				tempTwoWayLink = (MapLink) cb.getSelectedItem();
			}
		});
		tempTwoWayLink = (MapLink) comboBoxMapLinks.getSelectedItem();
		radioButtonOneWay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBoxMaps.setEnabled(true);
				textFieldX.setEnabled(true);
				textFieldY.setEnabled(true);
				comboBoxMapLinks.setEnabled(false);
			}
		});
		radioButtonTwoWay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBoxMaps.setEnabled(false);
				textFieldX.setEnabled(false);
				textFieldY.setEnabled(false);
				comboBoxMapLinks.setEnabled(true);
				if(currentMap == null){
					System.out.println(mapObjectIcon.posX);
				}
				tempMap = currentMap;
				tempMapX = mapObjectIcon.posX;
				tempMapY = mapObjectIcon.posY;
			}
		});
		if(mapLink.isTwoWayLink){
			radioButtonTwoWay.setSelected(true);
			comboBoxMaps.setEnabled(false);
			textFieldX.setEnabled(false);
			textFieldY.setEnabled(false);

		}
		else{
			radioButtonOneWay.setSelected(true);
			comboBoxMapLinks.setEnabled(false);
		}

		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				GUIObjectEditScreen.this.editScreenList.remove(GUIObjectEditScreen.this);
				mapLink.map = tempMap;
				mapLink.linkPosX = tempMapX;
				mapLink.linkPosY = tempMapY;

				if(radioButtonTwoWay.isSelected()){
					mapLink.twoWayLink = tempTwoWayLink;
					if(mapLink.twoWayLink != null){
						mapLink.isTwoWayLink = true;
						mapLink.twoWayLink.isTwoWayLink = true;
						mapLink.twoWayLink.twoWayLink = mapLink;
					}
				} else{
					mapLink.isTwoWayLink = false;
					if(mapLink.twoWayLink != null){
						mapLink.twoWayLink.isTwoWayLink = false;
						mapLink.twoWayLink.twoWayLink = null;
						mapLink.twoWayLink = null;
					}
				}
			}
		});
		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIObjectEditScreen.this.editScreenList.remove(GUIObjectEditScreen.this);
				dispose();
				tempMap = null;
			}
		});

		JPanel panelButtons = new JPanel();
		panelButtons.add(buttonOK);
		panelButtons.add(buttonCancel);

		LH.place(0,3,4,1,0,0,"n","c",null,this,c,textFieldLinkDescr);
		LH.place(0,4,1,1,0,0,"n","c",null,this,c,radioButtonOneWay);
		LH.place(2,4,1,1,0,0,"n","w",null,this,c,radioButtonTwoWay);
		LH.place(0,5,1,1,0,0,"n","c",null,this,c,textFieldMapDescr);
		LH.place(1,5,1,1,0,0,"v","w",null,this,c,comboBoxMaps);
		LH.place(2,5,1,1,0,0,"n","c",null,this,c,textFieldTwoWayLinkDescr);
		LH.place(3,5,1,1,0,0,"v","w",null,this,c,comboBoxMapLinks);
		LH.place(0,6,1,1,0,0,"n","c",null,this,c,textFieldXDescr);
		LH.place(1,6,1,1,0,0,"v","w",null,this,c,textFieldX);
		LH.place(0,7,1,1,0,0,"n","c",null,this,c,textFieldYDescr);
		LH.place(1,7,1,1,0,0,"v","w",null,this,c,textFieldY);
		LH.place(0,8,4,1,0,0,"v","c",null,this,c,panelButtons);
	}

	private void createNPCEditScreen(){
		Npc npc = (Npc)mapObjectIcon.mapObject;

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		createBasicGUI(gbl,c);

		JTextField textFieldInventoryDesc = new JTextField("Inventory");
		textFieldInventoryDesc.setEditable(false);

		LH.place(0,3,4,1,0,0,"n","c",null,this,c,textFieldInventoryDesc);

		if(npc.inventory != null){
			for(int i = 0; i < npc.inventory.size(); i++){
				Item item = npc.inventory.get(i);

				JButton buttonItemIcon = new JButton();
				buttonItemIcon.setPreferredSize(new Dimension(64,64));

				if (item.icon != null){
					ImageIcon imageIcon = new ImageIcon(item.icon.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
					buttonItemIcon.setIcon(imageIcon);
				}

				JTextField textFieldItemName = new JTextField(item.name);
				textFieldItemName.setColumns(20);

				Insets newInsets = new Insets(10,15,10,15);

				if(((i+1)%2) == 0){
					LH.place(2,3+i,1,1,0,0,"n","e",newInsets,this,c,buttonItemIcon);
					LH.place(3,3+i,1,1,1,0,"n","w",null,this,c,textFieldItemName);
				}
				else{
					LH.place(0,4+i,1,1,1,0,"n","e", newInsets,this,c,buttonItemIcon);
					LH.place(1,4+i,1,1,0,0,"n","w",null,this,c,textFieldItemName);

				}
			}
		}
	}

	private void createItemEditScreen(){
		Item item = (Item)mapObjectIcon.mapObject;

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		createBasicGUI(gbl,c);

		JTextField textAreaDescrDesc = new JTextField("Description");
		textAreaDescrDesc.setEditable(false);
		JTextArea textAreaDescr = new JTextArea(item.descr);
		textAreaDescr.setPreferredSize(new Dimension( 500, 300));

		LH.place(0,3,4,1,0,0,"n","c",null,this,c,textAreaDescrDesc);
		LH.place(0,4,4,1,1,1,"n","c",null,this,c,textAreaDescr);

	}




}
