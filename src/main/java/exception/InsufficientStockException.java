package exception;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String isbn, int available) {
        super("Insufficient stock for ISBN '" + isbn + "'. Available: " + available);
    }
}