package Exceptions;

/**
 * Клас винятку пустого списку. Виняток наслідує клас Exception.
 */
public class EmptyListError extends Exception {
    public EmptyListError() {
        super("The patient list is empty. Try getting the patient list from the file or adding the new patient.");
    }
    public EmptyListError(String message) {
        super(message);
    }

}
