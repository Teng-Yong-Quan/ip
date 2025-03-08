package yq.exceptions;

public class UnmarkNumberFormatException extends YqException {
    @Override
    public String getMessage() {
        return """
                    A valid integer, which represents the index of the task to be unmarked, must be 1 or above and
                    nothing else must be present after the 'unmark' word.
                    Please enter a valid 'unmark' command.
                """;
    }
}
