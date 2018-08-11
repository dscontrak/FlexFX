/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class AlertIcon extends Alert{

    private Image icon;
    Exception ex;
    
    //private 
    
    public AlertIcon(AlertType alertType) {
        super(alertType);
        
        if(null == alertType){
            setTitle("Informmation");
        }else switch (alertType) {
            case ERROR:
                setTitle("Error");
                break;
            case INFORMATION:
                setTitle("Informmation");
                break;
            case WARNING:
                setTitle("Warning");
                break;
            case CONFIRMATION:
                setTitle("Confirmation");
                break;    
            default:
                setTitle("Dialog Information");
                break;
        }
        
        setHeaderText(null);
        
    }
    
    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
        
        if(icon != null){
            ((Stage)this.getDialogPane()
                    .getScene().getWindow())
                    .getIcons().add(icon);
        }                
    }

    public void setExeption(Exception exeption) {
        ex = exeption;
        
        if(ex == null){
            return;
        }
        
        setTitle("Exception Dialog");
        setHeaderText("Look, an Exception Dialog");
        setContentText(ex.getMessage());
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();
                
        
        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        getDialogPane().setExpandableContent(expContent);
        
        
    }
    
    
    
    
    
}
