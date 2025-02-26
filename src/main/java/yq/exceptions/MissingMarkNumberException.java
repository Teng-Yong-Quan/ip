package yq.exceptions;

public class MissingMarkNumberException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The index of the task to be marked cannot be missing from the 'mark' command.
                    Please enter a valid 'mark' command.
                """;
    }
}
