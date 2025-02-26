package yq.exceptions;

public class MissingUnmarkNumberException extends YqException {
    public String getMessage(){
        return """
                    The index of the task to be unmarked cannot be missing from the 'unmark' command.
                    Please enter a valid 'unmark' command.
                """;
    }
}
