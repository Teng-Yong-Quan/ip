package yq.exceptions;

public class DuplicateTodoTaskException extends YqException{
    @Override
    public String getMessage() {
        return """
                    There is an existing Todo task in the task arraylist.
                    Duplicate Todo task is not allowed.
                    Please enter a valid 'todo' command.
                """;
    }
}
