package yq.exceptions;

public class EmptyFindCommandException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The word(s) that you are looking for cannot be missing from the 'find' command.
                    Please enter a valid 'find' command, so the relevant tasks with the matching word(s)
                    can be extracted.
                """;
    }
}
