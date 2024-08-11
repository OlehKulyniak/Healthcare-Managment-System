package exceptions;

/**
 * Клас винятку додавання пацієнта до списку. Виняток наслідує клас Exception.
 * */
public class PatientAddingError extends Exception {
    public PatientAddingError() {
        super("The patient was not added to the list.");
    }
    public PatientAddingError(String message) {
        super(message);
    }
}
