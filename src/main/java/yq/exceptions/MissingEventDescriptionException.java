package yq.exceptions;

public class MissingEventDescriptionException extends YqException {
    public String getMessage() {
        return """
                    The event, '/from' and '/to' descriptions cannot be missing.
                    Please enter a valid 'event' command.
                """;
    }
}
