package noob.gaming.engine;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;
import org.joml.Vector2f;

import lombok.Getter;

public class MouseInput {

	private final Vector2d previousPosition;
	
	private final Vector2d currentPosition;
	
	@Getter
	private final Vector2f displacementVector;
	
	private boolean inWindow = false;
	
	@Getter
	private boolean isLeftButtonPressed = false;
	
	@Getter
	private boolean isRightButtonPressed = false;
	
	public MouseInput() {
		previousPosition = new Vector2d(-1, -1);
		currentPosition = new Vector2d(0, 0);
		displacementVector = new Vector2f();
	}
	
	public void init(Window window) {
		glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xPosition, yPosition) -> {
			currentPosition.x = xPosition;
			currentPosition.y = yPosition;
		});
		glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
			inWindow = entered;
		});
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			isLeftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			isRightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		});
	}
	
	public void input(Window window) {
		displacementVector.x = 0;
		displacementVector.y = 0;
		if (previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
			double deltax = currentPosition.x - previousPosition.x;
			double deltay = currentPosition.y - previousPosition.y;
			boolean rotateX = deltax != 0;
			boolean rotateY = deltay != 0;
			if (rotateX) {
				displacementVector.y = (float) deltax;
			}
			if (rotateY) {
				displacementVector.x = (float) deltay;
			}
		}
		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}
}
