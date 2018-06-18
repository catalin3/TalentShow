package talentshow.service;

/**
 * Created by Dana on 15-Jun-17.
 */
public class ConfException extends Exception {
    public ConfException() {
        super();
    }

    public ConfException(String message) {
        super(message);
    }

    public ConfException(String message, Throwable cause) {
        super(message, cause);
    }
}
