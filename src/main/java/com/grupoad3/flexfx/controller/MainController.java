/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.MediaFilterService;
import com.grupoad3.flexfx.db.services.RssItemService;
import com.grupoad3.flexfx.db.services.RssService;
import com.grupoad3.flexfx.ui.AlertIcon;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private TableView<Rss> tableRss;
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

    // ------ Media filter section --------------
    @FXML
    private TableView<MediaFilters> tableFilters;

    @FXML
    private TableColumn<MediaFilters, String> columnFilterActive;

    @FXML
    private TableColumn<MediaFilters, String> columnFilterTitle;

    @FXML
    private TableColumn<MediaFilters, String> columnFilterMainFilter;

    @FXML
    private TableColumn<MediaFilters, String> columnFilterSecondaryFilter;

    @FXML
    private Button btnFilterAdd;

    @FXML
    private Button btnFilterDel;

    @FXML
    private Button btnFilterEdit;

    // Reference to the main application.
    private MainApp mainApp;
    private Rss rssSelected;
    private MediaFilters filterSelected;

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
        tableRss.setItems(mainApp.getRssData());

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

        tableRss.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rssSelected = newValue;
            showRssDetails(newValue);
        });

        tableFilters.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            filterSelected = newValue;
        }));

    }

    private void showRssDetails(Rss rss) {

        if (rss != null) {
            lblRssTitle.setText(rss.getTitle());
            lblRssUrl.setText(rss.getLinkrss());
            if (rss.getLastsync() != null) {
                lblRssLastSync.setText(rss.getLastsync().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            setLastItemsByRss(rss);
            setLastMediaFiltersByRss(rss);
            enableControls();

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

        if (items == null || items.isEmpty()) {
            return;
        }

        // add items
        items.forEach(i -> mainApp.getRssItemsData().add(i));
        tableRssItems.setItems(mainApp.getRssItemsData());

        columnRssItemTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnRssItemStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
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

    private SimpleStringProperty convertActivedToStringProp(BooleanProperty isActive) {
        if (isActive == null || isActive.get() == false) {
            return new SimpleStringProperty("INACTIVE");
        }

        return new SimpleStringProperty("ACTIVE");
    }

    private void setLastMediaFiltersByRss(Rss rss) {
        MediaFilterService filterService = new MediaFilterService();
        List<MediaFilters> filters = filterService.getLastItemsByRss(rss, 20, 0);

        // clear
        mainApp.getMediaFiltersData().clear();

        if (filters == null || filters.isEmpty()) {
            return;
        }

        // add items
        filters.forEach(f -> mainApp.getMediaFiltersData().add(f));
        tableFilters.setItems(mainApp.getMediaFiltersData());

        columnFilterActive.setCellValueFactory(cellData -> convertActivedToStringProp(cellData.getValue().activeProperty()));
        columnFilterTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnFilterMainFilter.setCellValueFactory(cellData -> cellData.getValue().filtermainProperty());
        columnFilterSecondaryFilter.setCellValueFactory(cellData -> cellData.getValue().filtersecondaryProperty());

    }

    @FXML
    void handleNewMediaFilter(ActionEvent event) {
        MediaFilters filter = new MediaFilters();
        boolean isSaved = mainApp.showMediaFilterEditDialog(filter, rssSelected);
        if (isSaved) {
            mainApp.getMediaFiltersData().add(filter);
        }

    }

    @FXML
    void handleMediaFilterDel(ActionEvent event) {
        MediaFilterService filterService;
        try {

            if (filterSelected == null) {
                return;
            }

            if (isConfirmDeletion()) {

                filterService = new MediaFilterService();
                filterService.deleteById(new Long(filterSelected.getId()));
                mainApp.getMediaFiltersData().remove(filterSelected);

            }

        } catch (IOException e) {
            mainApp.showAlertWithEx(e);
        }

    }

    @FXML
    void handleRssDel(ActionEvent event) {
        RssService rssService;

        try {

            if (rssSelected == null) {
                return;
            }

            if (isConfirmDeletion()) {

                rssService = new RssService();
                rssService.deleteById(new Long(rssSelected.getId()));
                mainApp.getRssData().remove(rssSelected);

            }

        } catch (IOException e) {
            mainApp.showAlertWithEx(e);
        }
    }

    public boolean isConfirmDeletion() {

        AlertIcon alert = new AlertIcon(Alert.AlertType.CONFIRMATION);

        alert.setIcon(mainApp.getIconoApp());
        alert.setContentText("Are yo want to delete this element?");
        alert.setContentText("Confirm deletion");

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    @FXML
    void handleRssAdd(ActionEvent event) {
        Rss rss = new Rss();
        boolean isSaved = mainApp.showRssEditDialog(rss);
        if (isSaved) {
            mainApp.getRssData().add(rss);
        }
    }

    @FXML
    void handleRssEdit(ActionEvent event) {
        if (rssSelected == null) {
            return;
        }

        mainApp.showRssEditDialog(rssSelected);
    }

    private void enableControls() {
        btnFilterAdd.setDisable(false);
        btnFilterDel.setDisable(false);
    }

}
