package yq.exceptions;

public class MarkNumberFormatException extends YqException{
    @Override
    public String getMessage() {
        return """
                   A valid integer, which represents the index of the task to be marked, must be 1 or above and nothing
                   else must be present after the 'mark' word.
                   Please enter a valid 'mark' command.
               """;
    }
}
