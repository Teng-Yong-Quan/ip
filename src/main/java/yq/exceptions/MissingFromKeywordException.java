package yq.exceptions;

public class MissingFromKeywordException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The '/from' keyword cannot be missing.
                    Please enter a valid 'event' command.
                """;
    }
}
