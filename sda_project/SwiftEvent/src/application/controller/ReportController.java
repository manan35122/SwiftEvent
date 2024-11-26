package application.controller;

import application.model.PersistentHandler;
import application.model.SessionManager;
import application.model.Event;
import application.model.EventDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReportController {

    @FXML
    private ComboBox<Event> eventComboBox; 

    @FXML
    private Label eventNameLabel;
    
    @FXML
    private Label attendeesLabel;
    
    @FXML
    private Label ratingLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Text eventDetailsText;


    private PersistentHandler persistentHandler;

    public ReportController() {
        persistentHandler = new PersistentHandler();
    }

    @FXML
    public void initialize() {
        if (eventDetailsText == null) {
            System.out.println("Error: eventDetailsText is not initialized!");
        } else {
            System.out.println("eventDetailsText is initialized successfully.");
        }

        // Populate the ComboBox with events from the database
        ObservableList<Event> eventList = FXCollections.observableArrayList(
            persistentHandler.getEventsByOrganizer(SessionManager.getLoggedInUserID())
        );
        
        eventComboBox.setItems(eventList);
        
        // Set the ComboBox to display event names (for user-friendly display)
        eventComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());  // Display the event name in the ComboBox
                }
            }
        });
        
        // Set the event handler to load event details when an event is selected
        eventComboBox.setOnAction(event -> loadEventDetails());
    }

    @FXML
    public void handleGenerateReport() {
        Event selectedEvent = eventComboBox.getValue();
        
        if (selectedEvent == null) {
            showAlert("Error", "Please select an event.");
            return;
        }

        // Generate report
        boolean reportGenerated = persistentHandler.generateEventReport(selectedEvent);

        if (reportGenerated) {
            showAlert("Success", "The report has been generated successfully.");
        } else {
            showAlert("Error", "There was an issue generating the report.");
        }
    }

    private void loadEventDetails() {
        Event selectedEvent = eventComboBox.getValue();

        if (selectedEvent != null) {
            EventDetails eventDetails = persistentHandler.getEventDetails(selectedEvent.getEventID());

            if (eventDetails != null) {
                eventNameLabel.setText("Event Name: " + eventDetails.getEventName());
                attendeesLabel.setText("Registered Attendees: " + eventDetails.getRegisteredAttendees());
                ratingLabel.setText("Average Rating: " + eventDetails.getEventRating());
            }
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
