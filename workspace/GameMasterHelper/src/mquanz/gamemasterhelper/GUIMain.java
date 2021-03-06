package mquanz.gamemasterhelper;

import com.jtattoo.plaf.texture.TextureLookAndFeel;

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
import java.util.Properties;


public class GUIMain extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * TODO: Map Link (link to other link or just a point, 1-way, 2-way)
	 * TODO: Standardization of icon size, grid size (-> meter)
	 * TODO: Zoom Function
	 * TODO: (Function) Undo-Redo
	 *
	 * TODO: Stair Tool (arrows, curve mode, corner mode)
	 * TODO: (Function) Copy & Paste (for objects and for drawing)
	 * TODO: Option to change background color, and texture
	 * TODO: Flow Diagram parts
	 * TODO: Furniture (-> z-order)
	 *
	 * Side Pane
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
	 *
	 * OTHER & CLEAN-UP
	 * TODO: (Clean up) line, rect.,etc. draw tool (move to drawing surface)
	 * TODO: (Clean up) versioning
	 * TODO: (Clean up) commenting
	 * TODO: (GUI) Add graphic to show selected color in ColorChooser and edit palette
	 * TODO: (Bug) Remove transparency slider from color chooser (buggy)
	 * TODO: (Bug) Box by the slider only updates with Enter
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
	static final double STAIR_TOOL_STEP_WIDTH = 4; //in Pixels
	
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

	//GUIColorChooser
	static final Color COLOR_CHOOSER_STANDARD_PALETTE[] = {Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.ORANGE, Color.YELLOW, Color.PINK, Color.DARK_GRAY};
	static final int COLOR_CHOOSER_COLOR_COUNT = 15;
	static final int COLORS_PER_ROW = 5;

	//TODO: Temporary
	static final int METER = 20;
	
	private GUIMain(String title, CampaignInformation campaignInformation){
		super(title);
		this.campaignInformation = campaignInformation;
	}
	
	CampaignInformation campaignInformation;
	GUIDrawingSurface drawingSurface;
	GUIDragScrollPane scrollPane;
	GUITopPane topPane;
	ControllerMap mapController;
	ControllerMapObject mapObjectController;
	
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
		Properties props = new Properties();
		props.put("logoString", "");
		TextureLookAndFeel.setCurrentTheme(props);
		com.jtattoo.plaf.texture.TextureLookAndFeel.setTheme("Leather","","");
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		CampaignInformation campaignInformation = new CampaignInformation();
		GUIMain mainFrame = new GUIMain("Gamemaster helper", campaignInformation);
		mainFrame.mapController = new ControllerMap(mainFrame);
		mainFrame.mapObjectController = new ControllerMapObject(mainFrame);

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
		drawingSurface = new GUIDrawingSurface(campaignInformation.maps.get(0), this.campaignInformation, this);
		scrollPane = new GUIDragScrollPane(this.drawingSurface,bottomPane,this);
		drawingSurface.dragScrollPane = scrollPane;
		
		GUISidePane sidePane = new GUISidePane(this);
		topPane = new GUITopPane(this);

		LH.place(0,0,1,1,1,0.1,"n","c",null,contentPane,c,topPane);
		LH.place(0,1,1,1,1,0.8,"b","c",null,contentPane,c,scrollPane);
		LH.place(1,1,1,1,0,0.8,"n","c",null,contentPane,c,sidePane);
		LH.place(0,2,2,1,1,0,"h","c",null,contentPane,c,bottomPane);

		setupKeybindings(contentPane);

		return contentPane;
	}
	void setupKeybindings(JPanel contentPane){
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
				mapObjectController.deleteSelectedObjects();
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
	}

	void saveToImage(){
		BufferedImage bufferedImage = ScreenImage.createImage(this.drawingSurface);

		JFileChooser fileChooser = new JFileChooser();	
		if (!(this.campaignInformation == null)) {
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
		if (!(this.campaignInformation == null)) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			boolean wasSaved = false;
			while (!wasSaved) {
				int returnVal = fileChooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fileChooser.getSelectedFile();
					String s = file.getPath();
					try {
						FileSaverLoader.saveData(s, this.campaignInformation);
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
		CampaignInformation loadedCampaignInformation;

		while (!wasLoaded) {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				String s = file.getPath();

				loadedCampaignInformation = FileSaverLoader.loadData(s);
				if (loadedCampaignInformation == null) {
					JOptionPane.showMessageDialog(this,
							"Incompatible File", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (this.campaignInformation == null) {
						wasLoaded = true;
						campaignInformation = loadedCampaignInformation;
						drawingSurface.changeMap(campaignInformation.maps.get(0));
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
							this.campaignInformation = loadedCampaignInformation;
							drawingSurface.changeMap(campaignInformation.maps.get(0));
							
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
