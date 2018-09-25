/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.services;

import com.grupoad3.flexfx.db.AbstractDaoService;
import com.grupoad3.flexfx.db.model.MediaFilters;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.util.List;

/**
 *
 * @author daniel_serna
 */
@SuppressWarnings("unchecked")
public class MediaFilterService extends AbstractDaoService<MediaFilters> {

    public MediaFilterService() {
        super(MediaFilters.class);
    }

    /*public List<MediaFilters> getLast(long limit, long offset, boolean withDeleted) throws IOException {

        // return all with deleted
        if (withDeleted) {
            return super.getAll(limit, offset);
        } else {

            try {
                QueryBuilder builder = dao.queryBuilder();
                if (limit != -1l) {
                    builder.limit(limit);
                }

                if (offset != -1l) {
                    builder.offset(offset);
                }

                builder.where().eq(RssItems.DELETED_FIELD_NAME, false);
                return builder.query();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }*/

    public List<MediaFilters> getLastItemsByRss(Rss rss, long limit, long offset) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            if (limit != -1l) {
                builder.limit(limit);
            }

            if (offset != -1l) {
                builder.offset(offset);
            }

            builder.where().eq(RssItems.ID_RSS_FIELD_NAME, rss.getId());
            builder.orderBy(MediaFilters.ACTIVE_FIELD_NAME, false);
            builder.orderBy(MediaFilters.FMOD_FIELD_NAME, false);


            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<MediaFilters> getAllActiveByRss(Rss rss) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where()
                    .eq(MediaFilters.RSS_ID_FIELD_NAME, rss.getId())
                    .and()
                    .eq(MediaFilters.ACTIVE_FIELD_NAME, true);
            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<MediaFilters> getAllByFilter(Rss rss, MediaFilters filter) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.limit(50l);

            Where where = builder.where();
            where.eq(MediaFilters.RSS_ID_FIELD_NAME, rss.getId());

            if(filter.getActive()){
                where.and().eq(MediaFilters.ACTIVE_FIELD_NAME, true);
            }


            if(filter.getTitle() != null && filter.getTitle().isEmpty() == false){
                where.and().like(MediaFilters.TITLE_FIELD_NAME, "%" + filter.getTitle().trim() +"%");
            }


            builder.orderBy(MediaFilters.ACTIVE_FIELD_NAME, false);
            builder.orderBy(MediaFilters.FMOD_FIELD_NAME, false);

            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
