/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grupoad3.flexfx.db.services;

import com.grupoad3.flexfx.db.AbstractDaoService;
import com.grupoad3.flexfx.db.model.Rss;
import com.j256.ormlite.stmt.QueryBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author daniel_serna
 */
@SuppressWarnings("unchecked")
public class RssService extends AbstractDaoService<Rss>{
    
    public RssService() {
        super(Rss.class);        
    }
    
    public List<Rss> getLastRssNotDeleted() throws IOException{
        return getLastRss(40, 0, false);
    }
    
    public List<Rss> getLastRss(long limit, long offset, boolean withDeleted) throws IOException{
        
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
                
                builder.where().eq(Rss.DELETED_FIELD_NAME, withDeleted);
                return builder.query();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    public boolean eraserSoft(Rss rss) throws IOException{
        
        try {
            rss.setDeleted(true);
            return dao.update(rss) > 0;
        } catch (SQLException e) {
            e.printStackTrace();       
            throw new IOException(e);
        }                
    }
    
}
