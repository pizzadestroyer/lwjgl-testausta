package noob.gaming.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import lombok.Getter;

public class Window {

	@Getter
	private long windowHandle;
	
	private String windowTitle;
	
	@Getter
	private int width;
	
	@Getter
	private int height;

	public Window(String windowTitle, int width, int height) {
		this.windowTitle = windowTitle;
		this.width = width;
		this.height = height;
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        windowHandle = glfwCreateWindow(width, height, windowTitle, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
        
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
	}
	
	public void update() {
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
	}
	
	public void cleanup() {
        glfwDestroyWindow(windowHandle);	
		glfwTerminate();
        glfwSetErrorCallback(null).free();
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowHandle);
	}
	
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
	}
}
