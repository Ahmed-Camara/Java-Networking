package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
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
	
	private int clientNumber = 0;
	private TextArea connection_information = new TextArea();
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		connection_information.setEditable(false);
		
		
		Scene scene = new Scene(new ScrollPane(connection_information),400,200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		new Thread(()->{
			
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				
				
				Platform.runLater(()->{
					
					connection_information.appendText("Server started at: "+new Date()+"\n");
				});
				
				
				while(true) {
					Socket socket = serverSocket.accept();
					clientNumber++;
					InetAddress inetAddress = socket.getInetAddress();
					
					Platform.runLater(()->{
						
						connection_information
							.appendText("Connected to client "+clientNumber+" at : "+
							new Date()+"\n");

						connection_information
								.appendText("Host name is "
								+inetAddress.getHostName()+"\n");
						
						connection_information
								.appendText("IP Address is "
								+inetAddress.getHostAddress()+"\n");
						connection_information.appendText("\n");
					});
					
					new Thread(new clientThread(socket)).start();
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	class clientThread implements Runnable{
		
		private Socket socket;
		
		clientThread(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			
			
			try {
			
			DataInputStream inputFromClient =
					new DataInputStream(socket.getInputStream());
			
			DataOutputStream outputToClient =
					new DataOutputStream(socket.getOutputStream());
			
			double annual_interest_rate = inputFromClient.readDouble();
			int number_years = inputFromClient.readInt();
			double loan_amount = inputFromClient.readDouble();
			
			Platform.runLater(()->{
				
				connection_information
					.appendText("Annual Interest Rate : "+annual_interest_rate+"\n");
				connection_information.appendText("Number of years : "+number_years+"\n");
				connection_information
					.appendText("Loan Amount : "+loan_amount+"\n");
			});
			 
			 
			double monthlyPayment = 
					getMonthlyPayment(annual_interest_rate,number_years,loan_amount);
			
			double totalPayment =
					getMonthlyPayment(annual_interest_rate,number_years,loan_amount) * number_years * 12;
			
			Platform.runLater(()->{
				
				connection_information.appendText("Monthly Payment: "+
						monthlyPayment+"\n"+"Total Payment: "+totalPayment+"\n");
			});
			
			outputToClient.writeDouble(monthlyPayment);
			outputToClient.writeDouble(totalPayment);
			
			
			}catch(Exception ex) {
				
			}
		}
		
	}
	
	
	private double getMonthlyPayment(double annual_interest_rate,
			int number_years,double loan_amount) {
		
		
		double monthlyInterestRate = annual_interest_rate / 1200;
		double monthlyPayment = loan_amount * monthlyInterestRate / (1 -
				(1 / Math.pow(1 + monthlyInterestRate, number_years * 12)));
		
		
		return monthlyPayment;
	}

}
