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
import javafx.stage.Stage;

public class RegisteredEventController {
	@FXML
    private ListView<Event> registeredList;
	
	private PersistentHandler persistentHandler;

    public RegisteredEventController() {
        persistentHandler = new PersistentHandler();
    }
    @FXML
    public void initialize() {
        loadRegisteredEvents();
    }
	@FXML
	private void handleBack(ActionEvent event) {
		 try {
	            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/Attendee.fxml"));
	            Scene registerScene = new Scene(registerRoot);
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(registerScene);
	            stage.setTitle("Main");
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	 private void loadRegisteredEvents() {
	        try {

	            int userID = SessionManager.getLoggedInUserID();
	            
	            List<Event> registeredEvents = persistentHandler.getRegisteredEvents(userID);
	            registeredList.getItems().setAll(registeredEvents);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Failed to load registered events.");
	        }
	    }
	
     

}
