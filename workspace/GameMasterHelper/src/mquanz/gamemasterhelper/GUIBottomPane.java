package mquanz.gamemasterhelper;

import javax.swing.*;

public class GUIBottomPane extends JPanel {

    private JTextField textFieldMouseC;
    private JTextField textFieldDistance;




    public GUIBottomPane(){
        super();

        this.textFieldMouseC = new JTextField("x: " + 0 + " y: " + 0);
        textFieldMouseC.setEditable(false);
        textFieldMouseC.setColumns(10);
        textFieldMouseC.setBorder(null);

        textFieldDistance = new JTextField("");
        textFieldDistance.setEditable(false);
        textFieldDistance.setColumns(20);
        textFieldDistance.setBorder(null);
        add(textFieldMouseC);
        add(textFieldDistance);

    }


    public void updateMouseCoordinates(int x, int y){
        this.textFieldMouseC.setText("x: " + x + " y: " + y);
    }
    public void setLineLength(int length){
        int lengthInMeters = length/GUIMain.METER;
        this.textFieldDistance.setText("Length: " + length + " (" + lengthInMeters + " meters)");
    }

    public void nullLineLength(){
        textFieldDistance.setText("");
    }

}
