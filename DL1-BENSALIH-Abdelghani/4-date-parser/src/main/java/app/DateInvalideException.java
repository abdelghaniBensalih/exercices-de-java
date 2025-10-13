package app;

/**
 * Exception lancée quand une date est invalide.
 * Auteur: abdel
 */

public class DateInvalideException extends Exception {
    public DateInvalideException(String message) {
        super(message);
    }

    public DateInvalideException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateInvalideException(Throwable cause) {
        super(cause);
    }
}
