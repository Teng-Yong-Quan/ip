package yq.exceptions;

public class MarkNumberFormatException extends YqException{
    @Override
    public String getMessage() {
        return """
                   A valid integer starting from 1 and nothing else must be present after the 'mark' word.
                   Please enter a valid 'mark' command.
               """;
    }
}
