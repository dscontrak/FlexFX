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
@DatabaseTable(tableName = "media_filters")
public class MediaFilters {

    @DatabaseField(columnName = "id")
    private Integer _id;
    private IntegerProperty id;
    @DatabaseField(columnName = "id_rss")
    private Integer _idrss;
    private IntegerProperty idrss;
    @DatabaseField(columnName = "title")
    private Integer _title;
    private IntegerProperty title;
    @DatabaseField(columnName = "filter_main")
    private Integer _filtermain;
    private IntegerProperty filtermain;
    @DatabaseField(columnName = "filter_secondary")
    private Integer _filtersecondary;
    private IntegerProperty filtersecondary;
    @DatabaseField(columnName = "category")
    private Integer _category;
    private IntegerProperty category;
    @DatabaseField(columnName = "folder_path")
    private Integer _folderpath;
    private IntegerProperty folderpath;
    @DatabaseField(columnName = "fmod")
    private LocalDate _fmod;
    private ObjectProperty<LocalDate> fmod;
    @DatabaseField(columnName = "fadd")
    private LocalDate _fadd;
    private ObjectProperty<LocalDate> fadd;

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

    public void setFiltermain(Integer filtermain) {
        _filtermain = filtermain;
        if (this.filtermain != null) {
            this.filtermain.set(filtermain);
        }
    }

    public Integer getFiltermain() {
        if (filtermain == null) {
            return _filtermain;
        } else {
            return filtermain.get();
        }
    }

    public IntegerProperty filtermainProperty() {
        if (filtermain == null) {
            filtermain = new SimpleIntegerProperty(this, "filtermain", _filtermain);
        }
        return filtermain;
    }

    public void setFiltersecondary(Integer filtersecondary) {
        _filtersecondary = filtersecondary;
        if (this.filtersecondary != null) {
            this.filtersecondary.set(filtersecondary);
        }
    }

    public Integer getFiltersecondary() {
        if (filtersecondary == null) {
            return _filtersecondary;
        } else {
            return filtersecondary.get();
        }
    }

    public IntegerProperty filtersecondaryProperty() {
        if (filtersecondary == null) {
            filtersecondary = new SimpleIntegerProperty(this, "filtersecondary", _filtersecondary);
        }
        return filtersecondary;
    }

    public void setCategory(Integer category) {
        _category = category;
        if (this.category != null) {
            this.category.set(category);
        }
    }

    public Integer getCategory() {
        if (category == null) {
            return _category;
        } else {
            return category.get();
        }
    }

    public IntegerProperty categoryProperty() {
        if (category == null) {
            category = new SimpleIntegerProperty(this, "category", _category);
        }
        return category;
    }

    public void setFolderpath(Integer folderpath) {
        _folderpath = folderpath;
        if (this.folderpath != null) {
            this.folderpath.set(folderpath);
        }
    }

    public Integer getFolderpath() {
        if (folderpath == null) {
            return _folderpath;
        } else {
            return folderpath.get();
        }
    }

    public IntegerProperty folderpathProperty() {
        if (folderpath == null) {
            folderpath = new SimpleIntegerProperty(this, "folderpath", _folderpath);
        }
        return folderpath;
    }

    public void setFmod(LocalDate fmod) {
        _fmod = fmod;
        if (this.fmod != null) {
            this.fmod.set(fmod);
        }
    }

    public LocalDate getFmod() {
        if (fmod == null) {
            return _fmod;
        } else {
            return fmod.get();
        }
    }

    public ObjectProperty<LocalDate> fmodProperty() {
        if (fmod == null) {
            fmod = new SimpleObjectProperty<>(this, "fmod", _fmod);
        }
        return fmod;
    }

    public void setFadd(LocalDate fadd) {
        _fadd = fadd;
        if (this.fadd != null) {
            this.fadd.set(fadd);
        }
    }

    public LocalDate getFadd() {
        if (fadd == null) {
            return _fadd;
        } else {
            return fadd.get();
        }
    }

    public ObjectProperty<LocalDate> faddProperty() {
        if (fadd == null) {
            fadd = new SimpleObjectProperty<>(this, "fadd", _fadd);
        }
        return fadd;
    }

}
