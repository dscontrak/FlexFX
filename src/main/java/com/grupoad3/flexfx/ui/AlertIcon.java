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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class AlertIcon extends Alert{

    private Image icon;
    Throwable ex;

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
        getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        

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

    public void setExeption(Throwable exeption) {
        ex = exeption;

        if(ex == null){
            return;
        }

        setTitle("Error alert");
        setHeaderText(ex.getMessage());

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");

        String stackTrace = getStackTrace(ex);
        TextArea textArea = new TextArea();
        textArea.setMaxWidth(200);
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(label, textArea);

        // Set content for Dialog Pane
        getDialogPane().setContent(dialogPaneContent);


    }


    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }




}
