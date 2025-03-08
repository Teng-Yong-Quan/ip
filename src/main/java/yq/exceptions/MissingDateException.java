package yq.exceptions;

public class MissingDateException extends YqException {
    @Override
    public String getMessage() {
        return """
                    Unable to detect any date. Please key in an appropriate date with the specified format.
                """;
    }
}
