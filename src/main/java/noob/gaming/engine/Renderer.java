package noob.gaming.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.joml.Matrix4f;

import noob.gaming.engine.graphic.Camera;
import noob.gaming.engine.graphic.Mesh;
import noob.gaming.engine.graphic.ShaderProgram;
import noob.gaming.engine.graphic.Transformation;
import noob.gaming.util.FileUtil;

public class Renderer {

	private Transformation transformation;
	private ShaderProgram shaderProgram;
	
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.0f;
	
	public Renderer() {
		transformation = new Transformation();
	}
	
	public void init(Window window) throws Exception {
		shaderProgram = new ShaderProgram();        
        shaderProgram.createVertexShader(FileUtil.getShaderCode("vertex.vs"));
        shaderProgram.createFragmentShader(FileUtil.getShaderCode("fragment.fs"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");
	}
	
	public void render(Window window, Camera camera, GameItem[] gameItems) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        shaderProgram.bind();
        
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        
        shaderProgram.setUniform("texture_sampler", 0);
        
        for (GameItem gameItem : gameItems) { 	
        	Mesh mesh = gameItem.getMesh();
        	Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
        	shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        	shaderProgram.setUniform("colour", mesh.getColour());
        	shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
        	mesh.render();
        }
        shaderProgram.unbind();
	}
	
	public void cleanup() {   
        if (shaderProgram != null) {
        	shaderProgram.cleanup();
        }
	}
}
