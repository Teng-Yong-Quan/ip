package yq.exceptions;

public class EmptyEventCommandException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The substring of the 'event' command cannot be empty.
                    Please enter a valid 'event' command.
                """;
    }
}
