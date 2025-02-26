package yq.exceptions;

public class NothingToDeleteException extends EmptyListException{
    public String getMessage(){
        return super.getMessage() + " There is nothing to delete." + "\n";
    }
}
