package yq.tasks;

public abstract class Task {
    protected String description; // Describe task
    protected boolean isDone; // Check if the task is marked as done or not done

    public Task(String description) {
        setDescription(description);
        setDone(false);
    }

    public String getStatusIcon() {
        return (isDone() ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void markAsDone() {
        if (!isDone()) {
            setDone(true);
            System.out.println("    Nice! I've marked this task as done:");
            System.out.println("        " + this);
        } else {
            System.out.println("    The task \"" + getDescription()
                    + "\" has already been marked as done.");
        }
    }

    public void markAsNotDone() {
        if (isDone()) {
            setDone(false);
            System.out.println("    OK, I've marked this task as not done yet:");
            System.out.println("        " + this);
        } else {
            System.out.println("    The task \"" + getDescription()
                    + "\" has already been marked as not done.");
        }
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    @Override
    public abstract boolean equals(Object obj);
}
