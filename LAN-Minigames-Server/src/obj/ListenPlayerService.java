package obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.Reference;
import main.ServerWindow;

public class ListenPlayerService extends Service<Player> {

	Player p;

	ServerSocket listenSocket;
	Socket commSocket;
	BufferedReader netIn;
	PrintWriter netOut;
	String[] msg;
	Server server = ServerWindow.server;

	@Override
	protected Task<Player> createTask() {
		return new Task<Player>() {

			@Override
			protected Player call() throws Exception {
				try {
					listenSocket = new ServerSocket(Reference.SOCKET_1);
					commSocket = listenSocket.accept();
					listenSocket.close();
					// Flux lecture et écriture
					netIn = new BufferedReader(new InputStreamReader(commSocket.getInputStream()));
					netOut = new PrintWriter(commSocket.getOutputStream(), true);

					if (!server.isFull) {
						msg = netIn.readLine().split("-"); // reçois le joueur
//						System.out.println(msg[0]);
						if (msg[0].equals("remove")) {
							System.out.println("[LOG] Le joueur '" + msg[1] + "' s'est déconnecté du serveur");
							if (server.p1.name.equals(msg[1])) {
								server.removePlayer(server.p1);
								return new Player("removed", "1");
							}
							if (server.p2.name.equals(msg[1])) {
								server.removePlayer(server.p2);
								return new Player("removed", "2");
							}
						}
						if (msg[0].equals("add")) {
							netOut.println("server-" + server.name + "-" + server.game); // envoie le serveur
							Thread.sleep(100);
							p = new Player(msg[1], msg[2], netIn, netOut);
							server.addPlayer(p);
							if (!server.isFull) {
								netOut.println("wait");
								System.out.println("[LOG] Joueur '" + p.name + "' (" + p.ip + ") s'est connecté au serveur et attend un autre joueur");
							} else {
								System.out.println("[LOG] Joueur '" + p.name + "' (" + p.ip + ") s'est connecté au serveur");
							}
						}
					}
					else{
						netOut.println("full");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return p;
			}
		};
	}
}
