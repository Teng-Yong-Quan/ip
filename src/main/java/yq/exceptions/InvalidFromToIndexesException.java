package yq.exceptions;

public class InvalidFromToIndexesException extends YqException {
    public String getMessage() {
        return """
                    The '/from' keyword cannot be inputted after the '/to' keyword.
                    Please enter a valid 'event' command.
                """;
    }
}
