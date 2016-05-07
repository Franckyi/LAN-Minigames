package obj;

import java.io.BufferedReader;
import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.ServerWindow;

public class ListenStringService2 extends Service<String> {

	Server server = ServerWindow.server;

	BufferedReader netIn2;
	String msg2;

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {

			@Override
			protected String call() throws Exception {
				try {
					netIn2 = server.p2.netIn;
					msg2 = netIn2.readLine();
				} catch (IOException e) {
//					e.printStackTrace();
				}
				System.out.println("[LOG] Message du joueur 2 : " + msg2);
				return msg2;
			}

		};
	}
}