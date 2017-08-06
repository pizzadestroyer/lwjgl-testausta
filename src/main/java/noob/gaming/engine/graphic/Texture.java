package noob.gaming.engine.graphic;

import lombok.Getter;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {

	@Getter
	private final int id;
	
	public Texture(String fileName) throws Exception {
		this(loadTexture(fileName));
	}
	
	public Texture(int id) {
		this.id = id;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	private static int loadTexture(String fileName) throws Exception {
		PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(
				4 * decoder.getWidth() * decoder.getHeight());
		decoder.decode(buffer, decoder.getWidth()*4, Format.RGBA);
		buffer.flip();
		
		int textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, 
				GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return textureId;		
	}
	
	public void cleanup() {
		glDeleteTextures(id);
	}
}
