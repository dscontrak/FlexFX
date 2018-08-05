/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.Rss;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author daniel_serna
 */
public class MainController{
    
    @FXML
    private TableView<Rss> rssTable;
    @FXML
    private TableColumn<Rss, String> titleRssTable;
    
    @FXML
    private Label lblRssTitle;
    
    @FXML
    private Label lblRssUrl;
    
    @FXML
    private Label lblRssLastSync;
    
    
    // Reference to the main application.
    private MainApp mainApp;

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        rssTable.setItems(mainApp.getRssData());
        
    }
    
      /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        titleRssTable.setCellValueFactory(cellData -> cellData.getValue().titleProperty());        
        
        // Listener to selected element of list
        showRssDetails(null);
        
        rssTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showRssDetails(newValue);
        });
        
    }

    private void showRssDetails(Rss rss) {
        if (rss != null) {
            lblRssTitle.setText( rss.getTitle() );
            lblRssUrl.setText( rss.getLinkrss() );
            if(rss.getLastsync() != null){
                lblRssLastSync.setText(rss.getLastsync().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));            
            }
            
        }else{
            lblRssTitle.setText( "" );
            lblRssUrl.setText( "" );
            lblRssLastSync.setText( "" );            
            
        }
    }
    
    
    
}
