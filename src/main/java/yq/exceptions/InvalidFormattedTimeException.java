package yq.exceptions;

public class InvalidFormattedTimeException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid time is detected. The task will not be created.
                """;
    }
}
