/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.ConfigApp;
import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.clientbit.ClientBittorrent;
import com.grupoad3.flexfx.clientbit.Qbittorrent41;
import com.grupoad3.flexfx.db.model.ClientAppTorrentType;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.grupoad3.flexfx.util.InputValidatorHelper;
import java.io.IOException;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
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
    private TextField txtClientHost;

    @FXML
    private TextField txtClientPort;

    @FXML
    private TextField txtClientUser;

    @FXML
    private PasswordField txtClientPass;

    @FXML
    private ComboBox<ClientAppTorrentType> cbxClientApp;

    @FXML
    private Button btnTestClient;
    
    @FXML
    private CheckBox chkAddPause;

    Map<String, String> valuesProperties;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        // Load info
        cbxClientApp.setItems(FXCollections.observableArrayList(ClientAppTorrentType.values()));

        // Property listener
        proxyListenerSet();
        clientListenerSet();

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        String useProxy;
        String useClient;
        String isAddPause;

        try {

            valuesProperties = ConfigApp.readAllProperties();

            useProxy = valuesProperties.get(ConfigApp.ConfigTypes.PROXY_USE.nameProp());
            useClient = valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_USE.nameProp());
            isAddPause = valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_ADD_PAUSE.nameProp());

            // Load config
            txtDownFolder.setText(valuesProperties.get(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD.nameProp()));

            txtProxyHost.setText(valuesProperties.get(ConfigApp.ConfigTypes.PROXY_HOST.nameProp()));
            txtProxyPort.setText(valuesProperties.get(ConfigApp.ConfigTypes.PROXY_PORT.nameProp()));

            txtClientHost.setText(valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_HOST.nameProp()));
            txtClientPort.setText(valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_PORT.nameProp()));
            txtClientUser.setText(valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_USER.nameProp()));
            txtClientPass.setText(valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_PASS.nameProp()));

            selectClientByProp(valuesProperties.get(ConfigApp.ConfigTypes.CLIENTTORR_APP.nameProp()));

            if ("true".equals(useProxy)) {
                rbnProxyActive.setSelected(true);
            } else {
                rbnProxyInactive.setSelected(true);
            }

            if ("true".equals(useClient)) {
                rbnClientActive.setSelected(true);
            } else {
                rbnClientInactive.setSelected(true);
            }
            
            if("true".equals(isAddPause)){
                chkAddPause.setSelected(true);
            }else{
                chkAddPause.setSelected(false);
            }

            if (rbnClientInactive.isSelected()) {
                inactiveClientInputs(true);
            }

            if (rbnProxyInactive.isSelected()) {
                inactiveProxyInputs(true);
            }

        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaved() {
        return isSaved;
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleClientTest(ActionEvent event) {
        ClientBittorrent cliente;
        InputValidatorHelper validatorClient = validClientData();
        StringBuilder url = new StringBuilder("http://");
        final String clientSelected = cbxClientApp.getValue().toString();
        final boolean isAddPause = chkAddPause.isSelected();

        final AlertIcon alert = new AlertIcon(Alert.AlertType.WARNING);
        alert.setIcon(mainApp.getIconoApp());

        if (validatorClient.isExistError()) {

            alert.setContentText(validatorClient.getErrorFomatText());
            alert.showAndWait();

            return;
        }

        url.append(txtClientHost.getText());

        if (txtClientPort.getText().isEmpty() == false) {
            url.append(":");
            url.append(txtClientPort.getText());
        }

        try {

            if (clientSelected.equals(ClientAppTorrentType.QBITTORRENT41.toString())) {
                
                cliente = new Qbittorrent41(url.toString());                
                cliente.setAddPauseState(isAddPause);
                cliente.login(txtClientUser.getText(), txtClientPass.getText());

            }else{
                throw new Exception("Dont exist implementation the current cliente selected");
            }

        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }
    }

    @FXML
    void handleSave(ActionEvent event) {

        InputValidatorHelper validatorClient = validClientData();
        InputValidatorHelper validatorProxy = validProxyData();

        AlertIcon alert = new AlertIcon(Alert.AlertType.WARNING);
        alert.setIcon(mainApp.getIconoApp());

        try {
            ConfigApp configApp = new ConfigApp();

            valuesProperties.put(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD.nameProp(), txtDownFolder.getText());

            if (rbnClientActive.isSelected()) {

                if (validatorClient.isExistError()) {
                    alert.setContentText(validatorClient.getErrorFomatText());
                    alert.showAndWait();

                    return;
                }
            }

            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_APP.nameProp(), cbxClientApp.getValue().toString());
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_HOST.nameProp(), txtClientHost.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_PASS.nameProp(), txtClientPass.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_PORT.nameProp(), txtClientPort.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_USER.nameProp(), txtClientUser.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_USE.nameProp(), rbnClientActive.isSelected() ? "true" : "false");
            valuesProperties.put(ConfigApp.ConfigTypes.CLIENTTORR_ADD_PAUSE.nameProp(), chkAddPause.isSelected() ? "true": "false" );

            if (rbnProxyActive.isSelected()) {

                if (validatorProxy.isExistError()) {
                    alert.setContentText(validatorProxy.getErrorFomatText());
                    alert.showAndWait();
                    return;
                }
            }

            valuesProperties.put(ConfigApp.ConfigTypes.PROXY_HOST.nameProp(), txtProxyHost.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.PROXY_PORT.nameProp(), txtProxyPort.getText());
            valuesProperties.put(ConfigApp.ConfigTypes.PROXY_USE.nameProp(), rbnProxyActive.isSelected() ? "true" : "false");
            

            configApp.writeAllProperties(valuesProperties);
            configApp.loadProperties();

            dialogStage.close();

        } catch (IOException ex) {
            mainApp.showAlertWithEx(ex);
        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }

    }

    private InputValidatorHelper validClientData() {
        InputValidatorHelper validator = new InputValidatorHelper();
        if (validator.isNullOrEmpty(txtClientHost.getText())) {
            validator.getErrors().add("No valid client host");
        }

        if (validator.isNullOrEmpty(txtClientUser.getText())) {
            validator.getErrors().add("No valid user");
        }

        if (validator.isNullOrEmpty(txtClientPass.getText())) {
            validator.getErrors().add("No valid pass");
        }

        if (cbxClientApp.getValue() == null) {
            validator.getErrors().add("No valid Main Filter");
        }

        // exist error
        if (validator.getErrors().isEmpty() == false) {
            validator.setExistError(true);
        }

        return validator;
    }

    private InputValidatorHelper validProxyData() {
        InputValidatorHelper validator = new InputValidatorHelper();
        if (validator.isNullOrEmpty(txtProxyHost.getText())) {
            validator.getErrors().add("No valid proxy host");

        }

        if (validator.isNullOrEmpty(txtProxyPort.getText())) {
            validator.getErrors().add("No valid proxy port");
        }

        // exist error
        if (validator.getErrors().isEmpty() == false) {
            validator.setExistError(true);
        }

        return validator;
    }

    private void selectClientByProp(String property) {

        for (ClientAppTorrentType value : cbxClientApp.getItems()) {
            if (value.toString().equals(property)) {
                cbxClientApp.setValue(value);
                break;
            }
        }

    }

    private void inactiveProxyInputs(boolean inactive) {

        txtProxyHost.setDisable(inactive);
        txtProxyPort.setDisable(inactive);

    }

    private void inactiveClientInputs(boolean inactive) {

        txtClientHost.setDisable(inactive);
        txtClientPass.setDisable(inactive);
        txtClientPort.setDisable(inactive);
        txtClientUser.setDisable(inactive);

        cbxClientApp.setDisable(inactive);
        btnTestClient.setDisable(inactive);
        chkAddPause.setDisable(inactive);

    }

    private void clientListenerSet() {
        rbnClientActive.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isNowSelected) -> {
            if (isNowSelected) {
                inactiveClientInputs(false);
            } else {
                inactiveClientInputs(true);
            }
        });
    }

    private void proxyListenerSet() {
        rbnProxyActive.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isNowSelected) -> {
            if (isNowSelected) {
                inactiveProxyInputs(false);
            } else {
                inactiveProxyInputs(true);
            }
        });
    }

}
