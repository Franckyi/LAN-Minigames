package obj;

public class Server {

	public int playerCount;
	public Player p1;
	public Player p2;
	public String ip;
	public String name;
	public String game;
	public boolean isStarted;
	public boolean isFull;
	public boolean isGameStarted;
	public Object info;
	public int timer;

	public Server(String ip, String name, String game) {
		playerCount = 0;
		p1 = null;
		p2 = null;
		this.ip = ip;
		this.name = name;
		this.game = game;
		isStarted = false;
		isFull = false;
		isGameStarted = false;
		timer = 0;
	}

	public void addPlayer(Player p) {
		if (!isFull) {
			if (p1 == null) {
				p1 = p;
				playerCount++;
			} else {
				p2 = p;
				playerCount++;
			}
		}
		if (playerCount == 2) {
			isFull = true;
			isStarted = true;
			startGame();
		}
	}

	public void startServer() {
		isStarted = true;
	}

	public void startGame() {
		if (game.equals("Morpion")) {

		}
		isGameStarted = true;
	}

	public int getPlayerId(Player p) {
		if (p.equals(p1))
			return 1;
		else if (p.equals(p2))
			return 2;
		else
			return 0;
	}

	public void removePlayer(Player p) {
		if (p.equals(p1)) {
			p1 = null;
			playerCount--;
		}
		if (p.equals(p2)) {
			p2 = null;
			playerCount--;
		}
	}
	
	public void setInfo(Object info){
		this.info = info;
	}
}
