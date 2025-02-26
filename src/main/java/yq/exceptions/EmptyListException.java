package yq.exceptions;

public abstract class EmptyListException extends YqException {
    @Override
    public String getMessage() {
        return "    The task list is empty.";
    }
}

