package yq.exceptions;

public class InvalidTimeIntervalException extends YqException {
    @Override
    public String getMessage() {
        return """
                    Invalid time interval. The 'from' date & time must be earlier than the 'to' date & time.
                """;
    }
}
