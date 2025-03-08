package yq.exceptions;

public class NothingToUnmarkException extends EmptyListException {
    public String getMessage() {
        return super.getMessage() + " There is nothing to unmark." + "\n";
    }
}
