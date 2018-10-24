/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.grupoad3.flexfx.MainApp;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author daniel_serna
 */
public class DatabaseUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:";


    public static JdbcPooledConnectionSource getConexion() throws SQLException, IOException {

        JdbcPooledConnectionSource connectionSource;



        connectionSource = new JdbcPooledConnectionSource(DATABASE_URL + MainApp.getDatabaseFile().getAbsolutePath());

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
