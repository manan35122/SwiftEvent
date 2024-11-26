package application.controller;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import application.model.PersistentHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class RegisterController {
	@FXML
	private TextField name;
	
	@FXML
	private TextField PhoneNumber;
	
	@FXML
	private TextField address;
	
	@FXML
	private TextField Email;
	
	@FXML
	private PasswordField  password;
	
	@FXML
	private ComboBox<String> combo;
	
	private  PersistentHandler persistentHandler;
	
	public RegisterController() {
		
		this.persistentHandler = new PersistentHandler();
	
	}
	public void initialize() {
        // Define the list of options for the ComboBox
        ObservableList<String> userTypeOptions = FXCollections.observableArrayList(
            "Member",
            "Event Organizer"
        );

        // Add the options to the ComboBox
        combo.setItems(userTypeOptions);
    }
	
	@FXML
	private void handleBack(ActionEvent event) {
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
	private void handleRegister(ActionEvent event) {
	    String userName = name.getText();
	    String userPhone = PhoneNumber.getText();
	    String userEmail = Email.getText();
	    String userPassword = password.getText();
	    String userType = combo.getValue();
	    String userAddress = address.getText();// Assuming combo box contains user types (e.g., "User", "Admin")

	    
	    if (userName.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userType == null) {
	        showAlert("Registration Failed", "Please fill in all fields.");
	        return;
	    }
	    

	   
	    boolean isSaved = persistentHandler.saveNewUser(userName, userEmail, userPassword, userAddress, userPhone, userType);

	    if (isSaved) {
	        showAlert("Registration Successful", "User registered successfully!");
	        clearFields(); 
	    } else {
	        showAlert("Registration Failed", "Could not register the user. Please try again.");
	    }
	}
	
	private void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	private void clearFields() {
	    name.clear();
	    PhoneNumber.clear();
	    Email.clear();
	    password.clear();
	    combo.setValue(null);
	    address.clear();
	}
	
	
	
}
