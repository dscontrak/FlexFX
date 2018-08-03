/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.model;

import com.grupoad3.flexfx.db.LocalDateTimePersister;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author daniel_serna
 */
@DatabaseTable(tableName = "rss")
public class Rss {

    public static final String ID_FIELD_NAME = "id";
    public static final String TITLE_FIELD_NAME = "title";
    public static final String LINK_RSS_FIELD_NAME = "link_rss";
    public static final String LINK_WEB_FIELD_NAME = "link_web";
    public static final String PUB_DATE_FIELD_NAME = "pub_date";
    public static final String LAST_SYNC_FIELD_NAME = "last_sync";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String ITEM_MOSTNEW_FIELD_NAME = "item_mostnew";
    public static final String DELETED_FIELD_NAME = "deleted";
    public static final String FMOD_FIELD_NAME = "fmod";
    public static final String FADD_FIELD_NAME = "fadd";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private Integer _id;
    private IntegerProperty id;
    @DatabaseField(columnName = TITLE_FIELD_NAME)
    private String _title;
    private StringProperty title;
    @DatabaseField(columnName = LINK_RSS_FIELD_NAME)
    private String _linkrss;
    private StringProperty linkrss;
    @DatabaseField(columnName = LINK_WEB_FIELD_NAME)
    private String _linkweb;
    private StringProperty linkweb;
    @DatabaseField(columnName = PUB_DATE_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _pubdate;
    private ObjectProperty<LocalDateTime> pubdate;
    @DatabaseField(columnName = LAST_SYNC_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _lastsync;
    private ObjectProperty<LocalDateTime> lastsync;
    @DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
    private String _description;
    private StringProperty description;
    @DatabaseField(columnName = ITEM_MOSTNEW_FIELD_NAME)
    private String _itemmostnew;
    private StringProperty itemmostnew;
    @DatabaseField(columnName = DELETED_FIELD_NAME)
    private Boolean _deleted;
    private BooleanProperty deleted;
    @DatabaseField(columnName = FMOD_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _fmod;
    private ObjectProperty<LocalDateTime> fmod;
    @DatabaseField(columnName = FADD_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _fadd;
    private ObjectProperty<LocalDateTime> fadd;

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

    public void setTitle(String title) {
        _title = title;
        if (this.title != null) {
            this.title.set(title);
        }
    }

    public String getTitle() {
        if (title == null) {
            return _title;
        } else {
            return title.get();
        }
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title", _title);
        }
        return title;
    }

    public void setLinkrss(String linkrss) {
        _linkrss = linkrss;
        if (this.linkrss != null) {
            this.linkrss.set(linkrss);
        }
    }

    public String getLinkrss() {
        if (linkrss == null) {
            return _linkrss;
        } else {
            return linkrss.get();
        }
    }

    public StringProperty linkrssProperty() {
        if (linkrss == null) {
            linkrss = new SimpleStringProperty(this, "linkrss", _linkrss);
        }
        return linkrss;
    }

    public void setLinkweb(String linkweb) {
        _linkweb = linkweb;
        if (this.linkweb != null) {
            this.linkweb.set(linkweb);
        }
    }

    public String getLinkweb() {
        if (linkweb == null) {
            return _linkweb;
        } else {
            return linkweb.get();
        }
    }

    public StringProperty linkwebProperty() {
        if (linkweb == null) {
            linkweb = new SimpleStringProperty(this, "linkweb", _linkweb);
        }
        return linkweb;
    }

    public void setPubdate(LocalDateTime pubdate) {
        _pubdate = pubdate;
        if (this.pubdate != null) {
            this.pubdate.set(pubdate);
        }
    }

    public LocalDateTime getPubdate() {
        if (pubdate == null) {
            return _pubdate;
        } else {
            return pubdate.get();
        }
    }

    public ObjectProperty<LocalDateTime> pubdateProperty() {
        if (pubdate == null) {
            pubdate = new SimpleObjectProperty<>(this, "pubdate", _pubdate);
        }
        return pubdate;
    }

    public void setLastsync(LocalDateTime lastsync) {
        _lastsync = lastsync;
        if (this.lastsync != null) {
            this.lastsync.set(lastsync);
        }
    }

    public LocalDateTime getLastsync() {
        if (lastsync == null) {
            return _lastsync;
        } else {
            return lastsync.get();
        }
    }

    public ObjectProperty<LocalDateTime> lastsyncProperty() {
        if (lastsync == null) {
            lastsync = new SimpleObjectProperty<>(this, "lastsync", _lastsync);
        }
        return lastsync;
    }

    public void setDescription(String description) {
        _description = description;
        if (this.description != null) {
            this.description.set(description);
        }
    }

    public String getDescription() {
        if (description == null) {
            return _description;
        } else {
            return description.get();
        }
    }

    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty(this, "description", _description);
        }
        return description;
    }

    public void setItemmostnew(String itemmostnew) {
        _itemmostnew = itemmostnew;
        if (this.itemmostnew != null) {
            this.itemmostnew.set(itemmostnew);
        }
    }

    public String getItemmostnew() {
        if (itemmostnew == null) {
            return _itemmostnew;
        } else {
            return itemmostnew.get();
        }
    }

    public StringProperty itemmostnewProperty() {
        if (itemmostnew == null) {
            itemmostnew = new SimpleStringProperty(this, "itemmostnew", _itemmostnew);
        }
        return itemmostnew;
    }

    public void setDeleted(Boolean deleted) {
        _deleted = deleted;
        if (this.deleted != null) {
            this.deleted.set(deleted);
        }
    }

    public Boolean getDeleted() {
        if (deleted == null) {
            return _deleted;
        } else {
            return deleted.get();
        }
    }

    public BooleanProperty deletedProperty() {
        if (deleted == null) {
            deleted = new SimpleBooleanProperty(this, "deleted", _deleted);
        }
        return deleted;
    }

    public void setFmod(LocalDateTime fmod) {
        _fmod = fmod;
        if (this.fmod != null) {
            this.fmod.set(fmod);
        }
    }

    public LocalDateTime getFmod() {
        if (fmod == null) {
            return _fmod;
        } else {
            return fmod.get();
        }
    }

    public ObjectProperty<LocalDateTime> fmodProperty() {
        if (fmod == null) {
            fmod = new SimpleObjectProperty<>(this, "fmod", _fmod);
        }
        return fmod;
    }

    public void setFadd(LocalDateTime fadd) {
        _fadd = fadd;
        if (this.fadd != null) {
            this.fadd.set(fadd);
        }
    }

    public LocalDateTime getFadd() {
        if (fadd == null) {
            return _fadd;
        } else {
            return fadd.get();
        }
    }

    public ObjectProperty<LocalDateTime> faddProperty() {
        if (fadd == null) {
            fadd = new SimpleObjectProperty<>(this, "fadd", _fadd);
        }
        return fadd;
    }

}
