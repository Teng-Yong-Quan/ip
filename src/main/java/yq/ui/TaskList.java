package yq.ui;

import yq.tasks.Task;

import java.util.ArrayList;

public class TaskList {
    ArrayList<Task> taskArrayList;

    @SafeVarargs
    TaskList(ArrayList<Task>... inputTaskArrayList) {
        int zerothIndex = 0;
        int zeroLength = 0;
        if (inputTaskArrayList.length == zeroLength) {
            setTaskArrayList(new ArrayList<>());
            return;
        }
        ArrayList<Task> selectedTaskArrayList = inputTaskArrayList[zerothIndex];
        setTaskArrayList(selectedTaskArrayList);
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public boolean isEmpty() {
        return taskArrayList.isEmpty();
    }

    public int size() {
        return taskArrayList.size();
    }
}
