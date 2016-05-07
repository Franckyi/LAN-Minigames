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
		info = null;
	}

	public void addPlayer(Player p) {
		if (!isFull) {
			if (p1 == null) {
				p1 = p;
				playerCount++;
			} else if (p2 == null) {
				p2 = p;
				playerCount++;
			}
		}
		if (playerCount == 2) {
			isFull = true;
			isStarted = true;
			startGame();
			isGameStarted = true;
		}
	}

	public boolean isPlayer(int i) {
		if (i == 1) {
			if (this.equals(p1))
				return true;
			else
				return false;
		} else if (i == 2) {
			if (this.equals(p2))
				return true;
			else
				return false;
		} else
			return false;
	}

	public void removePlayer() {
		if (this.isPlayer(1)) {
			p1 = null;
			isFull = false;
			playerCount--;
		}
	}

	public void startGame() {
		if (game.equals("Morpion")) {

		}
		isGameStarted = true;
	}

	public int getPlayerId(Player p) {
		if (p.name.equals(p1.name) && p.ip.equals(p1.ip))
			return 1;
		else if (p.name.equals(p2.name) && p.ip.equals(p2.ip))
			return 2;
		else
			return 0;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
}