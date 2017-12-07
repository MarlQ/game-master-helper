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
	private JSlider sliderStrokeSize;

	private JFormattedTextField textFieldStrokeSize;
	private JButton lastActiveButton;
	
	GUISidePane(GUIMain parentFrame) {
		super();
		this.parentFrame = parentFrame;
		setOpaque(true);

		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();

		setLayout(gbl);

		final JButton buttonModeDrag = new JButton();
		buttonModeDrag.setIcon(GeneralInformation.createImageIcon("res/gui/tool_drag.png"));
		buttonModeDrag.setPreferredSize(new Dimension(32,32));
		buttonModeDrag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 1;
				toggleEnabled(buttonModeDrag);
			}
		});
		lastActiveButton = buttonModeDrag;
		buttonModeDrag.setEnabled(false);
		
		final JButton buttonModePencil = new JButton(GeneralInformation.createImageIcon("res/gui/tool_pencil.png"));
		buttonModePencil.setPreferredSize(new Dimension(32,32));
		buttonModePencil.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 2;
				toggleEnabled(buttonModePencil);
			}
		});

		final JButton buttonModeLine = new JButton(GeneralInformation.createImageIcon("res/gui/tool_line.png"));
		buttonModeLine.setPreferredSize(new Dimension(32,32));
		buttonModeLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 3;
				toggleEnabled(buttonModeLine);
			}
		});

		final JButton buttonModeFill = new JButton(GeneralInformation.createImageIcon("res/gui/tool_fill.png"));
		buttonModeFill.setPreferredSize(new Dimension(32,32));
		buttonModeFill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 4;
				toggleEnabled(buttonModeFill);
			}
		});

		final JButton buttonModeRect = new JButton(GeneralInformation.createImageIcon("res/gui/tool_rectangle.png"));
		buttonModeRect.setPreferredSize(new Dimension(32,32));
		buttonModeRect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.mode = 5;
				toggleEnabled(buttonModeRect);
			}
		});


		final JToggleButton buttonDrawGrid = new JToggleButton(GeneralInformation.createImageIcon("res/gui/option_drawgrid.png"));
		buttonDrawGrid.setPreferredSize(new Dimension(32,32));
		buttonDrawGrid.setSelected(GUIMain.DRAW_GRID_DEFAULT);
		buttonDrawGrid.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.objectToMove.drawGrid = !GUISidePane.this.parentFrame.scrollPane.objectToMove.drawGrid;
				GUISidePane.this.parentFrame.scrollPane.objectToMove.repaint();
			}
		});

		final JToggleButton buttonSnapGrid = new JToggleButton(GeneralInformation.createImageIcon("res/gui/option_snapgrid.png"));
		buttonSnapGrid.setPreferredSize(new Dimension(32,32));
		buttonSnapGrid.setSelected(GUIMain.SNAP_TO_GRID_DEFAULT);
		buttonSnapGrid.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GUISidePane.this.parentFrame.scrollPane.snapToGrid = !GUISidePane.this.parentFrame.scrollPane.snapToGrid;
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


		LH.place(1,4,1,1,1,1,"n","e",null,this,gbl,c,buttonModeDrag);
		LH.place(2,4,1,1,0.55,1,"n","w",null,this,gbl,c,buttonModePencil);
		LH.place(1,5,1,1,1,1,"n","e",null,this,gbl,c,buttonModeLine);
		LH.place(2,5,1,1,0.55,1,"n","w",null,this,gbl,c,buttonModeFill);
		LH.place(1,6,1,1,1,1,"n","e",null,this,gbl,c,buttonModeRect);

		LH.place(1,7,1,1,1,1,"n","e",null,this,gbl,c,buttonDrawGrid);
		LH.place(2,7,1,1,0.55,1,"n","w",null,this,gbl,c,buttonSnapGrid);

		LH.place(0,8,3,1,1,1,"n","c",null,this,gbl,c,sliderStrokeSize);
		LH.place(3,8,1,1,0,1,"h","c",null,this,gbl,c,textFieldStrokeSize);
		LH.place(0,9,4,1,1,1,"n","c",null,this,gbl,c,colorChooser);

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
