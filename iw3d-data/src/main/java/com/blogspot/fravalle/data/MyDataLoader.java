package com.blogspot.fravalle.data;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.orm.derby.cayenne.iw3d.Iwebipv4;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SelectQuery;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class MyDataLoader {

    private static MyDataLoader instance;

    public static ServerRuntime cayenneRuntime;
    public static ObjectContext context;

    static {
        cayenneRuntime = MyDataConnector.cayenneRuntime;
        context = MyDataConnector.context;
    }

    private JComponent progressLoaderPane;

    public static void main(String[] args) {
        //System.err.println( MyDataLoader.getInstance().getStars().size() );
    }

    private MyDataLoader(JComponent newContentPane) {
        this.progressLoaderPane = newContentPane;
    }

    public static MyDataLoader getInstance() {
        if (instance==null) {
            instance = new MyDataLoader(null);
        }
        return instance;
    }

    public static MyDataLoader getInstance(JComponent newContentPane) {
        if (instance==null) {
            instance = new MyDataLoader(newContentPane);
        }
        return instance;
    }

    public List<Iwebipv4> getDomains(boolean isForAllSessions) {
        SelectQuery select1 = new SelectQuery(Iwebipv4.class);
        if (!isForAllSessions) {
            select1.andQualifier(Iwebipv4.IWWEBSHOTID.eq(DataConfiguration.SESSION_ID));
        }
        List<Iwebipv4> items = context.performQuery(select1);
        return items;
    }

    public List<Iwebipv4> getDomainsFromUrl(String sUrl) {
        List<Iwebipv4> items = ObjectSelect.query(Iwebipv4.class).where(Iwebipv4.IWWEBSHOTID.eq(DataConfiguration.SESSION_ID)).select(context);
        System.out.printf("GETTING DATA [Total: %1$s] WITH SESSION ID#%2$s\n", items.size(), DataConfiguration.SESSION_ID);
        return items;
    }
/*
    public List getStarsByDistance(BigDecimal starValue, boolean isGreatThanEqual) {
        List<Hygimport> items = isGreatThanEqual
                ? ObjectSelect.query(Hygimport.class).where(Hygimport.DIST.gte(starValue)).select(context)
                : ObjectSelect.query(Hygimport.class).where(Hygimport.DIST.lte(starValue)).select(context);
        return items;
    }*/

}
