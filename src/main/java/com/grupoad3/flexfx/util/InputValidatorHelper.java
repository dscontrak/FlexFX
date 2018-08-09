/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author daniel_serna
 */
public class InputValidatorHelper {
    
    private final List<String> errors = new ArrayList<>();
    private boolean existError = false;

    public List<String> getErrors() {
        return errors;
    }

    public boolean isExistError() {
        return existError;
    }

    public void setExistError(boolean existError) {
        this.existError = existError;
    }
    
    
    
    
    
    public boolean isValidEmail(String string){
        boolean valid;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        
        valid = matcher.matches();
        
        if(!valid){
            existError = true;
        }
                
        return valid;
    }

    public boolean isValidPassword(String string, boolean allowSpecialChars){
        String PATTERN;
        if(allowSpecialChars){
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
            PATTERN = "^[a-zA-Z@#$%]\\w{5,19}$";
        }else{
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
            PATTERN = "^[a-zA-Z]\\w{5,19}$";
        }


        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(string);
        
        boolean valid = matcher.matches();
        
        if(!valid){
            existError = true;
        }
        
        return valid;
    }

    public boolean isNullOrEmpty(String string){
        boolean isNullorEmpy = (string == null || string.isEmpty());
        
        if(isNullorEmpy){
            existError = true;
        }
        
        return isNullorEmpy;
    }

    public boolean isNumeric(String string){
        
        boolean isNumeric;
        
        isNumeric = string.matches("^[-+]?\\d+(\\.\\d+)?$");
        
        if(!isNumeric){
            existError = true;
        }
        
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }
}
