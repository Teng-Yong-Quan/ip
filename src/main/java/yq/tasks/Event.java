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

    /**
     * Checks whether the input object is a duplicate of the current Event task. If the input object has the same
     * event description, from description and to description as those of the current Event task, the input object is
     * considered to be a duplicate task. The purpose is to prevent duplicate Event tasks in the taskArrayList.
     *
     * @param object Object to be compared.
     * @return True if the object is equivalent to the current Event task else false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Event eventObject) {
            String objectDescription = eventObject.getDescription();
            String objectFrom = eventObject.getFrom();
            String objectTo = eventObject.getTo();
            String selfDescription = getDescription();
            String selfFrom = getFrom();
            String selfTo = getTo();

            return objectFrom.equalsIgnoreCase(selfFrom) && objectTo.equalsIgnoreCase(selfTo)
                    && objectDescription.equalsIgnoreCase(selfDescription);
        }
        return false;
    }
}
