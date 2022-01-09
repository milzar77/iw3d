package com.blogspot.fravalle.aws.dynamodb.beans;

import com.blogspot.fravalle.aws.dynamodb.Iw3dDynamoDB;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DynamoDbBean
public class Iw3dInternetNode {

    public final static String TABLE_NAME = "iw3d_iweb";

    private String sessionId;
    private Long iwid;
    private Long iwcategoryid;

    private Short iwhttpcode;
    private String iwdomainname;
    private String iwurl;

    private String iwtitle;
    private String iwcategoryname;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "iwsessionid")
    public String getSessionId() { return sessionId; };

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "iwcategoryid")
    public Long getIwcategoryid() {
        return iwcategoryid;
    }

    public void setIwcategoryid(Long iwcategoryid) {
        this.iwcategoryid = iwcategoryid;
    }

    @DynamoDbAttribute(value = "iwid")
    public Long getIwid() { return iwid; };

    public void setIwid(Long iwid) {
        this.iwid = iwid;
    }

    @DynamoDbAttribute(value = "iwhttpcode")
    public Short getIwhttpcode() {
        return iwhttpcode;
    }

    public void setIwhttpcode(Short iwhttpcode) {
        this.iwhttpcode = iwhttpcode;
    }

    @DynamoDbAttribute(value = "iwdomainname")
    public String getIwdomainname() {
        return iwdomainname;
    }

    public void setIwdomainname(String iwdomainname) {
        this.iwdomainname = iwdomainname;
    }

    @DynamoDbAttribute(value = "iwurl")
    public String getIwurl() {
        return iwurl;
    }

    public void setIwurl(String iwurl) {
        this.iwurl = iwurl;
    }

    @DynamoDbAttribute(value = "iwtitle")
    public String getIwtitle() {
        return iwtitle;
    }

    public void setIwtitle(String iwtitle) {
        this.iwtitle = iwtitle;
    }

    @DynamoDbAttribute(value = "iwcategoryname")
    public String getIwcategoryname() {
        return iwcategoryname;
    }

    public void setIwcategoryname(String iwcategoryname) {
        this.iwcategoryname = iwcategoryname;
    }

    private static final DynamoDbEnhancedClient DDB_ENHANCED_CLIENT = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(Iw3dDynamoDB.getInstance().getClient())
            .build();
            //DynamoDbEnhancedClient.create();

    private static final DynamoDbTable<Iw3dInternetNode> CUSTOMER_TABLE =
            DDB_ENHANCED_CLIENT.table(TABLE_NAME,
                    TableSchema.fromBean(Iw3dInternetNode.class));

    // Store this customer record in the database
    public void update() {
        CUSTOMER_TABLE.updateItem(this);
    }

    public void put() {
        CUSTOMER_TABLE.putItem(this);
    }

    public PutItemEnhancedResponse<Iw3dInternetNode> putWithResponse() {
        return CUSTOMER_TABLE.putItemWithResponse(PutItemEnhancedRequest.builder(Iw3dInternetNode.class).item(this).build());
    }

    public static List<Iw3dInternetNode> query(Object partitionSessionId) {
        System.err.println("Querying: " + partitionSessionId);
        QueryConditional queryConditional =
                partitionSessionId instanceof Number
                ?
                QueryConditional.sortGreaterThanOrEqualTo(Key.builder().partitionValue((Long)partitionSessionId).sortValue(0L).build())
                :
                QueryConditional.keyEqualTo(Key.builder().partitionValue(String.valueOf(partitionSessionId)).build());
        PageIterable<Iw3dInternetNode> results = CUSTOMER_TABLE.query(QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build());
        //FIX: results.items().stream().forEach(item -> System.out.println(item));

        return results.items().stream().collect(Collectors.toList());
    }

    // Retrieve a single customer record from the database
    public static Iw3dInternetNode load(Iw3dInternetNode customer) {
        return CUSTOMER_TABLE.getItem(customer);
    }

}
