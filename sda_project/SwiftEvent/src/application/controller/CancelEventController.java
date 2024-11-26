package application.controller;

import java.io.IOException;
import java.util.List;

import application.model.Event;
import application.model.PersistentHandler;
import application.model.SessionManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class CancelEventController {
	@FXML
    private ListView<Event> eventListView;
	PersistentHandler persistentHandler;
	
	  public CancelEventController() {
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
	        eventListView.getItems().addAll(events);
	        eventListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Failed to initialize event list.");
	    }
	}
	@FXML
	private void handleCancel() {
	    ObservableList<Event> selectedEvents = eventListView.getSelectionModel().getSelectedItems();
	    for (Event event : selectedEvents) {
	        if (persistentHandler.cancelEvent(event.getEventID())) {
	            System.out.println("Event " + event.getName() + " canceled successfully.");
	        } else {
	            System.out.println("Failed to cancel event: " + event.getName());
	        }
	    }
	    eventListView.getItems().removeAll(selectedEvents);
   }
}
