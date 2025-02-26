package yq.commands;

import yq.exceptions.DuplicateDeadlineTaskException;
import yq.exceptions.EmptyDeadlineCommandException;
import yq.exceptions.MissingByKeywordException;
import yq.exceptions.MissingDeadlineDescriptionException;
import yq.exceptions.YqException;
import yq.tasks.Deadline;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class DeadlineCommand extends Command {
    public DeadlineCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        autoExecute(taskArrayList,ui);
        storage.saveTaskArraylist(taskList,taskArrayList);
    }

    @Override
    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        final int START_INDEX_STRING_CMD = 0;
        String commandInput = getCommandInput();
        String lcCommandInput = commandInput.toLowerCase();
        conductPrimaryDlCheck(commandInput, lcCommandInput);

        int byIndex = lcCommandInput.indexOf("/by");
        String deadlineDescription = commandInput.substring(START_INDEX_STRING_CMD, byIndex).trim();

        int indexAfterByWord = byIndex + "/by".length();
        String by = commandInput.substring(indexAfterByWord).trim();

        checkEmptyDlInput(deadlineDescription, by);
        Deadline newDeadline = new Deadline(deadlineDescription, by);

        checkDuplicateDeadline(taskArrayList, newDeadline);
        taskArrayList.add(newDeadline);
        ui.printAddedDeadlineMessage(taskArrayList, newDeadline);
    }

    private static void checkEmptyDlInput(String deadlineDescription, String by) throws YqException {
        if (deadlineDescription.isEmpty() || by.isEmpty()) {
            throw new MissingDeadlineDescriptionException();
        }
    }

    private static void conductPrimaryDlCheck(String commandInput, String lcCommandInput) throws YqException {
        if (commandInput.isEmpty()) {
            throw new EmptyDeadlineCommandException();
        } else if (!lcCommandInput.contains("/by")) {
            throw new MissingByKeywordException();
        }
    }

    private static void checkDuplicateDeadline(ArrayList<Task> taskArrayList, Deadline newDeadline)
            throws YqException {
        for (Task task : taskArrayList) {
            if (task.equals(newDeadline)) {
                throw new DuplicateDeadlineTaskException();
            }
        }
    }
}
