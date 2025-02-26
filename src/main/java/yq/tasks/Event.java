package yq.tasks;

public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        setFrom(from);
        setTo(to);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + getFrom() + " to: " + getTo() + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Event eventObject) {
            String eventDescription = eventObject.getDescription();
            String eventFrom = eventObject.getFrom();
            String eventTo = eventObject.getTo();
            return eventFrom.equalsIgnoreCase(getFrom()) && eventTo.equalsIgnoreCase(getTo())
                    && eventDescription.equalsIgnoreCase(getDescription());
        }
        return false;
    }
}
