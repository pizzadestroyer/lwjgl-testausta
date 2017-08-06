package noob.gaming.game;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;

import noob.gaming.engine.GameItem;
import noob.gaming.engine.IGameLogic;
import noob.gaming.engine.MouseInput;
import noob.gaming.engine.Renderer;
import noob.gaming.engine.Window;
import noob.gaming.engine.graphic.Camera;
import noob.gaming.engine.graphic.Mesh;
import noob.gaming.engine.graphic.OBJLoader;
import noob.gaming.engine.graphic.Texture;

public class NoobGame implements IGameLogic {

	private static final float MOUSE_SENSITIVITY = 0.2f;
	private static final float CAMERA_POS_STEP = 0.05f;
	
	private final Vector3f cameraInc;

	private final Camera camera;

	private final Renderer renderer;
	
	private GameItem[] gameItems;
	
	public NoobGame() {
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
	}
	
	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		
		Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
		Texture texture = new Texture("/textures/cube_texture.png");
		mesh.setTexture(texture);

		GameItem gameItem = new GameItem(mesh);
		gameItem.setPosition(0, 0, -8);

		GameItem gameItem2 = new GameItem(mesh);
		gameItem2.setPosition(4, 0, -8);

		GameItem gameItem3 = new GameItem(mesh);
		gameItem3.setPosition(-4, 0, -8);

		gameItems = new GameItem[] { gameItem, gameItem2, gameItem3 };
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
        	cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
        	cameraInc.x = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
        	cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
        	cameraInc.y = 1;
        }
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		for (GameItem gameItem : gameItems) {
			float rotation = gameItem.getRotation().x + 1.5f;
			if (rotation > 360) {
				rotation = 0;
			}
			gameItem.setRotation(rotation, rotation, rotation);
		}
		
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, 
				cameraInc.y * CAMERA_POS_STEP, 
				cameraInc.z * CAMERA_POS_STEP);
		
		if (mouseInput.isRightButtonPressed()) {
			Vector2f rotationVector = mouseInput.getDisplacementVector();
			camera.moveRotation(rotationVector.x * MOUSE_SENSITIVITY, 
					rotationVector.y * MOUSE_SENSITIVITY, 0);
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanup();
		}
	}

}
