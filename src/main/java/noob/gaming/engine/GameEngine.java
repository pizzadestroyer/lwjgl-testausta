package noob.gaming.engine;

public class GameEngine implements Runnable {
	
	private final Window window;
	
	private final Thread gameLoopThread;
	
	private final IGameLogic gameLogic;
	
	private final MouseInput mouseInput;
	
	public GameEngine(String windowTitle, int width, int height, IGameLogic gameLogic) throws Exception {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Window(windowTitle, width, height);
		mouseInput = new MouseInput();
		this.gameLogic = gameLogic;
	}

	public void start() {
		gameLoopThread.start();
	}
	
	public void run() {
		try {
			init();
			loop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void init() throws Exception {
		window.init();
		mouseInput.init(window);
		gameLogic.init(window);
	}

	private void loop() throws InterruptedException {
		long msPerFrame = 20;

		while (!window.shouldClose()) {
			long now = System.currentTimeMillis();
			
			input();
			update(1);
			render();

			long sleep = now + msPerFrame - System.currentTimeMillis();
			if (sleep > 0) {
				Thread.sleep(sleep);
			}
		}
	}
	
	protected void input() {
		mouseInput.input(window);
		gameLogic.input(window, mouseInput);
    }
	
	 protected void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }
	
	protected void cleanup() {
        gameLogic.cleanup();                
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}