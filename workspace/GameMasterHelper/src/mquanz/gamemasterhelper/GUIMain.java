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
	 *
	 * TODO: (Function) Copy & Paste
	 * TODO: Renaming Maps
	 * TODO: Option to change background color, and texture
	 * TODO: Flow Diagram parts
	 * TODO: Furniture (-> z-order)
	 * TODO: (Function) Undo-Redo
	 * TODO: change size of map (-> ask, delete Items)
	 *
	 * Side Pane
	 * TODO: Color Chooser for prim and sec drawing (button to the left)
	 * TODO: Ability to change Color palette (button to the right) and save with Map
	 * TODO: (Tool) ladder creation tool (-> rectangle tool)
	 * TODO: (Tool) Box Selection
	 * TODO: (Tool) Cut (-> Scissors)
	 * TODO: (Opt.) Object quick info on the side
	 *
	 * Menu Bar
	 * TODO: Ability to set meter
	 * TODO: Custom Item/Npc-Type Manager
	 * TODO: Item/Npc/Maplink-Overview
	 *
	 * Bottom Bar:
	 * TODO: Changing Tooltip for the modes in the bottom right corner
	 *
	 * Edit Screen:
	 * TODO: Edit Screens for npcs & mapLink (with Dialog)
	 * TODO: Link to a file in the edit screens
	 * TODO: Portraits For Npcs (that you can enlarge)
	 * TODO: Auto-Resize For Icons (and maybe an option to toggle that off)

	 * OTHER & CLEAN-UP
	 * TODO: (Clean up) line, rect.,etc. draw tool (move to drawing surface)
	 * TODO: (Clean up) versioning
	 * TODO: (Clean up) commenting
	 * TODO: (Bug) Box by the slider only updates with Enter
	 * TODO: (Bug) No snapping to grid on right mouse button
	 * TODO: Move to Java 8 or 9, replace listeners with lambda expressions
	 * 
	 * FUTURE THINGS:
	 * TODO: Save and load function
	 * TODO: Minimap
	 * TODO: Placement tool and catalogue (maybe on the left?)
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

	//Temporary
	static final int METER = 20;
	
	private GUIMain(String title, GeneralInformation generalInformation){
		super(title);
		this.generalInformation = generalInformation;
	}
	
	GeneralInformation generalInformation;
	GUIDrawingSurface drawingSurface;
	GUIDragScrollPane scrollPane;


	GUITopPane topPane;
	ControllerMap mapController;
	
	public static void main(String[] args){
		
		Runnable r = new Runnable(){
			
			@Override
			public void run(){
				createGUI();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	private static void createGUI(){
		
		GeneralInformation generalInformation = new GeneralInformation();
		GUIMain mainFrame = new GUIMain("Gamemaster helper", generalInformation);

		mainFrame.mapController = new ControllerMap(mainFrame);
		
		
		mainFrame.setContentPane(mainFrame.createContentPane());
		mainFrame.setJMenuBar(new GUIMenuBar(mainFrame));

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		mainFrame.pack();
	
		//mainFrame.setSize(450, 260);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	private JPanel createContentPane(){
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		JPanel contentPane = new JPanel();
		contentPane.setLayout(gbl);

		GUIBottomPane bottomPane = new GUIBottomPane();
		drawingSurface = new GUIDrawingSurface(generalInformation.maps.get(0), this.generalInformation);
		scrollPane = new GUIDragScrollPane(this.drawingSurface,bottomPane);
		drawingSurface.dragScrollPane = scrollPane;
		
		GUISidePane sidePane = new GUISidePane(this);
		topPane = new GUITopPane(this);


		LH.place(0,0,1,1,1,0.1,"n","c",null,contentPane,c,topPane);
		LH.place(0,1,1,1,1,0.8,"b","c",null,contentPane,c,scrollPane);
		LH.place(1,1,1,1,0,0.8,"n","c",null,contentPane,c,sidePane);
		LH.place(0,2,2,1,1,0,"h","c",null,contentPane,c,bottomPane);

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
	void saveToImage(){
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
	
	void saveGame(){
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
    void loadGame() {
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
