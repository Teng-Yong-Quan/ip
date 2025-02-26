package yq.exceptions;

public class MarkIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The integer cannot be out of range.
                    Please enter a valid 'mark' command.
                """;
    }
}
