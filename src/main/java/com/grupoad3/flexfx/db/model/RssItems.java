/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author daniel_serna
 */
@DatabaseTable(tableName = "rss_items")
public class RssItems {

    @DatabaseField(columnName = "id")
    private Integer _id;
    private IntegerProperty id;
    @DatabaseField(columnName = "id_rss")
    private Integer _idrss;
    private IntegerProperty idrss;
    @DatabaseField(columnName = "id_mediafilter")
    private Integer _idmediafilter;
    private IntegerProperty idmediafilter;
    @DatabaseField(columnName = "status")
    private Integer _status;
    private IntegerProperty status;
    @DatabaseField(columnName = "title")
    private Integer _title;
    private IntegerProperty title;
    @DatabaseField(columnName = "link")
    private Integer _link;
    private IntegerProperty link;
    @DatabaseField(columnName = "file")
    private Integer _file;
    private IntegerProperty file;
    @DatabaseField(columnName = "guid")
    private Integer _guid;
    private IntegerProperty guid;
    @DatabaseField(columnName = "date_pub")
    private LocalDate _datepub;
    private ObjectProperty<LocalDate> datepub;
    @DatabaseField(columnName = "date_down")
    private LocalDate _datedown;
    private ObjectProperty<LocalDate> datedown;

    public void setId(Integer id) {
        _id = id;
        if (this.id != null) {
            this.id.set(id);
        }
    }

    public Integer getId() {
        if (id == null) {
            return _id;
        } else {
            return id.get();
        }
    }

    public IntegerProperty idProperty() {
        if (id == null) {
            id = new SimpleIntegerProperty(this, "id", _id);
        }
        return id;
    }

    public void setIdrss(Integer idrss) {
        _idrss = idrss;
        if (this.idrss != null) {
            this.idrss.set(idrss);
        }
    }

    public Integer getIdrss() {
        if (idrss == null) {
            return _idrss;
        } else {
            return idrss.get();
        }
    }

    public IntegerProperty idrssProperty() {
        if (idrss == null) {
            idrss = new SimpleIntegerProperty(this, "idrss", _idrss);
        }
        return idrss;
    }

    public void setIdmediafilter(Integer idmediafilter) {
        _idmediafilter = idmediafilter;
        if (this.idmediafilter != null) {
            this.idmediafilter.set(idmediafilter);
        }
    }

    public Integer getIdmediafilter() {
        if (idmediafilter == null) {
            return _idmediafilter;
        } else {
            return idmediafilter.get();
        }
    }

    public IntegerProperty idmediafilterProperty() {
        if (idmediafilter == null) {
            idmediafilter = new SimpleIntegerProperty(this, "idmediafilter", _idmediafilter);
        }
        return idmediafilter;
    }

    public void setStatus(Integer status) {
        _status = status;
        if (this.status != null) {
            this.status.set(status);
        }
    }

    public Integer getStatus() {
        if (status == null) {
            return _status;
        } else {
            return status.get();
        }
    }

    public IntegerProperty statusProperty() {
        if (status == null) {
            status = new SimpleIntegerProperty(this, "status", _status);
        }
        return status;
    }

    public void setTitle(Integer title) {
        _title = title;
        if (this.title != null) {
            this.title.set(title);
        }
    }

    public Integer getTitle() {
        if (title == null) {
            return _title;
        } else {
            return title.get();
        }
    }

    public IntegerProperty titleProperty() {
        if (title == null) {
            title = new SimpleIntegerProperty(this, "title", _title);
        }
        return title;
    }

    public void setLink(Integer link) {
        _link = link;
        if (this.link != null) {
            this.link.set(link);
        }
    }

    public Integer getLink() {
        if (link == null) {
            return _link;
        } else {
            return link.get();
        }
    }

    public IntegerProperty linkProperty() {
        if (link == null) {
            link = new SimpleIntegerProperty(this, "link", _link);
        }
        return link;
    }

    public void setFile(Integer file) {
        _file = file;
        if (this.file != null) {
            this.file.set(file);
        }
    }

    public Integer getFile() {
        if (file == null) {
            return _file;
        } else {
            return file.get();
        }
    }

    public IntegerProperty fileProperty() {
        if (file == null) {
            file = new SimpleIntegerProperty(this, "file", _file);
        }
        return file;
    }

    public void setGuid(Integer guid) {
        _guid = guid;
        if (this.guid != null) {
            this.guid.set(guid);
        }
    }

    public Integer getGuid() {
        if (guid == null) {
            return _guid;
        } else {
            return guid.get();
        }
    }

    public IntegerProperty guidProperty() {
        if (guid == null) {
            guid = new SimpleIntegerProperty(this, "guid", _guid);
        }
        return guid;
    }

    public void setDatepub(LocalDate datepub) {
        _datepub = datepub;
        if (this.datepub != null) {
            this.datepub.set(datepub);
        }
    }

    public LocalDate getDatepub() {
        if (datepub == null) {
            return _datepub;
        } else {
            return datepub.get();
        }
    }

    public ObjectProperty<LocalDate> datepubProperty() {
        if (datepub == null) {
            datepub = new SimpleObjectProperty<>(this, "datepub", _datepub);
        }
        return datepub;
    }

    public void setDatedown(LocalDate datedown) {
        _datedown = datedown;
        if (this.datedown != null) {
            this.datedown.set(datedown);
        }
    }

    public LocalDate getDatedown() {
        if (datedown == null) {
            return _datedown;
        } else {
            return datedown.get();
        }
    }

    public ObjectProperty<LocalDate> datedownProperty() {
        if (datedown == null) {
            datedown = new SimpleObjectProperty<>(this, "datedown", _datedown);
        }
        return datedown;
    }

}
