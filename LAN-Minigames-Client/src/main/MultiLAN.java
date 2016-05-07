package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import games.multilan.Morpion;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import obj.ListenServerService;
import obj.Player;
import obj.Server;

public class MultiLAN {

	public static Player pl = Main.p;
	public static Server server;
	Player p1, p2;
	Stage primaryStage;
	String serverIP;

	public static Socket commSocket;
	Alert alert;

	public static BufferedReader netIn;
	public PrintWriter netOut;

	public void serverJoin(String ip, Player p, Stage mainStage) {
		try {
			primaryStage = mainStage;
			serverIP = ip;
			commSocket = new Socket(serverIP, Reference.SOCKET);
			netIn = new BufferedReader(new InputStreamReader(commSocket.getInputStream()));
			netOut = new PrintWriter(commSocket.getOutputStream(), true);
			p.addFlux(netIn, netOut);
			netOut.println("add-" + p.name + "-" + p.ip);
			listen();

		} catch (IOException e) {
			alert = new Alert(AlertType.ERROR, "L'addresse IP est éronnée.", ButtonType.CLOSE);
			alert.setTitle(Reference.TITLE + "Erreur");
			alert.setHeaderText("Erreur");
			alert.show();
		}

	}

	public void listen() {
		Service<String> listen = new ListenServerService();
		listen.start();
		listen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent e) {
				String msg[] = listen.getValue().split("-");
				// System.out.println(msg[0]);
				if (msg[0].equals("server")) {
					server = new Server(serverIP, msg[1], msg[2]);
					System.out.println("[LOG] Connecté au serveur '" + server.name + "' (" + server.ip + ") [Jeu : "
							+ server.game + "]");
					listen.restart();
				}
				if (msg[0].equals("player")) {
					System.out.println("[LOG] Joueur '" + msg[1] + "' (" + msg[2] + ") reçu");
					server.addPlayer(new Player(msg[1], msg[2]));
					if (!server.isFull)
						server.addPlayer(pl);
					listen.restart();

				}
				if (msg[0].equals("start")) {
					server.isGameStarted = true;
					System.out.println("[LOG] Le jeu démarre...");
					startGame(server.game, Integer.parseInt(msg[1]));
					if (alert != null)
						alert.close();
				}
				if (msg[0].equals("wait")) {
					server.addPlayer(pl);
					alert = new Alert(AlertType.INFORMATION, "En attente d'un adversaire... veuillez patienter !",
							ButtonType.CANCEL);
					alert.setTitle(Reference.TITLE + "Attente d'un adversaire");
					alert.setHeaderText("Attente d'un adversaire");
					System.out.println("[LOG] En attente d'un adversaire...");
					listen.restart();
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.CANCEL && server.isGameStarted == false) {
						try {
							commSocket = new Socket(serverIP, Reference.SOCKET);
							netOut = new PrintWriter(commSocket.getOutputStream(), true);
							netOut.println("remove-" + pl.name);
							commSocket.close();
							netIn.close();
							netOut.close();
						} catch (UnknownHostException ex) {
							ex.printStackTrace();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						System.out.println("[LOG] Déconnecté du serveur");
					}
				}
			}
		});
	}

	public void startGame(String game, int p) {
		server.setInfo(p);
		p1 = server.p1;
		p2 = server.p2;
		if (game.equals("Morpion")) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/MorpionLAN.fxml"));
				Parent root = loader.load();
				Morpion controller = (Morpion) loader.getController();
				controller.setStage(primaryStage, netIn, netOut);
				Scene scene = new Scene(root, 740, 675);
				primaryStage.setTitle(Reference.TITLE + "Serveur '" + server.name + "' (" + server.ip + ") - Morpion - '" + pl.name + "'");
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
