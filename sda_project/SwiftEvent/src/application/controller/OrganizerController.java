package application.controller;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OrganizerController {
	
	
	
	
	
    @FXML 
	public void handleLogOut(ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/MainScreen.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    @FXML
    public void handleCreateEvent(ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/CreateEvent.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    public void handleCancel (ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/CancelEventScreen.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    public void handleViewScheduledEvents (ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/ScheduledEventScreen.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML 
    public void handleReport (ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/ReportScreen.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

}
