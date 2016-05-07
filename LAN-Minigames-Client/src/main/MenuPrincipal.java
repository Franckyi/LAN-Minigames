package main;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import obj.Player;

public class MenuPrincipal {

	public Player p = Main.p;
	Stage mainStage;

	public void setStage(Stage stage) {
		mainStage = stage;
	}

	@FXML
	public Label pseudo;

	@FXML
	public Button multiLAN;

	@FXML
	void initialize() {
		pseudo.setText("Connecté en tant que '" + p.name + "' (" + p.ip + ")");
	}

	public void solo(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/Solo.fxml"));
			Parent root = loader.load();
			Solo controller = (Solo) loader.getController();
			controller.setStage(mainStage);
			Scene scene = new Scene(root, 800, 400);
			mainStage.setTitle(Reference.TITLE + "Un joueur");
			mainStage.setScene(scene);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void multiLocal(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/MultiLocal.fxml"));
			Parent root = loader.load();
			MultiLocal controller = (MultiLocal) loader.getController();
			controller.setStage(mainStage);
			Scene scene = new Scene(root, 800, 400);
			mainStage.setTitle(Reference.TITLE + "Deux joueurs (Local)");
			mainStage.setScene(scene);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void multiLAN(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog("192.168.1.13");
		dialog.setTitle(Reference.TITLE + "Se connecter à un serveur");
		dialog.setHeaderText("Addresse IP requise");
		dialog.setContentText("Entrez l'addresse IP du serveur :");
		// Détection de données
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { // La personne appuie sur OK :
			MultiLAN multi = new MultiLAN();
			multi.serverJoin(result.get(), p, mainStage);
		}
	}

	public void exit(ActionEvent e) {
		System.out.println("[LOG] Joueur '" + p.name + "' (" + p.ip + ") déconnecté avec succès");
		System.exit(0);
	}

	public void siteWeb(ActionEvent e) {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("cmd /c start http://github.com/Franckyi/LAN-Minigames/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}