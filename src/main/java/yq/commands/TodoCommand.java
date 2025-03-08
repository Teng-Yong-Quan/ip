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
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    /**
     * Conduct various checks to ensure that the todo command input is valid and can be processed into a Todo
     * Task. After that, it is then checked for any duplicate Todo task that has already been present in the
     * taskArrayList. Once it passes all the tests, it is added into the taskArrayList.
     *
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the todo command input fails any of the test,
     */
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

    /**
     * Prevent the empty Todo command whereby the todo description is missing from being processed into a Todo task.
     *
     * @param commandInput Event command input.
     * @throws YqException If the Todo command is empty.
     */
    private static void conductPrimaryTodoCheck(String commandInput) throws YqException {
        if (commandInput.isEmpty()) {
            throw new MissingTodoDescriptionException();
        }
    }
}
