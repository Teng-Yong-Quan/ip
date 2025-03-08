package yq.exceptions;

public class InvalidFormattedDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid date is detected. The task will not be created.
                """;
    }
}
