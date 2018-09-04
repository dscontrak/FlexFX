/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.services;

import com.grupoad3.flexfx.db.AbstractDaoService;
import com.grupoad3.flexfx.db.model.ItemStatus;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author daniel_serna
 */
@SuppressWarnings("unchecked")
public class RssItemService extends AbstractDaoService<RssItems> {

    public RssItemService() {
        super(RssItems.class);
    }

    /*public List<RssItems> getLastItems(long limit, long offset, boolean withDeleted) throws IOException {

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
                builder.orderBy(RssItems.DATE_PUB_FIELD_NAME, false);
                return builder.query();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }*/

    // getLastRssNotDeleted
    /*public List<RssItems> getLastItemsDownloadedByRss(Rss rss) {
        try {
            QueryBuilder builder = dao.queryBuilder();

            builder.limit(50l);
            builder.offset(0l);

            builder.where()
                    .eq(RssItems.STATUS_FIELD_NAME, ItemStatus.DOWNLOADED)
                    .and()
                    .eq(RssItems.DELETED_FIELD_NAME, false)
                    .and()
                    .eq(RssItems.ID_RSS_FIELD_NAME, rss.getId());

            builder.orderBy(RssItems.DATE_PUB_FIELD_NAME, false);

            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }*/

    public List<RssItems> getAllItemsByFilter(Rss rss, RssItems itemFilter) {
        try {
            QueryBuilder builder = dao.queryBuilder();

            builder.limit(50l);
            builder.offset(0l);

            Where where = builder.where();
            where.eq(RssItems.DELETED_FIELD_NAME, false)
                    .and()
                    .eq(RssItems.ID_RSS_FIELD_NAME, rss.getId());

            if (itemFilter.getTitle() != null && itemFilter.getTitle().isEmpty() == false) {
                where.and().like(RssItems.TITLE_FIELD_NAME, "%" + itemFilter.getTitle().trim() + "%");
            }

            if (itemFilter.getStatus() != null && itemFilter.getStatus().equals(ItemStatus.DOWNLOADED.toString())) {
                where.and().eq(RssItems.STATUS_FIELD_NAME, ItemStatus.DOWNLOADED);
            }

            builder.orderBy(RssItems.DATE_PUB_FIELD_NAME, false);
                    

            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<RssItems> getLastAlltemsByRss(Rss rss, long limit, long offset, boolean withDeleted) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            if (limit != -1l) {
                builder.limit(limit);
            }

            if (offset != -1l) {
                builder.offset(offset);
            }

            builder.where()
                    .eq(RssItems.DELETED_FIELD_NAME, withDeleted)
                    .and()
                    .eq(RssItems.ID_RSS_FIELD_NAME, rss.getId());

            builder.orderBy(RssItems.DATE_PUB_FIELD_NAME, false);                    

            return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
