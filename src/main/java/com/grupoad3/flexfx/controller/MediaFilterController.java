/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.MediaType;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.services.MediaFilterService;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.grupoad3.flexfx.util.InputValidatorHelper;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author daniel_serna
 */
public class MediaFilterController {

    /*@FXML
    private ComboBox cbxCategory;*/

    private Stage dialogStage;
    private boolean isSaved = false;
    private MediaFilters currentFilter;
    // Reference to the main application.
    private MainApp mainApp;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtMainFilter;

    @FXML
    private TextField txtSecFilter;

    @FXML
    private ComboBox<MediaType> cbxCategory;

    @FXML
    private TextField txtFolder;

    @FXML
    private ToggleGroup groupActive;

    @FXML
    private RadioButton rbnActive;

    @FXML
    private RadioButton rbnInactive;

    private Rss rss;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        cbxCategory.setItems(FXCollections.observableArrayList(MediaType.values()));
        cbxCategory.setValue(MediaType.ANIME);

        /*Object o1 = cbxCategory.getSelectionModel();
        MediaType o2 = (MediaType)cbxCategory.getValue();*/

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    /*public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }*/

    public boolean isSaved() {
        return isSaved;
    }

    public void setMediaFilter(MediaFilters filter) {
        currentFilter = filter;

        if(currentFilter != null && currentFilter.getId() != 0){
            txtFolder.setText(currentFilter.getFolderpath());
            txtMainFilter.setText(currentFilter.getFiltermain());
            txtSecFilter.setText(currentFilter.getFiltersecondary());
            txtTitle.setText(currentFilter.getTitle());
            cbxCategory.setValue(currentFilter.getCategoryEnum());
            if(filter.getActive()){
                rbnActive.setSelected(true);
            }else{
                rbnActive.setSelected(false);
            }
        }

    }

    public void setCurrentRss(Rss rss){
        this.rss = rss;
    }

    @FXML
    private void handleSave(){
        MediaFilterService filterService;
        InputValidatorHelper validator = isInputValid();

        if(validator.isExistError() == false){
            filterService = new MediaFilterService();
            try {
                currentFilter.setTitle(txtTitle.getText());
                currentFilter.setFiltermain(txtMainFilter.getText());
                currentFilter.setFiltersecondary(txtSecFilter.getText());
                currentFilter.setRss(rss);
                currentFilter.setCategory(cbxCategory.getValue());
                currentFilter.setFolderpath(txtFolder.getText());
                currentFilter.setActive(isActiveSelected());

                if(currentFilter.getId() == null || currentFilter.getId() == 0){
                    filterService.create(currentFilter);
                }else{
                    filterService.update(currentFilter);
                }


                isSaved = true;
                dialogStage.close();
            } catch (IOException ex) {
                isSaved = false;
                ex.printStackTrace();
            }
        }else{
            AlertIcon alert = new AlertIcon(AlertType.WARNING);
            alert.setContentText(validator.getErrorFomatText());
            alert.setIcon(mainApp.getIconoApp());
            alert.showAndWait();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    private boolean isActiveSelected(){

        /*Toggle toggle;

        if(groupActive.getSelectedToggle() != null){
            toggle = groupActive.getSelectedToggle();
            if(toggle.getUserData().toString().toLowerCase().equals("active")){
                return true;
            }
        }*/

        RadioButton selectedRadioButton = (RadioButton) groupActive.getSelectedToggle();
        if(selectedRadioButton != null){
            if(selectedRadioButton.getText().toLowerCase().equals("active")){
                return true;
            }
        }

        return false;
    }

    private InputValidatorHelper isInputValid(){
        InputValidatorHelper validator = new InputValidatorHelper();
        if(validator.isNullOrEmpty(txtTitle.getText())){
            validator.getErrors().add("No valid Title");
        }

        if(validator.isNullOrEmpty(txtMainFilter.getText())){
            validator.getErrors().add("No valid Main Filter");
        }

        if(cbxCategory.getValue() == null ){
            validator.getErrors().add("No valid Main Filter");
            validator.setExistError(true);
        }

        if(groupActive.getSelectedToggle() == null){
            validator.getErrors().add("Active/Inactive is not valid");
            validator.setExistError(true);
        }

        return validator;
    }

}
