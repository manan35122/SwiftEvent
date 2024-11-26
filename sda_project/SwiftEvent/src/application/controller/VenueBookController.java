package application.controller;
import java.io.IOException;
import java.util.List;
import application.model.PersistentHandler;
import application.model.SessionManager;
import application.model.Venue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class VenueBookController {

    @FXML
    private ListView<Venue> venueListView;

    private PersistentHandler persistentHandler;
    private int eventID; 

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    @FXML
    public void initialize() {
        try {
            persistentHandler = new PersistentHandler();
            List<Venue> venues = persistentHandler.getAllVenues();
            ObservableList<Venue> venueItems = FXCollections.observableArrayList(venues);
            venueListView.setItems(venueItems);

            venueListView.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(Venue venue, boolean empty) {
                    super.updateItem(venue, empty);
                    if (empty || venue == null) {
                        setText(null);
                    } else {
                        setText(venue.getName() + " - Capacity: " + venue.getCapacity());
                    }
                }
            });

            venueListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize venue list.");
        }
    }
    
    public void handleBack(ActionEvent event) {
		try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/Organizer.fxml"));
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
    public void handleBook(ActionEvent event) {
        try {
            int organizerID = SessionManager.getLoggedInUserID();
            ObservableList<Venue> selectedVenues = venueListView.getSelectionModel().getSelectedItems();

            if (selectedVenues.isEmpty()) {
                System.out.println("No venues selected.");
                return;
            }
            StringBuilder successMessages = new StringBuilder();
            StringBuilder failureMessages = new StringBuilder();
            for (Venue venue : selectedVenues) {
                boolean success = persistentHandler.saveVenueForEvent(organizerID, eventID, venue.getVenueID(), venue.getName());
                if (success) {
                    successMessages.append("Successfully assigned Venue: ").append(venue.getName()).append("\n");
                } else {
                    failureMessages.append("Failed to assign Venue: ").append(venue.getName()).append("\n");
                }
            }
            
            if (successMessages.length() > 0) {
                showAlert("Booking Successful", successMessages.toString(), Alert.AlertType.INFORMATION);
            }
            if (failureMessages.length() > 0) {
                showAlert("Booking Failed", failureMessages.toString(), Alert.AlertType.ERROR);
            }
            venueListView.getSelectionModel().clearSelection();
            handleBack(event);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while booking venues.", Alert.AlertType.ERROR);
            System.out.println("Failed to book venues.");
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    

}

