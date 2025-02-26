package yq.commands;

import yq.exceptions.DuplicateTodoTaskException;
import yq.exceptions.MissingTodoDescriptionException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.tasks.Todo;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class TodoCommand extends Command {
    public TodoCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        autoExecute(taskArrayList, ui);
        storage.saveTaskArraylist(taskList,taskArrayList);
    }

    @Override
    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        String commandInput = getCommandInput();
        conductPrimaryTodoCheck(commandInput);
        Todo newTodo = new Todo(commandInput);
        checkDuplicateTodo(taskArrayList, newTodo);
        taskArrayList.add(newTodo);
        ui.printAddedTodoMessage(taskArrayList, newTodo);
    }

    private static void checkDuplicateTodo(ArrayList<Task> taskArrayList, Todo newTodo)
            throws YqException {
        for (Task task : taskArrayList) {
            if (task.equals(newTodo)) {
                throw new DuplicateTodoTaskException();
            }
        }
    }

    private static void conductPrimaryTodoCheck(String commandInput) throws YqException {
        if (commandInput.isEmpty()) {
            throw new MissingTodoDescriptionException();
        }
    }
}
