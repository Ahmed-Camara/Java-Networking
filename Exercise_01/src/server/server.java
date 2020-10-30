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
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class server extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		TextArea connection_information = new TextArea();
		connection_information.setEditable(false);
		
		
		Scene scene = new Scene(new ScrollPane(connection_information),400,200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		new Thread(()->{
			
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				
				
				Platform.runLater(()->{
					
					connection_information.appendText("Server started at: "+new Date());
				});
				
				Socket socket = serverSocket.accept();
				
				Platform.runLater(()->{
					
					connection_information.appendText("\nConnected to a client at: "+new Date());
				});
				
				DataInputStream inputFromClient =
						new DataInputStream(socket.getInputStream());
				
				DataOutputStream outputToClient =
						new DataOutputStream(socket.getOutputStream());
				
				
				while(true) {
					
					double annual_interest_rate = inputFromClient.readDouble();
					 int number_years = inputFromClient.readInt();
					 double loan_amount = inputFromClient.readDouble();
					
					Platform.runLater(()->{
						connection_information.appendText("\nAnnual Interest Rate : "+
								annual_interest_rate+"\n"+"Number of years: "+
								number_years+"\n"+"Loan Amount : "+loan_amount);
					});
					
					
					double monthlyPayment = 
							getMonthlyPayment(annual_interest_rate,number_years,loan_amount);
					
					double totalPayment =
							getMonthlyPayment(annual_interest_rate,number_years,loan_amount) * number_years * 12;
					
					Platform.runLater(()->{
						connection_information.appendText("\nMonthly Payment: "+
								monthlyPayment+"\n"+"Total Payment: "+totalPayment+"\n");
					});
					
					outputToClient.writeDouble(monthlyPayment);
					outputToClient.writeDouble(totalPayment);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		
	}
	
	private double getMonthlyPayment(double annual_interest_rate,
			int number_years,double loan_amount) {
		
		
		double monthlyInterestRate = annual_interest_rate / 1200;
		double monthlyPayment = loan_amount * monthlyInterestRate / (1 -
				(1 / Math.pow(1 + monthlyInterestRate, number_years * 12)));
		
		
		return monthlyPayment;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
