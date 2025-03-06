package yq.tasks;

public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        setBy(by);
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getBy() + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Deadline deadlineObject) {
            String objectDescription = deadlineObject.getDescription();
            String objectBy = deadlineObject.getBy();
            String description = getDescription();
            String by = getBy();
            return objectDescription.equalsIgnoreCase(description) &&
                    objectBy.equalsIgnoreCase(by);
        }
        return false;
    }
}
