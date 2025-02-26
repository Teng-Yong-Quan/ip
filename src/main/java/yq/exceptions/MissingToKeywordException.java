package yq.exceptions;

public class MissingToKeywordException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The '/to' keyword cannot be missing.
                    Please enter a valid 'event' command.
                """;
    }
}
