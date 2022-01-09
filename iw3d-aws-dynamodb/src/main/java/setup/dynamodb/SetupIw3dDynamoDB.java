package setup.dynamodb;

import com.blogspot.fravalle.aws.dynamodb.Iw3dDynamoDB;
import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SetupIw3dDynamoDB {

    Iw3dDynamoDB dynamodb;



    public SetupIw3dDynamoDB() {
        this.dynamodb = Iw3dDynamoDB.getInstance();
    }

    public void createSchemaIw3d() {
        try {

            //add ipv4 nested
            System.out.println( "table to create");

            DynamoDbWaiter dbWaiter = dynamodb.getClient().waiter();

            CreateTableRequest request = CreateTableRequest.builder()
                    .attributeDefinitions(AttributeDefinition.builder()
                                    .attributeName("iwsessionid")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("iwid")
                                    .attributeType(ScalarAttributeType.N)
                                    .build()/*,
                            AttributeDefinition.builder()
                                    .attributeName("iwcategoryid")
                                    .attributeType(ScalarAttributeType.N)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("iwhttpcode")
                                    .attributeType(ScalarAttributeType.N)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("iwdomainname")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()*/)
                    .keySchema(KeySchemaElement.builder()
                                    .attributeName("iwsessionid")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("iwid")
                                    .keyType(KeyType.RANGE)
                                    .build())
                    //.localSecondaryIndexes(
                    /*LocalSecondaryIndex.builder()
                                    .indexName("lsi_iwsessionid_iwid")
                                    .keySchema(KeySchemaElement.builder()
                                                    .attributeName("iwsessionid")
                                                    .keyType(KeyType.HASH)
                                                    .build(),
                                            KeySchemaElement.builder()
                                                    .attributeName("iwid")
                                                    .keyType(KeyType.RANGE)
                                                    .build())
                                    .projection(Projection.builder().projectionType(ProjectionType.KEYS_ONLY).build())
                                    .build(),
                            LocalSecondaryIndex.builder()
                                    .indexName("lsi_iwsessionid_iwdomainname")
                                    .keySchema(KeySchemaElement.builder()
                                                    .attributeName("iwsessionid")
                                                    .keyType(KeyType.HASH)
                                                    .build(),
                                            KeySchemaElement.builder()
                                                    .attributeName("iwdomainname")
                                                    .keyType(KeyType.RANGE)
                                                    .build())
                                    .projection(Projection.builder().projectionType(ProjectionType.KEYS_ONLY).build())
                                    .build(),
                            LocalSecondaryIndex.builder()
                                    .indexName("lsi_iwsessionid_iwhttpcode")
                                    .keySchema(KeySchemaElement.builder()
                                            .attributeName("iwsessionid")
                                            .keyType(KeyType.HASH)
                                            .build(),
                                    KeySchemaElement.builder()
                                            .attributeName("iwhttpcode")
                                            .keyType(KeyType.RANGE)
                                            .build())
                            .projection(Projection.builder().projectionType(ProjectionType.KEYS_ONLY).build())
                            .build()*///)
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(Long.valueOf(10))
                            .writeCapacityUnits(Long.valueOf(10))
                            .build())
                    .tableName(Iw3dInternetNode.TABLE_NAME)
                    .build();

            CreateTableResponse response = dynamodb.getClient().createTable(request);

            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(Iw3dInternetNode.TABLE_NAME)
                    .build();

            // Wait until the Amazon DynamoDB table is created
            WaiterResponse<DescribeTableResponse> waiterResponse =  dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);

            System.out.println( "DESC: " + response.tableDescription().tableName() );

            PutItemRequest startingItem = this.createStartingItem();
            dynamodb.getClient().putItem(startingItem);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private void cleanTables() {
        try {
            System.out.println( "tables to be deleted: " + dynamodb.getClient().listTables() );

            DeleteTableRequest deleteRequest = DeleteTableRequest.builder().tableName(Iw3dInternetNode.TABLE_NAME).build();
            dynamodb.getClient().deleteTable(deleteRequest);

            System.out.println( "tables after delete operation: " + dynamodb.getClient().listTables() );

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private PutItemRequest createStartingItem() {
        PutItemRequest putItemRequest = null;
        try {
            //Symbolics.com
            Map<String, AttributeValue> item = new HashMap<>();


            item.put("iwsessionid", AttributeValue.builder().s("client-0").build());
            item.put("iwid", AttributeValue.builder().n("0").build());
            item.put("iwhttpcode", AttributeValue.builder().n("200").build());
            item.put("iwcategoryid", AttributeValue.builder().n("0").build());
            item.put("iwdomainname", AttributeValue.builder().s("symbolics.com").build());
            item.put("iwurl", AttributeValue.builder().s("symbolics.com").build());
            item.put("iwhookurl", AttributeValue.builder().s("symbolics.com").build());
            item.put("iwtitle", AttributeValue.builder().s("Symbolics.com").build());

            putItemRequest = PutItemRequest.builder()
                    .tableName(Iw3dInternetNode.TABLE_NAME)
                    .item(item)
                    .expected(Collections.singletonMap("iwid",
                            ExpectedAttributeValue.builder().exists(false).build()))
                    .build();

            System.out.println("PUT DYNAMODB: " + putItemRequest);

            return putItemRequest;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return putItemRequest;
        }

    }


    public static void main(String[] args) {
        SetupIw3dDynamoDB app = new SetupIw3dDynamoDB();
        app.cleanTables();
        app.createSchemaIw3d();

/*
        Iw3dInternetNode searchItem = new Iw3dInternetNode();
        searchItem.setIwid(0L);
        searchItem.setIwcategoryid(0L);

        Iw3dInternetNode item = Iw3dInternetNode.load(searchItem);

        System.out.println("Iw3dInternetNode: url=" + item.getIwurl());

        item.setIwurl(item.getIwurl()+"/");
        item.save();

        System.out.println("Iw3dInternetNode: url=" + item.getIwurl());
*/
    }

}
