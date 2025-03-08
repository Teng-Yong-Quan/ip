package yq.exceptions;

public class DeleteIndexOutOfBoundsException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The index of the task to be deleted cannot be out of range.
                    Please enter a valid 'delete' command.
                """;
    }
}
