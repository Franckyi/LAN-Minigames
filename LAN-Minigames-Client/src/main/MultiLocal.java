package main;

import java.io.IOException;

import games.multilocal.Morpion;
import games.multilocal.Puissance4;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MultiLocal {

	Stage primaryStage;

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

	public void mrpStart(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/Morpion.fxml"));
			Parent root = loader.load();
			Morpion controller = (Morpion) loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root, 740, 675);
			primaryStage.setTitle(Reference.TITLE + "Morpion (Local)");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void p4Start(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/Puissance4.fxml"));
			Parent root = loader.load();
			Puissance4 controller = (Puissance4) loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root, 530, 530);
			primaryStage.setTitle(Reference.TITLE + "Puissance 4 (Local)");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
