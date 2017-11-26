package mquanz.gamemasterhelper;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


class GUIObjectEditScreen extends JDialog {
	private static final long serialVersionUID = 1L;
	
	
	private JButton changeIconButton;
	//Item item;
	MapObjectIcon mapObjectIcon;
	private ArrayList<GUIObjectEditScreen> editScreenList;
	
	GUIObjectEditScreen(JFrame parentFrame,MapObjectIcon mapObjectIcon, ArrayList<GUIObjectEditScreen> editScreenList){
		super(parentFrame,"Edit");

		MapObject mapObject = mapObjectIcon.mapObject;	
		
		this.editScreenList = editScreenList;
		this.mapObjectIcon = mapObjectIcon;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				GUIObjectEditScreen.this.editScreenList.remove(GUIObjectEditScreen.this);
				dispose();
				// TODO Auto-generated method stub
				
			}
		});
		

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
	
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
						imageIcon = GeneralInformation.createImageIcon(file.toURI().toURL());
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(imageIcon != null){
						ImageIcon resizedIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
						GUIObjectEditScreen.this.changeIconButton.setIcon(resizedIcon);
						//ObjectEditScreen.this.changeIconButton.repaint();
						//ObjectEditScreen.this.item.icon = imageIcon;
						GUIObjectEditScreen.this.mapObjectIcon.mapObject.icon = imageIcon;
						GUIObjectEditScreen.this.mapObjectIcon.updateIcon();
					}
					else{
						//TODO:
					}
					
				}
				
			}
			
		});
		
		if(mapObject.getClass() == Item.class){
			Item item = (Item)mapObject;
			
			JTextField textAreaDescrDesc = new JTextField("Description");
			textAreaDescrDesc.setEditable(false);
			JTextArea textAreaDescr = new JTextArea(item.descr);
			textAreaDescr.setPreferredSize(new Dimension( 500, 300));
			
			LH.place(0,0,4,1,1,1,"n","c",null,this,gbl,c,textFieldName);
			LH.place(1,1,1,1,0,0,"n","c",null,this,gbl,c,changeTextColorDesc);
			LH.place(2,1,1,1,0,0,"n","c",null,this,gbl,c,buttonChangeTextColor);
			LH.place(3,1,1,1,1,1,"n","e",null,this,gbl,c,iconDesc);
			LH.place(3,2,1,1,1,1,"n","e",null,this,gbl,c,changeIconButton);
			LH.place(0,3,4,1,0,0,"n","c",null,this,gbl,c,textAreaDescrDesc);
			LH.place(0,4,4,1,1,1,"n","c",null,this,gbl,c,textAreaDescr);
			
			
			
		}

		setVisible(true);
		pack();
	}
}
