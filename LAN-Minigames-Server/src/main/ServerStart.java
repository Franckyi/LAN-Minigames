package main;

import java.io.IOException;
import java.net.InetAddress;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import obj.Server;

public class ServerStart {

	Stage primaryStage;
	String localip;
	String ip, name, game;
	int timer;

	public static Server server;

	@FXML
	TextField serverName, serverIP;

	@FXML
	RadioButton morpion, puiss4;
	ToggleGroup g = new ToggleGroup();
	
	@FXML
	CheckBox restart;
	
	@FXML
	Label time;
	
	@FXML
	HBox sbox;
	
	@FXML
	Button add, remove;

	public void setStage(Stage stage) {
		primaryStage = stage;

	}
	
	public void add(ActionEvent e){
		if(timer < 10)
			timer++;
		if(timer > 0)
			remove.setDisable(false);
		if(timer == 10)
			add.setDisable(true);
		time.setText("Temps d'attente en fin de partie : " + timer + " secondes");
		
	}
	
	public void remove(ActionEvent e){
		if(timer > 0)
			timer--;
		if(timer < 10)
			add.setDisable(false);
		if(timer == 0)
			remove.setDisable(true);
		time.setText("Temps d'attente en fin de partie : " + timer + " secondes");
	}

	@FXML
	public void initialize() {
		try {
			localip = InetAddress.getLocalHost().getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		morpion.setSelected(true);
		puiss4.setDisable(true);
		morpion.setToggleGroup(g);
		puiss4.setToggleGroup(g);
		serverIP.setDisable(true);
		serverIP.setText(localip);
		serverName.setText("Serveur");
		timer = 3;
		time.setText("Temps d'attente en fin de partie : " + timer + " secondes");
		add.setPrefSize(30, 30);
		add.setBackground(new Background(new BackgroundImage(new Image("/img/add.png", 30, 30, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		remove.setPrefSize(30, 30);
		remove.setBackground(new Background(new BackgroundImage(new Image("/img/remove.png", 30, 30, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		sbox.setDisable(false);
		restart.setSelected(true);
		restart.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(restart.isSelected())
					sbox.setDisable(false);
				else{
					sbox.setDisable(true);
				}
			}
		});
	}

	public void serverStart(ActionEvent e) {
		ip = serverIP.getText();
		name = serverName.getText();
		if(!restart.isSelected())
			timer = 0;
		if (g.getSelectedToggle().equals(morpion))
			game = "Morpion";
		if (g.getSelectedToggle().equals(puiss4))
			game = "Puissance 4";
		if (!ip.equals("") && !name.equals("") && game != null) {
			server = new Server(ip, name, game);
			server.timer = timer;
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/ServerWindow.fxml"));
				Parent root = loader.load();
				ServerWindow controller = (ServerWindow) loader.getController();
				controller.getStage(primaryStage);
				Scene scene = new Scene(root, 800, 870);
				primaryStage.setTitle(Reference.TITLE + "Serveur '" + server.name + "' (" + server.ip + ")");
				primaryStage.setScene(scene);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING,
					"Vous devez entrer toutes les informations avant de créer un serveur.", ButtonType.OK);
			alert.setTitle("Attention");
			alert.show();
		}
	}

}
