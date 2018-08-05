/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.model;

import com.grupoad3.flexfx.db.LocalDateTimePersister;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author daniel_serna
 */
@DatabaseTable(tableName = "media_filters")
public class MediaFilters {

    public static final String ID_FIELD_NAME = "id";
    public static final String RSS_ID_FIELD_NAME = "rss_id";
    public static final String TITLE_FIELD_NAME = "title";
    public static final String FILTER_MAIN_FIELD_NAME = "filter_main";
    public static final String FILTER_SECONDARY_FIELD_NAME = "filter_secondary";
    public static final String CATEGORY_FIELD_NAME = "category";
    public static final String FOLDER_PATH_FIELD_NAME = "folder_path";
    public static final String FMOD_FIELD_NAME = "fmod";
    public static final String FADD_FIELD_NAME = "fadd";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private Integer _id;
    private IntegerProperty id;
    /*@DatabaseField(columnName = RSS_ID_FIELD_NAME, canBeNull = false)
    private Integer _rssid;
    private IntegerProperty rssid;*/
    @DatabaseField(canBeNull = false, foreign = true, uniqueIndexName = "media_filters_id_rss_title_uk")
    private Rss rss;    
    
    @DatabaseField(columnName = TITLE_FIELD_NAME, canBeNull = false, uniqueIndexName = "media_filters_id_rss_title_uk", indexName = "rss_items_title_filter_main_idx")
    private String _title;
    private StringProperty title;
    @DatabaseField(columnName = FILTER_MAIN_FIELD_NAME, canBeNull = false, indexName = "rss_items_title_filter_main_idx")
    private String _filtermain;
    private StringProperty filtermain;
    @DatabaseField(columnName = FILTER_SECONDARY_FIELD_NAME)
    private String _filtersecondary;
    private StringProperty filtersecondary;
    @DatabaseField(columnName = CATEGORY_FIELD_NAME, width = 20)
    private MediaType _category;
    private StringProperty category;
    
    @DatabaseField(columnName = FOLDER_PATH_FIELD_NAME, columnDefinition = "TEXT")
    private String _folderpath;
    private StringProperty folderpath;
    @DatabaseField(columnName = FMOD_FIELD_NAME, persisterClass = LocalDateTimePersister.class )
    private LocalDateTime _fmod;
    private ObjectProperty<LocalDateTime> fmod;
    @DatabaseField(columnName = FADD_FIELD_NAME, persisterClass = LocalDateTimePersister.class )
    private LocalDateTime _fadd;
    private ObjectProperty<LocalDateTime> fadd;

    public MediaFilters() {
        _fadd = LocalDateTime.now();
        _fmod = LocalDateTime.now();
        _category = MediaType.OTHER;
        //_deleted = false;
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

    public void setRss(Rss rss) {
        this.rss = rss;
    }

    public Rss getRss() {
        return rss;
    }

    /*public IntegerProperty rssidProperty() {
        if (rssid == null) {
            rssid = new SimpleIntegerProperty(this, "rssid", _rssid);
        }
        return rssid;
    }*/

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

    public void setFiltermain(String filtermain) {
        _filtermain = filtermain;
        if (this.filtermain != null) {
            this.filtermain.set(filtermain);
        }
    }

    public String getFiltermain() {
        if (filtermain == null) {
            return _filtermain;
        } else {
            return filtermain.get();
        }
    }

    public StringProperty filtermainProperty() {
        if (filtermain == null) {
            filtermain = new SimpleStringProperty(this, "filtermain", _filtermain);
        }
        return filtermain;
    }

    public void setFiltersecondary(String filtersecondary) {
        _filtersecondary = filtersecondary;
        if (this.filtersecondary != null) {
            this.filtersecondary.set(filtersecondary);
        }
    }

    public String getFiltersecondary() {
        if (filtersecondary == null) {
            return _filtersecondary;
        } else {
            return filtersecondary.get();
        }
    }

    public StringProperty filtersecondaryProperty() {
        if (filtersecondary == null) {
            filtersecondary = new SimpleStringProperty(this, "filtersecondary", _filtersecondary);
        }
        return filtersecondary;
    }

    public void setCategory(MediaType category) {
        _category = category;
        if (this.category != null) {
            this.category.set(category.toString());
        }
    }

    public String getCategory() {
        if (category == null) {
            return _category.toString();
        } else {
            return category.get();
        }
    }

    public StringProperty categoryProperty() {
        if (category == null) {
            category = new SimpleStringProperty(this, "category", _category.toString());
        }
        return category;
    }

    public void setFolderpath(String folderpath) {
        _folderpath = folderpath;
        if (this.folderpath != null) {
            this.folderpath.set(folderpath);
        }
    }

    public String getFolderpath() {
        if (folderpath == null) {
            return _folderpath;
        } else {
            return folderpath.get();
        }
    }

    public StringProperty folderpathProperty() {
        if (folderpath == null) {
            folderpath = new SimpleStringProperty(this, "folderpath", _folderpath);
        }
        return folderpath;
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
