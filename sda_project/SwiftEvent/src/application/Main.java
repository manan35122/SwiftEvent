package application;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			   FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
	            Scene scene = new Scene(loader.load());
	            
	            // Set the scene to the stage
	            primaryStage.setScene(scene);
	            
	            // Set the title
	            primaryStage.setTitle("Event Management System");
	            primaryStage.show();	           
	      
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		
		launch(args);
	}
}
