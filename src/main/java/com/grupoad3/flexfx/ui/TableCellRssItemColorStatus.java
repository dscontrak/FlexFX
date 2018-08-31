/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.ui;

import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.RssItems;
import javafx.scene.control.TableCell;

/**
 *
 * @author daniel_serna
 */
public class TableCellRssItemColorStatus extends TableCell<RssItems, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.        
        if (empty || item == null || item.isEmpty()) {
            setText(null);
        } else {

            int row = getIndex();
            RssItems rssItem = getTableView().getItems().get(row);
            if (rssItem.getStatus().equals(ItemStatus.IGNORED.toString())) {
                this.getStyleClass().add("inactive-column");
            }else{
                this.getStyleClass().remove("inactive-column");
            }

            this.setText(item);

        }
    }
}
