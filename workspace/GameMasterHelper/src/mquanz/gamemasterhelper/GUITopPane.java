package mquanz.gamemasterhelper;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUITopPane extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;

	GUIMain parentFrame;
	JTextField mapNameTextField;
	
	public GUITopPane(GUIMain parentFrame){
		mapNameTextField = new JTextField(parentFrame.drawingSurface.mapInformation.name);
		mapNameTextField.setSize(mapNameTextField.getPreferredSize());
		
		//add(mapNameTextField);
		//mapNameTextField.addPropertyChangeListener(this);
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		JTextField source = (JTextField) (arg0.getSource());
		if(source == mapNameTextField){
			mapNameTextField.setText(source.getText());
			parentFrame.drawingSurface.mapInformation.name = source.getText();
		}
		
	}
}
