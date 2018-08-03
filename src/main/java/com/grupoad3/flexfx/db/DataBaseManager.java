/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.grupoad3.flexfx.db.model.Rss;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel_serna
 */
@Deprecated
public class DataBaseManager {
     // we are using the in-memory SQLite
    
    private Dao<Rss, Integer> rssDao;
    private ConnectionSource connectionSource;

    private File databaseFile;

    /*public Dao<Rss, Integer> getRssDao() {
        return personDao;
    }*/

    public DataBaseManager(File fileDB) throws SQLException {
        databaseFile = fileDB;
        // create our data-source for the database
        

        try {
            connectionSource = DatabaseUtils.getConexion();
            //Loading the sqlite drivers
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            //Should never happen
            throw new SQLException(e);
        } catch (IOException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create dao
        //personDao = DaoManager.createDao(connectionSource, Rss.class);

    }

    /**
     * Setup our database and DAOs
     */
    public void setupDatabase() throws Exception {

        rssDao = DaoManager.createDao(connectionSource, Rss.class);

        
    }
    
    /*public void migrationTables() throws SQLException{
        // if you need to create the table
        TableUtils.createTableIfNotExists(connectionSource, Rss.class);
    }
    
    public void saveRss(Rss p) throws SQLException {
        rssDao.create(p);
    }
    
    public void editRss(Rss p) throws SQLException {
        rssDao.update(p);
    }
    
    public void deleteRss(Rss p) throws SQLException {
        rssDao.delete(p);
    }*/
}
