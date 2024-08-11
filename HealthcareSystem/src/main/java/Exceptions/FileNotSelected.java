package exceptions;

/**
 * Клас винятку невибраного файлу. Виняток наслідує клас Exception.
 */
public class FileNotSelected extends Exception {
    public FileNotSelected() {
        super("File was not selected.");
    }
    public FileNotSelected(String message) {
        super(message);
    }
}
