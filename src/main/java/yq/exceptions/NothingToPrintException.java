package yq.exceptions;

public class NothingToPrintException extends EmptyListException{
    public String getMessage(){
        return super.getMessage() + " There is nothing to print." + "\n";
    }
}
