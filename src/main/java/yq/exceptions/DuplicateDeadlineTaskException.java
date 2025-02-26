package yq.exceptions;

public class DuplicateDeadlineTaskException extends YqException {
    public String getMessage() {
        return """
                    There is an existing Deadline task in the task arraylist.
                    Duplicate deadline task is not allowed.
                    Please enter a valid 'deadline' command.
                """;
    }
}
