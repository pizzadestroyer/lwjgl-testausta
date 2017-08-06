package noob.gaming.engine.graphic;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lombok.Getter;
import noob.gaming.util.FileUtil;

public class OBJLoader {

	public static Mesh loadMesh(String fileName) throws Exception {
		List<String> lines = FileUtil.readAllLines(fileName);
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();
		
		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			String token = tokens[0];
			
			if ("v".equals(token)) {
				Vector3f vec3f = new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				vertices.add(vec3f);
			} else if ("vt".equals(token)) {
				Vector2f vec2f = new Vector2f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]));
				textures.add(vec2f);
			} else if ("vn".equals(token)) {
				Vector3f vec3fNorm = new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				normals.add(vec3fNorm);
			} else if ("f".equals(token)) {
				Face face = new Face(tokens[1], tokens[2], tokens[3]);
				faces.add(face);
			}
		}
		return reorderLists(vertices, textures, normals, faces);
	}
	
	private static Mesh reorderLists(List<Vector3f> posList, 
			List<Vector2f> textCoordList, List<Vector3f> normList, List<Face> faceList) {
		List<Integer> indices = new ArrayList<>();
		float[] posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}
		float[] textCoordArr = new float[posList.size() * 2];
		float[] normArr = new float[posList.size() * 3];
		
		for (Face face : faceList) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IdxGroup indValue : faceVertexIndices) {
				processFaceVertex(indValue, textCoordList, normList, 
						indices, textCoordArr, normArr);
			}
		}
		int[] indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		Mesh mesh = new Mesh(posArr, textCoordArr, normArr, indicesArr);
		return mesh;
	}
	
	private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList, 
			List<Vector3f> normList, List<Integer> indicesList, 
			float[] texCoordArr, float[] normArr) {
		int posIndex = indices.idxPos;
		indicesList.add(posIndex);
		
		if (indices.idxTextCoord >= 0) {
			Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
		
		if (indices.idxVecNormal >= 0) {
			Vector3f vecNorm = normList.get(indices.idxVecNormal);
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}
	
	protected static class IdxGroup {

		public static final int NO_VALUE = -1;
		
		public int idxPos;
		
		public int idxTextCoord;
		
		public int idxVecNormal;
		
		public IdxGroup() {
			idxPos = NO_VALUE;
			idxTextCoord = NO_VALUE;
			idxVecNormal = NO_VALUE;
		}
	}
	
	protected static class Face {
		
		@Getter
		private IdxGroup[] faceVertexIndices = new IdxGroup[3];
		
		public Face(String v1, String v2, String v3) {
			faceVertexIndices = new IdxGroup[3];
			faceVertexIndices[0] = parseLine(v1);
			faceVertexIndices[1] = parseLine(v2);
			faceVertexIndices[2] = parseLine(v3);
		}
		
		private IdxGroup parseLine(String line) {
			IdxGroup idxGroup = new IdxGroup();
			
			String[] lineTokens = line.split("/");
			int length = lineTokens.length;
			idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
			if (length > 1) {
				String textCoord = lineTokens[1];
				idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
				
				if (length > 2) {
					idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
				}
			}
			
			return idxGroup;
		}
	}
}
  

