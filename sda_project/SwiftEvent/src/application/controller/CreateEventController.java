package application.controller;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import application.model.Event;
import java.io.IOException;
import application.model.PersistentHandler;
import application.model.SessionManager;
import application.model.Vendor;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
public class CreateEventController {
	@FXML
    private ListView<Vendor> vendorListView;
	@FXML
	private TextField Title;
	
	@FXML
	private TextField Location;
	
	@FXML
	private DatePicker startDate;
	
	@FXML
	private DatePicker endDate;
	
	@FXML
	private TextField Description;
	
	@FXML
	private TextField price;
	
	@FXML
	private Spinner<Integer> capacity;
	
	
	@FXML
	public void initialize() {
	    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1);
	    capacity.setValueFactory(valueFactory);

	    capacity.setEditable(true);

	    populateVendorList();
	}
	private void populateVendorList() {
	    try {
	        PersistentHandler persistentHandler = new PersistentHandler(); 
	        ObservableList<Vendor> vendors = persistentHandler.getAllVendors();
	        vendorListView.setItems(vendors);

	        vendorListView.setCellFactory(listView -> new ListCell<Vendor>() {
	            private final CheckBox checkBox = new CheckBox();

	            @Override
	            protected void updateItem(Vendor vendor, boolean empty) {
	                super.updateItem(vendor, empty);
	                if (empty || vendor == null) {
	                    setText(null);
	                    setGraphic(null);
	                } else {
	                    checkBox.setText(vendor.getName() + " - " + vendor.getServiceType() + " ($" + vendor.getPricing() + ")");
	                    checkBox.setSelected(vendor.isSelected());  
	                    checkBox.setOnAction(event -> vendor.setSelected(checkBox.isSelected())); 

	                    setGraphic(checkBox);
	                }
	            }
	        });

	        vendorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	    } catch (Exception e) {
	        e.printStackTrace();
	        showAlert("Error", "Failed to fetch vendors from the database.");
	    }
	}
	
	public void openVenueBooking(ActionEvent event, int eventID) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/VenueBookScreen.fxml"));
	        Parent venueBookRoot = loader.load();

	        // Get the controller of VenueBookScreen
	        VenueBookController venueBookController = loader.getController();

	        // Pass the eventID to the VenueBookController
	        venueBookController.setEventID(eventID);

	        // Show the VenueBookScreen
	        Scene venueBookScene = new Scene(venueBookRoot);
	        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	        stage.setScene(venueBookScene);
	        stage.setTitle("Book Venue");
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}




	@FXML
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
	public void handleSave(ActionEvent event) {
	    try {
	        if (Title.getText().trim().isEmpty() || Location.getText().trim().isEmpty() || Description.getText().trim().isEmpty() || price.getText().trim().isEmpty()) {
	            showAlert("Error", "Please enter all fields.");
	            return;
	        }
	        Event ev = new Event();
	        ev.setName(Title.getText().trim());
	        ev.setVenue(Location.getText().trim());
	        ev.setDescription(Description.getText().trim());
	        try {
	            int ticketPrice = Integer.parseInt(price.getText().trim());
	            ev.setTicketPrice(ticketPrice);
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid price input. Please enter a valid number.");
	        }
	        if (startDate.getValue() == null || endDate.getValue() == null) {
	            showAlert("Error", "Start Date and End Date must be selected.");
	            return;
	        }
	        ev.setStartDate(startDate.getValue().toString());
	        ev.setEndDate(endDate.getValue().toString());
	        if (capacity.getValue() == null || (int) capacity.getValue() <= 0) {
	            showAlert("Error", "Capacity must be a positive number.");
	            return;
	        }
	        int spinnerValue = capacity.getValue();
	        ev.setMaxCapacity(spinnerValue);
	        ev.setStatus("Scheduled");
	        int organizerID = SessionManager.getLoggedInUserID();

	        PersistentHandler persistentHandler = new PersistentHandler(); 
	        boolean isSaved = persistentHandler.saveEvent(ev, organizerID);

	        if (isSaved) {
	            int eventID = ev.getEventID(); // Retrieve the generated event ID
	            ObservableList<Vendor> vendors = vendorListView.getItems();

	            for (Vendor vendor : vendors) {
	                if (vendor.isSelected()) {
	                    boolean vendorAssigned = persistentHandler.assignVendorsToEvent(eventID, organizerID, vendor.getVendorID(), vendor.getName());
	                    if (!vendorAssigned) {
	                        System.out.println("Failed to assign vendor: " + vendor.getName());
	                    }
	                }
	            }

	            showAlert("Success", "Event created successfully!");
	            clearForm();
	            openVenueBooking(event, eventID); // Pass eventID to VenueBookController
	        } else {
	            showAlert("Error", "Failed to create the event. Please try again.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        showAlert("Error", "An unexpected error occurred: " + e.getMessage());
	    }
	}


	
	private void clearForm() {
	    Title.clear();
	    Location.clear();
	    startDate.setValue(null);
	    endDate.setValue(null);
	    Description.clear();
	    price.clear();

	    if (capacity.getValueFactory() != null) {
	        capacity.getValueFactory().setValue(1); 
	    } else {
	        capacity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
	    }
	}


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
