package yq.exceptions;

public class InvalidTimeException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid time is detected. Please enter a valid time.
                """;
    }
}
