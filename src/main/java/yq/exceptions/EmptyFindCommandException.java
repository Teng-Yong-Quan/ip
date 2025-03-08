package yq.exceptions;

public class EmptyFindCommandException extends YqException {
    @Override
    public String getMessage() {
        return """
                    Unable to detect any words that you want to search for. Please key in the contents you want to find
                    so the relevant tasks with the matching words can be extracted.
                """;
    }
}
