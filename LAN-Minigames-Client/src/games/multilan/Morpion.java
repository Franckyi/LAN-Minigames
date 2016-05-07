package games.multilan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.MenuPrincipal;
import main.MultiLAN;
import main.Reference;
import obj.ListenServerService;
import obj.Player;
import obj.Server;

public class Morpion {

	Player pl = MultiLAN.pl;
	Player p1, p2;
	Server server = MultiLAN.server;
	int turn, row, col, nb, win, pid;
	Stage primaryStage;

	Socket commSocket;
	PrintWriter netOut;
	BufferedReader netIn;

	int[] cases;
	
	Service<String> listen;

	@FXML
	VBox plateau;

	@FXML
	Label info, name1, score1, name2, score2;

	@FXML
	GridPane grille;

	Background bg = new Background(new BackgroundImage(new Image("/img/morpion/blank.png", 200, 200, false, true),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
	Background bleu = new Background(new BackgroundImage(
			new Image("/img/morpion/croixBleue.png", 200, 200, false, true), BackgroundRepeat.REPEAT,
			BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
	Background rouge = new Background(
			new BackgroundImage(new Image("/img/morpion/rondRouge.png", 200, 200, false, true), BackgroundRepeat.REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

	@FXML
	public void initialize() {
		primaryStage = new Stage();
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			 @Override
			 public void handle(WindowEvent event) {
				 exit(new ActionEvent());
			 }
		});
		// Initialisation des variables
		turn = (int) server.info;
		p1 = server.p1;
		p2 = server.p2;
		pid = server.getPlayerId(pl);
		listen();
		// Création de la grille
		for (row = 0; row < 3; row++) {
			RowConstraints rowConstraints = new RowConstraints();
			grille.getRowConstraints().add(rowConstraints);
		}
		for (col = 0; col < 3; col++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			grille.getColumnConstraints().add(columnConstraints);
		}
		grille.setVgap(10);
		grille.setHgap(10);
		grille.setPadding(new Insets(10));
		// Ajout des boutons
		for (row = 0; row < 3; row++) {
			for (col = 0; col < 3; col++) {
				Button b = new Button();
				b.setId(3 * col + row + "");
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						clk(Integer.parseInt(b.getId()));
					}
				});
				b.setBackground(bg);
				b.setPrefSize(200, 200);
				grille.add(b, col, row);
			}
		}
		init(0);
	}

	public void init(int p) {
		grille.setDisable(false);
		if (p != 0)
			turn = p;
		// Initialisation jeu
		System.out.println("[LOG] Jeu 'Morpion' démarré");
		if (turn == pid) {
			info.setText("Vous commencez à jouer ! [" + turn + "]");
		} else {
			info.setText("Au tour de l'adversaire... [" + turn + "]");
		}
		cases = new int[9];
		for (int i = 0; i < 9; i++) {
			cases[i] = 0;
			Button bu = (Button) recupImg(i % 3, i / 3);
			bu.setBackground(bg);
		}
		// Affichage
		score1.setText(server.p1.score + "");
		score2.setText(server.p2.score + "");
		name1.setText(server.p1.name);
		name2.setText(server.p2.name);
	}

	public Node recupImg(int row, int col) {
		for (Node node : grille.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	public void clk(int b) {
		if (cases[b] == 0 && turn == server.getPlayerId(pl)) {
			cases[b] = turn;
			Button bu = (Button) recupImg(b % 3, b / 3);
			System.out.println("[LOG] Le joueur " + turn + " a cliqué sur la case " + b);
			if (turn == 1) {
				bu.setBackground(bleu);
				turn = 2;
			} else {
				bu.setBackground(rouge);
				turn = 1;
			}
			send("place-" + b);
			if (turn == pid) {
				info.setText("A votre tour ! [" + turn + "]");
			} else {
				info.setText("Au tour de l'adversaire... [" + turn + "]");
			}
			nb++;

		}
	}

	public void send(String msg) {
		netOut.println(msg + "-" + pid);
	}

	public void listen() {
		listen = new ListenServerService();
		listen.start();
		listen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent e) {
				String msg[] = listen.getValue().split("-");
				System.out.println("[LOG] Le joueur " + pid + " a reçu le message : " + listen.getValue());
				if (msg[0].equals("end")) {
					grille.setDisable(true);
					if (msg[1].equals("1")) {
						info.setText("Le joueur 1 '" + p1.name + "' a gagné !");
						p1.score++;
						score1.setText(p1.score + "");
					}
					if (msg[1].equals("2")) {
						info.setText("Le joueur 1 '" + p2.name + "' a gagné !");
						p2.score++;
						score2.setText(p2.score + "");
					}
				}
				if (msg[0].equals("place")) {
					int b = Integer.parseInt(msg[1]);
					Button bu = (Button) recupImg(b % 3, b / 3);
					cases[b] = turn;
					if (turn == 1) {
						bu.setBackground(bleu);
						turn = 2;
					} else {
						bu.setBackground(rouge);
						turn = 1;
					}
					if (turn == pid) {
						info.setText("A votre tour ! [" + turn + "]");
					} else {
						info.setText("Au tour de l'adversaire... [" + turn + "]");
					}
					nb++;
				}
				if (msg[0].equals("quit")) {
					quit();
					Alert a = new Alert(AlertType.WARNING, "Votre adversaire s'est déconnecté", ButtonType.OK);
					a.showAndWait();
				}
				if (msg[0].equals("start")) {
					init(Integer.parseInt(msg[1]));
				}
				listen.restart();
			}
		});
	}

	public void setStage(Stage stage, BufferedReader netIn, PrintWriter netOut) {
		primaryStage = stage;
		this.netIn = netIn;
		this.netOut = netOut;
	}

	public void exit(ActionEvent e) {
		System.out.println("[LOG] Jeu 'Morpion' quitté (Sortie)");
		pl.score = 0;
		send("quit");
		listen.cancel();
		listen.reset();
		quit();
	}
	public void quit(){
		try{
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
