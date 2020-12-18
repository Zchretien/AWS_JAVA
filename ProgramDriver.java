package finalproject;

//Imports for AWS
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

//Imports for JavaFx
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProgramDriver extends Application {

	public AmazonDynamoDB getDBClient()
	{
		//Create the DB Client with the credentials and region
		AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		
		return db;
	}
	
	public CreateTableRequest createTableRequest(String tableName,String attributeName)
	{
		CreateTableRequest ctr = new CreateTableRequest().withTableName(tableName)
				.withKeySchema(new KeySchemaElement().withAttributeName(attributeName).withKeyType(KeyType.HASH))
				.withAttributeDefinitions(new AttributeDefinition().withAttributeName(attributeName).withAttributeType(ScalarAttributeType.S))
				.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
		
		return ctr;
	}

	public void start(Stage primaryStage) throws Exception
	{
		//The AWS Credentials and the Table Check need to be completed before the GUI is built
        //Once the program starts call the init function to set up the AWS credentials and DB Client builder
		init();
		
		AmazonDynamoDB db = getDBClient();
		//Check for user table, if it does not exist it will be created
        try {
        	
        	//Table vars
        	String tableName = "user-list";
        	String attributeName = "email";
        	
        	//Create Table Request
        	CreateTableRequest ctr = createTableRequest(tableName,attributeName);
        	
        	//Create the table if it does not exist
        	TableUtils.createTableIfNotExists(db,ctr);
        	
        	//Wait for the table to be created and put in the active state
        	//DynamoDB object and the name of the table we are waiting for
        	TableUtils.waitUntilActive(db, "user-list");
        	
        	//Create table description request object while supplying the table name
        	DescribeTableRequest dtr = new DescribeTableRequest().withTableName("user-list");
        	
        	//Create Table Description
        	TableDescription tableDescription = db.describeTable(dtr).getTable();
        	
        	System.out.println("User Table Exists");
        	
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
		
		//Login Scene
		LoginScene loginObj = new LoginScene();
		Scene loginScene = loginObj.create(primaryStage);
		
		//Initial Startup
        primaryStage.setScene(loginScene);
        primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
