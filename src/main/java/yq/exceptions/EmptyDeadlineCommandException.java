package yq.exceptions;

public class EmptyDeadlineCommandException extends YqException {

    @Override
    public String getMessage() {
        return """
                   The substring of the 'deadline' command cannot be empty.
                   Please enter a valid 'deadline' command.
               """;
    }
}
