package exceptions;

public class OwnershipMismatchException extends RuntimeException {
    public OwnershipMismatchException(String message) {
        super(message);
    }
}
