package exception;

// Simple custom exception to manage business rule errors.
public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }
}