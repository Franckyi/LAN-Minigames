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

public class Morpion {

	Stage primaryStage;
	int i, j, turn;

	Player p1 = Main.p;
	Player p2 = new Player("Invité", null);

	int[] cases;
	int win, nb, row, col;

	public void setStage(Stage mainStage) {
		primaryStage = mainStage;
	}

	@FXML
	public Label info, score1, score2, name1, name2;

	@FXML
	public VBox plateau;

	@FXML
	public GridPane grille;

	@FXML
	public Button reset;

	Background bg = new Background(new BackgroundImage(new Image("/img/morpion/blank.png", 200, 200, false, true),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
	Background bleu = new Background(new BackgroundImage(
			new Image("/img/morpion/croixBleue.png", 200, 200, false, true), BackgroundRepeat.REPEAT,
			BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
	Background rouge = new Background(
			new BackgroundImage(new Image("/img/morpion/rondRouge.png", 200, 200, false, true), BackgroundRepeat.REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

	@FXML
	void initialize() {
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
				b.setPrefSize(200, 200);
				grille.add(b, col, row);
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
		cases = new int[9];
		for (int i = 0; i < 9; i++) {
			cases[i] = 0;
			Button b = (Button) recupImg(i % 3, i / 3);
			b.setBackground(bg);
		}
		win = 0;
		nb = 0;
		if (turn == 1) {
			info.setText("Le joueur 1 (" + p1.name + ") commence !");
		} else {
			info.setText("Le joueur 2 (" + p2.name + ") commence !");
		}
		System.out.println("[LOG] Jeu 'Morpion' commencé");

	}

	public void clk(int b) {
		if (cases[b] == 0) {
			cases[b] = turn;
			Button bu = (Button) recupImg(b % 3, b / 3);
			if (turn == 1) {
				bu.setBackground(bleu);
				turn = 2;
			} else {
				bu.setBackground(rouge);
				turn = 1;
			}
			info.setText("Au tour du joueur " + turn + " !");
			nb++;
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

	public void detectWin() {
		for (i = 1; i <= 2; i++) {
			for (j = 0; j <= 6; j = j + 3) {
				if (cases[j] == i && cases[j + 1] == i && cases[j + 2] == i) {
					info.setText("Le joueur " + i + " a gagné !");
					win = i;
				}
			}
			for (j = 0; j <= 2; j++) {
				if (cases[j] == i && cases[j + 3] == i && cases[j + 6] == i) {
					info.setText("Le joueur " + i + " a gagné !");
					win = i;
				}
			}
			if ((cases[0] == i && cases[4] == i && cases[8] == i)
					|| (cases[2] == i && cases[4] == i && cases[6] == i)) {
				info.setText("Le joueur " + i + " a gagné !");
				win = i;
			}
		}
		if (nb == 9 && win == 0) {
			info.setText("Egalité !");
			win = 3;
		}
		if (win != 0) {
			if (win == 1) {
				p1.score++;
			}
			if (win == 2) {
				p2.score++;
			}
			disableButtons(true);
			reset.setDisable(false);
			System.out.println("[LOG] Jeu 'Morpion' arrêté (Victoire/Egalité)");
			score1.setText("" + p1.score);
			score2.setText("" + p2.score);
		}
	}

	public void exit(ActionEvent e) {
		try {
			System.out.println("[LOG] Jeu 'Morpion' quitté (Sortie)");
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

	public void disableButtons(boolean bool) {
		for (i = 0; i < 9; i++) {
			Button bu = (Button) recupImg(i % 3, i / 3);
			bu.setDisable(bool);
		}
	}

	public void reset(ActionEvent e) {
		disableButtons(false);
		set();
	}
}