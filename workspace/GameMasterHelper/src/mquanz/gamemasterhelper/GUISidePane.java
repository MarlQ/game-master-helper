package mquanz.gamemasterhelper;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GUISidePane extends JPanel implements ChangeListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	GUIMain parentFrame;
	GUIMapPopupMenu changeMapPopupMenu;
	JSlider sliderStrokeSize;

	JFormattedTextField textFieldStrokeSize;
	JButton buttonChangeMap;
	JButton lastActiveButton;
	
	public GUISidePane(GUIMain parentFrame) {
		super();
		this.parentFrame = parentFrame;
		setOpaque(true);

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();

		setLayout(gbl);

		changeMapPopupMenu = new GUIMapPopupMenu(this);

		buttonChangeMap = new JButton("Map 01");
		buttonChangeMap.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonChangeMap.setVerticalTextPosition(SwingConstants.CENTER);
		buttonChangeMap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				changeMapPopupMenu.show(buttonChangeMap, buttonChangeMap.getX() + buttonChangeMap.getSize().width,
						buttonChangeMap.getY());
			}
		});

		JButton buttonSave = new JButton("Save");
		buttonSave.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonSave.setVerticalTextPosition(SwingConstants.CENTER);
		buttonSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.saveGame();

			}
		});

		JButton buttonLoad = new JButton("Load");
		buttonLoad.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonLoad.setVerticalTextPosition(SwingConstants.CENTER);
		buttonLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.loadGame();
			}
		});

		JButton buttonSaveToImage = new JButton("Save to Image");
		buttonSaveToImage.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonSaveToImage.setVerticalTextPosition(SwingConstants.CENTER);
		buttonSaveToImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.saveToImage();
			}
		});

		final JButton buttonModeDrag = new JButton();
		buttonModeDrag.setIcon(GeneralInformation.createImageIcon("/gui/tool_drag.png"));
		buttonModeDrag.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeDrag.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModeDrag.setPreferredSize(new Dimension(32,32));
		buttonModeDrag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 1;
				toggleEnabled(buttonModeDrag);
			}
		});
		lastActiveButton = buttonModeDrag;
		buttonModeDrag.setEnabled(false);
		
		final JButton buttonModePencil = new JButton(GeneralInformation.createImageIcon("/gui/tool_pencil.png"));
		buttonModePencil.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModePencil.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModePencil.setPreferredSize(new Dimension(32,32));
		buttonModePencil.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 2;
				toggleEnabled(buttonModePencil);
			}
		});

		final JButton buttonModeLine = new JButton(GeneralInformation.createImageIcon("/gui/tool_line.png"));
		buttonModeLine.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeLine.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModeLine.setPreferredSize(new Dimension(32,32));
		buttonModeLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 3;
				toggleEnabled(buttonModeLine);
			}
		});

		final JButton buttonModeFill = new JButton(GeneralInformation.createImageIcon("/gui/tool_fill.png"));
		buttonModeFill.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeFill.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModeFill.setPreferredSize(new Dimension(32,32));
		buttonModeFill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 4;
				toggleEnabled(buttonModeFill);
			}
		});
		
		JPanel colorChooser = new GUIColorChooser(this);

		sliderStrokeSize = new JSlider(JSlider.HORIZONTAL, GUIMain.STROKE_MIN, GUIMain.STROKE_MAX, GUIMain.STROKE_INIT);
		sliderStrokeSize.addChangeListener(this);
		sliderStrokeSize.setMajorTickSpacing(10);
		sliderStrokeSize.setMinorTickSpacing(1);
		sliderStrokeSize.setPaintTicks(true);

		textFieldStrokeSize = new JFormattedTextField(GUIMain.STROKE_INIT);
		textFieldStrokeSize.setAlignmentX(CENTER_ALIGNMENT);
		textFieldStrokeSize.setColumns(3);
		textFieldStrokeSize.addPropertyChangeListener(this);

		LH.place(0,0,4,1,1,1,"h","c",null,this,gbl,c,buttonChangeMap);
		LH.place(0,1,4,1,1,1,"h","c",null,this,gbl,c,buttonSave);
		LH.place(0,2,4,1,1,1,"h","c",null,this,gbl,c,buttonLoad);
		LH.place(0,3,4,1,1,1,"h","c",null,this,gbl,c,buttonSaveToImage);

		LH.place(1,4,1,1,1,1,"n","e",null,this,gbl,c,buttonModeDrag);
		LH.place(2,4,1,1,0.55,1,"n","w",null,this,gbl,c,buttonModePencil);
		LH.place(1,5,1,1,1,1,"n","e",null,this,gbl,c,buttonModeLine);
		LH.place(2,5,1,1,0.55,1,"n","w",null,this,gbl,c,buttonModeFill);

		LH.place(0,6,3,1,1,1,"n","c",null,this,gbl,c,sliderStrokeSize);
		LH.place(3,6,1,1,0,1,"h","c",null,this,gbl,c,textFieldStrokeSize);
		LH.place(0,7,4,1,1,1,"n","c",null,this,gbl,c,colorChooser);

		setVisible(true);
	}
	private void toggleEnabled(JButton button){
		lastActiveButton.setEnabled(true);
		button.setEnabled(false);
		lastActiveButton = button;
	}
	
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		JSlider source = (JSlider) arg0.getSource();
		int strokeSize = (int) source.getValue();
		textFieldStrokeSize.setValue(strokeSize);
		if (!source.getValueIsAdjusting()) {
			parentFrame.drawingSurface.setDrawingStroke(strokeSize);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		JTextField source = (JTextField) (arg0.getSource());
		if (source == textFieldStrokeSize) {
			try {
				int strokeSize = Integer.parseInt(source.getText());
				if (strokeSize < GUIMain.STROKE_MIN) {
					textFieldStrokeSize.setValue(GUIMain.STROKE_MIN);
					sliderStrokeSize.setValue(GUIMain.STROKE_MIN);
					parentFrame.drawingSurface.setDrawingStroke(GUIMain.STROKE_MIN);
				} else if (strokeSize > GUIMain.STROKE_MAX) {
					textFieldStrokeSize.setValue(GUIMain.STROKE_MAX);
					sliderStrokeSize.setValue(GUIMain.STROKE_MAX);
					parentFrame.drawingSurface.setDrawingStroke(GUIMain.STROKE_MAX);
				} else {
					textFieldStrokeSize.setValue(strokeSize);
					sliderStrokeSize.setValue(strokeSize);
					parentFrame.drawingSurface.setDrawingStroke(strokeSize);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
