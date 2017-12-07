package mquanz.gamemasterhelper;

import javax.swing.*;

class GUIBottomPane extends JPanel {

    private JTextField textFieldMouseC;
    private JTextField textFieldContextualInfo;

     GUIBottomPane(){
        super();

        this.textFieldMouseC = new JTextField("x: " + 0 + " y: " + 0);
        textFieldMouseC.setEditable(false);
        textFieldMouseC.setColumns(10);
        textFieldMouseC.setBorder(null);

         textFieldContextualInfo = new JTextField("");
         textFieldContextualInfo.setEditable(false);
         textFieldContextualInfo.setColumns(20);
         textFieldContextualInfo.setBorder(null);
        add(textFieldMouseC);
        add(textFieldContextualInfo);

    }


    void updateMouseCoordinates(int x, int y){
        this.textFieldMouseC.setText("x: " + x + " y: " + y);
    }
    void setContextualInfoLineTool(int length){
        int lengthInMeters = length/GUIMain.METER;
        this.textFieldContextualInfo.setText("Length: " + length + " (" + lengthInMeters + " meters)");
    }

    void resetContextualInfoLineTool(){
        textFieldContextualInfo.setText("");
    }

    void setContextualInfoRectTool(int lengthX, int lengthY){

        int lengthInMetersX = lengthX/GUIMain.METER;
        int lengthInMetersY = lengthY/GUIMain.METER;
        this.textFieldContextualInfo.setText("Dimensions: " + lengthX + " x " + lengthY + " (" + lengthInMetersX + " x " + lengthInMetersY + " meters)");
    }

}
