package exception;

public class ExistingISBNException extends Exception {
    public ExistingISBNException(String isbn) {
        super("The ISBN '" + isbn + "' already exists in the system.");
    }
}