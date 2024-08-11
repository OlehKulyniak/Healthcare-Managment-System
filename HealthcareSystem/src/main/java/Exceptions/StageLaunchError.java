package exceptions;

/**
 * Клас винятку помилки запуску нового Stage. Виняток наслідує клас Exception.
 */
public class StageLaunchError extends Exception {
    public StageLaunchError() {}
    public StageLaunchError(String message) {
        super(message);
    }
}
