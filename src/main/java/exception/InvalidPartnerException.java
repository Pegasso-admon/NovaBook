package exception;

public class InvalidPartnerException extends Exception {
    public InvalidPartnerException(int partnerId) {
        super("The partner with ID " + partnerId + " is inactive or doesn't exists.");
    }
}