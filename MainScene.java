package finalproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainScene extends BaseScene{
	
	public Scene create(Stage primaryStage)
	{
        //Labels
        Text headerLbl = new Text("Amazon Web Services");
        
        //Buttons
        Button s3Btn = new Button("S3");
        Button dbBtn = new Button("DynamoDB");

        Button snsBtn = new Button ("SNS");
        Button quitBtn = new Button("Exit");
        
        //Grid Pane
        GridPane mainGrid = new GridPane();
        
        //Pane Settings
        mainGrid.setMinSize(640,480);
        mainGrid.setPadding(new Insets(10,10,10,10));
        mainGrid.setVgap(5);
        mainGrid.setHgap(5);
        mainGrid.setAlignment(Pos.CENTER);
        
        //Insert Objects into grid
        mainGrid.add(headerLbl,2,0);
        mainGrid.add(s3Btn,0,1);
        mainGrid.add(dbBtn,1,1);
        mainGrid.add(snsBtn,2,1);
        mainGrid.add(quitBtn,4,1);
        
        //Set text size
        headerLbl.setStyle("-fx-font: 16 arial");
        
        //Scene
        Scene mainScene = new Scene(mainGrid);
        
        //Functions
        //Main Scene
        s3Btn.setOnAction(value ->{
        	S3Scene s3Obj = new S3Scene();
        	Scene s3Scene = s3Obj.create(primaryStage);
        	
        	//Set stage for S3 Scene
        	primaryStage.setScene(s3Scene);
        	primaryStage.setTitle("AWS: S3");
        });
        
        dbBtn.setOnAction(value ->{
        	DBScene dbObj = new DBScene();
        	Scene dbScene = dbObj.create(primaryStage);
        	
        	//Set stage for DB Scene
        	primaryStage.setScene(dbScene);
        	primaryStage.setTitle("AWS: DynamoDB");
        });
        
        snsBtn.setOnAction(value ->{
        	SnsScene snsObj = new SnsScene();
        	Scene snsScene = snsObj.create(primaryStage);
        	
        	//Set stage for SNS Scene
        	primaryStage.setScene(snsScene);
        	primaryStage.setTitle("AWS: SNS");
        });

        //Exit Application
        quitBtn.setOnAction(value ->{
        	primaryStage.close();
        });
        
        return mainScene;
	}
}
