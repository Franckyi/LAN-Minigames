package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/ServerStart.fxml"));
		Parent root = loader.load();
		ServerStart controller = (ServerStart) loader.getController();
		controller.setStage(primaryStage);
		Scene scene = new Scene(root, 800, 400);
		primaryStage.setTitle(Reference.TITLE + "Créer un serveur");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
