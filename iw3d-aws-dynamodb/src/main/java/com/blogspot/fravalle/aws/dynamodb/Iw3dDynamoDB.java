package com.blogspot.fravalle.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.blogspot.fravalle.core.DataConfiguration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.io.File;
import java.net.URI;

public class Iw3dDynamoDB {

    private static Iw3dDynamoDB instance;

    public static DynamoDBProxyServer server;

    private DynamoDbClient client;



    private Iw3dDynamoDB() {

        if (!DataConfiguration.isDynamoDbOnCloud()) {

            if (DataConfiguration.useDynamoDbInternal()) {

                try {
                    if (server == null) {
                        //System.setProperty("sqlite4java.library.path", "C:\\Users\\piuma\\IdeaProjects\\iw3d\\native-libs");
                        System.setProperty("sqlite4java.library.path", "./native-libs");
                        File f = new File(".");
                        System.out.println("Base Local DynamoDB shared directory: " + f.getAbsolutePath());
                        //System.setProperty("java.library.path", ".\\native-libs");
                        final String[] localArgs = {"-sharedDb"};// c:/tmp/awstmp/dynamodblocal/
                        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
                        server.start();
                    }

                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                            System.out.println("Shutdown DynamoDBLocal");
                            if (Iw3dDynamoDB.server != null) {
                                try {
                                    Iw3dDynamoDB.server.stop();
                                } catch (Exception ex) {
                                    System.err.println(ex.getMessage());
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    System.err.printf("DynamoDB: using INTERNAL server connection from localhost");
                    this.client = DynamoDbClient.builder()
                            .endpointOverride(URI.create("http://localhost:8000"))
                            .build();
                }
            } else {
                System.err.printf("DynamoDB: using LOCAL server connection from [%1$s]", DataConfiguration.getLocalDynamoDbAddress());
                this.client = DynamoDbClient.builder()
                        .endpointOverride(URI.create( DataConfiguration.getLocalDynamoDbAddress() ))
                        .build();
            }

        } else {

            System.err.printf("DynamoDB: using CLOUD server connection from [%1$s] with IAM credential %2$s", "eu-south-1", "fv_reader");
            this.client = DynamoDbClient.builder()
                    .region(Region.of("eu-south-1"))
                    .credentialsProvider(ProfileCredentialsProvider.create("fv_reader"))
                    .build();
        }

    }

    public DynamoDbClient getClient() {
        return client;
    }

    public static final Iw3dDynamoDB getInstance() {
        if (instance==null) {
            instance = new Iw3dDynamoDB();
        }
        return instance;
    }

    public static final Iw3dDynamoDB getInstance(boolean reset) {
        if (reset) {
            instance.client.close();
            instance = new Iw3dDynamoDB();
        } else {
            return Iw3dDynamoDB.getInstance();
        }
        return instance;
    }

}
