package noob.gaming.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {
	
	public static String getShaderCode(String fileName) {
		StringBuilder sb = new StringBuilder();
		
		Path res = Paths.get("C:/Users/kahvinkeitin/workspace/client/src/main/resources/shaders/" + fileName);
		
		try (Stream<String> stream = Files.lines(res)) {	
			stream.forEach((line) -> {
				sb.append(line);
				sb.append("\r");
			});
		} catch (IOException e) {
			e.printStackTrace();
		} 	
		
		return sb.toString();
	}
	
	public static List<String> readAllLines(String fileName) throws Exception {
		List<String> list = new ArrayList<String>();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(FileUtil.class.getClass().getResourceAsStream(fileName)))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
		}
		return list;
	}
}
