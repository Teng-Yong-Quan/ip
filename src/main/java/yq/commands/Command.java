package yq.commands;

import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.TaskList;
import yq.ui.Ui;
import yq.ui.Storage;

import java.util.ArrayList;

public abstract class Command {
    protected String commandInput;
    protected Boolean isExit; // Signal the program to stop running when it is true

    public String getCommandInput() {
        return this.commandInput;
    }

    public void setCommandInput(String commandInput) {
        this.commandInput = commandInput;
    }

    public Boolean isExit() {
        return this.isExit;
    }

    public void setExit(Boolean isExit) {
        this.isExit = isExit;
    }

    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws YqException;

    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
    }

}
