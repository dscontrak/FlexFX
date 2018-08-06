/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.RssItemService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author daniel_serna
 */
public class MainController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

    // -------- Rss section --------
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

    // -------- Rss item section --------
    @FXML
    private TableView<RssItems> tableRssItems;

    @FXML
    private TableColumn<RssItems, String> columnRssItemStatus;

    @FXML
    private TableColumn<RssItems, String> columnRssItemTitle;

    @FXML
    private TableColumn<RssItems, String> columnRssItemFile;

    @FXML
    private TableColumn<RssItems, String> columnRssItemPublication;

    @FXML
    private TableColumn<RssItems, String> columnRssItemDownloaded;

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
            lblRssTitle.setText(rss.getTitle());
            lblRssUrl.setText(rss.getLinkrss());
            if (rss.getLastsync() != null) {
                lblRssLastSync.setText(rss.getLastsync().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            setLastItemsByRss(rss);

        } else {
            lblRssTitle.setText("");
            lblRssUrl.setText("");
            lblRssLastSync.setText("");

        }
    }

    private void setLastItemsByRss(Rss rss) {
        RssItemService itemService = new RssItemService();
        List<RssItems> items = itemService.getLastItemsByRss(rss, 30, 0);

        // clear
        mainApp.getRssItemsData().clear();

        // add items
        items.forEach(i -> {
            mainApp.getRssItemsData().add(i);
        });
        tableRssItems.setItems(mainApp.getRssItemsData());

        columnRssItemTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnRssItemStatus.setCellValueFactory(cellData -> getStringNotNull(cellData.getValue().statusProperty()));
        columnRssItemFile.setCellValueFactory(cellData -> cellData.getValue().fileProperty());
        columnRssItemPublication.setCellValueFactory(cellData -> convertDateToStringProp(cellData.getValue().getDatepub()));
        columnRssItemDownloaded.setCellValueFactory(cellData -> convertDateToStringProp(cellData.getValue().getDatedown()));

    }

    private SimpleStringProperty convertDateToStringProp(LocalDateTime dateConvert) {
        if (dateConvert == null) {
            return new SimpleStringProperty("");
        }

        return new SimpleStringProperty(dateConvert.format(formatter));
    }

    private StringProperty getStringNotNull(StringProperty value) {
        if (value == null) {
            return new SimpleStringProperty("");
        }
        return value;
    }

}
