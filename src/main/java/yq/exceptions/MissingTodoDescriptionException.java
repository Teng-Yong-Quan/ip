package yq.exceptions;

public class MissingTodoDescriptionException extends YqException {
    @Override
    public String getMessage() {
        return """
                    The todo description cannot be missing.
                    Please enter a valid 'todo' command.
                """;
    }
}
