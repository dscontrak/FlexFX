/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.services.RssService;
import com.grupoad3.flexfx.process.WorkIndicatorDialog;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.grupoad3.flexfx.util.InputValidatorHelper;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author daniel_serna
 */
public class RssController {

    private WorkIndicatorDialog wd = null;

    private Stage dialogStage;
    private boolean isSaved = false;
    private Rss currentRss;
    // Reference to the main application.
    private MainApp mainApp;

    private Rss rss;

    @FXML
    private TextField txtUrl;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUrl;

    @FXML
    private Label lblDescription;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        /*Object o1 = cbxCategory.getSelectionModel();
        MediaType o2 = (MediaType)cbxCategory.getValue();*/

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setRss(Rss rss) {
        this.rss = rss;
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleGet(ActionEvent event) {
        
        
        
        try {
            //System.out.println("Entro a handleGet");
            URL feedUrl = new URL(txtUrl.getText());
            
            wd = new WorkIndicatorDialog(dialogStage.getScene().getWindow(), "Loading Rss Data...");
 
            wd.addTaskEndNotification(result -> {
               System.out.println(result);
               wd=null; // don't keep the object, cleanup
            });

            wd.exec("123", inputParam -> {
               // Thinks to do...
               // NO ACCESS TO UI ELEMENTS!
               for (int i = 0; i < 2; i++) {
                   System.out.println("Loading data... '123' =->"+inputParam);
                   try {
                      Thread.sleep(1000);
                   }
                   catch (InterruptedException e) {
                      e.printStackTrace();
                   }
               }
               
                try {
                    final SyndFeed feed = new SyndFeedInput().build(new InputStreamReader(feedUrl.openStream()));
                } catch (IOException | IllegalArgumentException | FeedException ex) {
                    Logger.getLogger(RssController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
               return new Integer(1);
            });
            
            

            //System.out.println(feed);
        } catch (MalformedURLException ex) {
            mainApp.showAlertWithEx(ex);        
        }
        
        

    }

    @FXML
    void handleSave(ActionEvent event) {
        RssService rssService;
        InputValidatorHelper validator = isInputValid();

        if (validator.isExistError() == false) {
            rssService = new RssService();
            try {
                currentRss.setTitle(lblTitle.getText());

                rssService.create(currentRss);

                isSaved = true;
                dialogStage.close();
            } catch (IOException ex) {
                isSaved = false;
                ex.printStackTrace();
            }
        } else {
            AlertIcon alert = new AlertIcon(AlertType.WARNING);
            alert.setContentText(validator.getErrorFomatText());
            alert.setIcon(mainApp.getIconoApp());
            alert.showAndWait();
        }
    }

    private InputValidatorHelper isInputValid() {
        InputValidatorHelper validator = new InputValidatorHelper();
        if (validator.isNullOrEmpty(lblTitle.getText())) {
            validator.getErrors().add("No valid Title");
        }

        return validator;
    }

}
