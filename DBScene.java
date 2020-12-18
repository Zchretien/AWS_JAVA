package finalproject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DBScene extends BaseScene{

	public Scene create(Stage primaryStage)
	{   
        //Buttons
        Button createTableBtn = new Button("Create Table");
        Button deleteTableBtn = new Button("Delete Table");
        Button addItemToTableBtn = new Button("Add Item to Table");
        Button removeItemFromTableBtn = new Button("Remove Item from Table");
        Button scanTableBtn = new Button("Scan DynamoDB Document");
        Button dbGoBackBtn = new Button("Back to Main");
        
        //Text Area
        TextArea dbTextArea = new TextArea();
        VBox dbVBox = new VBox(dbTextArea);
        
        //Grid Pane
        GridPane dbGrid = new GridPane();
        
        //Pane Settings
        dbGrid.setMinSize(640,480);
        dbGrid.setPadding(new Insets(10,10,10,10));
        dbGrid.setVgap(5);
        dbGrid.setHgap(5);
        dbGrid.setAlignment(Pos.CENTER);
        
        dbGrid.add(createTableBtn,0,0);
        dbGrid.add(deleteTableBtn,1,0);
        
        dbGrid.add(addItemToTableBtn,2,1);
        dbGrid.add(removeItemFromTableBtn,1,1);
        
        dbGrid.add(scanTableBtn,2,2);
        dbGrid.add(dbGoBackBtn,1,2);
        
        dbGrid.add(dbVBox,0,3);
        
        //Create Scene
        Scene dbScene = new Scene(dbGrid);
        
        dbGoBackBtn.setOnAction(value ->{
        	MainScene mainObj = new MainScene();
        	Scene mainScene = mainObj.create(primaryStage);
        	
        	//Back to main stage
        	primaryStage.setScene(mainScene);
        	primaryStage.setTitle("Home");
        });
        
        createTableBtn.setOnAction(value ->{
        	//Get DB Client
        	AmazonDynamoDB dbClient = getDBClient();
        	
        	//Get name for the table
        	String adj = getAdjective();
        	String noun = getNoun();
        	String tableName = adj + "-" + noun +"-aws-final";
        	
        	//Hash Key attribute
        	String attributeName = "primarykey";
        	
        	//Create a table request
        	CreateTableRequest ctr = createTableRequest(tableName,attributeName);
        	
        	//Create the table
        	try
        	{
        		//Create the table
        		TableUtils.createTableIfNotExists(dbClient,ctr);
        		
        		//Wait until table is active
        		TableUtils.waitUntilActive(dbClient,tableName);
        		
        		//Make a Describe Table Request
        		DescribeTableRequest dtr = new DescribeTableRequest().withTableName(tableName);
        		
        		//Create the table description
        		TableDescription td = dbClient.describeTable(dtr).getTable();
        		
        		//Alert User table was made
        		String tableDescrip ="Table Description: \n" + td.toString();
        		dbTextArea.setText(tableDescrip);
        		
        	}catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        	}  	
        });
        
        deleteTableBtn.setOnAction(valuye ->{
        	//Get Client
        	AmazonDynamoDB client = getDBClient();
        	DynamoDB dynamoDB = new DynamoDB(client);
        	
        	//Table List and Iterator
        	TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        	Iterator<Table> iterator = tables.iterator();
        	
        	while (iterator.hasNext()) {
        		
        	    Table table = iterator.next();
        	    String name = table.getTableName();
        	    
        	    if(name.endsWith("aws-final"))
        	    {	
        	    	try {
        	    		
						table.delete();
        	    		table.waitForDelete();
            	    	dbTextArea.setText("Table: " + name + " deleted.");
        	    		
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
        	    }
        	}
            
        });
        
        addItemToTableBtn.setOnAction(value ->{
        	AmazonDynamoDB db = getDBClient();
        	
        	//Table name
        	String tableName = "aws-final-table";
        	
        	Random rand = new Random();
        	
        	//Create item attributes
        	Map<String, AttributeValue> item = newItem(Integer.toString(rand.nextInt(250)),"pineapple",rand.nextInt(500));
        	
        	try {
        		
        		db.putItem(createPutItemRequest(tableName,item));
        		
        		dbTextArea.setText("Item was successfully place in table aws-final-table");
        		
        	}catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        	}
        	
        });
        
    	removeItemFromTableBtn.setOnAction(value ->{
    		
    		//Get Client
    		AmazonDynamoDB client = getDBClient();
    		
    		//Table Name
    		String tableName = "aws-final-table";
    		
    		//Scan request
    		ScanRequest request = new ScanRequest().withTableName(tableName);
    		
    		//Scan Result
    		ScanResult result = client.scan(request);
    		
    		//Random
    		Random rand = new Random();
    		
    		int randomElement = rand.nextInt(result.getItems().size());
    		
    		//Get A random element from the table
    		Map<String, AttributeValue> randomItem = result.getItems().get(randomElement);
    		
    		//Get just the Hash Key from the random element
    		Map<String, AttributeValue> keyToGet = new HashMap<String, AttributeValue>();
    		keyToGet.put("ItemID", randomItem.get("ItemID"));
    		
    		try {
    			
    			//Delete item
    			client.deleteItem(tableName, keyToGet);
    			dbTextArea.setText(keyToGet.toString() + " deleted from Table: aws-final-table");
    			
    		}catch(Exception e)
    		{
    			dbTextArea.setText("Error: Unable to delete item from table");
    			e.getMessage();
    		}
    	});
    	
    	scanTableBtn.setOnAction(value ->{
    		
    		//Get Client
    		AmazonDynamoDB client = getDBClient();
        	DynamoDB dynamoDB = new DynamoDB(client);
        	
        	//Table List and Iterator
        	TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        	Iterator<Table> iterator = tables.iterator();
    		
    		//Table Name
    		String tableName = "aws-final-table";
    		
    		//Scan request
    		ScanRequest request = new ScanRequest().withTableName(tableName);
    		
    		//Scan Result
    		ScanResult result = client.scan(request);
    		
    		String results = "Tables in DynamoDB: \n";
    		
        	while (iterator.hasNext()) {
        		
        	    Table table = iterator.next();
        	    String name = table.getTableName();
        	    
        	    results += name + "\n";
        	    
        	}
        	
        	results += "\nItems in Table: aws-final-table \n";
        	
    		for(Map<String, AttributeValue> item: result.getItems())
    		{
    			results += item.toString() + "\n";
    		}		
    		
    		dbTextArea.setText(results);
    	});
        
        return dbScene;
	}
}
