package yq.exceptions;

public class NothingToFindException extends EmptyListException{

    public String getMessage() {
        return super.getMessage() + " There is nothing to find.";
    }
}
