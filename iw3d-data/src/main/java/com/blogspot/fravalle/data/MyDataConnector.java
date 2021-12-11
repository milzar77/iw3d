package com.blogspot.fravalle.data;

import com.blogspot.fravalle.core.DataConfiguration;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;

import java.util.ResourceBundle;

public class MyDataConnector {

    private static final ResourceBundle rbConfig;

    public static ServerRuntime cayenneRuntime;
    public static ObjectContext context;

    static {

        rbConfig = ResourceBundle.getBundle("db-setup");

        cayenneRuntime = ServerRuntime.builder().addConfig(DataConfiguration.getCayennProjectPath())
                .dataSource(DataSourceBuilder
                        .url("jdbc:derby:"+ DataConfiguration.getBaseDerbyDirectory() + DataConfiguration.getDdlSchemaName() +";create=true")
                        .driver(org.apache.derby.jdbc.EmbeddedDriver.class.getName())
                        .build())
                .build();
        context = cayenneRuntime.newContext();
        System.out.println("KEY_CAYENNE_PROJECT" + ":" + DataConfiguration.getCayennProjectPath());
        System.out.println("TEST CAYENNE CONTEXT: " + context.toString());

    }

}
