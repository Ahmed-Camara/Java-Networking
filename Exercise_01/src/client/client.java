package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class client extends Application {
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	@Override
	public void start(Stage primaryStage) {
		BorderPane pane = new BorderPane();
		TextArea taInformation = new TextArea();
		GridPane gridPane = new GridPane();
		
		pane.setCenter(new ScrollPane(taInformation));
		pane.setTop(gridPane);
		
		TextField tfAnnualInterestRate = new TextField();
		TextField tfNumberOfYears = new TextField();
		TextField tfLoanAmount = new TextField();
		
		
		Button btnSubmit = new Button("Submit");
		
		
		gridPane.add(new Label("Annual Interest Rate: "), 0, 0);
		gridPane.add(tfAnnualInterestRate, 1, 0);
		
		gridPane.add(new Label("Number of years : "), 0, 1);
		gridPane.add(tfNumberOfYears, 1, 1);
		
		gridPane.add(btnSubmit, 2, 1);
		
		gridPane.add(new Label("Loan Amount : "), 0, 2);
		gridPane.add(tfLoanAmount, 1, 2);
		
		
		Scene scene = new Scene(pane,400,200);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		btnSubmit.setOnAction(e->{
			try {
				String ann_rate = tfAnnualInterestRate.getText();
				String num_year = tfNumberOfYears.getText();
				String loan = tfLoanAmount.getText();
				
				if(ann_rate.length() != 0 &&
						num_year.length() != 0 && loan.length() != 0) {
					
					double AnnualInterestRate = Double.parseDouble(ann_rate);
					int Number_years = Integer.parseInt(num_year);
					double Loan = Double.parseDouble(loan);
					
					toServer.writeDouble(AnnualInterestRate);
					toServer.writeInt(Number_years);
					toServer.writeDouble(Loan);
					
					taInformation.appendText("Annual Interest Rate : "+AnnualInterestRate+
							"\n Number of years : "+Number_years+"\n Loan Amount : "+
							Loan+"\n");
					
					
					double monthlyPayment = fromServer.readDouble();
					double totalPayment = fromServer.readDouble();
					
					taInformation.appendText("Monthly Payment : "+monthlyPayment);
					taInformation.appendText("\nTotal Payment : "+totalPayment);
				}
				
			} catch (IOException e1) {
				System.out.println(e1.getMessage());;
			}
		});
		
		
		try {
			Socket socket = new Socket("localhost",8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}catch(IOException ex) {
			taInformation.appendText(ex.toString()+"\n");
		}
		
	}
	


	public static void main(String[] args) {
		launch(args);
	}
}
