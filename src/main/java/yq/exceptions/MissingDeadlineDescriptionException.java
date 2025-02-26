package yq.exceptions;

public class MissingDeadlineDescriptionException extends YqException {
    public String getMessage() {
        return """
                    The deadline and '/by' descriptions cannot be missing.
                    Please enter a valid 'deadline' command.
                """;
    }
}
