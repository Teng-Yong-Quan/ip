public class YqException extends Exception {
}

class EmptyListException extends YqException {
}

class InvalidCommandException extends YqException {
}

class MissingMarkNumberException extends YqException {
}

class MissingUnmarkNumberException extends YqException {
}

class MissingTodoDescriptionException extends YqException {
}

class MissingByKeywordException extends YqException {
}

class MissingDeadlineDescriptionException extends YqException {
}

class EmptyDeadlineCommandException extends YqException {
}

class EmptyEventCommandException extends YqException {
}

class MissingFromKeywordException extends YqException {
}

class MissingToKeywordException extends YqException {
}

class InvalidFromToIndexesException extends YqException {
}

class MissingEventDescriptionException extends YqException {
}
