package yq.exceptions;

/**
 * Ignores the invalid task line that is being retrieved from the input file when
 * it does not follow the todo, deadline or event format.
 */
public class InvalidTaskLineException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid line is detected and it will be ignored.
                """;
    }
}
