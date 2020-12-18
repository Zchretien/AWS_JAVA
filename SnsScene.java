package finalproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SnsScene extends BaseScene{
	
	public Scene create(Stage primaryStage)
	{
        Text phoneLbl = new Text("Phone Number: ");
        Text emailLbl = new Text("Email: ");
        
        //Buttons
        Button sendEmailBtn = new Button("Send Email");
        Button sendTextBtn = new Button ("Send Text");
        Button snsGoBackBtn = new Button("Back To Main");
        
        //Text Fields
        TextField snsEmailTF = new TextField();
        TextField snsTextTF = new TextField();
        
        //Grid Pane
        GridPane snsGrid = new GridPane();
        
        snsGrid.setMinSize(640,480);
        snsGrid.setPadding(new Insets(10,10,10,10));
        snsGrid.setVgap(5);
        snsGrid.setHgap(5);
        snsGrid.setAlignment(Pos.CENTER);
        
        snsGrid.add(emailLbl,0,0);
        snsGrid.add(snsEmailTF,1,0);
        snsGrid.add(sendEmailBtn,2,0);
        
        snsGrid.add(phoneLbl,0,1);
        snsGrid.add(snsTextTF,1,1);
        snsGrid.add(sendTextBtn,2,1);
        
        snsGrid.add(snsGoBackBtn,2,3);
        
        //Create Scene
        Scene snsScene = new Scene(snsGrid);
		
		//Sns Functions
    	snsGoBackBtn.setOnAction(value ->{
    		
    		MainScene mainObj = new MainScene();
    		Scene mainScene = mainObj.create(primaryStage);
    		
    		primaryStage.setScene(mainScene);
    		primaryStage.setTitle("Main");
    	});
    	
    	sendEmailBtn.setOnAction(value ->{
    		//Take in data from text field
    		String sendTo = snsEmailTF.getText();
    		
    		String subject = "AWS: SNS Email Service";
    		String htmlBody = "<p>This email was sent automatically using <h1>AWS Simple Notification Service</h1><p>"
    				+ "<br>" + "<p>The service is easy to set up and automatically email people with updates on a product</p>";
    		String textBody = "This email was sent from AWS SNS";
    		
    		//Send Email
    		SendEmail(subject,htmlBody,textBody,sendTo);
    	});
    	
    	sendTextBtn.setOnAction(value ->{
    		
    		//Get Phone Number
    		String phone = snsTextTF.getText();
    		
    		//Message to send
    		String message = "This message was sent using AWS: SNS";
    		
    		//Send Message
    		SendSMS(phone,message);
    		
    	});
    	
    	return snsScene;
	}
}
