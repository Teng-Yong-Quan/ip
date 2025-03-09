package yq.exceptions;

public class NoMatchingContentException extends YqException {
    String commandInput;

    public NoMatchingContentException(String commandInput) {
        this.commandInput = commandInput;
    }

    @Override
    public String getMessage() {
        return """
                    Unable to find tasks with '%s' in the task descriptions.
                    If it is due to incorrect spelling, please key in only the relevant words that
                    you are searching for with the correct spelling (case-insensitive).
                """.formatted(commandInput);
    }
}
