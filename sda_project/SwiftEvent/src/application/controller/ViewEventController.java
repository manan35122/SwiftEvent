package application.controller;

import java.io.IOException;
import java.util.List;

import application.model.Event;
import application.model.PersistentHandler;
import application.model.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class ViewEventController {
	
	@FXML
    private ListView<Event> ViewEvents;
	PersistentHandler persistentHandler;
	
	
	public ViewEventController() {
		persistentHandler =  new PersistentHandler();
	}
	
	@FXML
    public void handleBack(ActionEvent event) {
    	try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/Organizer.fxml"));
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
	public void initialize() {
	    try {
	        int organizerID = SessionManager.getLoggedInUserID();
	        List<Event> events = persistentHandler.getEventsByOrganizer(organizerID);
	        ViewEvents.getItems().addAll(events);
	        ViewEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Failed to initialize event list.");
	    }
	}

}
