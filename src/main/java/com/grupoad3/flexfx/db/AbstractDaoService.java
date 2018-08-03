/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;

/**
 *
 * @author daniel_serna
 * @param <T>
 */
public abstract class AbstractDaoService<T> {
    //private static final Logger logger = LoggerFactory.getLogger(AbstractDaoService.class);

    private final Class clazz;
    protected Dao dao;
    protected JdbcPooledConnectionSource connection;

    public AbstractDaoService(Class clazz) {

        try {
            this.clazz = clazz;

            // TODO: let user create it's own database
            /*connection = DatabaseUtils.getH2OrmliteConnectionPool(configuration.getDatabasePath(),
                    // please do not laugh :)
                    SpringConfiguration.DB_LOGIN, SpringConfiguration.DB_PASSWORD);*/
            connection = DatabaseUtils.getConexion();            
            

            // instantiate the dao
            dao = DaoManager.createDao(connection, clazz);

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    @PreDestroy
    public void dispose() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Clear all entities in this repository
     *
     * @throws IOException
     */
    public void clearAllEntities() throws IOException {
        try {
            TableUtils.clearTable(connection, clazz);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Delete entity corresponding to specified id
     *
     * @param objectId
     * @throws IOException
     */
    public void deleteById(Long objectId) throws IOException {
        try {
            dao.deleteById(objectId);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return object corresponding to specified id or null
     *
     * @param id
     * @return
     * @throws IOException
     */
    public T getById(Long id) throws IOException {
        try {
            return (T) dao.queryForId(id);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<T> getByIds(long[] objectIds) throws IOException {

        if (objectIds == null || objectIds.length < 0) {
            return new ArrayList<>();
        }

        ArrayList<T> results = new ArrayList<>();
        for (long id : objectIds) {
            results.add(getById(id));
        }

        return results;
    }

    /**
     * Create an entity
     *
     * @param toCreate
     * @return
     * @throws IOException
     */
    public T create(T toCreate) throws IOException {
        try {
            dao.create(toCreate);
            return toCreate;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Refresh information of a customer object from database
     *
     * @param obj
     * @throws IOException
     */
    public void refresh(T obj) throws IOException {

        if (obj == null) {
            ////logger.error("Cannot refresh null object: " + obj, new NullPointerException("Cannot refresh null object: " + obj));
            return;
        }

        try {
            dao.refresh(obj);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Refresh information of a customer object from database
     *
     * @param listObj
     * @throws IOException
     */
    public void refresh(List<T> listObj) throws IOException {

        if (listObj == null) {
            throw new NullPointerException("Cannot refresh null object");
        }

        for (T o : listObj) {
            refresh(o);
        }

    }

    /**
     * Return a list of all entities
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<T> getAll(Long limit, Long offset) throws IOException {
        try {
            QueryBuilder builder = dao.queryBuilder();
            if (limit != -1l) {
                builder.limit(limit);
            }
            if (offset != -1l) {
                builder.offset(offset);
            }
            return builder.query();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Update specified entity in database
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public int update(T obj) throws IOException {
        try {
            return dao.update(obj);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Update specified entities in database
     *
     * @param list
     * @return
     * @throws IOException
     */
    public int update(List<T> list) throws IOException {
        int result = 0;
        try {
            for (T obj : list) {
                result += dao.update(obj);
            }
            return result;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
