package finalproject;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegistrationScene extends BaseScene {

	public Scene create(Stage primaryStage)
	{
        //Labels
        Text regEmailLbl = new Text("Email");
        Text regPassLbl = new Text("Password");
        Text regPhoneLbl = new Text("Phone");
        
        //Text Fields
        TextField regEmail = new TextField();
        TextField regPhone = new TextField();
        PasswordField regPassword = new PasswordField();
        
        //Buttons
        Button regSubmit = new Button("Submit Registration");
        Button regClear = new Button("Clear");
        Button regGoBack = new Button("Back to Login");
        
        //Grid Pane
        GridPane registerGridPane = new GridPane();
        
        //Grid Pane Settings
        registerGridPane.setMinSize(640, 480);
        registerGridPane.setPadding(new Insets(10,10,10,10));
        registerGridPane.setVgap(5);
        registerGridPane.setHgap(5);
        registerGridPane.setAlignment(Pos.CENTER);
        
        //Insert Objects into Pane
        registerGridPane.add(regEmailLbl,0,0);
        registerGridPane.add(regEmail,1,0);
        registerGridPane.add(regPassLbl,0,1);
        registerGridPane.add(regPassword,1,1);
        registerGridPane.add(regPhoneLbl,0,2);
        registerGridPane.add(regPhone,1,2);
        registerGridPane.add(regSubmit,0,4);
        registerGridPane.add(regClear,1,4);
        registerGridPane.add(regGoBack,2,6);
        
        //Create Scene
        Scene registerScene = new Scene(registerGridPane);
        
        //Functions
        //Clear Text
        regClear.setOnAction(value ->{
        	//Call Clear Function
        	regEmail.clear();
        	regPassword.clear();
        	regPhone.clear();
        });
        
        //Go Back to the login Scene
        regGoBack.setOnAction(value ->{
        	
        	//Create Login Scene Object
        	LoginScene loginObj = new LoginScene();
        	Scene loginScene = loginObj.create(primaryStage);
        	
        	//Change scene to login
        	primaryStage.setScene(loginScene);
        	primaryStage.setTitle("Login");
        });
        
        //Submit Registration
        regSubmit.setOnAction(value ->{
        	
        	AmazonDynamoDB db = getDBClient();
        	
        	try {
	        	//Get value in text fields
	        	String email = regEmail.getText();
	        	String password = regPassword.getText();
	        	String phoneNumb = regPhone.getText();
	        	
	        	//Add country code and + at beginning of phone
	        	String phone = "+1" + phoneNumb;
	        	
	        	//Crate user HashMap with email and password
	        	Map<String, AttributeValue> user = newUser(email,password,phone);
	        	
	        	//Put Item Request
	        	PutItemRequest pir = new PutItemRequest("user-list",user);
	        	
	        	//Put Item Result
	        	PutItemResult putItemResult = db.putItem(pir);
	        	
	        	//Send out Thanks email
	        	SendEmail(email);
	        	
	        	//Send out Thanks Text
	        	SendSMS(phone);
	        	
	        	System.out.print("User added to user-list");        	
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error: " + e.getMessage());
        	}
        	
        });
        
        return registerScene;
	}
}
