package yq.exceptions;

/**
 * Ignores the task line that is being retrieved from the input file when it
 * contains an invalid time.
 */
public class InvalidFormattedTimeException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid time is detected. The task will not be created.
                """;
    }
}
