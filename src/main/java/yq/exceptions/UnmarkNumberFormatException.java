package yq.exceptions;

public class UnmarkNumberFormatException extends YqException {
    @Override
    public String getMessage() {
        return """
                    A valid integer starting from 1 and nothing else must be present after the 'unmark' word.
                    Please enter a valid 'unmark' command.
                """;
    }
}
