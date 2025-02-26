package yq.exceptions;

public class InvalidCommandException extends YqException {
    public String userCommand;

    public InvalidCommandException(String lcUserCmd) {
        this.userCommand = lcUserCmd;
    }

    public String getMessage() {
        return "    Unknown option: " + userCommand + "\n" + "    Please enter a valid option." + "\n";
    }
}
