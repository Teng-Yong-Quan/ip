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

    /**
     * Check the find command input is valid and process it. It can only be processed if both the
     * taskArrayList and the command input are not empty. An array list of tasks containing the matching words will be
     * printed by the User Interface. If there are no such tasks, it will notify the user.
     *
     * @param taskList The object which contains the taskArrayList.
     * @param ui       User Interface.
     * @param storage  The object which handles the transferring and storing of tasks between the file and the program.
     * @throws YqException If the find command input violates any of the checks.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        ArrayList<Task> matchingTaskArrayList = new ArrayList<>();
        String commandInput = getCommandInput();
        if (taskArrayList.isEmpty()) {
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
            String lcTaskDescription = task.getDescription().toLowerCase();
            String lcCommandInput = commandInput.toLowerCase();
            if (lcTaskDescription.contains(lcCommandInput)) {
                matchingTaskArrayList.add(task);
            }
        }
    }

    private void printMatchingTask(ArrayList<Task> matchingTaskArrayList, Ui ui) {
        ui.printMatchingTaskList(matchingTaskArrayList);
    }
}
