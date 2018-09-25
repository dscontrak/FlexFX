/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.controller;

import com.grupoad3.flexfx.ConfigApp;
import com.grupoad3.flexfx.MainApp;
import com.grupoad3.flexfx.clientbit.ClientBitorrentException;
import com.grupoad3.flexfx.clientbit.ClientBittorrent;
import com.grupoad3.flexfx.clientbit.ManageClientBit;
import com.grupoad3.flexfx.clientbit.Qbittorrent41;
import com.grupoad3.flexfx.db.model.ClientAppTorrentType;
import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.MediaFilterService;
import com.grupoad3.flexfx.db.services.RssItemService;
import com.grupoad3.flexfx.db.services.RssService;
import com.grupoad3.flexfx.process.ServiceRssItemDownloadTask;
import com.grupoad3.flexfx.process.ServiceRssItemsTask;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.grupoad3.flexfx.ui.TableCellMediaFilterColorActive;
import com.grupoad3.flexfx.ui.TableCellRssItemColorStatus;
import com.grupoad3.flexfx.ui.TableCellRssItemDateHuman;
import com.grupoad3.flexfx.util.ConvertionUtil;
import com.grupoad3.flexfx.util.OpenFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;

/**
 *
 * @author daniel_serna
 */
public class MainController {

    public final String PATTERN_DATE = "yyyy/MM/dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);

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

    @FXML
    private HBox hboxProgress;

    @FXML
    private Label lblProgress;

    @FXML
    private Button btnRssDownload;

    @FXML
    private Button btnRssConfig;

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

    @FXML
    private CheckBox chkItemsDownloaded;

    @FXML
    private TextField txtItemsSearch;

    @FXML
    private Button btnRssItemOpen;

    @FXML
    private Button btnRssItemOpenClient;

    /*@FXML
    private Button btnRssItemLinkClient;*/
    @FXML
    private Button btnRssItemDownload;

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
    private TableColumn<MediaFilters, String> columnFilterIgnore;

    @FXML
    private Button btnFilterAdd;

    @FXML
    private Button btnFilterDel;

    @FXML
    private Button btnFilterEdit;

    @FXML
    private CheckBox chkFilterActive;

    @FXML
    private TextField txtFilterSearch;

    // TODO: Probar la descarga aquellos ignorados de forma manual.
    // TODO: Soportar mas versiones de QBitorrent
    
    // Reference to the main application.
    private MainApp mainApp;
    private Rss rssSelected;
    private MediaFilters filterSelected;
    private RssItems rssLastItemSelected;

    private HashSet<RssItems> rssAllSelected = new HashSet<>();

    ServiceRssItemsTask serviceRssItemsService;
    ServiceRssItemDownloadTask downloadTask;

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
        // Add observable list data to the table and bindings
        tableRss.setItems(mainApp.getRssData());
        tableFilters.setItems(mainApp.getMediaFiltersData());
        tableRssItems.setItems(mainApp.getRssItemsData());

        setShortCuts();

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

        listenerSelectionTables();

        hboxProgress.setVisible(false);
        serviceRssItemsService = new ServiceRssItemsTask();
        downloadTask = new ServiceRssItemDownloadTask();

        mapColumnsFilters();
        mapColumnsRssItems();
        //listerCheckboxItemsDownload();
        //listerCheckboxActiveFilter();
        listenerTxtSearchItems();
        listenerTxtSearchFilters();
        listenerRowItemDoubleClick();
        listenerRowRssDoubleClick();
        listernerRowFilterDoubleClick();

