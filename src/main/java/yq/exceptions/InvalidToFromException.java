package yq.exceptions;

public class InvalidToFromException extends YqException {
    public String getMessage() {
        return """
                    The '/to' keyword cannot be inputted before the '/from' keyword.
                    Please enter a valid 'event' command.
                """;
    }
}
