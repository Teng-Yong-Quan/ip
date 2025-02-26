package yq.exceptions;

public class DeleteNumberFormatException extends YqException {
    public String getMessage() {
        return """
                    A valid integer starting from 1 and nothing else must be present after the 'delete' word.
                    Please enter a valid 'delete' command.
                """;
    }
}
