package yq.exceptions;

/**
 * Ignores the task line that is being retrieved from the input file when it
 * is missing a date.
 */
public class MissingFormattedDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    Unable to detect any date. The task will not be created.
                """;
    }
}
