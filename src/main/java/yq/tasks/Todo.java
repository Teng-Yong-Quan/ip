package yq.tasks;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Checks whether the input object is a duplicate of the current Todo task. If the input object has the same
     * todo description as that of the current Todo task, the input object is considered to be a duplicate task. The
     * purpose is to prevent duplicate Todo tasks in the taskArrayList.
     *
     * @param object Object to be compared.
     * @return True if the object is equivalent to the current Todo task else false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Todo todoObject) {
            String objectDescription = todoObject.getDescription();
            String selfDescription = getDescription();
            return objectDescription.equalsIgnoreCase(selfDescription);
        }
        return false;
    }
}
