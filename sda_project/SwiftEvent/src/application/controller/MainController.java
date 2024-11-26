package application.controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import application.model.PersistentHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private PersistentHandler persistentHandler;

    
    public MainController() {
        this.persistentHandler = new PersistentHandler();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String userType = persistentHandler.validateLogin(username, password);

        if (userType == null) {
            showAlert("Login Failed", "Invalid username or password.");
        } else if (userType.equalsIgnoreCase("Event Organizer")) {
            loadFXML("/application/Organizer.fxml", "Event Organizer Dashboard");
        } else if (userType.equalsIgnoreCase("Member")) {
            loadFXML("/application/Attendee.fxml", "Attendee Dashboard");
        } else {
            showAlert("Login Failed", "Unknown user type.");
        }
    }
    private void loadFXML(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow(); 
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the interface: " + fxmlFile);
        }
    }


    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            // Load the register screen
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/application/RegisterScreen.fxml"));
            Scene registerScene = new Scene(registerRoot);
            
            // Get the current stage and switch scenes
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
