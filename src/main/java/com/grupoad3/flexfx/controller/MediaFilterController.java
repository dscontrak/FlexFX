/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.db.model.MediaType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 *
 * @author daniel_serna
 */
public class MediaFilterController {
    
    @FXML
    private ComboBox cbxCategory;
    
    private Stage dialogStage;
    
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
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
}
