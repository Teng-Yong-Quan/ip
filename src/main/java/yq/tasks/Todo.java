package yq.tasks;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Todo todoObject) {
            String objectDescription = todoObject.getDescription();
            String description = getDescription();
            return objectDescription.equalsIgnoreCase(description);
        }
        return false;
    }
}
