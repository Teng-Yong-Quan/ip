package yq.exceptions;

public class MarkIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The index of the task to be marked cannot be out of range.
                    Please enter a valid 'mark' command.
                """;
    }
}
