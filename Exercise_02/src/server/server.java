package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class server extends Application{
	
	
	@Override
	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		TextArea connection_information = new TextArea();
		connection_information.setEditable(false);
		root.setCenter(new ScrollPane(connection_information));
		
		Scene scene = new Scene(root,400,200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SERVER");
		primaryStage.show();
		
		new Thread(()->{
			
			
			try {
				
				final double KILOGRAMS_PER_POUND = 0.45359237;
				final double METERS_PER_INCH = 0.0254;
				
				
				ServerSocket serverSocket = new ServerSocket(8000);
				
				Platform.runLater(()->{
					connection_information.appendText("Server started at : "+new Date());
				});
				
				Socket socket = serverSocket.accept();
				
				Platform.runLater(()->{
					connection_information.appendText("\nConnected to a client at : "+new Date());
				});
				
				DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
				DataInputStream fromClient = new DataInputStream(socket.getInputStream());
				
				
				while(true) {
					
					double weight = fromClient.readDouble();
					
					double height = fromClient.readDouble();
					
					Platform.runLater(()->{
						connection_information.appendText("\n Weight : "+weight);
						connection_information.appendText("\n Height : "+height);
					});
					
					double w = weight * KILOGRAMS_PER_POUND;
					double h = height * METERS_PER_INCH;
					
					
					double BMI = w / Math.pow(h, 2);
					
					
					Platform.runLater(()->{
						connection_information.appendText("\n BMI is : "+BMI+" underweight");
					});
					
					toClient.writeDouble(BMI);
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

