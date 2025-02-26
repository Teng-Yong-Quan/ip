package yq.exceptions;

public class MissingDeleteNumberException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The index of the task to be deleted cannot be missing from the 'delete' command.
                    Please enter a valid 'delete' command.
                """;
    }
}
