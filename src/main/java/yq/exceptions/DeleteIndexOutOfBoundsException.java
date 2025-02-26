package yq.exceptions;

public class DeleteIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The integer cannot be out of range.
                    Please enter a valid 'delete' command.
                """;
    }
}
