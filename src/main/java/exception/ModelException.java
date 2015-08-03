package exception;

import system.Executor;

/**
 * Created by Rodion on 27.05.2015.
 */
public class ModelException extends Exception {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }
}
