/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author daniel_serna
 */
public class DatabaseUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:";
    private static final File databaseFile = new File("./database.db");
    
    public static JdbcPooledConnectionSource getConexion() throws SQLException, IOException {
        
        JdbcPooledConnectionSource connectionSource;
        
        if (databaseFile.exists() == false) {
                databaseFile.createNewFile();
        }
        
        connectionSource = new JdbcPooledConnectionSource(DATABASE_URL + databaseFile.getAbsolutePath());

        try {
            //Loading the sqlite drivers
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            //Should never happen
            throw new SQLException(e);
        }
        
        return connectionSource;
    }
    
}
