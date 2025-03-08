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
                    If the tasks with '%s' are actually present in the list, please key in only the
                    relevant words that you are searching for with the correct spelling in uppercase or lowercase when
                    necessary as it is case-sensitive.
                """.formatted(commandInput, commandInput);
    }
}
