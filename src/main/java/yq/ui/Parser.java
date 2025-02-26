package yq.ui;


import yq.commands.Command;
import yq.commands.DeadlineCommand;
import yq.commands.DeleteCommand;
import yq.commands.EventCommand;
import yq.commands.ExitCommand;
import yq.commands.ListCommand;
import yq.commands.MarkCommand;
import yq.commands.TodoCommand;
import yq.commands.UnmarkCommand;
import yq.exceptions.InvalidCommandException;
import yq.exceptions.YqException;

public class Parser {
    private static int indexAfterCmdWord;
    private static String extractedDescription;
    private static String extractedIndex;
    private static String lcUserCmd = "";

    public static Command parse(String userCmd) throws YqException {
        String substringOfUserCmd;
        lcUserCmd = userCmd.toLowerCase();
        if (lcUserCmd.contains("bye")) {
            return new ExitCommand(lcUserCmd);
        } else if (lcUserCmd.contains("list")) {
            return new ListCommand(lcUserCmd);
        } else if (lcUserCmd.contains("unmark")) {
            substringOfUserCmd = extractUnmarkIndex(userCmd);
            return new UnmarkCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains("mark")) {
            substringOfUserCmd = extractMarkIndex(userCmd);
            return new MarkCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains("delete")) {
            substringOfUserCmd = extractDeleteIndex(userCmd);
            return new DeleteCommand(substringOfUserCmd);
        } else {
            return autoParse(userCmd);
        }
    }

    private static String extractEventDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("event") + "event".length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractDlDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("deadline") + "deadline".length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractTodoDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("todo") + "todo".length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractDeleteIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("delete") + "delete".length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }

    private static String extractMarkIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("mark") + "mark".length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }

    private static String extractUnmarkIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf("unmark") + "unmark".length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }

    public static Command autoParse(String finalTaskDescription) throws YqException {
        String substringOfUserCmd;
        String lcFinalTaskDescription = finalTaskDescription.toLowerCase();
        if (lcFinalTaskDescription.contains("todo")) {
            substringOfUserCmd = extractTodoDescription(finalTaskDescription);
            return new TodoCommand(substringOfUserCmd);
        } else if (lcFinalTaskDescription.contains("deadline")) {
            substringOfUserCmd = extractDlDescription(finalTaskDescription);
            return new DeadlineCommand(substringOfUserCmd);
        } else if (lcFinalTaskDescription.contains("event")) {
            substringOfUserCmd = extractEventDescription(finalTaskDescription);
            return new EventCommand(substringOfUserCmd);
        } else {
            throw new InvalidCommandException(finalTaskDescription);
        }
    }
}