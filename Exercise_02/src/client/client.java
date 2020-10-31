package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class client extends Application{
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
		TextArea taInformation = new TextArea();
		GridPane gridPane = new GridPane();
		
		gridPane.setPadding(new Insets(5,5,5,5));
		
		Button btnSubmit = new Button("Submit");
		
		TextField tfWeight = new TextField();
		TextField tfHeight = new TextField();
		
		gridPane.add(new Label("Weight in pounds : "), 0, 0);
		gridPane.add(tfWeight, 1, 0);
		
		
		gridPane.add(new Label("Height in inches : "), 0, 1);
		gridPane.add(tfHeight, 1, 1);
		
		gridPane.add(btnSubmit, 2, 1);
		
		
		root.setCenter(new ScrollPane(taInformation));
		root.setTop(gridPane);
		Scene scene = new Scene(root,400,200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CLIENT");
		primaryStage.show();
		
		btnSubmit.setOnAction(e->{
			
			try {
				String WeightString = tfWeight.getText().trim();
				String HeightString = tfHeight.getText().trim();
				
				if(WeightString.length() != 0 && HeightString.length() != 0) {
					
					double weight = Double.parseDouble(WeightString);
					double height = Double.parseDouble(HeightString);
					
					toServer.writeDouble(weight);
					toServer.writeDouble(height);
					
					taInformation.appendText("\n Weight : "+weight);
					taInformation.appendText("\n Height : "+height);
					
					double BMI = fromServer.readDouble();
					
					taInformation.appendText("\n BMI : "+BMI);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		try {
			
			Socket socket = new Socket("localhost",8000);
			toServer = new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
