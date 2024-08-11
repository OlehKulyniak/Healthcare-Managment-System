package exceptions;

/**
 * Клас винятку неправильних даних у файлі. Виняток наслідує клас Exception.
 */
public class InvalidFileData extends Exception {
    public InvalidFileData() {
        super("Incorrect file content.");
    }
    public InvalidFileData(String message) {
        super(message);
    }
}
