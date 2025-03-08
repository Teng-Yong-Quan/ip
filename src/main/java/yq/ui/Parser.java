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
    private static String lcUserCmd; // User command in lowercase
    private static final String BYE = "bye";
    private static final String LIST = "list";
    private static final String DEADLINE = "deadline";
    private static final String EVENT = "event";
    private static final String MARK = "mark";
    private static final String UNMARK = "unmark";
    private static final String DELETE = "delete";
    private static final String TODO = "todo";


    public static Command parse(String userCmd) throws YqException {
        String substringOfUserCmd;
        lcUserCmd = userCmd.toLowerCase();
        if (lcUserCmd.contains(BYE)) {
            return new ExitCommand(userCmd);
        } else if (lcUserCmd.contains(LIST)) {
            return new ListCommand(userCmd);
        } else if (lcUserCmd.contains(UNMARK)) {
            substringOfUserCmd = extractUnmarkIndex(userCmd);
            return new UnmarkCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains(MARK)) {
            substringOfUserCmd = extractMarkIndex(userCmd);
            return new MarkCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains(DELETE)) {
            substringOfUserCmd = extractDeleteIndex(userCmd);
            return new DeleteCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains(TODO)) {
            substringOfUserCmd = extractTodoDescription(userCmd);
            return new TodoCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains(DEADLINE)) {
            substringOfUserCmd = extractDlDescription(userCmd);
            return new DeadlineCommand(substringOfUserCmd);
        } else if (lcUserCmd.contains(EVENT)) {
            substringOfUserCmd = extractEventDescription(userCmd);
            return new EventCommand(substringOfUserCmd);
        } else {
            throw new InvalidCommandException(userCmd);
        }
    }

    private static String extractEventDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(EVENT) + EVENT.length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractDlDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(DEADLINE) + DEADLINE.length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractTodoDescription(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(TODO) + TODO.length();
        extractedDescription = userCmd.substring(indexAfterCmdWord).trim();
        return extractedDescription;
    }

    private static String extractDeleteIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(DELETE) + DELETE.length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }

    private static String extractMarkIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(MARK) + MARK.length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }

    private static String extractUnmarkIndex(String userCmd) {
        lcUserCmd = userCmd.toLowerCase();
        indexAfterCmdWord = lcUserCmd.indexOf(UNMARK) + UNMARK.length();
        extractedIndex = userCmd.substring(indexAfterCmdWord).trim();
        return extractedIndex;
    }
}