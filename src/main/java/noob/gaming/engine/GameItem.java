package noob.gaming.engine;

import org.joml.Vector3f;

import lombok.Getter;
import lombok.Setter;
import noob.gaming.engine.graphic.Mesh;

public class GameItem {
	
	@Getter
	private final Mesh mesh;
	
	@Getter
	private final Vector3f position;
	
	@Getter @Setter
	private float scale;
	
	@Getter
	private final Vector3f rotation;
	
	public GameItem(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
}

