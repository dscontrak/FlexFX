/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.ui;

import com.grupoad3.flexfx.db.model.MediaFilters;
import javafx.scene.control.TableCell;

/**
 *
 * @author daniel_serna
 */
public class TableCellMediaFilterColorActive extends TableCell<MediaFilters, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (empty || item == null || item.isEmpty()) {
            setText(null);
        } else {

            int row = getIndex();
            MediaFilters rssItem = getTableView().getItems().get(row);
            if (rssItem.getActive() == false) {
                if (this.getStyleClass().contains("inactive-column") == false) {
                    this.getStyleClass().add("inactive-column");
                }
            } else {
                this.getStyleClass().removeAll("inactive-column");
            }

            this.setText(item);

        }
    }

}
