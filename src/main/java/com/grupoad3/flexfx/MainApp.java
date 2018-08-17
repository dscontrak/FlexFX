package com.grupoad3.flexfx;

import com.grupoad3.flexfx.controller.MainController;
import com.grupoad3.flexfx.controller.MediaFilterController;
import com.grupoad3.flexfx.controller.RssController;
import com.grupoad3.flexfx.db.DatabaseUtils;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.db.services.RssService;
import com.grupoad3.flexfx.ui.AlertIcon;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final Image iconoApp = new Image(getClass().getResourceAsStream("/img/icon.png"));            
    private Stage primaryStage;
    private final ObservableList<Rss> rssData = FXCollections.observableArrayList();
    private final ObservableList<RssItems> rssItemsData = FXCollections.observableArrayList();
    private final ObservableList<MediaFilters> mediaFiltersData = FXCollections.observableArrayList();
    
    public ObservableList<Rss> getRssData() {
        return rssData;
    }

    public ObservableList<RssItems> getRssItemsData() {
        return rssItemsData;
    }

    public ObservableList<MediaFilters> getMediaFiltersData() {
        return mediaFiltersData;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Image getIconoApp() {
        return iconoApp;
    }
        
        
    @Override
    public void start(Stage stage) throws Exception {
         
        primaryStage = stage;
        primaryStage.setTitle("FlexFX");
        primaryStage.getIcons().add(iconoApp);

        initProperties();
        initDBMigration();
        initData();
        
        showMainViewWindow();                
    }
    
    /**
     * 
     * @param ex Set error exeption
     */
    public void showAlertWithEx(Throwable ex){
        AlertIcon alert = new AlertIcon(Alert.AlertType.ERROR);
        alert.setIcon(iconoApp);
        alert.setExeption(ex);
        alert.showAndWait();
    }

    private void showMainViewWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane mainWindow = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainWindow);

            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Poniendo el controlador de la vista y poniendo la app contenedora.
            MainController controlador = loader.getController();
            controlador.setMainApp(this);                       
            
            
        } catch (IOException e) {
            showAlertWithEx(e);
        }

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void initProperties() {
        try {
            ConfigApp config = new ConfigApp();
            config.setup();
            config.loadProperties();
            
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initData() {
        try {
            RssService rssService = new RssService();                                         
            
            List<Rss> listRss = rssService.getLastRss(10, 0, true);
            listRss.forEach((r) -> {
                rssData.add(r);
            });                                    
            
        } catch (IOException ex) {
            showAlertWithEx(ex);
        }
    }
   

    private void initDBMigration() {
        try {
            ConfigApp config = new ConfigApp();
            config.loadProperties();
            String isMigrated = ConfigApp.readProperty(ConfigApp.ConfigTypes.ISMIGRATED);
            
            if (isMigrated != null && isMigrated.equals("false")) {
                JdbcPooledConnectionSource connection = DatabaseUtils.getConexion();
                
                // Tables
                TableUtils.createTableIfNotExists(connection, Rss.class);
                TableUtils.createTableIfNotExists(connection, RssItems.class);
                TableUtils.createTableIfNotExists(connection, MediaFilters.class);
                
                // Writeproperties
                config.writeProperty(ConfigApp.ConfigTypes.ISMIGRATED, "true");
                
            }
        } catch (Exception e) {
            showAlertWithEx(e);        
        }
    }

    public boolean showMediaFilterEditDialog(MediaFilters filter, Rss rss) {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        
        try {
            loader.setLocation(getClass().getResource("/fxml/MediaFilterAdd.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            // Create dialog stage
            Stage dialoStage = new Stage();
            dialoStage.setTitle("Media Filter");
            dialoStage.initModality(Modality.WINDOW_MODAL);
            dialoStage.initOwner(primaryStage);
            
            
            dialoStage.getIcons().add(iconoApp);
            
            // Scene
            Scene scene = new Scene(page);
            
            // Set scene and controller
            dialoStage.setScene(scene);
            MediaFilterController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialoStage);
            controller.setMediaFilter(filter);
            controller.setCurrentRss(rss);
            
            // Show
            dialoStage.showAndWait();
                    
            return controller.isSaved();
            
        } catch (Exception e) {
            showAlertWithEx(e);
            return false;
        }
        
        
    }

    public boolean showRssEditDialog(Rss rss) {
        
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        
        try {
            loader.setLocation(getClass().getResource("/fxml/RssAdd.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            // Create dialog stage
            Stage dialoStage = new Stage();
            dialoStage.setTitle("Rss");
            dialoStage.initModality(Modality.WINDOW_MODAL);
            dialoStage.initOwner(primaryStage);            
            
            dialoStage.getIcons().add(iconoApp);
            
            // Scene
            Scene scene = new Scene(page);
            
            // Set scene and controller
            dialoStage.setScene(scene);
            RssController controller = loader.getController();
            controller.setMainApp(this);               
            controller.setDialogStage(dialoStage);
            controller.setRss(rss);
            
            
            // Show
            dialoStage.showAndWait();
                    
            return controller.isSaved();
            
        } catch (Exception e) {
            showAlertWithEx(e);
            return false;
        }
        
        
    }

}
