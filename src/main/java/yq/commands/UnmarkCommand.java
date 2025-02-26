package yq.commands;

import yq.exceptions.MissingUnmarkNumberException;
import yq.exceptions.NothingToUnmarkException;
import yq.exceptions.UnmarkIndexOutOfBoundsException;
import yq.exceptions.UnmarkNumberFormatException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;


import java.util.ArrayList;

public class UnmarkCommand extends Command {
    public UnmarkCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        String commandInput = getCommandInput();
        unmarkTask(commandInput, taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    private static void unmarkTask(String substringOfUnmarkCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidUnmarkCmd(substringOfUnmarkCmd, taskArrayList);
        checkValidUnmarkNumber(substringOfUnmarkCmd, taskArrayList, ui);
    }

    private static void checkValidUnmarkCmd(String substringOfUnmarkCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToUnmarkException();
        } else if (substringOfUnmarkCmd.isEmpty()) {
            throw new MissingUnmarkNumberException();
        }
    }

    private static void checkValidUnmarkNumber(String unmarkNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            unmarkingTask(unmarkNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new UnmarkNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new UnmarkIndexOutOfBoundsException();
        }
    }

    private static void unmarkingTask(String unmarkNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenUnmarkIndex = Integer.parseInt(unmarkNumber);
        Task selectedTask = taskArrayList.get(chosenUnmarkIndex - LIST_INDEX_ADJUSTMENT);
        ui.printTaskUnmarked(selectedTask);
    }
}