        setTooltips();

    }

    private void showRssDetails(Rss rss) {

        if (rss != null) {
            rssSelected = rss;
            lblRssTitle.setText(rss.getTitle());
            lblRssUrl.setText(rss.getLinkrss());
            if (rss.getLastsync() != null) {
                lblRssLastSync.setText("Last sync: " + ConvertionUtil.convertHumanDate(rss.getLastsync()));
            }

            setLastItemsByRss(rss);
            setLastMediaFiltersByRss(rss);
            enableControls();

        } else {
            lblRssTitle.setText("");
            lblRssUrl.setText("");
            lblRssLastSync.setText("Unsynchronized");

        }
    }

    private void setLastItemsByRss(Rss rss) {
        RssItemService itemService = new RssItemService();
        List<RssItems> items = itemService.getLastAlltemsByRss(rss, 100, 0, false);

        // clear
        mainApp.getRssItemsData().clear();

        if (items == null || items.isEmpty()) {
            return;
        }

        // add items
        items.forEach(i -> mainApp.getRssItemsData().add(i));
        //tableRssItems.setItems(mainApp.getRssItemsData());

        //mapColumnsRssItems();
    }

    private void mapColumnsFilters() {
        // Values
        columnFilterActive.setCellValueFactory(cellData -> convertActivedToStringProp(cellData.getValue().activeProperty()));
        columnFilterTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnFilterMainFilter.setCellValueFactory(cellData -> cellData.getValue().filtermainProperty());
        columnFilterSecondaryFilter.setCellValueFactory(cellData -> cellData.getValue().filtersecondaryProperty());
        columnFilterIgnore.setCellValueFactory(cellData -> cellData.getValue().filterignoreProperty());

        // Factory
        columnFilterActive.setCellFactory((param) -> {
            return new TableCellMediaFilterColorActive();
        });
        columnFilterTitle.setCellFactory((param) -> {
            return new TableCellMediaFilterColorActive();
        });
        columnFilterMainFilter.setCellFactory((param) -> {
            return new TableCellMediaFilterColorActive();
        });
        columnFilterSecondaryFilter.setCellFactory((param) -> {
            return new TableCellMediaFilterColorActive();
        });
        columnFilterIgnore.setCellFactory((param) -> {
            return new TableCellMediaFilterColorActive();
        });

    }

    private void mapColumnsRssItems() {
        // Values
        columnRssItemStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        columnRssItemTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnRssItemFile.setCellValueFactory(cellData -> cellData.getValue().fileProperty());

        columnRssItemPublication.setCellValueFactory(cellData -> convertDateToStringProp(cellData.getValue().getDatepub()));
        columnRssItemDownloaded.setCellValueFactory(cellData -> convertDateToStringProp(cellData.getValue().getDatedown()));

        // Factories
        columnRssItemStatus.setCellFactory((param) -> {
            return new TableCellRssItemColorStatus();
        });
        columnRssItemTitle.setCellFactory((param) -> {
            return new TableCellRssItemColorStatus();
        });
        columnRssItemFile.setCellFactory((param) -> {
            return new TableCellRssItemColorStatus();
        });
        columnRssItemPublication.setCellFactory((param) -> {
            return new TableCellRssItemDateHuman();
        });
        columnRssItemDownloaded.setCellFactory((param) -> {
            return new TableCellRssItemDateHuman();
        });

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
        List<MediaFilters> filters = filterService.getLastItemsByRss(rss, 50, 0);

        // clear
        mainApp.getMediaFiltersData().clear();

        if (filters == null || filters.isEmpty()) {
            return;
        }

        // add items
        filters.forEach(f -> mainApp.getMediaFiltersData().add(f));

        //mapColumnsFilters();
    }

    @FXML
    void handleConfig(ActionEvent event) {
        boolean isSaved = mainApp.showConfigDialog();
        if (isSaved) {
            System.out.println("Is saved");
        }
    }

    @FXML
    void handleMediaFilterAdd(ActionEvent event) {
        MediaFilters filter = new MediaFilters();
        boolean isSaved = mainApp.showMediaFilterEditDialog(filter, rssSelected);
        if (isSaved) {
            mainApp.getMediaFiltersData().add(filter);
        }

    }

    @FXML
    void handleMediaFilterEdit(ActionEvent event) {
        MediaFilters filter = filterSelected;

        if (filterSelected == null) {
            return;
        }

        boolean isSaved = mainApp.showMediaFilterEditDialog(filter, rssSelected);
        if (isSaved) {
            // Is observable
            tableFilters.refresh();
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
                //rssService.deleteById(new Long(rssSelected.getId()));
                boolean isAffected = rssService.eraserSoft(rssSelected);
                if (isAffected) {
                    mainApp.getRssData().remove(rssSelected);
                }

            }

        } catch (IOException e) {
            mainApp.showAlertWithEx(e);
        }
    }

    @FXML
    void handleCheckDownloaded(ActionEvent event) {
        //System.out.println("com.grupoad3.flexfx.controller.MainController.handleCheckDownloaded()");
        searchItemsByFilter();
    }

    @FXML
    void handleCheckActive(ActionEvent event) {
        searchMediafilterbyFilter();
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

    @FXML
    void handleRssDownload(ActionEvent event) {
        AlertIcon alert;
        String pathDir;
        boolean isClientUse = false;
        File filePath;

        if (mainApp.getMediaFiltersData() != null && mainApp.getMediaFiltersData().isEmpty()) {
            alert = new AlertIcon(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Dont you have any filter to apply with this RSS");
            alert.setIcon(mainApp.getIconoApp());
            alert.showAndWait();
            return;
        }
        try {
            isClientUse = ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_USE).equals("true");
            pathDir = ConfigApp.readProperty(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD);
            filePath = new File(pathDir);
            if (!filePath.exists()) {
                if (!filePath.mkdirs()) {
                    throw new Exception("Dont is possible create a folder in: \n" + filePath.getAbsolutePath());
                }
            }

            if (!serviceRssItemsService.isRunning()) {

                hboxProgress.setVisible(true);
                lblProgress.textProperty().bind(serviceRssItemsService.messageProperty());

                serviceRssItemsService.setRss(rssSelected);
                serviceRssItemsService.setIsClientUse(isClientUse);
                //serviceRssItemsService.setFilters(mainApp.getMediaFiltersData()); //Change only active
                serviceRssItemsService.setPath(pathDir);

                // event fail
                serviceRssItemsService.setOnFailed(eventFail -> {
                    hboxProgress.setVisible(false);
                    mainApp.showAlertWithEx(serviceRssItemsService.getException());
                });

                // event success
                serviceRssItemsService.setOnSucceeded(eventSuccess -> {
                    if (serviceRssItemsService.getValue() != null && serviceRssItemsService.getValue().isEmpty() == false) {
                        mainApp.getRssItemsData().setAll(serviceRssItemsService.getValue());
                        tableRssItems.setItems(mainApp.getRssItemsData());

                        try {
                            RssService rssService = new RssService();
                            rssSelected.setLastsync(LocalDateTime.now());
                            lblRssLastSync.setText("Last Sync: " + ConvertionUtil.convertHumanDate(rssSelected.getLastsync()));
                            rssService.update(rssSelected);
                        } catch (IOException ex) {
                            mainApp.showAlertWithEx(ex);
                        }
                    }
                    hboxProgress.setVisible(false);
                });

                // start/reset thread
                serviceRssItemsService.reset();
                serviceRssItemsService.start();
            }
        } catch (Exception e) {
            hboxProgress.setVisible(false);
            mainApp.showAlertWithEx(e);
        }

    }

    private void enableControls() {
        btnFilterAdd.setDisable(false);
        btnFilterDel.setDisable(false);
        btnFilterEdit.setDisable(false);
        chkItemsDownloaded.setSelected(false);
        chkItemsDownloaded.setDisable(false);

        chkFilterActive.setDisable(false);
        chkFilterActive.setSelected(false);

        txtItemsSearch.setDisable(false);
        txtFilterSearch.setDisable(false);

        btnRssItemOpen.setDisable(false);
        btnRssItemDownload.setDisable(false);

        try {
            if (ConfigApp.readProperty(ConfigApp.ConfigTypes.CLIENTTORR_USE).equals("true")) {
                //btnRssItemLinkClient.setDisable(false);
                btnRssItemOpenClient.setDisable(false);
            }
        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }

    }

    private void listenerTxtSearchItems() {
        txtItemsSearch.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchItemsByFilter();
            }
        });

        txtItemsSearch.setOnAction((event) -> {
            System.out.println("----- Prevent default ENTER -------");
        });

    }

    private synchronized void searchMediafilterbyFilter() {
        if (rssSelected == null) {
            return;
        }

        MediaFilterService filterService = new MediaFilterService();
        List<MediaFilters> filters;

        MediaFilters filterSearch = new MediaFilters();
        filterSearch.setActive(chkFilterActive.isSelected());
        filterSearch.setTitle(txtFilterSearch.getText());

        filters = filterService.getAllByFilter(rssSelected, filterSearch);

        mainApp.getMediaFiltersData().setAll(filters);
    }

    private synchronized void searchItemsByFilter() {
        if (rssSelected == null) {
            return;
        }

        RssItems itemFilter = new RssItems();
        List<RssItems> items;

        // Set filter
        RssItemService itemService = new RssItemService();
        if (chkItemsDownloaded.isSelected()) {
            itemFilter.setStatus(ItemStatus.DOWNLOADED);
        }
        itemFilter.setTitle(txtItemsSearch.getText());

        // Search BD
        items = itemService.getAllItemsByFilter(rssSelected, itemFilter);

        // Set data
        mainApp.getRssItemsData().setAll(items);

    }

    private void listenerTxtSearchFilters() {
        txtFilterSearch.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchMediafilterbyFilter();
            }
        });

        txtFilterSearch.setOnAction((event) -> {
            System.out.println("----- Prevent default ENTER -------");
        });
    }

    @FXML
    void handleRssItemOpen(ActionEvent event) {
        if (rssAllSelected.isEmpty()) {
            return;
        }
        if (rssAllSelected.size() == 1) {
            openFileItemSelected(rssLastItemSelected);
        } else {
            openFileItemSelected(new ArrayList<>(rssAllSelected));
        }

    }

    @FXML
    void handleRssItemOpenClient(ActionEvent event) {
        if (rssAllSelected.isEmpty()) {
            return;
        }
        
        if (rssAllSelected.size() == 1) {
            openFileClientTorrent(rssLastItemSelected);
        }else{
            openFileClientTorrent(new ArrayList<>(rssAllSelected));
        }
        
        
    }

    /*@FXML
    void handleRssItemLinkClient(ActionEvent event) {
         System.out.println("com.grupoad3.flexfx.controller.MainController.handleRssItemLinkClient()");
    }*/
    @FXML
    void handleRssItemDownload(ActionEvent event) {
        AlertIcon alert;
        String pathDir;
        File filePath;

        alert = new AlertIcon(Alert.AlertType.WARNING);
        alert.setIcon(mainApp.getIconoApp());
        alert.setTitle("Warning");

        if (rssLastItemSelected == null) {
            alert.setContentText("Select one item to download");
            alert.showAndWait();
            return;
        }

        if (rssLastItemSelected.getLink() == null || rssLastItemSelected.getLink().isEmpty()) {
            alert.setContentText("Item selected does not have a link to download");
            alert.showAndWait();
            return;
        }

        if (serviceRssItemsService.isRunning()) {
            alert.setContentText("Other task is running");
            alert.showAndWait();
            return;
        }

        try {
            pathDir = ConfigApp.readProperty(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD);
            filePath = new File(pathDir);
            if (!filePath.exists()) {
                if (!filePath.mkdirs()) {
                    throw new Exception("Dont is possible create a folder in: \n" + filePath.getAbsolutePath());
                }
            }

            hboxProgress.setVisible(true);
            lblProgress.textProperty().bind(downloadTask.messageProperty());

            downloadTask.setRssItem(rssLastItemSelected);
            downloadTask.setPath(pathDir);

            // event fail
            downloadTask.setOnFailed(eventFail -> {
                hboxProgress.setVisible(false);
                mainApp.showAlertWithEx(downloadTask.getException());
            });

            // event success
            downloadTask.setOnSucceeded((eventSuccess) -> {
                if (downloadTask.getValue()) {
                    rssLastItemSelected.setStatus(ItemStatus.DOWNLOADED);
                }
                hboxProgress.setVisible(false);
            });

            downloadTask.reset();
            downloadTask.start();

        } catch (Exception e) {
            hboxProgress.setVisible(false);
            mainApp.showAlertWithEx(e);
        }

    }

    private synchronized void openFileItemSelected(RssItems item) {
        try {
            AlertIcon alertIcon = new AlertIcon(Alert.AlertType.WARNING);
            alertIcon.setIcon(mainApp.getIconoApp());
            alertIcon.setTitle("Warning!!");

            OpenFile fileOpen;

            if (item == null) {
                alertIcon.setContentText("Selected one item to open");
                alertIcon.showAndWait();
                return;
            }

            if (item.getFile() == null || item.getFile().isEmpty()) {
                alertIcon.setContentText("The item selected dont have file to open");
                alertIcon.showAndWait();
                return;
            }

            File file;
            String directory = ConfigApp.readProperty(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD);

            file = new File(directory + "/" + item.getFile());

            fileOpen = new OpenFile(file);
            fileOpen.openWithProcess();

        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }
    }

    private synchronized void openFileItemSelected(List<RssItems> items) {
        try {
            AlertIcon alertIcon = new AlertIcon(Alert.AlertType.WARNING);
            alertIcon.setIcon(mainApp.getIconoApp());
            alertIcon.setTitle("Warning!!");

            OpenFile fileOpen;

            if (items == null || items.isEmpty()) {
                alertIcon.setContentText("Selected items to open");
                alertIcon.showAndWait();
                return;
            }

            /*if (item.getFile() == null || item.getFile().isEmpty()) {
                alertIcon.setContentText("The item selected dont have file to open");
                alertIcon.showAndWait();
                return;
            }*/
            File file;
            String directory = ConfigApp.readProperty(ConfigApp.ConfigTypes.FOLDER_DOWNLOAD);
            for (RssItems rssItems : items) {
                file = new File(directory + "/" + rssItems.getFile());

                fileOpen = new OpenFile(file);
                fileOpen.openWithProcess();
            }

        } catch (Exception ex) {
            mainApp.showAlertWithEx(ex);
        }
    }

    private void listenerRowItemDoubleClick() {
        tableRssItems.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                RssItems item = tableRssItems.getSelectionModel().getSelectedItem();
                openFileItemSelected(item);
            }
        });
    }

    private void listenerRowRssDoubleClick() {

        tableRss.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                handleRssEdit(null);
            }
        });

    }

    private void listernerRowFilterDoubleClick() {
        handleMediaFilterEdit(null);

        tableFilters.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                handleMediaFilterEdit(null);
            }
        });
    }

    private void setTooltips() {
        final Tooltip tipBtnRssDownload = new Tooltip();
        final Tooltip tipBtnRssConfig = new Tooltip();
        final Tooltip tipBtnOpenItem = new Tooltip();
        final Tooltip tipBtnSendFileItem = new Tooltip();

        // set text
        tipBtnRssDownload.setText("Syncronize Rss and Download files \nfrom RSS (Ctrl+D)");
        tipBtnRssConfig.setText("Preferences (Ctrl+P)");
        tipBtnOpenItem.setText("Open file with default torrent client (Ctrl+O)");
        tipBtnSendFileItem.setText("Send file by API \nwith torrent cliente configured  (Ctrl+P)");

        // bind with control
        btnRssDownload.setTooltip(tipBtnRssDownload);
        btnRssConfig.setTooltip(tipBtnRssConfig);
        btnRssItemOpen.setTooltip(tipBtnOpenItem);
        btnRssItemOpenClient.setTooltip(tipBtnSendFileItem);

    }

    private void setShortCuts() {

        if (mainApp == null || mainApp.getPrimaryStage() == null || mainApp.getPrimaryStage().getScene() == null) {
            return;
        }

        // Combination
        KeyCombination kcDonwload = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_ANY);
        KeyCombination kcConfig = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
        KeyCombination kcOpenItem = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_ANY);
        KeyCombination kcSendFileItem = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_ANY);

        // Run on shorcut
        Runnable runDownload = () -> handleRssDownload(null);
        Runnable runConfig = () -> handleConfig(null);
        Runnable runOpenItem = () -> handleRssItemOpen(null);
        Runnable runSendFileItem = () -> handleRssItemOpenClient(null);

        // Set shortcut to run
        mainApp.getPrimaryStage().getScene().getAccelerators().put(kcDonwload, runDownload);
        mainApp.getPrimaryStage().getScene().getAccelerators().put(kcConfig, runConfig);
        mainApp.getPrimaryStage().getScene().getAccelerators().put(kcOpenItem, runOpenItem);
        mainApp.getPrimaryStage().getScene().getAccelerators().put(kcSendFileItem, runSendFileItem);

    }

    private void listenerSelectionTables() {
        tableRss.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showRssDetails(newValue);
        });

        tableFilters.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            filterSelected = newValue;
        }));

        tableRssItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableRssItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rssLastItemSelected = newValue;
        });
        tableRssItems.setOnMouseReleased((event) -> {
            ObservableList<RssItems> selectedItems = tableRssItems.getSelectionModel().getSelectedItems();
            rssAllSelected.clear();
            for (RssItems selectedItem : selectedItems) {
                rssAllSelected.add(selectedItem);
            }
        });
    }

    private void openFileClientTorrent(RssItems item) {
        if (item == null) {
            return;
        }
        final AlertIcon alert = new AlertIcon(Alert.AlertType.WARNING);
        alert.setIcon(mainApp.getIconoApp());

        
        try {

            ManageClientBit clientBit = new ManageClientBit();
            clientBit.sendFile(item.getFile(), item.getMediafilter().getFolderpath());

        } catch (Exception ex) {
            if (ex instanceof ClientBitorrentException) {
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            } else {
                mainApp.showAlertWithEx(ex);
            }
        }
    }
    
    private void openFileClientTorrent(List<RssItems> items) {
        
        if (items == null || items.isEmpty()) {
            return;
        }
        
        StringBuilder warnings = new StringBuilder();
        final AlertIcon alert = new AlertIcon(Alert.AlertType.WARNING);
        alert.setIcon(mainApp.getIconoApp());
        
        try {
            
            ManageClientBit clientBit = new ManageClientBit();
            
            for (RssItems item : items) {
                clientBit.sendFile(item.getFile(), item.getMediafilter().getFolderpath());
            }
            

        } catch (Exception ex) {
            if (ex instanceof ClientBitorrentException) {
                warnings.append(ex.getMessage()).append("\n");
            } else {
                mainApp.showAlertWithEx(ex);
            }
        }
        
        if(warnings.length() > 1){
            alert.setContentText(warnings.toString());
            alert.showAndWait();
        }
    }

}
