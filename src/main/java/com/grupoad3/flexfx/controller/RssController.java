/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.ConfigApp;
import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.services.RssService;
import com.grupoad3.flexfx.process.ServiceRssTask;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.grupoad3.flexfx.util.InputValidatorHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author daniel_serna
 */
public class RssController {

    ServiceRssTask serviceRssService;

    private Stage dialogStage;
    private boolean isSaved = false;
    //private Rss currentRss;
    // Reference to the main application.
    private MainApp mainApp;

    private Rss currentRss;

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtWeb;

    @FXML
    private TextField txtDescription;

    @FXML
    private HBox hboxProgress;

    @FXML
    private Label lblProgress;

    @FXML
    private Button btnSave;

     @FXML
    private Button btnGet;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        serviceRssService = new ServiceRssTask();
        hboxProgress.setVisible(false);

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

    public void setCurrentRss(Rss rss) {
        this.currentRss = rss;
        if(currentRss != null && currentRss.getId() != null && currentRss.getId() != 0){
            txtUrl.setDisable(true);
            btnGet.setDisable(true);

            activeEdition();
            mapToForm();
        }else{
            desactiveEdition();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    private void activeEdition(){
        txtDescription.setDisable(false);
        txtTitle.setDisable(false);
        btnSave.setDisable(false);

        txtTitle.requestFocus();

    }

    private void desactiveEdition(){
        txtDescription.setDisable(true);
        txtTitle.setDisable(true);
        btnSave.setDisable(true);

        txtUrl.requestFocus();
    }

    @FXML
    void handleGet(ActionEvent event) {

        if(txtUrl.getText().isEmpty()){
            return;
        }

        try {

            hboxProgress.setVisible(true);

            desactiveEdition();

            serviceRssService.setProxyuse(ConfigApp.readProperty(ConfigApp.ConfigTypes.PROXY_USE).equals("true"));
            serviceRssService.setProxyhost(ConfigApp.readProperty(ConfigApp.ConfigTypes.PROXY_HOST));
            serviceRssService.setProxyport(Integer.parseInt(ConfigApp.readProperty(ConfigApp.ConfigTypes.PROXY_PORT)));

            serviceRssService.setUrl(txtUrl.getText());
            // if thread is not running
            if (!serviceRssService.isRunning()) {

                lblProgress.textProperty().bind(serviceRssService.messageProperty());

                // event fail
                serviceRssService.setOnFailed(eventFail -> {
                    serviceRssService.getException().printStackTrace(System.err);
                    hboxProgress.setVisible(false);
                    mainApp.showAlertWithEx(serviceRssService.getException());
                });

                // event success
                serviceRssService.setOnSucceeded(eventSuccess -> {
                    Rss r = serviceRssService.getValue();
                    if (r != null && r.getLinkrss().isEmpty() == false) {
                        txtTitle.setText(r.getTitle());
                        txtWeb.setText(r.getLinkweb());
                        txtDescription.setText(r.getDescription());

                        activeEdition();

                        currentRss.setLinkrss(r.getLinkrss());

                    }

                    hboxProgress.setVisible(false);

                });

                // start/reset thread
                serviceRssService.reset();
                serviceRssService.start();
            }// END: isRunning

        } catch (Exception ex) {
            hboxProgress.setVisible(false);
            mainApp.showAlertWithEx(ex);
        }

    }

    @FXML
    void handleSave(ActionEvent event) {
        RssService rssService;
        InputValidatorHelper validator = isInputValid();

        if (currentRss == null) {
            return;
        }

        if(currentRss.getLinkrss() == null){
            return;
        }

        if (validator.isExistError() == false) {
            rssService = new RssService();
            try {
                currentRss.setTitle(txtTitle.getText());
                currentRss.setDescription(txtDescription.getText());
                currentRss.setLinkweb(txtWeb.getText());

                if(currentRss.getId() == null || currentRss.getId() == 0){
                    rssService.create(currentRss);
                }else{
                    rssService.update(currentRss);
                }

                isSaved = true;
                dialogStage.close();
            } catch (IOException ex) {
                isSaved = false;
                mainApp.showAlertWithEx(ex);
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
        if (validator.isNullOrEmpty(txtTitle.getText())) {
            validator.getErrors().add("No valid Title");
        }

        if (validator.isNullOrEmpty(txtDescription.getText())) {
            validator.getErrors().add("No valid Description");
        }

        if (validator.isNullOrEmpty(txtWeb.getText())) {
            validator.getErrors().add("No valid Web");
        }

        return validator;
    }

    private void mapToForm() {
        txtTitle.setText(currentRss.getTitle());
        txtDescription.setText(currentRss.getDescription());
        txtWeb.setText(currentRss.getLinkweb());
    }

}
