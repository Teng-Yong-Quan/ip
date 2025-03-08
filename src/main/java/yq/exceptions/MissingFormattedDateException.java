package yq.exceptions;

public class MissingFormattedDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    Unable to detect any date. The task will not be created.
                """;
    }
}
