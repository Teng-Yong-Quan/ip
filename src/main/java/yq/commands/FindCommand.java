package yq.commands;

import yq.exceptions.EmptyFindCommandException;
import yq.exceptions.NoMatchingContentException;
import yq.exceptions.NothingToFindException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class FindCommand extends Command {
    public FindCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        ArrayList<Task> matchingTaskArrayList = new ArrayList<>();
        String commandInput = getCommandInput();
        if(taskArrayList.isEmpty()){
            throw new NothingToFindException();
        }
        if (commandInput.isEmpty()) {
            throw new EmptyFindCommandException();
        }
        addMatchingTasks(taskArrayList, commandInput, matchingTaskArrayList);
        storage.saveTaskArraylist(taskList, taskArrayList);
        if (matchingTaskArrayList.isEmpty()) {
            throw new NoMatchingContentException(commandInput);
        }
        printMatchingTask(matchingTaskArrayList, ui);
    }

    private static void addMatchingTasks(ArrayList<Task> taskArrayList, String commandInput, ArrayList<Task> matchingTaskArrayList) {
        for (Task task : taskArrayList) {
            String taskDescription = task.getDescription();
            if (taskDescription.contains(commandInput)) {
                matchingTaskArrayList.add(task);
            }
        }
    }

    private void printMatchingTask(ArrayList<Task> matchingTaskArrayList, Ui ui) {
        ui.printMatchingTaskList(matchingTaskArrayList);
    }
}
