package noob.gaming.game;

import noob.gaming.engine.GameEngine;
import noob.gaming.engine.IGameLogic;

public class Main {

	public static void main(String... args) {
		try {
			IGameLogic gameLogic = new NoobGame();
			GameEngine gameEngine = new GameEngine("NOOB GAME", 600, 480, gameLogic);
			gameEngine.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
