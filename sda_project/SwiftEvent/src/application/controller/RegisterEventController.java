package application.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import application.model.Event;
import application.model.PersistentHandler;
import application.model.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class RegisterEventController {
	 @FXML
	private ComboBox<String> payment; 

    @FXML
    private ComboBox<String> locationFilter;
    @FXML
    private ComboBox<String> priceFilter;
    @FXML
    private ListView<Event> eventListView;

    private PersistentHandler persistentHandler;
    private List<Event> allEvents;

    public RegisterEventController () {
        persistentHandler = new PersistentHandler();
    }

    private List<Event> loadAllEvents() {
        return persistentHandler.getAllEvents(SessionManager.getLoggedInUserID());
    }

    @FXML
    public void initialize() {
        allEvents = loadAllEvents();
        eventListView.getItems().setAll(allEvents);


        locationFilter.setValue("All Locations");
        priceFilter.setValue("All Prices");

        locationFilter.getItems().addAll("All Locations", "Lahore", "Islamabad", "Karachi"); // example locations
        priceFilter.getItems().addAll("All Prices", "Below 500", "1000+", "5000+");

        payment.getItems().addAll("By Cash", "By Credit/Debit Card");
        payment.setPromptText("Select Payment Method");

        locationFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        priceFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }
    private void applyFilters() {
        String selectedLocation = locationFilter.getValue();
        String selectedPrice = priceFilter.getValue();
        List<Event> filteredEvents = allEvents.stream()
            .filter(event -> filterByLocation(event, selectedLocation))
            .filter(event -> filterByPrice(event, selectedPrice))
            .collect(Collectors.toList());
        eventListView.getItems().setAll(filteredEvents);
    }
    private boolean filterByLocation(Event event, String selectedLocation) {
        if ("All Locations".equals(selectedLocation)) {
            return true; 
        }
        return event.getVenue().equalsIgnoreCase(selectedLocation); 
    }

    private boolean filterByPrice(Event event, String selectedPrice) {
        if ("All Prices".equals(selectedPrice)) {
            return true;  
        }
        switch (selectedPrice) {
            case "Below 500":
                return event.getTicketPrice() < 500;
            case "1000+":
                return event.getTicketPrice() >= 1000 && event.getTicketPrice() < 5000;
            case "5000+":
                return event.getTicketPrice() >= 5000;
            default:
                return true;  
        }
    }
    @FXML
    public void handleRegister(ActionEvent event) {
        Event selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        String paymentMethod = payment.getValue();

        if (selectedEvent != null) {
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                showAlert("Warning", "Payment Method Required", 
                          "Please select a payment method to proceed with registration.", Alert.AlertType.WARNING);
                return;
            }

            int userID = SessionManager.getLoggedInUserID();
            int eventID = selectedEvent.getEventID();

            boolean isRegistered = persistentHandler.registerUserForEvent(userID, eventID);

            if (isRegistered) {
                showAlert("Success", "Registration Successful", 
                          "You have successfully registered for the event: " + selectedEvent.getName(), Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Registration Failed", 
                          "Could not register for the event: " + selectedEvent.getName(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "No Event Selected", 
                      "Please select an event to register.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); 
    }
    @FXML 
	public void handleBack(ActionEvent event) {
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
