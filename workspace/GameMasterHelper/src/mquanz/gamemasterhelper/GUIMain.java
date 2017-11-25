package mquanz.gamemasterhelper;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class GUIMain extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * TODO: Changing Tooltip for the modes in the bottom right corner
	 * TODO: (SidePane) Toggle for grid
	 * TODO: (SidePane) Snap to grid option
	 * TODO: (SidePane) show distance option
	 * TODO: (Menu) ability to set meter
	 * TODO: Edit Screens for npcs & mapLink (with Dialog)
	 * TODO: Link to a file in the edit screens
	 * TODO: Adding, Deleting, Renaming Maps
	 * TODO: (Menu) Custom Item/Npc-Type Manager
	 * TODO: Portraits For Npcs (that you can enlarge)
	 * TODO: Auto-Resize For Icons (and maybe an option to toggle that off)
	 * TODO: (Menu) Item/Npc/Maplink-Overview
	 * TODO: (Opt.) Object quick info on the side
	 * TODO: Option to change background color, and texture
	 * TODO: (Button) Box Selection
	 * TODO: (Button) Copy & Paste
	 * TODO: (Button) Clear map
	 * TODO: (Button) Cut
	 * TODO: Icons for Pencil, Line and Fill tool
	 * TODO: Flow Diagramm parts
	 * TODO: Furniture
	 * 
	 * OTHER & CLEAN-UP
	 * TODO: set up Github
	 * TODO: (Clean up) gridbag stuff
	 * TODO: (Clean up) versioning
	 * TODO: (Bug) Box by the slider only updates with Enter
	 * 
	 * 
	 * FUTURE THINGS:
	 * TODO: Save and load function
	 * TODO: Minimap
	 * TODO: Placement tool and catalogue (maybe on the left?)
	 * 
	 * 
	 */
	
	//GUIDragScrollPane
	static final boolean SNAP_TO_GRID_DEFAULT = true;
	static final int MODE_DEFAULT = 1;
	
	//GUIDrawingSurface
	static final Color COLOR_PRIM_DEFAULT = Color.BLACK;
	static final Color COLOR_SEC_DEFAULT = Color.WHITE;
	static final Color COLOR_GRID_DEFAULT = Color.DARK_GRAY;
	static final Color COLOR_BACKGROUND_DEFAULT = Color.WHITE;//new Color(150, 120, 100);
	static final int STROKE_MIN = 1;
	static final int STROKE_MAX = 50;
	static final int STROKE_INIT = 4;
	static final int GRID_WIDTH_DEFAULT = 1;
	static final boolean DRAW_GRID_DEFAULT = true;
	static final boolean ALLOW_ANTIALIASING = false;

	//Tempory
	static final int METER = 20;
	
	public GUIMain(String title, GeneralInformation generalInformation){
		super(title);
		this.generalInformation = generalInformation;
	}
	
	GeneralInformation generalInformation;
	GUIDrawingSurface drawingSurface;
	GUIDragScrollPane scrollPane;
	
	public static void main(String[] args){
		
		Runnable r = new Runnable(){
			
			@Override
			public void run(){
				createGUI();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	public static void createGUI(){
		
		GeneralInformation generalInformation = new GeneralInformation();
		GUIMain mainFrame = new GUIMain("Gamemaster helper", generalInformation);
		
		
		
		mainFrame.setContentPane(mainFrame.createContentPane());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.pack();
	
		//mainFrame.setSize(450, 260);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	public JPanel createContentPane(){
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout contentPaneLayout = new GridBagLayout();
		JPanel contentPane = new JPanel();
		contentPane.setLayout(contentPaneLayout);

		GUIBottomPane bottomPane = new GUIBottomPane();
		drawingSurface = new GUIDrawingSurface(generalInformation.maps.get(0), this.generalInformation);
		scrollPane = new GUIDragScrollPane(this.drawingSurface,bottomPane);
		drawingSurface.dragScrollPane = scrollPane;
		
		GUISidePane sidePane = new GUISidePane(this);
		GUITopPane topPane = new GUITopPane(this);


		
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		
		contentPane.add(topPane);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0.8;
		c.gridy = 1;
		//c.insets = new Insets(50, 0, 0, 0);

		contentPane.add(scrollPane, c);
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridx++;
		contentPane.add(sidePane, c);
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		contentPane.add(bottomPane, c);
		
		
		AbstractAction pressedShiftAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
		    public void actionPerformed(ActionEvent e) {	
		    	if(!GUIMain.this.scrollPane.shiftPressed){
					GUIMain.this.scrollPane.shiftPressed = true;
					GUIMain.this.scrollPane.shiftLineHorizontal = 0;
		    	}
		    }
		};
		AbstractAction releasedShiftAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
		    public void actionPerformed(ActionEvent e) {		
					GUIMain.this.scrollPane.shiftPressed = false;
					GUIMain.this.scrollPane.shiftLineHorizontal = 0;
		    }
		};

		AbstractAction pressedDeleteAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
		    public void actionPerformed(ActionEvent e) {		
					if(scrollPane.selectedIcon != null){
						for(GUIObjectEditScreen editScreen : scrollPane.objectEditScreenList){
							if(editScreen.mapObjectIcon == scrollPane.selectedIcon){
								editScreen.dispose();
								scrollPane.objectEditScreenList.remove(editScreen);
								break;
							}
						}
						scrollPane.objectToMove.componentMover.deregisterComponent(scrollPane.selectedIcon);
						scrollPane.objectToMove.remove(scrollPane.selectedIcon);
						scrollPane.objectToMove.mapInformation.itemIcons.remove(scrollPane.selectedIcon);
						scrollPane.selectedIcon = null;
						scrollPane.objectToMove.repaint();
					}
		    }
		};
		
		contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false),
                "pressedShift");
		contentPane.getActionMap().put("pressedShift",
				pressedShiftAction);
		contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("released SHIFT"),"releasedShift");
	    contentPane.getActionMap().put("releasedShift",releasedShiftAction);
		
		contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("DELETE"),
                "pressedDelete");
		contentPane.getActionMap().put("pressedDelete",
				pressedDeleteAction);
		

		return contentPane;
	}
	public void saveToImage(){
		BufferedImage bufferedImage = ScreenImage.createImage(this.drawingSurface);
		
		
		JFileChooser fileChooser = new JFileChooser();	
		if (!(this.generalInformation == null)) {
			boolean wasSaved = false;
			while (!wasSaved) {
				int returnVal = fileChooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					//TODO: FIle Extensions
					File file = fileChooser.getSelectedFile();
					String s = file.getPath();
					try {
						ScreenImage.writeImage(bufferedImage, s);
						wasSaved = true;
						
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(this,
								"Unknown File Path", "Error",
								JOptionPane.WARNING_MESSAGE);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (returnVal == JFileChooser.CANCEL_OPTION) {
					return;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"There was a problem in saving the data. Aborted.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}		
	}
	
	public void saveGame(){
		JFileChooser fileChooser = new JFileChooser();	
		if (!(this.generalInformation == null)) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			boolean wasSaved = false;
			while (!wasSaved) {
				int returnVal = fileChooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					String s = file.getPath();
					try {
						FileSaverLoader.saveData(s, this.generalInformation);
						wasSaved = true;
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(this,
								"Unknown File Path", "Error",
								JOptionPane.WARNING_MESSAGE);
					}
				} else if (returnVal == JFileChooser.CANCEL_OPTION) {
					return;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"There was a problem in saving the data. Aborted.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	public void loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "dat");
		fileChooser.setFileFilter(filter);
		boolean wasLoaded = false;
		GeneralInformation loadedGeneralInformation;

		while (!wasLoaded) {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				String s = file.getPath();

				loadedGeneralInformation = FileSaverLoader.loadData(s);
				if (loadedGeneralInformation == null) {
					JOptionPane.showMessageDialog(this,
							"Incompatible File", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (this.generalInformation == null) {
						wasLoaded = true;
						generalInformation = loadedGeneralInformation;
						drawingSurface.changeMap(generalInformation.maps.get(0));
						//TODO:
					} else {
						//TODO:
						int n = JOptionPane
								.showConfirmDialog(
										this,
										"There is already a game running.\n"
												+ "Are you sure you want to create a new one?\n"
												+ "(all data will be lost)",
										"Warning", JOptionPane.YES_NO_OPTION,
										JOptionPane.WARNING_MESSAGE);
						if (n == JOptionPane.YES_OPTION) {
							wasLoaded = true;
							this.generalInformation = loadedGeneralInformation;
							drawingSurface.changeMap(generalInformation.maps.get(0));
							
						} else if (n == JOptionPane.NO_OPTION) {
							return;

						}

					}
				}

			} else if (returnVal == JFileChooser.CANCEL_OPTION) {
				return;
			}
		}
	}
}
