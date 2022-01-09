package com.blogspot.fravalle.aws.dynamodb;

import com.blogspot.fravalle.core.DataConfiguration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

public class Iw3dDynamoDB {

    private static Iw3dDynamoDB instance;

    private DynamoDbClient client;

    private Iw3dDynamoDB() {
        if (!DataConfiguration.isDynamoDbOnCloud()) {
            System.err.printf("DynamoDB: using local server connection from [%1$s]", DataConfiguration.getLocalDynamoDbAddress());
            this.client = DynamoDbClient.builder()
                    //.endpointOverride(URI.create("http://localhost:8000"))
                    .endpointOverride(URI.create( DataConfiguration.getLocalDynamoDbAddress() ))
                    .build();
        } else {
            System.err.printf("DynamoDB: using cloud server connection from [%1$s] with IAM credential %2$s", "eu-south-1", "fv_reader");
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
