package yq.exceptions;

public class DeleteNumberFormatException extends YqException {
    public String getMessage() {
        return """
                    A valid integer, which represents the index of the task to be deleted, must be 1 or above
                    and nothing else must be present after the 'delete' word.
                    Please enter a valid 'delete' command.
                """;
    }
}
