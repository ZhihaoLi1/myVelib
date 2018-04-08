package clui;

/**
 * Create clui thread and run it.
 * @author animato
 *
 */
public class Main {
	public static void main(String[] args) {
		Thread t = new CLUIThread();
		t.start();
	}
}
