package games.multilocal;

import java.io.IOException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import main.Main;
import main.MultiLocal;
import main.Reference;
import obj.Player;

public class Puissance4 {

	Player p1 = Main.p;
	Player p2 = new Player("Invité", null);

	Stage primaryStage;

	@FXML
	Button reset;

	@FXML
	Label score1, name1, score2, name2, info;

	@FXML
	GridPane grille;

	@FXML
	VBox score;

	public void setStage(Stage mainStage) {
		primaryStage = mainStage;
	}

	int row, col, turn, win, nb;
	int[] cases;

	Background blueArrow = new Background(
			new BackgroundImage(new Image("/img/puiss4/blueArrow.gif", 50, 50, false, true), BackgroundRepeat.REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

	Background redArrow = new Background(new BackgroundImage(new Image("/img/puiss4/redArrow.gif", 50, 50, false, true),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

	@FXML
	public void initialize() {
		// Création de la grille
		for (row = 0; row < 8; row++) {
			RowConstraints rowConstraints = new RowConstraints();
			grille.getRowConstraints().add(rowConstraints);
		}
		for (col = 0; col < 7; col++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			grille.getColumnConstraints().add(columnConstraints);
		}
		grille.setVgap(10);
		grille.setHgap(10);
		grille.setPadding(new Insets(10));
		// Ajout des boutons
		for (row = 0; row < 8; row++) {
			for (col = 0; col < 7; col++) {
				if (row == 0) {
					Button b = new Button();
					b.setId(col + "");
					b.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							clk(Integer.parseInt(b.getId()));
						}

					});
					b.setPrefSize(50, 50);
					grille.add(b, col, row);
				} else {
					ImageView img = new ImageView(new Image("img/puiss4/blank.png"));
					grille.add(img, col, row);
				}
			}
		}
		// Affichage
		score1.setText(p1.score + "");
		score2.setText(p2.score + "");
		name1.setText(p1.name);
		name2.setText(p2.name);
		// Initialisation du jeu
		set();
	}

	public void set() {
		reset.setDisable(true);
		Random rand = new Random();
		turn = rand.nextInt(2) + 1;
		cases = new int[49];
		for (int i = 0; i < 49; i++) {
			cases[i] = 0;
		}
		win = 0;
		nb = 0;
		if (turn == 1) {
			info.setText("Le joueur 1 (" + p1.name + ") commence !");
		} else {
			info.setText("Le joueur 2 (" + p2.name + ") commence !");
		}
		changeButtons();
		System.out.println("[LOG] Jeu 'Puissance 4' commencé");
	}

	public void clk(int b) {
		// Un pion peut-il être placé ?
		if (cases[b] == 0 && win == 0) {
			for (int i = 6; i >= 0; i--) {
				if (cases[7 * i + b] == 0) {
					cases[7 * i + b] = turn;
					ImageView img = (ImageView) recupImg(i + 1, b);
					i = -1;
					nb++;
					if (turn == 1) {
						img.setImage(new Image("img/puiss4/blueDisk.png"));
						turn++;
					} else {
						img.setImage(new Image("img/puiss4/redDisk.png"));
						turn--;
					}
				}
			}
			detectWin();
		}
	}

	public Node recupImg(int row, int col) {
		for (Node node : grille.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	public void changeButtons() {
		for (int i = 0; i < 7; i++) {
			Button img = (Button) recupImg(0, i);
			if (turn == 1) {
				img.setBackground(blueArrow);
			} else {
				img.setBackground(redArrow);
			}
		}
	}

	public void detectWin() {
		info.setText("Au tour du joueur " + turn + " !");
		if (nb == 49) {
			win = 3;
		}
		changeButtons();
		for (int p = 1; p < 3; p++) {
			// Détection verticale
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 7; j++) {
					if (cases[i * 7 + j] == p && cases[i * 7 + j + 7] == p && cases[i * 7 + j + 14] == p
							&& cases[i * 7 + j + 21] == p) {
						win = p;
					}
				}
			}
			// Détection horizontale
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 4; j++) {
					if (cases[i * 7 + j] == p && cases[i * 7 + j + 1] == p && cases[i * 7 + j + 2] == p
							&& cases[i * 7 + j + 3] == p) {
						win = p;
					}
				}
			}
			// Détection diagonale
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if ((cases[i * 7 + j] == p && cases[i * 7 + j + 8] == p && cases[i * 7 + j + 16] == p
							&& cases[i * 7 + j + 24] == p)
							|| (cases[i * 7 + j] == p && cases[i * 7 + j + 6] == p && cases[i * 7 + j + 12] == p
									&& cases[i * 7 + j + 18] == p)) {
						win = p;
					}
				}
			}
		}
		if (win != 0) {
			if (win == 1) {
				info.setText("Le joueur 1 (" + p1.name + ") a gagné !");
				p1.score++;
				score1.setText(p1.score + "");
			}
			if (win == 2) {
				info.setText("Le joueur 2 (" + p2.name + ") a gagné !");
				p2.score++;
				score2.setText(p2.score + "");
			}
			if (win == 3) {
				info.setText("Egalité !");
			}
			reset.setDisable(false);
			System.out.println("[LOG] Jeu 'Puissance 4' arrêté (Victoire/Egalité)");
		}

	}

	public void reset(ActionEvent e) {
		for (row = 1; row < 8; row++) {
			for (col = 0; col < 7; col++) {
				ImageView img = (ImageView) recupImg(row, col);
				img.setImage(new Image("img/puiss4/blank.png"));
			}
		}
		set();
	}

	public void exit(ActionEvent e) {
		try {
			System.out.println("[LOG] Jeu 'Puissance 4' quitté (Sortie)");
			p1.score = 0;

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/fxml/MultiLocal.fxml"));
			Parent root = loader.load();
			MultiLocal controller = (MultiLocal) loader.getController();
			controller.setStage(primaryStage);
			Scene scene = new Scene(root, 800, 400);
			primaryStage.setTitle(Reference.TITLE + "Deux joueurs (Local)");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}