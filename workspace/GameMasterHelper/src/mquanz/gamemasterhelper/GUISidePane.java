package mquanz.gamemasterhelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		GridBagLayout gridbagLayout = new GridBagLayout();

		setLayout(gridbagLayout);

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
		buttonModeDrag.setIcon(GeneralInformation.createImageIcon("/gui/DragMode.png"));
		buttonModeDrag.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeDrag.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModeDrag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 1;
				toggleEnabled(buttonModeDrag);
			}
		});
		lastActiveButton = buttonModeDrag;
		buttonModeDrag.setEnabled(false);
		
		final JButton buttonModePencil = new JButton("Pencil");
		buttonModePencil.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModePencil.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModePencil.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 2;
				toggleEnabled(buttonModePencil);
			}
		});

		final JButton buttonModeLine = new JButton("Line");
		buttonModeLine.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeLine.setVerticalTextPosition(SwingConstants.CENTER);
		buttonModeLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 3;
				toggleEnabled(buttonModeLine);
			}
		});

		final JButton buttonModeFill = new JButton("Fill");
		buttonModeFill.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonModeFill.setVerticalTextPosition(SwingConstants.CENTER);
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

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(buttonChangeMap, c);
		c.gridy = 1;
		add(buttonSave, c);
		c.gridy = 2;
		add(buttonLoad, c);
		c.gridy = 3;
		add(buttonSaveToImage, c);
		c.gridy = 4;
		add(buttonModeDrag, c);
		c.gridy = 5;
		add(buttonModePencil, c);
		c.gridy = 6;
		add(buttonModeLine, c);
		c.gridy = 7;
		add(buttonModeFill, c);
		c.gridy = 8;
		// c.weightx = 0;
		add(sliderStrokeSize, c);
		c.gridx = 1;
		
		// c.weightx = 0.2;
		add(textFieldStrokeSize, c);
		c.gridy = 9;
		c.gridx = 0;
		c.fill = GridBagConstraints.CENTER;
		add(colorChooser, c);
		

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
