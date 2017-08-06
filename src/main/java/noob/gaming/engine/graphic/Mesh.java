package noob.gaming.engine.graphic;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import lombok.Getter;
import lombok.Setter;

public class Mesh {

	private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);
	
	@Getter
	private final int vaoId;
	
	private final List<Integer> vboIdList;
	
	@Getter
	private final int vertexCount;
	
	@Getter
	@Setter
	private Texture texture;
	
	@Getter
	@Setter
	private Vector3f colour;
	
	public Mesh(float[] positions, float[] textCoordinates, float[] normals, int[] indices) {   
		FloatBuffer positionBuffer = null;
		FloatBuffer textCoordinatesBuffer = null;
		FloatBuffer vecNormalsBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			colour = DEFAULT_COLOUR;
			vertexCount = indices.length;
			vboIdList = new ArrayList<>();
			
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			int vboId = glGenBuffers();
			vboIdList.add(vboId);
			positionBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			textCoordinatesBuffer = MemoryUtil.memAllocFloat(textCoordinates.length);
			textCoordinatesBuffer.put(textCoordinates).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordinatesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			vecNormalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
			
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (positionBuffer != null) {
				MemoryUtil.memFree(positionBuffer);
			}
			if (textCoordinatesBuffer != null) {
				MemoryUtil.memFree(textCoordinatesBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			if (vecNormalsBuffer != null) {
				MemoryUtil.memFree(vecNormalsBuffer);
			}
 		}
	}

	public boolean isTextured() {
		return this.texture != null;
	}
	
	public void render() {
		if (texture != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getId());	
		}
		
		glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void cleanup() {
		glDisableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (Integer id : vboIdList) {
			glDeleteBuffers(id);
		}
		
		if (texture != null) {
			texture.cleanup();
		}
		
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
