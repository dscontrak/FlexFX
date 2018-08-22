/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.util;

import com.grupoad3.flexfx.db.model.RssItems;
import java.util.Comparator;

/**
 *
 * @author daniel
 */
public class ComparatorRssDate implements Comparator<RssItems>{

    @Override
    public int compare(RssItems o1, RssItems o2) {
        return o1.getDatepub().compareTo(o2.getDatepub()) * -1;
    }


}
