package exception;

/**
 * Created by Rodion on 06.05.2015.
 */
public class BruteForceException extends Exception {
    public BruteForceException(String message) {
        super(message);
    }

    public BruteForceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BruteForceException(Throwable cause) {
        super(cause);
    }
}
