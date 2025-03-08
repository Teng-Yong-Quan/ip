package yq.exceptions;

public class UnmarkIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The index of the task to be unmarked cannot be out of range.
                    Please enter a valid 'unmark' command.
                """;
    }
}
