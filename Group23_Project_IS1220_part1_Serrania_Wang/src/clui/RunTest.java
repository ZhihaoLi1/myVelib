package clui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunTest {

	public static void main(String[] args) {
		String fileName = args[0];
		String message = "";
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			String line = "";
			CLUIThread clui = new CLUIThread();
			while ((line = reader.readLine()) != null) { // read the file linebyline
				// pass line to CLUIThread.parseUserInput
				if (!line.substring(0, 1).equals("#")) {
					message = clui.parseUserInput(line);
					System.out.println(message);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {reader.close();}
				catch (IOException e) {}
				if (file != null) {
					try {file.close();}
					catch (IOException e) {}
				}
			}
		}
	}
}
