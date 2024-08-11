package exceptions;

/**
 * Клас винятку помилки видалення пацієнта зі списку. Виняток наслідує клас Exception.
 */
public class PatientDeletionError extends Exception {
    public PatientDeletionError() {
        super("Patient was not selected for deletion. Try selecting the patient to delete.");
    }
    public PatientDeletionError(String message) {
        super(message);
    }

}
