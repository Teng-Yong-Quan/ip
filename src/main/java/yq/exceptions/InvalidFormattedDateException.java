package yq.exceptions;

/**
 * Ignores the task line that is being retrieved from the input file when it
 * contains an invalid date.
 */
public class InvalidFormattedDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid date is detected. The task will not be created.
                """;
    }
}
