package clui;

/**
 * create clui thread and runs it.
 * @author animato
 *
 */
public class Main {
	public static void main(String[] args) {
		Thread t = new CLUIThread();
		t.start();
	}
}
