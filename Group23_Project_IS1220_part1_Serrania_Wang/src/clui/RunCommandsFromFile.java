package clui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunCommandsFromFile {

	public static void run(String filename, CLUIThread clui) {
		String message = "";
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(filename);
			reader = new BufferedReader(file);
			String line = "";
			while ((line = reader.readLine()) != null) { // read the file linebyline
				// pass line to CLUIThread.parseUserInput
				// ignores empty lines or lines starting with #
				if (line.length() > 0 && !line.substring(0, 1).equals("#")) {
					message = clui.parseUserInput(line);
					System.out.println(message);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
