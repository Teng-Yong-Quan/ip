package yq.exceptions;

public class InvalidDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    An invalid date is detected. Please enter a valid date.
                """;
    }
}
