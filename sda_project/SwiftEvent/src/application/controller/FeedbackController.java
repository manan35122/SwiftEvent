package application.controller;

import application.model.Event;
import application.model.PersistentHandler;
import application.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackController {
    @FXML
    private Spinner<Double> rating;

    @FXML
    private ComboBox<String> SelectedEvent;

    @FXML
    private TextField comment;

    private PersistentHandler persistentHandler;
    private Map<String, Event> eventMap = new HashMap<>();

    public FeedbackController() {
        this.persistentHandler = new PersistentHandler();
    }

    @FXML
    public void initialize() {
    	SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 0.0, 0.5);
        rating.setValueFactory(valueFactory);
        List<Event> events = persistentHandler.getRegisteredEvents(SessionManager.getLoggedInUserID());
        ObservableList<String> eventNames = FXCollections.observableArrayList();
        for (Event event : events) {
            eventNames.add(event.getName());
            eventMap.put(event.getName(), event);
        }
        SelectedEvent.setItems(eventNames);
    }

    @FXML
    public void submitFeedback(ActionEvent event) {
        if (SelectedEvent.getValue() == null || comment.getText().trim().isEmpty() || rating.getValue() == null) {
            showAlert("Error", "Please fill all fields before submitting feedback.");
            return;
        }
        String selectedEventName = SelectedEvent.getValue();
        Event selectedEventObj = eventMap.get(selectedEventName);

        if (selectedEventObj != null) {
            int eventID = selectedEventObj.getEventID();
            Double ratingValue = rating.getValue();  
            String userComment = comment.getText();

            persistentHandler.insertFeedback(eventID, ratingValue, userComment);

            resetForm();
            showAlert("Success", "Feedback submitted successfully!");
        } else {
            showAlert("Error", "Selected event not found.");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void resetForm() {
        SelectedEvent.setValue(null);
        rating.getValueFactory().setValue((double) 1);  
        comment.clear();
    }
}
