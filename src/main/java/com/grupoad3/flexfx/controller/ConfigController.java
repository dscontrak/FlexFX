/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.ClientAppTorrentType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author daniel_serna
 */
public class ConfigController {

    //ServiceRssTask serviceRssService;

    private Stage dialogStage;
    private boolean isSaved = false;

    // Reference to the main application.
    private MainApp mainApp;

    @FXML
    private TextField txtDownFolder;

    @FXML
    private RadioButton rbnProxyActive;

    @FXML
    private ToggleGroup groupActiveProxy;

    @FXML
    private RadioButton rbnProxyInactive;

    @FXML
    private TextField txtProxyHost;

    @FXML
    private TextField txtProxyPort;

    @FXML
    private RadioButton rbnClientActive;

    @FXML
    private ToggleGroup groupActiveClient;

    @FXML
    private RadioButton rbnClientInactive;

    @FXML
    private TextField txtClientAddress;

    @FXML
    private TextField txtClientPort;

    @FXML
    private TextField txtClientUser;

    @FXML
    private TextField txtClientPass;

    @FXML
    private ComboBox<ClientAppTorrentType> cbxClientApp;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        //serviceRssService = new ServiceRssTask();

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

    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleClientTest(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

}
