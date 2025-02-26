package yq.commands;

import yq.exceptions.NothingToPrintException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;


public class ListCommand extends Command {
    public ListCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        checkEmptyTaskArrayList(taskArrayList);
        ui.printList(taskArrayList);
        storage.saveTaskArraylist(taskList,taskArrayList);
    }

    private static void checkEmptyTaskArrayList(ArrayList<Task> taskArrayList) throws YqException{
        if (taskArrayList.isEmpty()) {
            throw new NothingToPrintException();
        }
    }

}
