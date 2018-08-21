/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.services;

import com.grupoad3.flexfx.db.AbstractDaoService;
import com.grupoad3.flexfx.db.model.Rss;
import com.grupoad3.flexfx.db.model.RssItems;
import com.j256.ormlite.stmt.QueryBuilder;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author daniel_serna
 */
@SuppressWarnings("unchecked")
public class RssItemService extends AbstractDaoService<RssItems>{

    public RssItemService() {
        super(RssItems.class);
    }

    public List<RssItems> getLastItems(long limit, long offset, boolean withDeleted) throws IOException{

        // return all with deleted
        if (withDeleted) {
            return super.getAll(limit, offset);
        }else{

            try {
                QueryBuilder builder = dao.queryBuilder();
                if(limit != -1l)
                {
                    builder.limit(limit);
                }

                if(offset != -1l){
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
    }

    public List<RssItems> getLastItemsByRss(Rss rss, long limit, long offset ){
        try {
            QueryBuilder builder = dao.queryBuilder();
                if(limit != -1l)
                {
                    builder.limit(limit);
                }

                if(offset != -1l){
                    builder.offset(offset);
                }

                builder.where().eq(RssItems.DELETED_FIELD_NAME, false);
                builder.where().eq(RssItems.ID_RSS_FIELD_NAME, rss.getId());
                builder.orderBy(RssItems.DATE_PUB_FIELD_NAME, false);

                return builder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
