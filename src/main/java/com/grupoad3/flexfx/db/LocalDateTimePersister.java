/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.TimeStampType;

import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 *
 * @author daniel_serna
 */
public class LocalDateTimePersister extends TimeStampType{
    
    private static final LocalDateTimePersister INSTANCE = new LocalDateTimePersister();
    
    private LocalDateTimePersister() {
        super(SqlType.DATE, new Class<?>[] { LocalDateTimePersister.class });
    }
    
    public LocalDateTimePersister(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }
    
    public static LocalDateTimePersister getSingleton() {
        return INSTANCE;
    }

    // Java program to Database
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDateTime fecha = (LocalDateTime) javaObject;
        Timestamp fechaConvertir = null;
        
        if(fecha == null){
            return null;
        }else{
            fechaConvertir = Timestamp.valueOf(fecha);
            return fechaConvertir;
        }                
        
    }
    
    // Database to java program
    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        
        java.sql.Timestamp  fechaBD = (java.sql.Timestamp) sqlArg;
        if(fechaBD == null){
            return null;
        }else{
            return fechaBD.toLocalDateTime();
        }
    }

    
    
    
    
    
}
