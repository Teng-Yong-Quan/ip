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

    /**
     * Checks whether the input object is a duplicate of the current Deadline task. If the input object has the same
     * deadline description and by description as those of the current Deadline task, the input object is considered to
     * be a duplicate task. The purpose is to prevent duplicate Deadline tasks in the taskArrayList.
     *
     * @param object Object to be compared.
     * @return True if the object is equivalent to the current Deadline task else false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Deadline deadlineObject) {
            String objectDescription = deadlineObject.getDescription();
            String objectBy = deadlineObject.getBy();
            String selfDescription = getDescription();
            String selfBy = getBy();
            return objectDescription.equalsIgnoreCase(selfDescription) &&
                    objectBy.equalsIgnoreCase(selfBy);
        }
        return false;
    }
}
