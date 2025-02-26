package yq.exceptions;

public class NothingToMarkException extends EmptyListException{
    public String getMessage(){
        return super.getMessage() + " There is nothing to mark." + "\n";
    }
}
