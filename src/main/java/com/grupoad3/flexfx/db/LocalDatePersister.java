/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;
import java.time.LocalDate;


/**
 *
 * @author daniel_serna
 */
public class LocalDatePersister extends DateType{
    
    private static final LocalDatePersister INSTANCE = new LocalDatePersister();
    
    private LocalDatePersister() {
        super(SqlType.DATE, new Class<?>[] { LocalDatePersister.class });
    }
    
    public LocalDatePersister(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }
    
    public static LocalDatePersister getSingleton() {
        return INSTANCE;
    }

    // Java program to Database
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDate fecha = (LocalDate) javaObject;
        if(fecha == null){
            return null;
        }else{
            return java.sql.Date.valueOf(fecha);
        }
        
        
        
    }
    
    // Database to java program
    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        
        java.sql.Timestamp  fechaBD = (java.sql.Timestamp) sqlArg;
        if(fechaBD == null){
            return null;
        }else{
            return fechaBD.toLocalDateTime().toLocalDate();
        }
    }

    
    
    
    
    
}
