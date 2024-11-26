package application.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AttendeeController {
   
	
	
	 @FXML 
		public void handleLogout(ActionEvent event) {
	    	try {
	            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/MainScreen.fxml"));
	            Scene registerScene = new Scene(registerRoot);
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(registerScene);
	            stage.setTitle("Main");
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		@FXML 
		public void handleEventRegister(ActionEvent event) {
	    	try {
	            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/RegisterEventScreen.fxml"));
	            Scene registerScene = new Scene(registerRoot);
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(registerScene);
	            stage.setTitle("Main");
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		@FXML 
		public void handleEventRegistered(ActionEvent event) {
	    	try {
	            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/RegisteredEventsScreen.fxml"));
	            Scene registerScene = new Scene(registerRoot);
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(registerScene);
	            stage.setTitle("Main");
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		@FXML 
		public void handleFeedback(ActionEvent event) {
	    	try {
	            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/FeedbackScreen.fxml"));
	            Scene registerScene = new Scene(registerRoot);
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(registerScene);
	            stage.setTitle("Main");
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}

}
