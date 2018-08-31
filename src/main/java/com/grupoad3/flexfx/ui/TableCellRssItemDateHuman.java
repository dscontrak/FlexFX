/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.ui;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.RssItems;
import com.grupoad3.flexfx.util.ConvertionUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

/**
 *
 * @author daniel_serna
 */
public class TableCellRssItemDateHuman extends TableCell<RssItems, String> {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");          
    //private final TableColumn<RssItems, String> param; 
    /*public TableCellDateHuman(TableColumn<RssItems, String> param) {
        this.param = param;
    }*/

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        Date date;
        if (empty || item == null || item.isEmpty()) {
            setText(null);
        } else {
            try {
                
                int row = getIndex();
                RssItems rssItem = getTableView().getItems().get(row);
                if(rssItem.getStatus().equals(ItemStatus.IGNORED.toString())){
                    this.getStyleClass().add("inactive-column");
                }else{
                    this.getStyleClass().remove("inactive-column");
                }
                
                date = format.parse(item);
                item = ConvertionUtil.convertHumanDate(date);
            } catch (ParseException ex) {
                //ignored
            }
            this.setText(item);
            
        }
    }
}
