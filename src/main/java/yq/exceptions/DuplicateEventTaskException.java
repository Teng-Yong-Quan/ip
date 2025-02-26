package yq.exceptions;

public class DuplicateEventTaskException extends YqException{

    @Override
    public String getMessage() {
        return """
                    There is an existing Event task in the task arraylist.
                    Duplicate Event task is not allowed.
                    Please enter a valid 'event' command.
                """;
    }
}
