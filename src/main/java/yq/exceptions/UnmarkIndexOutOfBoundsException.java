package yq.exceptions;

public class UnmarkIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The integer cannot be out of range.
                    Please enter a valid 'unmark' command.
                """;
    }
}
