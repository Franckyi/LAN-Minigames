package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Solo {

	Stage primaryStage;

	@FXML
	public Button disabled1, disabled2;

	@FXML
	void initialize() {
		disabled1.setDisable(true);
		disabled2.setDisable(true);
	}

	public void setStage(Stage mainStage) {
		primaryStage = mainStage;
	}

	public void retour(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/MenuPrincipal.fxml"));
			Parent root = loader.load();
			MenuPrincipal controller = (MenuPrincipal) loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root, 800, 400);
			primaryStage.setTitle(Reference.TITLE + "Menu");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
