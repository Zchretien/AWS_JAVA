package finalproject;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginScene extends BaseScene{
	
	public Scene create(Stage primaryStage)
	{
		//Labels
		Text emailLbl = new Text("Email: ");
		Text passwordLbl = new Text("Passowrd: ");
		
		//Text Fields
		TextField emailField = new TextField();
		PasswordField passwordField = new PasswordField();
		
		//Buttons
		Button loginBtn = new Button("Login");
		Button clearBtn = new Button("Clear Text");
		Button registerBtn = new Button("Register");
			
        //Create Grid Pane
        GridPane loginGridPane = new GridPane();
        
        //Adjust grid pane settings
        loginGridPane.setMinSize(640, 480);
        loginGridPane.setPadding(new Insets(10,10,10,10));
        loginGridPane.setVgap(5);
        loginGridPane.setHgap(5);
        loginGridPane.setAlignment(Pos.CENTER);
        
        //Insert objects into grid pane
        loginGridPane.add(emailLbl,0,0);
        loginGridPane.add(emailField,1,0);
        loginGridPane.add(passwordLbl,0,1);
        loginGridPane.add(passwordField,1,1);
        loginGridPane.add(loginBtn,0,3);
        loginGridPane.add(clearBtn,1,3);
        loginGridPane.add(registerBtn,2,5);
        
        //Create Scene
        Scene loginScene = new Scene(loginGridPane);
        
        //Scene Functions
        //Clear Text fields
        clearBtn.setOnAction(value -> {
        	//Call clear function
        	emailField.clear();
        	passwordField.clear();
        });
        
        //Switch to register scene
        registerBtn.setOnAction(value ->{
        	RegistrationScene registerObj = new RegistrationScene();
        	Scene registerScene = registerObj.create(primaryStage);
        	
        	//Set new scene
        	primaryStage.setScene(registerScene);
        	primaryStage.setTitle("Registration");
        });
        
        //Check Credentials
        loginBtn.setOnAction(value ->{
        	
        	//Get values from input fields
        	String email = emailField.getText();
        	String password = passwordField.getText();
        	
        	//Try to get user record from table for comparison
        	//Create Client
        	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-1").build();
        	DynamoDB userDB = new DynamoDB(client);
        	
        	//Get user-list Table
        	Table userTable = userDB.getTable("user-list");
        	
        	try {
        		Item userRecord = userTable.getItem("email",email);
        		
        		String recordedEmail = userRecord.getString("email");
        		String recordedPassword = userRecord.getString("password");
        		
        		//Compare Strings
        		if(email.equals(recordedEmail) && password.equals(recordedPassword))
        		{
        			System.out.println("Login Success");
        			
        			MainScene mainObj = new MainScene();
        			Scene mainScene = mainObj.create(primaryStage);
        			
        			//Change Scene
        			primaryStage.setScene(mainScene);
        			primaryStage.setTitle("Welcome!!");
        		}
        		else
        		{
        			System.out.println("Wrong.");
        			System.out.println("User: " + email + " " + recordedEmail);
        			System.out.println("Pass: " + password + " " + recordedPassword);
        		}
        		
        	}catch(Exception e)
        	{
        		System.out.println("Error: " + e.getMessage());
        	}
        	
        });
        
        return loginScene;
	}
}
