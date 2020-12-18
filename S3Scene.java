package finalproject;

import java.io.File;
import java.util.List;
import java.util.Random;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class S3Scene extends BaseScene{

	public Scene create(Stage primaryStage)
	{   
        //Buttons
        Button listBucketsBtn = new Button("List All Buckets");
        Button listObjectsInBucketBtn = new Button("List Items In Bucket");
        Button createBucketBtn = new Button("Create Bucket");
        Button addObjectToBucketBtn = new Button("Add Object To Bucket");
        Button removeObjectFromBucketBtn = new Button("Remove Object From Bucket");
        Button deleteBucketBtn = new Button("Delete Bucket");
        Button s3GoBackBtn = new Button("Back to Main");
        
        //TextArea
        TextArea s3TextArea = new TextArea();
        VBox s3VBox = new VBox(s3TextArea);
        
        //Grid Pane
        GridPane s3Grid = new GridPane();
        
        s3Grid.add(listBucketsBtn,0,0);
        s3Grid.add(listObjectsInBucketBtn,1,0);
        
        s3Grid.add(addObjectToBucketBtn,2,1);
        s3Grid.add(removeObjectFromBucketBtn,1,1);
        
        s3Grid.add(createBucketBtn,2,2);
        s3Grid.add(deleteBucketBtn,1,2);
        
        s3Grid.add(s3GoBackBtn,2,0);
        s3Grid.add(s3VBox,0,3);
        
        //Create Scene
        Scene s3Scene = new Scene(s3Grid);
        
        //S3 Scene
        s3GoBackBtn.setOnAction(value ->{
        	MainScene mainObj = new MainScene();
        	Scene mainScene = mainObj.create(primaryStage);
        	
        	//Set stage as main
        	primaryStage.setScene(mainScene);
        	primaryStage.setTitle("Home");
        });
        
        listBucketsBtn.setOnAction(value ->{
        	
        	//Clear Text Area of Previous text
        	s3TextArea.clear();
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = getS3Client();
        	
        	//Create a list to hold the buckets
        	List<Bucket> s3Buckets = s3Client.listBuckets();
        	
        	//String var to hold full text
        	String results = "These are the S3 Buckets I created: \n";
        	
        	//Loop through results
        	for(Bucket b : s3Buckets)
        	{
        		results += b.getName() + "\n";
        	}
        	
        	//Post results to text area
        	s3TextArea.setText(results);
        	
        });
        
        listObjectsInBucketBtn.setOnAction(value ->{
        	//Clear text area
        	s3TextArea.clear();
        	
        	//Bucket name
        	String bucketName = "final-project-bucket-aws";
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = getS3Client();
        	
        	//ListObjectsResult Object 
        	ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        	
        	//List of objects in Bucket
        	List<S3ObjectSummary> objects = result.getObjectSummaries();
        	
        	//String var to hold items in bucket
        	String bucketItems = "These are the items in the final-project-bucket-aws S3 Bucket: \n";
        	
        	//Loop through list
        	for(S3ObjectSummary os: objects)
        	{
        		String objectName = os.getKey();
        		
        		bucketItems += objectName + "\n";
        	}
        	
        	//Add bucketItems to TextArea showing what was in the bucket
        	s3TextArea.setText(bucketItems);
        	
        });
        
        addObjectToBucketBtn.setOnAction(value ->{
        	//Clear Text Area
        	s3TextArea.clear();
        	
        	//Bucket Name
        	String bucketName = "final-project-bucket-aws";
        	
        	//File Chooser
        	FileChooser s3FileChooser = new FileChooser();
        	s3FileChooser.setTitle("Choose File To Upload to S3");
        	File file = s3FileChooser.showOpenDialog(primaryStage);
        	
        	//Generate two random letters to append to the end of the Key name to keep name unique
        	String startString = "S3_TEST_";
        	String endString = ".txt";
        	
        	Random rand = new Random();
        	
        	//Get a random number between 97 and 122.
        	//These numbers correspond to lower case letters on the acsii table
        	int randNumb1 = rand.nextInt(122 - 97 ) + 97;
        	int randNumb2 = rand.nextInt(122 - 97) + 97;
        	
        	String randomNumbString = Character.toString((char) randNumb1);
        	randomNumbString += Character.toString((char) randNumb2);
        	
        	//Key Name
        	String keyName = startString + randomNumbString + endString;
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = getS3Client();
        	
        	try {
        		//Put item in s3 Bucket
        		s3Client.putObject(bucketName,keyName,file);
        		
        		//Alert user by adding text to textArea
        		s3TextArea.setText("Added object to S3 Bucket with Key Name: " + keyName);
        		
        	} catch(Exception e){
        		System.out.println(e.getMessage());
        	}
        });
        
        removeObjectFromBucketBtn.setOnAction(value ->{
        	//Clear Text
        	s3TextArea.clear();
        	
        	//Bucket Name
        	String bucketName = "final-project-bucket-aws";
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        	
        	//Get object in bucket and selected one randomly to be deleted
        	//ListObjectsResult Object 
        	ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        	
        	//List of objects in Bucket
        	List<S3ObjectSummary> objects = result.getObjectSummaries();
        	
        	//Create random object
        	Random rand = new Random();
        	
        	//Select random object
        	S3ObjectSummary randomElement = objects.get(rand.nextInt(objects.size()));
        	
        	//Get object key name
        	String keyName = randomElement.getKey();
        	
        	//Delete object
        	try {
        		
        		//Call delete object method
        		s3Client.deleteObject(bucketName, keyName);
        		
        		//Alert user what item was deleted from which bucket
        		String deleteResult = "Object: " + keyName + " was deleted from Bucket: " + bucketName;
        		
        		s3TextArea.setText(deleteResult);
        		
        		
        	} catch(Exception e) {
        		System.out.println(e.getMessage());
        	}
        });
        
        createBucketBtn.setOnAction(value ->{
        	//Clear Text
        	s3TextArea.clear();
        	
        	String adj = getAdjective();
        	String noun = getNoun();
        	
        	String bucketName = adj + "-" + noun + "-aws-final";
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = getS3Client();
        	
        	Bucket newBucket = null;
        	
        	try {
        		
        		newBucket = s3Client.createBucket(bucketName);
        		
        		if(newBucket == null)
        		{
        			s3TextArea.setText("Error: Unable to create bucket!");
        		}else
        		{
        			s3TextArea.setText("Bucket: " + bucketName + " created!");
        		}
        		
        	}catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        	}
        });
        
        deleteBucketBtn.setOnAction(value ->{
        	//Clear Text Area
        	s3TextArea.clear();
        	
        	//Create client to access S3 services
        	AmazonS3 s3Client = getS3Client();
        	
        	//Create and populate a list to hold the buckets
        	List<Bucket> s3Buckets = s3Client.listBuckets();
        	
        	//Bucket is corrupted cannot delete at the moment just skip over it
        	String skipBucket = "lucky-protection-aws-final";
        	
        	for(Bucket b : s3Buckets)
        	{
        		String name = b.getName();
        		System.out.println(name);
        		
        		if((name.endsWith("aws-final")) && !(name.equals(skipBucket)))
        		{
                	//Delete the bucket
                	s3Client.deleteBucket(name);
                	
                	//Alert user
                	s3TextArea.setText("Bucket: " + name + " was deleted");
                	
        		}else {
        			
        			s3TextArea.setText("There are no more buckets you can delete");
        		}
        	}        	
        });
        
        return s3Scene;
	}
}
