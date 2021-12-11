package com.blogspot.fravalle.data;

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

    public List<Iwebipv4> getDomains() {
        SelectQuery select1 = new SelectQuery(Iwebipv4.class);
        List<Iwebipv4> items = context.performQuery(select1);
        for (Iwebipv4 hygimport : items) {
            System.err.println("DEBUG HYG: " + hygimport);
        }
        return items;
    }

}
