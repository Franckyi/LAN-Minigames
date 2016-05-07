package main;

import java.net.InetAddress;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import obj.Player;

public class Main extends Application {

	public static Player p;
	String pseudo;
	String ip;

	@Override
	public void start(Stage primaryStage) {
		try {
			// Initialisation - fen�tre de connexion
			TextInputDialog dialog = new TextInputDialog("Joueur");
			dialog.setTitle(Reference.TITLE + "Connexion");
			dialog.setHeaderText("Pseudo requis");
			dialog.setContentText("Entrez votre pseudo :");
			// D�tection de donn�es
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) { // La personne appuie sur OK :
				// R�cup�ration des donn�es
				pseudo = result.get();
				if (!pseudo.equals("")) { // Un pseudo est entr�
					// Cr�ation du joueur
					ip = InetAddress.getLocalHost().getHostAddress();
					p = new Player(pseudo, ip);
					System.out.println("[LOG] Joueur '" + p.name + "' (" + p.ip + ") connect� avec succ�s");
				} else { // Aucun pseudo n'est entr�
					System.exit(0);
				}
			} else { // La personne a quitt�
				System.exit(0);
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/MenuPrincipal.fxml"));
			Parent root = loader.load();
			MenuPrincipal controller = (MenuPrincipal) loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root, 800, 400);

			// Initialisation - menu principal
			primaryStage.setTitle(Reference.TITLE + "Menu");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
