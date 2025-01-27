public class Task {
    /** Description of the task */
    protected String description;
    /** Indicator to check if the task is marked as done or not done */
    protected boolean isDone;

    public Task(String description) {
        setDescription(description);
        setIsDone(false);
    }

    public String getStatusIcon() {
        return (getIsDone() ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Marks the selected task as done.
     */
    public void markAsDone() {
        if (!getIsDone()) {
            setIsDone(true);
            System.out.println("    Nice! I've marked this task as done:");
            System.out.println("        [" + getStatusIcon() + "] "
                    + getDescription());
        } else {
            System.out.println("    The task \"" + getDescription()
                    + "\" has already been marked as done.");
        }
    }

    /**
     * Marks the selected task as not done.
     */
    public void markAsNotDone() {
        if (getIsDone()) {
            setIsDone(false);
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("        [" + getStatusIcon() + "] "
                    + getDescription());
        } else {
            System.out.println("    The task \"" + getDescription()
                    + "\" has already been marked as not done.");
        }
    }
}
