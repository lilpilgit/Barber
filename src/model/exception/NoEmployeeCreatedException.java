package model.exception;

public class NoEmployeeCreatedException extends Exception {
    public NoEmployeeCreatedException(String errorMessage) {
        super(errorMessage);
    }

    public NoEmployeeCreatedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
