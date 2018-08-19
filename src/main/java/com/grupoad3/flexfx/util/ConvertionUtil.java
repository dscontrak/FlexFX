/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.util;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author daniel
 */
public class ConvertionUtil {

    public synchronized static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public synchronized static String convertHumanDate(Date date) {
        // http://lea.verou.me/2009/04/java-pretty-dates/
        long current = (new Date()).getTime(),
                timestamp = date.getTime(),
                diff = (current - timestamp) / 1000;
        int amount = 0;
        String what = "";

        /**
         * Second counts 3600: hour 86400: day 604800: week 2592000: month
         * 31536000: year
         */
        if (diff > 31536000) {
            amount = (int) (diff / 31536000);
            what = "year";
        } else if (diff > 31536000) {
            amount = (int) (diff / 31536000);
            what = "month";
        } else if (diff > 604800) {
            amount = (int) (diff / 604800);
            what = "week";
        } else if (diff > 86400) {
            amount = (int) (diff / 86400);
            what = "day";
        } else if (diff > 3600) {
            amount = (int) (diff / 3600);
            what = "hour";
        } else if (diff > 60) {
            amount = (int) (diff / 60);
            what = "minute";
        } else {
            amount = (int) diff;
            what = "second";
            if (amount < 6) {
                return "Just now";
            }
        }

        if (amount == 1) {
            if (what.equals("day")) {
                return "Yesterday";
            } else if (what.equals("week") || what.equals("month") || what.equals("year")) {
                return "Last " + what;
            }
        } else {
            what += "s";
        }

        return amount + " " + what + " ago";
    }
}
