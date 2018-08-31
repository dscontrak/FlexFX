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
@DatabaseTable(tableName = "rss_items")
public class RssItems {

    public static final String ID_FIELD_NAME = "id";
    public static final String ID_RSS_FIELD_NAME = "rss_id";
    public static final String ID_MEDIAFILTER_FIELD_NAME = "mediafilter_id";
    public static final String STATUS_FIELD_NAME = "status";
    public static final String TITLE_FIELD_NAME = "title";
    public static final String LINK_FIELD_NAME = "link";
    public static final String FILE_FIELD_NAME = "file";
    public static final String GUID_FIELD_NAME = "guid";
    public static final String DATE_PUB_FIELD_NAME = "date_pub";
    public static final String DATE_DOWN_FIELD_NAME = "date_down";
    public static final String FMOD_FIELD_NAME = "fmod";
    public static final String FADD_FIELD_NAME = "fadd";
    public static final String DELETED_FIELD_NAME = "deleted";

    public RssItems() {
        _status = ItemStatus.IGNORED;
        _deleted = false;
        _fadd = LocalDateTime.now();
        _fmod = LocalDateTime.now();
        //downloadNow = false;
        originRss = false;
        applyMainFilter = false;
        applySecondFilter = false;
        applyIgnoreFilter = false;
    }

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private Integer _id;
    private IntegerProperty id;
    /*@DatabaseField(columnName = ID_RSS_FIELD_NAME, canBeNull = false)
    private Integer _idrss;
    private IntegerProperty idrss;*/

    @DatabaseField(canBeNull = false, foreign = true)
    private Rss rss;

    @DatabaseField(canBeNull = true, foreign = true)
    private MediaFilters mediafilter;
    /*private Integer _idmediafilter;
    private IntegerProperty idmediafilter;*/

    @DatabaseField(columnName = STATUS_FIELD_NAME, canBeNull = false)
    private ItemStatus _status;
    private StringProperty status;
    @DatabaseField(columnName = TITLE_FIELD_NAME, canBeNull = false, index = true)
    private String _title;
    private StringProperty title;
    @DatabaseField(columnName = LINK_FIELD_NAME, canBeNull = false)
    private String _link;
    private StringProperty link;
    @DatabaseField(columnName = FILE_FIELD_NAME)
    private String _file;
    private StringProperty file;
    @DatabaseField(columnName = GUID_FIELD_NAME, canBeNull = false, uniqueIndex = true)
    private String _guid;
    private StringProperty guid;
    @DatabaseField(columnName = DATE_PUB_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _datepub;
    private ObjectProperty<LocalDateTime> datepub;
    @DatabaseField(columnName = DATE_DOWN_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _datedown;
    private ObjectProperty<LocalDateTime> datedown;
    @DatabaseField(columnName = FMOD_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _fmod;
    private ObjectProperty<LocalDateTime> fmod;
    @DatabaseField(columnName = FADD_FIELD_NAME, persisterClass = LocalDateTimePersister.class)
    private LocalDateTime _fadd;
    private ObjectProperty<LocalDateTime> fadd;
    @DatabaseField(columnName = DELETED_FIELD_NAME)
    private Boolean _deleted;
    private BooleanProperty deleted;

    // No database property
    //private boolean downloadNow;
    private boolean originRss;

    private boolean applyMainFilter;
    private boolean applySecondFilter;
    private boolean applyIgnoreFilter;

    public boolean isApplyMainFilter() {
        return applyMainFilter;
    }

    public void setApplyMainFilter(boolean applyMainFilter) {
        this.applyMainFilter = applyMainFilter;
    }

    public boolean isApplySecondFilter() {
        return applySecondFilter;
    }

    public void setApplySecondFilter(boolean applySecondFilter) {
        this.applySecondFilter = applySecondFilter;
    }

    public boolean isApplyIgnoreFilter() {
        return applyIgnoreFilter;
    }

    public void setApplyIgnoreFilter(boolean applyIgnoreFilter) {
        this.applyIgnoreFilter = applyIgnoreFilter;
    }



    /*public boolean isDonwloadNow() {
        return downloadNow;
    }*/

    /*public void setDonwloadNow(boolean originBD) {
        this.downloadNow = originBD;
    }*/

    public boolean isOriginRss() {
        return originRss;
    }

    public void setOriginRss(boolean originRss) {
        this.originRss = originRss;
    }

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

    /*public void setIdrss(Integer idrss) {
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
    }*/
    public Rss getRss() {
        return rss;
    }

    public void setRss(Rss rss) {
        this.rss = rss;
    }

    public MediaFilters getMediafilter() {
        return mediafilter;
    }

    public void setMediafilter(MediaFilters mediafilter) {
        this.mediafilter = mediafilter;
    }

    /*public void setIdmediafilter(Integer idmediafilter) {
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
    }*/
    public void setStatus(ItemStatus status) {
        _status = status;
        if (this.status != null) {
            this.status.set(status.toString());
        }
    }

    public String getStatus() {
        if (status == null) {
            return _status.toString();
        } else {
            return status.get();
        }
    }

    public StringProperty statusProperty() {
        if (status == null) {
            status = new SimpleStringProperty(this, "status", _status.toString());
        }
        return status;
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

    public void setLink(String link) {
        _link = link;
        if (this.link != null) {
            this.link.set(link);
        }
    }

    public String getLink() {
        if (link == null) {
            return _link;
        } else {
            return link.get();
        }
    }

    public StringProperty linkProperty() {
        if (link == null) {
            link = new SimpleStringProperty(this, "link", _link);
        }
        return link;
    }

    public void setFile(String file) {
        _file = file;
        if (this.file != null) {
            this.file.set(file);
        }
    }

    public String getFile() {
        if (file == null) {
            return _file;
        } else {
            return file.get();
        }
    }

    public StringProperty fileProperty() {
        if (file == null) {
            file = new SimpleStringProperty(this, "file", _file);
        }
        return file;
    }

    public void setGuid(String guid) {
        _guid = guid;
        if (this.guid != null) {
            this.guid.set(guid);
        }
    }

    public String getGuid() {
        if (guid == null) {
            return _guid;
        } else {
            return guid.get();
        }
    }

    public StringProperty guidProperty() {
        if (guid == null) {
            guid = new SimpleStringProperty(this, "guid", _guid);
        }
        return guid;
    }

    public void setDatepub(LocalDateTime datepub) {
        _datepub = datepub;
        if (this.datepub != null) {
            this.datepub.set(datepub);
        }
    }

    public LocalDateTime getDatepub() {
        if (datepub == null) {
            return _datepub;
        } else {
            return datepub.get();
        }
    }

    public ObjectProperty<LocalDateTime> datepubProperty() {
        if (datepub == null) {
            datepub = new SimpleObjectProperty<>(this, "datepub", _datepub);
        }
        return datepub;
    }

    public void setDatedown(LocalDateTime datedown) {
        _datedown = datedown;
        if (this.datedown != null) {
            this.datedown.set(datedown);
        }
    }

    public LocalDateTime getDatedown() {
        if (datedown == null) {
            return _datedown;
        } else {
            return datedown.get();
        }
    }

    public ObjectProperty<LocalDateTime> datedownProperty() {
        if (datedown == null) {
            datedown = new SimpleObjectProperty<>(this, "datedown", _datedown);
        }
        return datedown;
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

    @Override
    public int hashCode() {
        return this.getLink().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }
        return this.getLink().equals( ((RssItems) obj).getLink() );
    }

}
