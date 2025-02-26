package yq.exceptions;

public class MissingByKeywordException extends YqException {
    public String getMessage() {
        return """
                    The '/by' keyword cannot be missing.
                    Please enter a valid 'deadline' command.
                """;
    }
}
